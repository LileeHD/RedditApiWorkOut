package lilee.hd.redditapiworkout.redditapi;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static lilee.hd.redditapiworkout.redditapi.RedditConstant.BASE_URL;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.CLIENT_ID;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.GRANT_TYPE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.REDIRECT_URI;

public class CatchToken {

    private void catchAccessToken() {
        final Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {

            final String code = uri.getQueryParameter("code");
            String authString = CLIENT_ID + ":"+"";
            final String encodedAuthString = "Basic" + Base64.encodeToString(authString.getBytes(),
                    Base64.NO_WRAP);
            String postData = "grant_type=authorization_code&code="+code+"&redirect_uri="+REDIRECT_URI;
            Log.v(TAG_TOKEN, "onResume: URI received " + uri.toString());


            client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("Authorization", encodedAuthString)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
//            final Request request = new Request.Builder()
//                    .addHeader("User-Agent", "Sample App")
//                    .addHeader("Authorization", encodedAuthString)
//                    .url(ACCESS_TOKEN_URL)
//                    .build();
//            client.networkInterceptors().add(new Interceptor() {
//                @NotNull
//                @Override
//                public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
//                    return chain.proceed(
//                            chain.request()
//                            .newBuilder()
//                            .addHeader("Authorization", encodedAuthString)
//                            .build()
//                    );
//                }
//            });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RedditAPI redditAPI = retrofit.create(RedditAPI.class);
            final Call<TokenResponse> responseCall = redditAPI.getAccessToken(
                    "grant_type"+GRANT_TYPE,"code" +code,"redirect_uri"+ REDIRECT_URI
            );

            responseCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(@NotNull Call<TokenResponse> call, @NotNull Response<TokenResponse> response) {
                    if (response.isSuccessful()){
                        Log.d(TAG_TOKEN, "onResponse: "+ response.toString());
                    }else {
                        Log.d(TAG_TOKEN, "responce was not successfull triggered");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<TokenResponse> call, @NotNull Throwable t) {
                    Log.e(TAG_TOKEN, "onFailure: token error:" + t.getMessage() );
                }
            });

        }
    }
}
