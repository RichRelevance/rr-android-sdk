package com.richrelevance.internal.net;


import com.richrelevance.Error;

public class SimpleResultCallback<T> implements WebRequest.ResultCallback<T> {

    private T result;
    private com.richrelevance.Error error;

    @Override
    public void onSuccess(T result) {
        this.result = result;
    }

    @Override
    public void onError(Error error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public Error getError() {
        return error;
    }
}
