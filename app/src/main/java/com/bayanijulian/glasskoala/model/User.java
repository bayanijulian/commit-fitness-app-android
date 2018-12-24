package com.bayanijulian.glasskoala.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    public static final String LABEL = "com.bayanijulian.glasskoala.model.User";
    private String id;
    private String name;
    private String phoneNumber;
    private String profileImg;
    private List<String> friendIds;
    private List<String> groupIds;

    public User() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(List<String> friendIds) {
        this.friendIds = friendIds;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public void addFriend(User newUser) {
        // if they have no friends, it will be null
        if (friendIds == null) {
            friendIds = new ArrayList<>();
        }
        friendIds.add(newUser.id);
    }

    /**
     *
     * @param newUser user to add
     * @return true if user is removed, false if the user was never being followed
     */
    public boolean removeFriend(User newUser) {
        if (!isFollowing(newUser)) return false;
        friendIds.remove(newUser.id);
        return true;
    }

    /**
     *
     * @param newUser user to check
     * @return true if they are following newUser, false otherwise
     */
    public boolean isFollowing(User newUser) {
        if (friendIds == null) {
            friendIds = new ArrayList<>();
            // nothing to do if they have aren't following anyone to remove
            return false;
        }
        // if they aren't following them
        if (!friendIds.contains(newUser.id)) {
            return false;
        }
        return true;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.profileImg);
        dest.writeStringList(this.friendIds);
        dest.writeStringList(this.groupIds);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.phoneNumber = in.readString();
        this.profileImg = in.readString();
        this.friendIds = in.createStringArrayList();
        this.groupIds = in.createStringArrayList();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
