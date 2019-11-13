package lilee.hd.redditapiworkout;

import java.util.Map;

import lilee.hd.redditapiworkout.model.Feed;
import lilee.hd.redditapiworkout.user.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RedditAPI {

    String BASE_URL = "https://www.reddit.com/";

    @Headers("Content-Type: application/json")

// home display not logged
    @GET("subreddits/default.json")
    Call<Feed> getData();

    // search
    @GET("r/search.json?&raw_json=1&type=sr")
    Call<Feed> searchSubreddit(@Query("q") String subredditName,
                               @Query("t") String time, @Query("sort") String sort);


}
