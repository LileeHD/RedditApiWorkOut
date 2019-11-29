package lilee.hd.redditapiworkout.user.usermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserChildren {
    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("data")
    @Expose
    private UserChildrenData data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public UserChildrenData getData() {
        return data;
    }

    public void setData(UserChildrenData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Children{" +
                "kind='" + kind + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
