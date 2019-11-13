package lilee.hd.redditapiworkout.model.children;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {
    @SerializedName("subreddit")
    @Expose
    private String subreddit;

    @SerializedName("ups")
    @Expose
    private int ups;

    @SerializedName("title")
    @Expose
    private String title;

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Data{" +
                "subreddit=" + subreddit +
                ", ups='" + ups + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
