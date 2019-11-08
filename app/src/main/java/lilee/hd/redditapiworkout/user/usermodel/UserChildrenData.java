package lilee.hd.redditapiworkout.user.usermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserChildrenData {
    @SerializedName("subreddit")
    @Expose
    private String subreddit;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("link_url")
    @Expose
    private String link_url;

    @SerializedName("ups")
    @Expose
    private int ups;

    @SerializedName("downs")
    @Expose
    private int downs;

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    @Override
    public String toString() {
        return "UserChildrenData{" +
                "subreddit='" + subreddit + '\'' +
                ", body='" + body + '\'' +
                ", link_url='" + link_url + '\'' +
                ", ups=" + ups +
                ", downs=" + downs +
                '}';
    }
}
