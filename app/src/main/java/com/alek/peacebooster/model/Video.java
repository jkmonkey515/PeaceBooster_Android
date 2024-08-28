package com.alek.peacebooster.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Objects;

public class Video {

    @DocumentId
    private String mId;
    private String description;
    private String videoId;

    public Video() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getThumbnail(){
        return "https://img.youtube.com/vi/"+videoId+"/0.jpg";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return mId.equals(video.mId) && description.equals(video.description) && videoId.equals(video.videoId);
    }

}
