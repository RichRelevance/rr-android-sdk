package com.richrelevance;

/**
 * Callback called when a request returns.
 * @param <T> The type of result being returned.
 */
public interface Callback<T> {

    /**
     * Called when the request succeeds and returns a result.
     * @param result The returned result.
     */
    public void onResult(T result);

    /**
     * Called when the request returns an error.
     * @param error The returned error.
     */
    public void onError(Error error);
}
