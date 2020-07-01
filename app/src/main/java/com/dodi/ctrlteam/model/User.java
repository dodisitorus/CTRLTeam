package com.dodi.ctrlteam.model;


import java.io.Serializable;

public class User implements Serializable {
    String nickname;
    String userId;

    public User(String nickname, String userId) {
        this.nickname = nickname;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
