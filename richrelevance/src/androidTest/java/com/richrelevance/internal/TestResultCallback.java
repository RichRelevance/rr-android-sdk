package com.richrelevance.internal;

import com.richrelevance.*;
import com.richrelevance.Error;
import com.richrelevance.internal.net.WebRequest;

import junit.framework.TestCase;

public abstract class TestResultCallback<T extends ResponseInfo>
        implements WebRequest.ResultCallback<ResponseInfo> {

    private boolean succeeded = false;

    @Override
    public void onSuccess(ResponseInfo result) {
        testResponse((T) result, null);
        succeeded = true;
    }

    @Override
    public void onError(com.richrelevance.Error error) {
        testResponse(null, error);
        succeeded = true;
    }

    public void assertSuccess(TestCase test) {
        test.assertTrue("Response did not return", succeeded);
    }

    protected abstract void testResponse(T response, Error error);
}
