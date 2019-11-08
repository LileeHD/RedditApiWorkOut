package lilee.hd.redditapiworkout.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lilee.hd.redditapiworkout.model.children.Children;
import lilee.hd.redditapiworkout.user.usermodel.UserChildren;

public class UserData {
    @SerializedName("modhash")
    @Expose
    private String modhash;

    @SerializedName("dist")
    @Expose
    private String dist;

    @SerializedName("children")
    @Expose
    private ArrayList<UserChildren> children;

    public String getModhash() {
        return modhash;
    }

    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    public ArrayList<UserChildren> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<UserChildren> children) {
        this.children = children;
    }
}
