package com.slappsm.android.model;

public class Friend {
    String nick;
    String avatar;

    public String getNick() {
        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
