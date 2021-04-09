package com.example.wanandroid.network.response;

import java.io.Serializable;
import java.util.List;

public class LoginResp implements Serializable {
    private boolean admin;
    private List<String> chapterTops;
    private List<String> collectIds;
    private String email;
    private String icon;
    private String id;
    private String nickname;
    private String password;
    private String publicName;
    private String token;
    private String type;
    private String username;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPublicName() {
        return publicName;
    }

    public String getToken() {
        return token;
    }

    public List<String> getChapterTops() {
        return chapterTops;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getCollectIds() {
        return collectIds;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setChapterTops(List<String> chapterTops) {
        this.chapterTops = chapterTops;
    }

    public void setCollectIds(List<String> collectIds) {
        this.collectIds = collectIds;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getAdmin() {
        return admin;
    }

}

