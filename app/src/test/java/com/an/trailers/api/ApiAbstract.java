package com.an.trailers.api;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@RunWith(JUnit4.class)
public class ApiAbstract<T> {

    private MockWebServer mockWebServer;


    @Before
    public void mockServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @After
    public void stopServer() throws IOException {
        mockWebServer.shutdown();
    }

    public void enqueueResponse(String fileName) throws IOException {
        enqueueResponse(fileName, Collections.EMPTY_MAP);
    }


    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
        InputStream inputStream = ApiAbstract.class.getClassLoader().getResourceAsStream(String.format("api-response/%s", fileName));
        Source source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            mockResponse.addHeader(entry.getKey(), entry.getValue());
        }
        mockWebServer.enqueue(mockResponse.setBody(((BufferedSource) source).readString(StandardCharsets.UTF_8)));
    }

    public T createService(Class<T> clazz) {
        return new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(clazz);
    }

    public void assertRequestPath(String path) throws InterruptedException {
        RecordedRequest request = mockWebServer.takeRequest();
        MatcherAssert.assertThat(request.getPath(), CoreMatchers.is(path));
    }
}
