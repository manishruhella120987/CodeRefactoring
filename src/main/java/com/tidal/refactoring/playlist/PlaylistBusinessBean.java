package com.tidal.refactoring.playlist;

import com.google.inject.Inject; 
import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.dao.PlayListInputValidator;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.exception.PlaylistException;

import java.util.*;

public class PlaylistBusinessBean {

    private PlaylistDaoBean playlistDaoBean;

    @Inject
    public PlaylistBusinessBean(PlaylistDaoBean playlistDaoBean){
        this.playlistDaoBean = playlistDaoBean;
    }
    
	/**
	 * Method to add Tracks at a given index 
	 * and returns Added tracks.
	 */
    public List<PlayListTrack> addTracks(String uuid, List<Track> tracksToAdd, int toIndex) throws PlaylistException {

        try {
            PlayList playList = playlistDaoBean.getPlaylistByUUID(uuid);
            PlayListInputValidator inputValidator = new PlayListInputValidator();
            /** If track list to be added is null or playList is null then return empty list */
            if(!inputValidator.validTracksToBeAdded(tracksToAdd) || !inputValidator.validPlayList(playList)){
                return Collections.emptyList();
            }
            inputValidator.validateMaxTracksLimitInPlayList(playList,tracksToAdd);

            toIndex = inputValidator.validateAndFixIndex(toIndex, playList);
            
            List<PlayListTrack> addedPlayListTracks = new ArrayList<>(tracksToAdd.size());
            
			for (Track track : tracksToAdd) {
				addedPlayListTracks.add(addTrackToPlayList(playList,track,toIndex));
				toIndex++;
			}
            return addedPlayListTracks;
        } catch (Exception e) {
            e.printStackTrace();
            /** log4j can be used to create Logger and set multiple log levels like error, debug, warning*/
            throw new PlaylistException("Generic error");
        }
    }

	private PlayListTrack addTrackToPlayList(PlayList playList, Track track, int toIndex) {
		PlayListTrack playListTrack = PlayListTrack.generateNewPlayListTrack(playList, track);
		playList.add(toIndex, playListTrack);
		playList.setDuration(addTrackDurationToPlaylist(playList, track));
		//playList.setNrOfTracks(playList.getPlayListTracks().size());
		return playListTrack;
	}

	/**
	 * Method to remove tracks from the playList located at the sent indexes
	 * @param uuid
	 * @param indexes
	 * @return : List of PlaylistTrack
	 * @throws PlaylistException
	 */
	public List<PlayListTrack> removeTracks(String uuid, List<Integer> indexes) throws PlaylistException {
		PlayListInputValidator inputValidator = new PlayListInputValidator();
		List<PlayListTrack> removedTracks= new ArrayList<>(indexes.size());
        PlayList playList = playlistDaoBean.getPlaylistByUUID(uuid);
        if(!inputValidator.validPlayListForRemoval(playList) ||!inputValidator.validIndexToBeRemoved(indexes)){
            return Collections.emptyList();
        }
        for(Integer index:indexes){
            if(inputValidator.validIndex(index,playList)){
            	playList.setDuration(removeTrackDurationFromPlaylist(playList, playList.get(index).getTrack()));    
            	removedTracks.add(playList.remove(index));
            }else{
                throw new PlaylistException("Invalid index "+index+" provided for playlist with id "+uuid);
            }
        }
        return removedTracks;
    }

    private float addTrackDurationToPlaylist(PlayList playList, Track track) {
        return (track != null ? track.getDuration() : 0)
                + (playList != null && playList.getDuration() != null ? playList.getDuration() : 0);
    }
    
	private float removeTrackDurationFromPlaylist(PlayList playList, Track track) {
		return Math.abs((playList != null && playList.getDuration() != null ? playList.getDuration() : 0)
				- (track != null ? track.getDuration() : 0));
	}  
}