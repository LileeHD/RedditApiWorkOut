package lilee.hd.redditapiworkout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import lilee.hd.redditapiworkout.model.Feed;
import lilee.hd.redditapiworkout.model.children.Children;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
//    TODO: search subs logic

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://www.reddit.com/";
    private TextView textView;
    private Button btn;
    private EditText mFeedName;
    private String mCurrentFeed;
    private String sort = "new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redditCall();
        jsonPlaceHolderCall();
        btn = findViewById(R.id.btnGetData);
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
    }

    private void searchCall() {
        Button btn = findViewById(R.id.btnGetData);
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
                            "thumbnail: " + childrenArrayList.get(i).getData().getThumbnail() + "\n" +
                            "title: " + childrenArrayList.get(i).getData().getTitle() + "\n" +
                            "subs: " + childrenArrayList.get(i).getData().getAuthor_fullname() + "\n" +
                            "-------------------------------------------------------------------------\n\n");

                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("searchCall", "onFailure: ERROR: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void redditCall() {
        Button btn = findViewById(R.id.btnGetData);
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
//                for (int i = 0; i < childrenArrayList.size(); i++) {
//
//                    Log.d(TAG, "onResponse: \n" +
//                            "kind: " + childrenArrayList.get(i).getKind() + "\n" +
//                            "display_name: " + childrenArrayList.get(i).getData().getDisplay_name() + "\n" +
//                            "title: " + childrenArrayList.get(i).getData().getTitle() + "\n" +
//                            "subs: " + childrenArrayList.get(i).getData().getSubscribers() + "\n" +
//                            "-------------------------------------------------------------------------\n\n");
//
//                }
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
    private void jsonPlaceHolderCall(){
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
                if (!response.isSuccessful()){
                    textView.setText("Code" + response.code());
                    return;
                }
                List<Post> posts = response.body();
                for (Post post: posts) {
                    String content = "";
                    content+= "ID: " + post.getId() + "\n";
                    content+= "User ID: " + post.getUserId() + "\n";
                    content+= "Title: " + post.getTitle() + "\n";
                    content+= "Text: " + post.getText() + "\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

}
