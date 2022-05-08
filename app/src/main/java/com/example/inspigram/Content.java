package com.example.inspigram;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private String id;
    private int profilImage;
    private String userName;
    private String likes;
    private int contentImage;
    private ArrayList<String> comments;
    private String comment;

    public Content() {}

    public Content(int profilImage, String userName, String likes, int contentImage, String comment) {
        this.profilImage = profilImage;
        this.userName = userName;
        this.likes = likes;
        this.contentImage = contentImage;
        this.comments = new ArrayList<>();
        this.comment = comment;
    }

    public int getProfilImage() { return profilImage; }

    public void setProfilImage(int profilImage) { this.profilImage = profilImage; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getLikes() { return likes; }

    public void setLikes(String likes) { this.likes = likes; }

    public int getContentImage() { return contentImage; }

    public void setContentImage(int contentImage) { this.contentImage = contentImage; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public String _getId() { return id; }

    public void setId(String id) { this.id = id; }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}
