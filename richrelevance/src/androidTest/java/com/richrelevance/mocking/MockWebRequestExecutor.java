package com.richrelevance.mocking;

import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebRequestExecutor;
import com.richrelevance.internal.net.WebRequestExecutorFactory;
import com.richrelevance.internal.net.WebResultInfo;

public class MockWebRequestExecutor<T> implements WebRequestExecutor<T> {

    private WebRequest<T> request;

    public MockWebRequestExecutor(WebRequest<T> request) {
        this.request = request;
    }

    @Override
    public WebResultInfo<T> execute() {
        return MockResponseManager.getInstance().getResult(request);
    }

    public static final WebRequestExecutorFactory FACTORY = new WebRequestExecutorFactory() {
        @Override
        public <Result> WebRequestExecutor<Result> create(WebRequest<Result> request, int connectTimeout, int readTimeout) {
            return new MockWebRequestExecutor<Result>(request);
        }
    };
}
