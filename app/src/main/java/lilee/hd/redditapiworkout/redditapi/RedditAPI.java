package lilee.hd.redditapiworkout.redditapi;

import lilee.hd.redditapiworkout.redditapi.model.Feed;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RedditAPI {


    // home display not logged
    @GET("subreddits/default.json?&raw_json=1&type=sr")
    Call<Feed> getData();

    // search
    @GET("r/{subredditName}/about.json?raw_json=1")
    Call<String> getSubredditData(@Path("subredditName") String subredditName);

    @GET("search.json?&sort=new&raw_json=1&type=link")
    Call<Feed> searchPost(@Query("q") String postName,
                          @Query("t") String time, @Query("sort") String sort);

    @GET("search.json?&type=&sort=new&raw_json=1&type=sr")
    Call<Feed> searchSubreddit(@Query("q") String postName,
                               @Query("t") String time, @Query("sort") String sort);

    // access token

    @FormUrlEncoded
    @POST("api/v1/access_token")
    Call<TokenResponse> getAccessToken(
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("redirect_uri") String redirect_uri
    );


    //    refresh token
    @FormUrlEncoded
    @POST("api/v1/access_token")
    Call<TokenResponse> getRefreshToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refresh_token
    );


}
