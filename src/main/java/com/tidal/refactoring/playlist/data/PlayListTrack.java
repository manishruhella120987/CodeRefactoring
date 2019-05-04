package com.tidal.refactoring.playlist.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
* hashCode and equal method have been changed to decide uniqueness of PlayListTrack.
* Track id of underlying track is considered only unique property.
*
* */
public class PlayListTrack implements Serializable, Comparable<PlayListTrack> {

    private static final long serialVersionUID = 5464240796158432162L;
    private static final Random randomGenerator = new Random();

    private Integer id;
    private PlayList playlist;
    private int index;
    private Date dateAdded;
    private int trackId;
    private Track track;


    public static PlayListTrack generateNewPlayListTrack(PlayList playList,Track track){
        PlayListTrack playListTrack= new PlayListTrack();
        playListTrack.setId(randomGenerator.nextInt(10000));
        playListTrack.setTrackPlaylist(playList);
        playListTrack.setTrack(track);
        return playListTrack;
    }


    public PlayListTrack() {
        dateAdded = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTrackId() {
        return trackId;
    }

    public PlayList getTrackPlayList() {
        return playlist;
    }

    private void setTrackPlaylist(PlayList playlist) {
        this.playlist = playlist;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
        this.trackId=track.getId();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int compareTo(PlayListTrack o) {
        return this.getIndex() - o.getIndex();
    }

    /**
    * equals method should not depend upon date,id and index id
    * one track my have different index id and date in the life time of playList
    * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayListTrack)) return false;

        PlayListTrack that = (PlayListTrack) o;

        return getTrackId() == that.getTrackId();

    }

    /**
    * hashCode method should not depend upon date,id and index id
    * one track my have different index id and date in the life time of playList
    * */
    @Override
    public int hashCode() {
        return getTrackId();
    }

    public String toString() {
        return "PlayListTrack id[" + getId() + "], trackId[" + getTrackId() + "]";
    }

}
