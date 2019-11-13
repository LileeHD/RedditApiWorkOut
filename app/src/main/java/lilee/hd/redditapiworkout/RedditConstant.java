package lilee.hd.redditapiworkout;

public class RedditConstant {

    public static final String API_BASE_URL = "https://your.api-base.url";

    public static final String AUTH_URL_W = "https://www.reddit.com/api/v1/authorize.compact";
    public static final String AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=CLIENT_ID&response_type=TYPE&state=STATE&redirect_uri=REDIRECT_URI&duration=DURATION&scope=SCOPE_STRING";


    public static final String ACCESS_TOKEN_URL  = "https://www.reddit.com/api/v1/access_token/";

    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_ID = "C1bC4X4KjSHM9w";

    public static final String TYPE_KEY = "response_type";
    public static final String TYPE = "code";

    public static final String STATE_KEY  = "state";
    public static final String STATE  = "NERF_DIS";

    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String REDIRECT_URI = "lilee://callback";

    public static final String DURATION_KEY = "duration";
    public static final String DURATION = "temporary";

    public static final String SCOPE_KEY = "scope";
    public static final String SCOPE = "identity";

}
