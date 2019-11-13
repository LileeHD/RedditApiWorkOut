package lilee.hd.redditapiworkout;

import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lilee.hd.redditapiworkout.model.Feed;
import lilee.hd.redditapiworkout.model.children.Children;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static lilee.hd.redditapiworkout.RedditConstant.ACCESS_TOKEN_URL;
import static lilee.hd.redditapiworkout.RedditConstant.AUTH_URL_W;
import static lilee.hd.redditapiworkout.RedditConstant.CLIENT_ID;
import static lilee.hd.redditapiworkout.RedditConstant.CLIENT_ID_KEY;
import static lilee.hd.redditapiworkout.RedditConstant.DURATION;
import static lilee.hd.redditapiworkout.RedditConstant.DURATION_KEY;
import static lilee.hd.redditapiworkout.RedditConstant.REDIRECT_URI;
import static lilee.hd.redditapiworkout.RedditConstant.REDIRECT_URI_KEY;
import static lilee.hd.redditapiworkout.RedditConstant.SCOPE;
import static lilee.hd.redditapiworkout.RedditConstant.SCOPE_KEY;
import static lilee.hd.redditapiworkout.RedditConstant.STATE;
import static lilee.hd.redditapiworkout.RedditConstant.STATE_KEY;
import static lilee.hd.redditapiworkout.RedditConstant.TYPE;
import static lilee.hd.redditapiworkout.RedditConstant.TYPE_KEY;

public class MainActivity extends AppCompatActivity {


//    TODO: search subs logic

    private static final String TAG = "Take a bow";
    private static final String BASE_URL = "https://www.reddit.com/";
    private TextView textView;
    private Button btn;
    private Button login;
    private EditText mFeedName;
    private String mCurrentFeed;
    private String sort = "new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redditCall();
//        jsonPlaceHolderCall();

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

        Uri baseUri = Uri.parse(AUTH_URL_W);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter(CLIENT_ID_KEY, CLIENT_ID);
        builder.appendQueryParameter(TYPE_KEY, TYPE);
        builder.appendQueryParameter(STATE_KEY, STATE);
        builder.appendQueryParameter(REDIRECT_URI_KEY, REDIRECT_URI);
        builder.appendQueryParameter(DURATION_KEY, DURATION);
        builder.appendQueryParameter(SCOPE_KEY, SCOPE);
        String url = builder.toString();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        Log.d(TAG, "startSignIn: URL " + url);
    }

    private void searchCall() {
        textView = findViewById(R.id.text_result);
        Button btn = findViewById(R.id.btnFetchFeed);
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
                    content += "ups: " + post.getData().getUps() + "\n\n";
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
        Button btn = findViewById(R.id.btnFetchFeed);
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
                            "subreddit: " + childrenArrayList.get(i).getData().getSubreddit() + "\n" +
                            "title: " + childrenArrayList.get(i).getData().getTitle() + "\n" +
                            "ups: " + childrenArrayList.get(i).getData().getUps() + "\n" +
                            "-------------------------------------------------------------------------\n\n");
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: ERROR: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void jsonPlaceHolderCall() {
        textView = findViewById(R.id.text_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        Call<List<Post>> call = jsonPlaceHolderAPI.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Code" + response.code());
                    return;
                }
                List<Post> posts = response.body();
                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Uri uri = getIntent().getData();
//
//        if (uri != null && uri.toString().startsWith(REDIRECT_URI)){
//            String code = uri.getQueryParameter("code");
//            if (code!=null){
//
//            }
//            Toast.makeText(this, "OTTER", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onResume: OTTER TOKEN" + getIntent().getDataString());
//        }
        if (getIntent() != null && Objects.equals(getIntent().getAction(), Intent.ACTION_VIEW)) {
            Uri uri = getIntent().getData();
            assert uri != null;
            if (uri.getQueryParameter("error") != null) {

                String error = uri.getQueryParameter("error");
                Log.e(TAG, "onResume: An error occured: " + error);
            } else {
                String state = uri.getQueryParameter("state");
                Toast.makeText(this, "OTTER", Toast.LENGTH_SHORT).show();
                if (state.equals(STATE)) {
                    String code = uri.getQueryParameter("code");
                    getAccessToken(code);
                }
            }
        }
    }

    private void getAccessToken(String code){
        OkHttpClient client = new OkHttpClient();
        String authString = CLIENT_ID + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);

        Request request = new Request.Builder()
                .addHeader("User-Agent", "Sample App")
                .addHeader("Authorization", "Basic" + encodedAuthString)
                .url(BASE_URL)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                        "grant_type=authorization_code&code=" + code + "&redirect_uri=" + REDIRECT_URI))
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: OTTER " + e );
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {

                String json = Objects.requireNonNull(response.body()).string();
                JSONObject data;

                try{
                    data = new JSONObject(json);
                    String accessToken = data.optString("access_token");
                    String refreshToken  = data.optString("refresh_token");

                    Log.d(TAG, "Access Token = " + accessToken);
                    Log.d(TAG, "Refresh Token = " + refreshToken);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
