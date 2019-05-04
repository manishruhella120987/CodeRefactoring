package com.tidal.refactoring.playlist.dao;

import java.util.List;

import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.exception.PlaylistException;

public class PlayListInputValidator {
	
    /**
     * Validates playList is not null before any addition or removal operation.
     */
	public boolean validPlayList(PlayList playList) {
    	return playList!=null;
    }
    
	/**
	 * Validates Track list to be added is not null or empty.
	 */
     public boolean validTracksToBeAdded(List<Track> tracksToAdd) {
        return (tracksToAdd!=null && !tracksToAdd.isEmpty());
    }

	/**
	 * If index is out of bounds, Tracks must be added at the end of the
	 * playList, so update the input index.
	 */
     public int validateAndFixIndex(int toIndex, PlayList playList) {
        int currentMaxIndex=playList.getNrOfTracks()-1;
        int finalIndex=toIndex;
        if(toIndex<0 || toIndex>currentMaxIndex){
        	finalIndex=currentMaxIndex+1;
        }
        return finalIndex;
	}
     
 	/** 
 	 *  Validates current playList Track limit.
 	 *  We should not allow greater than defined limit of tracks in new playList. 
 	 */
 	public void validateMaxTracksLimitInPlayList(PlayList playList, List<Track> tracksToAdd) {
 		int numberOfExistingTracks = playList.getNrOfTracks();
 		int numberOfTracksToBeAdded = tracksToAdd.size();
 		int targetPlayListSize = numberOfExistingTracks + numberOfTracksToBeAdded;
 		if (targetPlayListSize > PlayList.MAX_TRACKS_LIMIT) {
 			throw new PlaylistException("Playlist cannot have more than " + PlayList.MAX_TRACKS_LIMIT + " tracks");
 		}
 	}
	
    /**
     * Validates playList should not be null or empty for removal of tracks.
     */
     public boolean validPlayListForRemoval(PlayList playList){
        return validPlayList(playList)||!playList.isEmpty();
    }
     
    /**
      * Validates input index list for removal is null or empty.
     */
     public boolean validIndexToBeRemoved(List<Integer> indexList) {
         return (indexList!=null && !indexList.isEmpty());
     }
     
     /**
      * Validates if input index for removal is out of bounds.
     */
     public boolean validIndex(int index,PlayList playList) {
         return (index>=0 && index<playList.getPlayListTracks().size());
     }
}
