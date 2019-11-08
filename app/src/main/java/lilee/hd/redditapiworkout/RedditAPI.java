package lilee.hd.redditapiworkout;

import java.util.List;
import java.util.Map;

import lilee.hd.redditapiworkout.model.Feed;
import lilee.hd.redditapiworkout.user.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RedditAPI {

    String BASE_URL = "https://www.reddit.com/";
    @Headers("Content-Type: application/json")

// home display not logged
    @GET("subreddits/default.json")
    Call<Feed> getData();

// search
    @GET("search.json?&raw_json=1&type=sr")
    Call<Feed> searchSubreddit(@Query("q") String subredditName,
                               @Query("t") String time, @Query("sort") String sort);

    @GET("search.json?&raw_json=1&type=user")
    Call<Feed> searchUser(@Query("q") String userName,
                          @Query("t") String time, @Query("sort") String sort);

    @GET("search.json?&raw_json=1&type=link")
    Call<Feed> searchPost(@Query("q") String postName,
                          @Query("t") String time, @Query("sort") String sort);

// Users
    @GET("user/{username}/{where}.json?&type=links&raw_json=1&limit=25")
    Call<User> getUserProfile(@Query("username") String userName,
                              @Path("where") String where);


}
