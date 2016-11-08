package com.richrelevance;

import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebRequestManager;
import com.richrelevance.internal.net.WebResultInfo;

/**
 * Implementation of {@link RichRelevanceClient}.
 */
class RichRelevanceClientImpl implements RichRelevanceClient {

    private ClientConfiguration configuration;

    @Override
    public ClientConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T extends ResponseInfo> void executeRequest(final RequestBuilder<T> request) {
        request.setClient(this);

        CallbackWebListener<T> listener = new CallbackWebListener<>(request.getCallback());

        WebRequest<T> webRequest = request.getWebRequest();
        if (webRequest != null) {
            RichRelevance.getWebRequestManager().executeInBackground(webRequest, listener);
        }
    }

    // region Inner Classes

    private static class CallbackWebListener<T> implements WebRequestManager.WebRequestListener<T> {

        private Callback<? super T> callback;

        public CallbackWebListener(Callback<? super T> callback) {
            this.callback = callback;
        }

        @Override
        public void onRequestComplete(WebResultInfo<T> resultInfo) {
            if (callback != null) {
                if (resultInfo.getError() != null) {
                    callback.onError(resultInfo.getError());
                } else {
                    callback.onResult(resultInfo.getResult());
                }
            }
        }
    }

    // endregion Inner Classes
}
