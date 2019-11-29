package lilee.hd.redditapiworkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lilee.hd.redditapiworkout.redditapi.RedditAPI;
import lilee.hd.redditapiworkout.redditapi.TokenResponse;
import lilee.hd.redditapiworkout.redditapi.model.Feed;
import lilee.hd.redditapiworkout.redditapi.model.children.Children;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static lilee.hd.redditapiworkout.redditapi.RedditConstant.AccessToken;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.BASE_URL;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.CLIENT_ID;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.CLIENT_ID_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.DURATION;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.DURATION_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.GRANT_TYPE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.OAUTH_BASE_URL;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.REDIRECT_URI;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.REDIRECT_URI_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.RESPONSE_TYPE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.RESPONSE_TYPE_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.SCOPE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.SCOPE_KEY;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.STATE;
import static lilee.hd.redditapiworkout.redditapi.RedditConstant.STATE_KEY;

public class MainActivity extends AppCompatActivity {


//    TODO: search subs logic

    public static final MediaType CONTENT_TYPE = MediaType.get("application/x-www-form-urlencoded");
    private static final String TAG = "Take a bow";
    private static final String TAG_TOKEN = "OTTER";
    public OkHttpClient.Builder client;
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

    private void catchAccessToken() {
        final Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            final String code = uri.getQueryParameter("code");

            client = new OkHttpClient.Builder();
            client.addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request();
                    String credentials = Credentials.basic(CLIENT_ID, "");
                    Request.Builder newRequest = request.newBuilder()
                            .addHeader("Authorization", credentials);
                    return chain.proceed(newRequest.build());
                }
            });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final RedditAPI redditAPI = retrofit.create(RedditAPI.class);

            Call<TokenResponse> call = redditAPI.getAccessToken("authorization_code", code, REDIRECT_URI);
            call.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(@NotNull Call<TokenResponse> call, @NotNull Response<TokenResponse> response) {

                    Log.d(TAG_TOKEN, "auth code: " + code);

                    if (response.isSuccessful()&& response.body()!=null) {
                        Log.d(TAG_TOKEN, "response raw: " + response.raw());
                        Log.d(TAG_TOKEN, "response body: " + response.body().toString());

                        TextView accessToken = findViewById(R.id.access_token);
                        TextView tokenType = findViewById(R.id.token_type);
                        TextView expiresIn = findViewById(R.id.expires_in);
                        TextView refreshToken = findViewById(R.id.refresh_token);
                        TextView scope = findViewById(R.id.scope);

                        final String tokenResponses = response.body().getAccessToken();
                        Log.d(TAG_TOKEN, "token: " + tokenResponses);
                        accessToken.setText(tokenResponses);

                        final String tokenTypes = response.body().getTokenType();
                        Log.d(TAG_TOKEN, "token type: " + tokenTypes);
                        tokenType.setText(tokenTypes);

                        final String expiresIns = String.valueOf(response.body().getExpiresIn());
                        Log.d(TAG_TOKEN, "expires in: " + expiresIns);
                        expiresIn.setText(expiresIns);

                        final String refreshTokens = response.body().getRefreshToken();
                        Log.d(TAG_TOKEN, "refresh token: " + refreshTokens);
                        refreshToken.setText(refreshTokens);

                        final String scopes = response.body().getScope();
                        Log.d(TAG_TOKEN, "refresh token: " + scopes);
                        scope.setText(scopes);

                    }

                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Log.e(TAG_TOKEN, "onFailure: ERROR: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}

