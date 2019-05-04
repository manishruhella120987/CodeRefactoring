package com.tidal.refactoring.playlist.data;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 * A very simplified version of TrackPlaylist
 */
public class PlayList {
    private Integer id;
    private String playListName;
    private PlayListTrackList playListTracks= new PlayListTrackList();
    private Date registeredDate;
    private Date lastUpdated;
    private String uuid;
    private int nrOfTracks;
    private boolean deleted;
    private Float duration;
    
    /**MAX_TRACKS_LIMIT value must be configured somewhere in property file*/
    public static final int MAX_TRACKS_LIMIT = 500;
    public PlayList() {
        this.uuid = UUID.randomUUID().toString();
        Date d = new Date();
        this.registeredDate = d;
        this.lastUpdated = d;
        this.playListTracks =new PlayListTrackList();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public PlayListTrackList getPlayListTracks() {
        return playListTracks;
    }

    public void setPlayListTracks(PlayListTrackList playListTracks) {
        this.playListTracks = playListTracks;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }


    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getNrOfTracks() {
        return playListTracks.size();
    }

    public void setNrOfTracks(int nrOfTracks) {
        this.nrOfTracks = nrOfTracks;
    }
    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public boolean isEmpty() {
        return getPlayListTracks().isEmpty();
    }

    public PlayListTrack get(int index) {
        return getPlayListTracks().get(index);
    }

    public Iterator<PlayListTrack> iterator() {
        return getPlayListTracks().iterator();
    }

    public void add(PlayListTrack e) {
        getPlayListTracks().add(e);
    }

    public void add(int index, PlayListTrack e) {
        getPlayListTracks().add(index, e);
    }

    public PlayListTrack remove(int index) {
        return getPlayListTracks().remove(index);
    }

    public boolean remove(PlayListTrack e) {
        return getPlayListTracks().remove(e);
    }




}