package com.richrelevance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.richrelevance.internal.net.HttpMethod;
import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebRequestManager;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.internal.net.WebResultInfo;

import java.util.LinkedList;
import java.util.Queue;

class ClickTrackingManager {

    private static final ClickTrackingManager INSTANCE = new ClickTrackingManager();

    public static ClickTrackingManager getInstance() {
        return INSTANCE;
    }

    private Context context;
    private ConnectivityManager connectivityManager;

    private WebRequestManager webManager;

    private final Queue<WebRequest<?>> requestQueue;

    private ClickTrackingManager() {
        this.webManager = new WebRequestManager(1);
        this.requestQueue = new LinkedList<>();
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        this.connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void trackClick(String url) {
        trackClick(new ClickRequest(url));
    }

    public void trackClick(WebRequest<?> request) {
        queueRequest(request);
        processQueue();
    }

    /**
     * Call to manually attempt to flush the queue of any remaining click tracking requests.
     */
    public void flush() {
        processQueue();
    }

    public int getQueuedCount() {
        return requestQueue.size();
    }

    private void processQueue() {
        synchronized (this) {
            WebRequest<?> request = requestQueue.peek();
            if (request != null) {
                processNext(request);
            } else {
                unregisterStateListener();
            }
        }
    }

    private <T> void processNext(final WebRequest<T> request) {
        if (isConnected()) {
            synchronized (this) {
                webManager.executeInBackground(request, new WebRequestManager.WebRequestListener<T>() {
                    @Override
                    public void onRequestComplete(WebResultInfo<T> resultInfo) {
                        synchronized (ClickTrackingManager.this) {
                            if (resultInfo.getResponseCode() != -1) {
                                requestQueue.remove(request);
                                processQueue();
                            } else {
                                registerStateListener();
                            }
                        }
                    }
                });
            }
        } else {
            registerStateListener();
        }
    }

    void queueRequest(WebRequest<?> request) {
        synchronized (this) {
            requestQueue.add(request);
        }
    }

    private boolean isConnected() {
        if (connectivityManager != null) {
            try {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if (activeNetwork == null || !activeNetwork.isConnected()) {
                    return false;
                }
            } catch (Exception e) {
                // We're going to try, but if the system throws any sort of permission exception etc, we don't want to crash
            }
        } else {
            logUninitializedWarning();
        }

        // We couldn't tell that we weren't connected, so just assume we are
        return true;
    }

    private void registerStateListener() {
        synchronized (this) {
            if (context != null) {
                try {
                    context.registerReceiver(CONNECTION_STATE_RECEIVER,
                            new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
                } catch (Exception e) {
                    // Shouldn't happen, but we don't want to crash if this ever changes
                }
            } else {
                logUninitializedWarning();
            }
        }
    }

    private void unregisterStateListener() {
        synchronized (this) {
            if (context != null) {
                try {
                    context.unregisterReceiver(CONNECTION_STATE_RECEIVER);
                } catch (Exception e) {
                    // Don't care
                }
            } else {
                logUninitializedWarning();
            }
        }
    }

    private void logUninitializedWarning() {
        RRLog.w("RichRelevance - Click Tracking", "SDK was not initialized - click tracking cannot detect connection state");
    }

    private final BroadcastReceiver CONNECTION_STATE_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isConnected()) {
                processQueue();
            }
        }
    };

    private static class ClickRequest implements WebRequest<Void> {

        private String url;

        public ClickRequest(String url) {
            this.url = url;
        }

        @Override
        public WebRequestBuilder getRequestBuilder() {
            return new WebRequestBuilder(HttpMethod.Get, url);
        }

        @Override
        public void translate(WebResponse response, ResultCallback<Void> resultCallback) {

        }
    }
}
