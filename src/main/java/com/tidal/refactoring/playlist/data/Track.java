package com.tidal.refactoring.playlist.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class Track {

	@NotBlank 
    private String title;
    
    @Positive
    private float duration;
    
    @Positive
    private int artistId;
    
    @Positive
    private int id;

    public Track() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}