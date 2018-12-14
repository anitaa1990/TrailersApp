package com.an.trailers.data.remote.interceptor;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkInterceptor implements Interceptor {

    private Context context;
    public NetworkInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(ConnectivityStatus.isConnected(context)) {
            request.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build();

        } else {
            request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
        }

        return chain.proceed(request);
    }
}
