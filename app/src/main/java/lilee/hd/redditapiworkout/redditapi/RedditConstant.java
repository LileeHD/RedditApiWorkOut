package lilee.hd.redditapiworkout.redditapi;

import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RedditConstant {

    public static final String CLIENT_NAME = "Reddit Api Workout";
    /**
     * Base urls
     */
    public static final String BASE_URL = "https://www.reddit.com/";
    public static final String BASE_URL_SSL = "https://ssl.www.reddit.com/";
    public static final String DEFAULT_URL = "https://www.reddit.com/subreddits/default.json";

    public static final String OAUTH_URL_ACCESS = "https://oauth.reddit.com/";
    public static final String OAUTH_BASE_URL = "https://www.reddit.com/api/v1/authorize.compact";
//    public static final String OAUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=CLIENT_ID&response_type=TYPE&state=STATE&redirect_uri=REDIRECT_URI&duration=DURATION&scope=SCOPE_STRING";

    /**
     * Token
     */
//    public static final String ACCESS_TOKEN_URL  = "https://www.reddit.com/api/v1/access_token/";
    public static final String ACCESS_TOKEN_URL = "https://oauth.reddit.com/api/v1/access_token/";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String REFRESH_TOKEN_KEY = "refresh_token";
    public static final String TOKEN_TYPE_KEY = "token_type";
    public static final String TOKEN_TYPE = "bearer";
    public static final String EXPIRE_IN_KEY = "expires_in";
    /**
     * Everything else
     */
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_ID = "C1bC4X4KjSHM9w";
    public static final String CLIENT_SECRET = " ";
    public static final String RESPONSE_TYPE_KEY = "response_type";
    public static final String RESPONSE_TYPE = "code";
    public static final String STATE_KEY = "state";
    public static final String STATE = "ZENYATTA_IS_EVERYWHERE";
//    public static final String STATE = UUID.randomUUID().toString().replace("-", "");
    public static final String ERROR_KEY = "error";
    public static final String ERROR = "";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String REDIRECT_URI = "lilee.hd://callback";
    public static final String DURATION_KEY = "duration";
    public static final String DURATION = "permanent";
    public static final String SCOPE_KEY = "scope";
    public static final String SCOPE = "identity, mysubreddits";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String GRANT_TYPE = "authorization_code";
    public static String REFRESH_TOKEN = "refresh_token";

    //
    public static String AccessToken;
    public static String Tokentype;
    public static String Expiresin;
    public static String Scope;
    public static String State;
    public static String Refreshtoken;

}
