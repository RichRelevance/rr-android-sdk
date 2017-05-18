package com.richrelevance.internal.net;

import android.os.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class which is responsible for managing and executing web requests.
 */
public class WebRequestManager {

    // region Constants

    private static final int DEFAULT_MAX_CONNECTIONS = 5;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30 * 1000; // 30 sec
    private static final int DEFAULT_READ_TIMEOUT = 30 * 1000; // 30 sec
    private static final String NAME_BACKGROUND_THREAD = "RichRelevance Web Thread";

    // endregion Constants

    // region Interfaces

    /**
     * Interface which responds to the completion of a web request.
     *
     * @param <Result> The result type of the web request.
     */
    public interface WebRequestListener<Result> {
        /**
         * Called when the web request completes successfully or unsuccessfully.
         *
         * @param resultInfo An object containing information about the result of the request.
         */
        public void onRequestComplete(WebResultInfo<Result> resultInfo);
    }

    // endregion Interfaces

    // region Members

    private Semaphore connectionSemaphore;
    private ThreadPoolExecutor backgroundPoolExecutor;

    private int maxConnections;
    private int connectionTimeout, readTimeout;

    private WebRequestExecutorFactory executorFactory;

    // endregion Members

    // region Constructors

    /**
     * Constructs a new {@link WebRequestManager} with default values.
     */
    public WebRequestManager() {
        this(DEFAULT_MAX_CONNECTIONS);
    }

    /**
     * Constructs a new {@link WebRequestManager} with the given number
     * of maximum concurrent connections.
     *
     * @param maxConnections The maximum number of concurrent connections.
     */
    public WebRequestManager(int maxConnections) {
        backgroundPoolExecutor = createBackgroundThreadPool(maxConnections);
        setMaxConnections(maxConnections);
        setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        setReadTimeout(DEFAULT_READ_TIMEOUT);
        executorFactory = HttpUrlConnectionExecutor.FACTORY;
    }

    // endregion Constructors

    //region Accessors

    /**
     * @return The current maximum number of allowed connections.
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * Sets the maximum number of concurrent connections. This will return
     * immediately if increasing, but will block until adequate connections
     * have finished if decreasing. This method is synchronized, so
     * this may also block if another thread is waiting on a decrease.
     *
     * @param maxConnections The new number of maximum concurrent connections.
     */
    public synchronized void setMaxConnections(int maxConnections) {
        if (connectionSemaphore == null) {
            connectionSemaphore = new Semaphore(maxConnections);
        } else {
            int deltaConnections = maxConnections - this.maxConnections;
            if (deltaConnections > 0) {
                connectionSemaphore.release(deltaConnections);
            } else if (deltaConnections < 0) {
                connectionSemaphore.acquireUninterruptibly(-deltaConnections);
            }
        }

        this.maxConnections = maxConnections;

        if (backgroundPoolExecutor != null) {
            backgroundPoolExecutor.setMaximumPoolSize(maxConnections);
            backgroundPoolExecutor.setCorePoolSize(maxConnections);
        }
    }

    /**
     * @return The timeout for establishing a connection (in milliseconds)
     * @see #setConnectionTimeout(int)
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the timeout for establishing a connection. Setting this to zero
     * means a timeout is not used.
     *
     * @param timeoutMillis The timeout value in milliseconds
     */
    public void setConnectionTimeout(int timeoutMillis) {
        connectionTimeout = timeoutMillis;
    }

    /**
     * @return The timeout for reading data from the connection (in milliseconds)
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Sets the timeout for establishing a connection. Setting this to zero
     * means a timeout is not used.
     *
     * @param timeoutMillis The timeout value in milliseconds.
     */
    public void setReadTimeout(int timeoutMillis) {
        readTimeout = timeoutMillis;
    }

    public void setExecutorFactory(WebRequestExecutorFactory factory) {
        this.executorFactory = factory;
    }

    // endregion Accessors

    // region Methods

    /**
     * Executes the given request synchronously and returns the result.
     *
     * @param request  The request to execute.
     * @param <Result> The result type of the request.
     * @return An object containing the information about the result of the web request.
     */
    public <Result> WebResultInfo<Result> execute(WebRequest<Result> request) {
        WebRequestExecutor<Result> executor = executorFactory.create(request, getConnectionTimeout(), getReadTimeout());

        beginConnection();
        WebResultInfo<Result> result = executor.execute();
        endConnection();

        return result;
    }

    /**
     * Executes the given request in the background and calls the listener when the request finishes.
     *
     * @param request  The request to execute.
     * @param listener The listener to call with results, or null.
     * @param <Result> The result type of the request.
     */
    public <Result> void executeInBackground(WebRequest<Result> request, WebRequestListener<Result> listener) {
        backgroundPoolExecutor.execute(createRequestRunnable(request, listener));
    }

    protected <Result> Runnable createRequestRunnable(final WebRequest<Result> request, final WebRequestListener<Result> listener) {
        return new DownloadRunnable() {
            @Override
            public void run() {
                WebResultInfo<Result> result = WebRequestManager.this.execute(request);
                if (listener != null) {
                    listener.onRequestComplete(result);
                }
            }
        };
    }

    /**
     * Called to get the {@link ThreadPoolExecutor} to use to execute background
     * requests.
     *
     * @param maxConnections The maximum number of connections allowed.
     * @return The {@link ThreadPoolExecutor} to use to execute background requests.
     */
    protected ThreadPoolExecutor createBackgroundThreadPool(int maxConnections) {
        final BlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();
        // Keep 1 thread alive at all times, keep idle threads alive for 3 seconds
        ThreadPoolExecutor executor = new ThreadPoolExecutor(maxConnections, maxConnections, 3, TimeUnit.SECONDS, queue);
        executor.setThreadFactory(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, NAME_BACKGROUND_THREAD);
            }
        });
        return executor;
    }

    private void beginConnection() {
        connectionSemaphore.acquireUninterruptibly();
    }

    private void endConnection() {
        connectionSemaphore.release();
    }

    // endregion Methods

    // region Inner Classes

    private static abstract class DownloadRunnable implements
            Comparable<DownloadRunnable>, Runnable {

        private int priority;

        public DownloadRunnable() {
            this.priority = 0;
        }

        public DownloadRunnable(int priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(DownloadRunnable another) {
            return this.priority - another.priority;
        }
    }

    // endregion Inner Classes
}
