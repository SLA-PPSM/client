package com.slappsm.android.model;

public class Profile {
    String nick;
    String avatar;
    String scrobbles;

    public String getNick() {
        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getScrobbles() {
        return scrobbles;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", scrobbles='" + scrobbles + '\'' +
                '}';
    }
}
