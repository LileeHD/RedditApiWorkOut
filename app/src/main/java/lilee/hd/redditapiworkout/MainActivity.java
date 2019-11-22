package lilee.hd.redditapiworkout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import lilee.hd.redditapiworkout.redditapi.OAuthServer;
import lilee.hd.redditapiworkout.redditapi.RedditAPI;
import lilee.hd.redditapiworkout.redditapi.ServiceGenerator;
import lilee.hd.redditapiworkout.redditapi.TokenResponse;
import lilee.hd.redditapiworkout.redditapi.model.Feed;
import lilee.hd.redditapiworkout.redditapi.model.children.Children;
import lilee.hd.redditapiworkout.user.User;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static lilee.hd.redditapiworkout.redditapi.RedditConstant.ACCESS_TOKEN_URL;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.Authcode;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.BASE_URL;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.CLIENT_ID;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.CLIENT_ID_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.DURATION;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.DURATION_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.Expiresin;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.GRANT_TYPE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.GRANT_TYPE_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.OAUTH_BASE_URL;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.REDIRECT_URI;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.REDIRECT_URI_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.RESPONSE_TYPE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.RESPONSE_TYPE_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.Refreshtoken;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.SCOPE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.SCOPE_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.STATE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.STATE_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.Scope;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.Tokentype;

public class MainActivity extends AppCompatActivity {


//    TODO: search subs logic

    public static final MediaType CONTENT_TYPE = MediaType.get("application/x-www-form-urlencoded");
    private static final String TAG = "Take a bow";
    private static final String TAG_TOKEN = "OTTER";
    public OkHttpClient client;
    private TextView textView;
    private Button btn;
    private Button login;
    private EditText mFeedName;
    private String mCurrentFeed;
    private String sort = "new";
    private FirebaseAuth mAuth;
    private String code;
    private String authString = CLIENT_ID + "";
    private String encodedAuthString = Base64.encodeToString(authString.getBytes(),
            Base64.NO_WRAP);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        redditCall();
//        jsonPlaceHolderCall();
//        startSignIn();

        btn = findViewById(R.id.btnFetchFeed);
        mFeedName = findViewById(R.id.edit_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedName = mFeedName.getText().toString();
                if (!feedName.equals("")) {
                    mCurrentFeed = feedName;
                    searchCall();
                } else {
                    searchCall();
                }
            }
        });

        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    /**
     * Firebase logique
     */
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//
//    private void updateUI(FirebaseUser currentUser) {
//
//    }
    private void startSignIn() {

        Uri baseUri = Uri.parse(OAUTH_BASE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter(CLIENT_ID_KEY, CLIENT_ID);
        builder.appendQueryParameter(RESPONSE_TYPE_KEY, RESPONSE_TYPE);
        builder.appendQueryParameter(STATE_KEY, STATE);
        builder.appendQueryParameter(REDIRECT_URI_KEY, REDIRECT_URI);
        builder.appendQueryParameter(DURATION_KEY, DURATION);
        builder.appendQueryParameter(SCOPE_KEY, SCOPE);
        String url = builder.toString();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        Log.d(TAG_TOKEN, "startSignIn: URL " + url);
    }

    private void searchCall() {
        textView = findViewById(R.id.text_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        String time = "new";
        Call<Feed> call = redditAPI.searchSubreddit(mCurrentFeed, time, sort);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d("searchCall", "onResponse: Server Response" + response.toString());
                Log.d("searchCall", "onResponse: received information" + response.body().toString());

                ArrayList<Children> childrenArrayList = response.body().getData().getChildren();
                for (int i = 0; i < childrenArrayList.size(); i++) {

                    Log.d("searchCall", "onResponse: \n" +
                            "kind: " + childrenArrayList.get(i).getKind() + "\n" +
                            "subreddit: " + childrenArrayList.get(i).getData().getSubreddit() + "\n" +
                            "title: " + childrenArrayList.get(i).getData().getTitle() + "\n" +
                            "ups: " + childrenArrayList.get(i).getData().getUps() + "\n" +
                            "isVideo: " + childrenArrayList.get(i).getData().isVideo() + "\n" +
                            "-------------------------------------------------------------------------\n\n");
                }
                if (!response.isSuccessful()) {
                    textView.setText("Code" + response.code());
                    return;
                }
                for (Children post : childrenArrayList) {
                    String content = "";
                    content += "kind: " + post.getKind() + "\n";
                    content += "subreddit: " + post.getData().getSubreddit() + "\n";
                    content += "title: " + post.getData().getTitle() + "\n";
                    content += "ups: " + post.getData().getUps() + "\n";
                    content += "isVideo: " + post.getData().isVideo() + "\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("searchCall", "onFailure: ERROR: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                textView.setText(t.getMessage());
            }
        });
    }

    private void redditCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        Call<Feed> call = redditAPI.getData();
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d(TAG, "onResponse: Server Response" + response.toString());
                Log.d(TAG, "onResponse: received information" + response.body().toString());

                ArrayList<Children> childrenArrayList = response.body().getData().getChildren();
                for (int i = 0; i < childrenArrayList.size(); i++) {

                    Log.d(TAG, "onResponse: \n" +
                            "kind: " + childrenArrayList.get(i).getKind() + "\n" +
                            "title: " + childrenArrayList.get(i).getData().getTitle() + "\n" +
                            "subscribers: " + childrenArrayList.get(i).getData().getSubscribers() + "\n" +
                            "public_description: " + childrenArrayList.get(i).getData().getPublicDescription() + "\n" +
                            "header_title: " + childrenArrayList.get(i).getData().getHeaderTitle() + "\n" +
                            "url: " + childrenArrayList.get(i).getData().getUrl() + "\n" +
                            "-------------------------------------------------------------------------\n\n");
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: ERROR: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        catchAccessToken();

    }

    private void catchRedirectUri(){
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {

            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            String error = uri.getQueryParameter("access_denied");
            Log.v(TAG_TOKEN, "onResume: URI received " + uri.toString());
            if (code != null) {
                Log.v(TAG_TOKEN, "onResume: STATE: " + state + " And CODE: " + code);
//                catchAccessToken();

            } else if (uri.getQueryParameter("error") != null) {
                Log.e(TAG_TOKEN, "onResume: TOKEN NOT FOUND" + error);
            }
        }
    }

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
                    Log.e(TAG, "onFailure: token error:" + t.getMessage() );
                }
            });

        }
    }


//    private void parsingResponse() {
//        final String postData = "grant_type=authorization_code&code="+code+"&redirect_uri="+REDIRECT_URI;
//        client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//            @NotNull
//            @Override
//            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        .addHeader("Authorization", encodedAuthString)
//                        .post(RequestBody.create(postData, MediaType.parse("application/x-www-form-urlencoded")))
//                        .build();
//                return chain.proceed(request);
//            }
//        }).build();
//
//        Uri uri = getIntent().getData();
//        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
//            code = uri.getQueryParameter(RESPONSE_TYPE);
//
//            if (!TextUtils.isEmpty(code)) {
//                Log.v(TAG_TOKEN, "onResume: TOKEN FOUND " + code);
//
////                Using Retrofit builder getting Authorization code
//                Retrofit retrofit = new Retrofit.Builder()
//                        .client(client)
//                        .baseUrl(BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                OAuthServer.OAuthServerIntface oauthServerInterface = retrofit.create(OAuthServer.OAuthServerIntface.class);
//                Call<OAuthToken> accessTokenCall = oauthServerInterface.getAccessToken(
//                        GRANT_TYPE, RESPONSE_TYPE, REDIRECT_URI);
//
//                accessTokenCall.enqueue(new Callback<OAuthToken>() {
//                    @Override
//                    public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
//                        Log.d(TAG_TOKEN, "onResponse: received information" + response.body().toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<OAuthToken> call, Throwable t) {
//                        Log.e(TAG_TOKEN, "onFailure: DVA OUT" + t.getMessage());
//                    }
//                });
//            }
//            if (TextUtils.isEmpty(code)) {
//                //a problem occurs, the user reject our granting request or something like that
//                Log.e(TAG_TOKEN, "parsingResponse: Not working");
//                Toast.makeText(this, "Not working", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
//
//    }

    //    private void jsonPlaceHolderCall() {
//        textView = findViewById(R.id.text_result);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://jsonplaceholder.typicode.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
//
//        Call<List<Post>> call = jsonPlaceHolderAPI.getPosts();
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                if (!response.isSuccessful()) {
//                    textView.setText("Code" + response.code());
//                    return;
//                }
//                List<Post> posts = response.body();
//                for (Post post : posts) {
//                    String content = "";
//                    content += "ID: " + post.getId() + "\n";
//                    content += "User ID: " + post.getUserId() + "\n";
//                    content += "Title: " + post.getTitle() + "\n";
//                    content += "Text: " + post.getText() + "\n\n";
//                    textView.append(content);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                textView.setText(t.getMessage());
//            }
//        });
//    }

    private void accessToken() {

        OkHttpClient client = new OkHttpClient();
        String authString = CLIENT_ID + ":" +" ";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);
        String infos = GRANT_TYPE_KEY + GRANT_TYPE + "code" + code + REDIRECT_URI_KEY + REDIRECT_URI;

        Request request = new Request.Builder()
                .addHeader("Authorization", encodedAuthString)
                .url(BASE_URL)
                .post(RequestBody.create(infos, MediaType.parse("application/x-www-form-urlencoded")))
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.e(TAG_TOKEN, "onFailure: OTTER " + e);
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                JSONObject data;

                try {
                    data = new JSONObject(json);
                    String accessToken = data.optString("access_token");
                    String refreshToken = data.optString("token_type");

                    Log.d(TAG_TOKEN, "Access Token = " + accessToken);
                    Log.d(TAG_TOKEN, "Refresh Token = " + refreshToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void accessTokenTwo() {

        OkHttpClient client = new OkHttpClient();
        String authString = CLIENT_ID + "";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);
        String postData = "grant_type=authorization_code&code="+code+"&redirect_uri="+REDIRECT_URI;

        Request request = new Request.Builder()
                .addHeader("Authorization", encodedAuthString)
                .url(BASE_URL)
                .post(RequestBody.create(postData, MediaType.parse("application/x-www-form-urlencoded")))
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.e(TAG_TOKEN, "onFailure: OTTER " + e);
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                JSONObject data;

                try {
                    data = new JSONObject(json);
                    String accessToken = data.optString("access_token");
                    String refreshToken = data.optString("refresh_token");

                    Log.d(TAG_TOKEN, "Access Token = " + accessToken);
                    Log.d(TAG_TOKEN, "Refresh Token = " + refreshToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setOkClient() {
        String authString = CLIENT_ID + "";
        final String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);
        final String infos = GRANT_TYPE_KEY + GRANT_TYPE + "code" + code + REDIRECT_URI_KEY + REDIRECT_URI;

        client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", encodedAuthString)
                        .post(RequestBody.create(infos, MediaType.parse("application/x-www-form-urlencoded")))
                        .build();
                return chain.proceed(request);
            }
        }).build();
    }
}
