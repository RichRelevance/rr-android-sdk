package com.richrelevance;

import android.util.Log;

import com.richrelevance.internal.SimpleJsonRequest;
import com.richrelevance.internal.net.HttpMethod;
import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebRequestManager;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.internal.net.WebResultInfo;
import com.richrelevance.internal.OneShotLock;
import com.richrelevance.utils.Wrapper;

import junit.framework.TestCase;

import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;

public class WebRequestManagerTests extends TestCase {
    private WebRequestManager manager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        manager = new WebRequestManager();
    }

    public void testSynchronous() {
        WebResultInfo<JSONObject> resultInfo = manager.execute(new SimpleTestRequest());
        assertNotNull(resultInfo);
        assertNotNull(resultInfo.getResult());
        Log.i(getClass().getSimpleName() + " Sync", resultInfo.getResult().toString());
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void testAsynchronous() {
        final OneShotLock lock = new OneShotLock();
        final Wrapper<WebResultInfo<JSONObject>> payload = new Wrapper<>();
        manager.executeInBackground(new SimpleTestRequest(), new WebRequestManager.WebRequestListener<JSONObject>() {
            @Override
            public void onRequestComplete(WebResultInfo<JSONObject> resultInfo) {
                payload.set(resultInfo);
                lock.unlock();
            }
        });

        lock.waitUntilUnlocked();

        WebResultInfo<JSONObject> resultInfo = payload.get();
        assertNotNull(resultInfo);
        assertNotNull(resultInfo.getResult());
        Log.i(getClass().getSimpleName() + " Async", resultInfo.getResult().toString());
    }

    public void testFailure() {
        WebResultInfo<Void> resultInfo = manager.execute(new WebRequest<Void>() {
            @Override
            public WebRequestBuilder getRequestBuilder() {
                return new WebRequestBuilder(HttpMethod.Get, "notavalidurl");
            }

            @Override
            public void translate(WebResponse response, ResultCallback<Void> resultCallback) {

            }
        });
        assertNotNull(resultInfo);
        assertEquals(WebResultInfo.RESPONSE_CODE_FAILED, resultInfo.getResponseCode());
    }

    public void testMaxConnections() {
        Collection<BlockingRequest> requests = new HashSet<>();
        int maxConnections = manager.getMaxConnections();
        for (int i = 0; i < maxConnections; i++) {
            BlockingRequest request = new BlockingRequest();
            manager.executeInBackground(request, null);
            requests.add(request);
        }

        final OneShotLock lock = new OneShotLock();

        SimpleTestRequest blockedRequest = new SimpleTestRequest();
        final Wrapper<WebResultInfo<JSONObject>> payload = new Wrapper<>();
        manager.executeInBackground(blockedRequest, new WebRequestManager.WebRequestListener<JSONObject>() {
            @Override
            public void onRequestComplete(WebResultInfo<JSONObject> resultInfo) {
                payload.set(resultInfo);
                lock.unlock();
            }
        });

        // Make sure the request that should be blocked still doesn't execute
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Not that important, don't care
        }

        assertEquals("Extra request isn't blocked", false, lock.isUnlocked());

        for (BlockingRequest request : requests) {
            request.unblock();
        }

        lock.waitUntilUnlocked();
        WebResultInfo<JSONObject> resultInfo = payload.get();
        assertNotNull(resultInfo);
        assertNotNull(resultInfo.getResult());
    }

    private class SimpleTestRequest extends SimpleJsonRequest {
        public SimpleTestRequest() {
            super("http://jsonplaceholder.typicode.com/posts/1");
        }
    }

    private class BlockingRequest implements WebRequest<Void> {

        private OneShotLock lock = new OneShotLock();

        @Override
        public WebRequestBuilder getRequestBuilder() {
            return new WebRequestBuilder(HttpMethod.Get, "http://www.google.com");
        }

        @Override
        public void translate(WebResponse response, ResultCallback<Void> resultCallback) {
            lock.waitUntilUnlocked();

            Log.w(getClass().getSimpleName(), "Completed");
        }

        public void unblock() {
            Log.w(getClass().getSimpleName(), "Unblocked");
            lock.unlock();
        }
    }
}
