package com.tidal.refactoring.playlist;

import com.tidal.refactoring.playlist.dao.PlaylistDaoBean;
import com.tidal.refactoring.playlist.data.PlayList;
import com.tidal.refactoring.playlist.data.PlayListTrack;
import com.tidal.refactoring.playlist.data.PlayListTrackList;
import com.tidal.refactoring.playlist.data.Track;
import com.tidal.refactoring.playlist.exception.PlaylistException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class PlaylistBusinessBeanTest {

    @Mock
    PlaylistDaoBean playlistDaoBean;

    @InjectMocks
    PlaylistBusinessBean playlistBusinessBean;

    @BeforeClass
    public void classSetUp() {
        MockitoAnnotations.initMocks(this);
    }

    private String randomUUID;

    @BeforeMethod
    public void setUp() throws Exception {
        randomUUID=UUID.randomUUID().toString();
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }
    
    @DataProvider(name = "testAddTracks_OneTrackInBetween")
    public Object[][] createData_testAddTracks_OneTrackInBetween() {
     return new Object[][] {
       { new Integer(5), new Integer(1), new Integer(10) },
       { new Integer(10), new Integer(2), new Integer(15) }
     };
    }
    
	/**
	 * Test case that adds tracks more than the defined limit and validates that
	 * the proper Exception is thrown above defined limit.
	 */
    @Test(expectedExceptions = PlaylistException.class)
    public void testAddTracks_AboveLimitValidation(){
    	int numberOfTracks = 1;
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(createPlayList(randomUUID,500));
        playlistBusinessBean.addTracks(randomUUID, createSampleTrackListToAdd(numberOfTracks), 1);
    }

	/**
	 * Test case that adds one track in the middle of the playList and validates
	 * for successful addition of the track.
	 */
    @Test(dataProvider = "testAddTracks_OneTrackInBetween")
    public void testAddTracks_OneTrackInBetween(int index, int numberOfTracksToBeAdded, int numberOfTracksExisting){
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
 
        int oldPlayListSize = oldPlayList.getNrOfTracks();
        List<PlayListTrack> addedPlayListTracks = playlistBusinessBean.addTracks(randomUUID, createSampleTrackListToAdd(numberOfTracksToBeAdded), index);
        PlayListTrack playListTrack=null;
        Iterator<PlayListTrack> playListTrackIterator=oldPlayList.iterator();
        int newPlayListSize =0;
        while(playListTrackIterator.hasNext()){
            PlayListTrack track=playListTrackIterator.next();
            if(track.getIndex()==index){
                playListTrack=track;
            }
            newPlayListSize++;
        }
        assertEquals(newPlayListSize, oldPlayListSize + addedPlayListTracks.size());
        assertTrackEqual(playListTrack.getTrack(), addedPlayListTracks.get(0).getTrack());
    }
    
	/**
	 * Test case that adds multiple tracks in the middle of the playList and
	 * validates for successful addition of all the tracks.
	 */
	@Test
	public void testAddTracks_MultipleTracksInBetween() throws Exception {
        int index =10;
        int numberOfTracksToBeAdded =2;
    	int numberOfTracksExisting =20;
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
 
        int oldPlayListSize = oldPlayList.getNrOfTracks();
        List<PlayListTrack> addedPlayListTracks = playlistBusinessBean.addTracks(randomUUID, createSampleTrackListToAdd(numberOfTracksToBeAdded), index);
        PlayListTrack playListTrack1=null;
        PlayListTrack playListTrack2=null;
        Iterator<PlayListTrack> playListTrackIterator=oldPlayList.iterator();
        int newPlayListSize =0;
        while(playListTrackIterator.hasNext()){
            PlayListTrack track=playListTrackIterator.next();
            if(track.getIndex()==index){
                playListTrack1=track;
            }else if(track.getIndex()==index+1){
            	playListTrack2=track;
            }
            
            newPlayListSize++;
        }
        assertEquals(newPlayListSize, oldPlayListSize + addedPlayListTracks.size());
        assertTrackEqual(playListTrack1.getTrack(), addedPlayListTracks.get(0).getTrack());
        assertTrackEqual(playListTrack2.getTrack(), addedPlayListTracks.get(1).getTrack());
	}
	
	
	/**
	 * Test case that adds Tracks at the end of the playList when index is
	 * greater than the current size of the playList and validates that the
	 * track is successfully added to the playList
	 */
	@Test
	public void testAddTracks_WithIndexGreaterThanCurrentPlaylistSize() throws Exception {
        int numberOfTracksToBeAdded =1;
    	int numberOfTracksExisting =30;
    	int index =numberOfTracksExisting+5; /** Assigning index with value greater than current size of the playList*/
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        int oldPlayListSize = oldPlayList.getNrOfTracks();
        List<PlayListTrack> addedPlayListTracks = playlistBusinessBean.addTracks(randomUUID, createSampleTrackListToAdd(numberOfTracksToBeAdded), index);
        PlayListTrack playListTrack=null;
        Iterator<PlayListTrack> playListTrackIterator=oldPlayList.iterator();
        int newPlayListSize =0;
        while(playListTrackIterator.hasNext()){
            PlayListTrack track=playListTrackIterator.next();
            if(track.getIndex()==oldPlayListSize){ /** New Track will be added at the end of the playList so new track index equals to oldPlayListSize*/
                playListTrack=track;
            }
            newPlayListSize++;
        }
        assertEquals(newPlayListSize, oldPlayListSize + addedPlayListTracks.size());
        assertTrackEqual(playListTrack.getTrack(), addedPlayListTracks.get(0).getTrack());
	}
	
	/**
	 * Test case that adds track at the end of the playList when provided index is negative.
	 */
	@Test
	public void testAddTracks_WithNegativeIndex() throws Exception {
        int numberOfTracksToBeAdded =1;
    	int numberOfTracksExisting =5;
    	int negativeIndex =-10;
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
 
        int oldPlayListSize = oldPlayList.getNrOfTracks();
        
        List<PlayListTrack> addedPlayListTracks = playlistBusinessBean.addTracks(randomUUID, createSampleTrackListToAdd(numberOfTracksToBeAdded), negativeIndex);

        PlayListTrack playListTrack=null;
        Iterator<PlayListTrack> playListTrackIterator=oldPlayList.iterator();
        int newPlayListSize =0;
        while(playListTrackIterator.hasNext()){
            PlayListTrack track=playListTrackIterator.next();
            if(track.getIndex()==oldPlayListSize){ /** New Track will be added at the end of the playList so new track index equals to oldPlayListSize*/
                playListTrack=track;
            }
            newPlayListSize++;
        }
        assertEquals(newPlayListSize, oldPlayListSize + addedPlayListTracks.size());
        assertTrackEqual(playListTrack.getTrack(), addedPlayListTracks.get(0).getTrack());
	}
	
	/**
	 * Test case that validates duplicate Tracks not added in the playList.
	 */
    @Test
    public void testAddTracks_DoNotAddDuplicateTrack(){
    	int numberOfTracks =1;
    	int index =1;
    	PlayList oldPlayList=createPlayList(randomUUID,10);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        int oldPlayListSize = oldPlayList.getNrOfTracks();
        List<Track> duplicateTrackListToAdd = createSampleTrackListToAdd(numberOfTracks);
        playlistBusinessBean.addTracks(randomUUID, duplicateTrackListToAdd, index);
        playlistBusinessBean.addTracks(randomUUID, duplicateTrackListToAdd, index+1); /** Trying to add same TrackList again, it should not add */
        int newPlayListSize = oldPlayList.getNrOfTracks();
        assertEquals(oldPlayListSize+1, newPlayListSize);
    }
	
	/**
	 * Test case that validates playList duration is updated 
	 * after addition of tracks
	 */
	@Test
	public void testAddTracks_VerifyPlayListDuration() throws Exception {
        int numberOfTracksToBeAdded =2;
    	int numberOfTracksExisting =10;
    	int index =8;
    	float durationOfTracksTobeAdded=0;
    	float oldPlayListDuration =0;
    	float newPlayListDuration=0;
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        List<Track> trackListToBeAdded = createSampleTrackListToAdd(numberOfTracksToBeAdded);
        for(Track track:trackListToBeAdded){
        	durationOfTracksTobeAdded =durationOfTracksTobeAdded+track.getDuration();
        }
        oldPlayListDuration = oldPlayList.getDuration();
        playlistBusinessBean.addTracks(randomUUID, trackListToBeAdded, index);
        newPlayListDuration = oldPlayList.getDuration();
        assertEquals(oldPlayListDuration + durationOfTracksTobeAdded, newPlayListDuration);
	}
	
	/**
	 * Test case that removes track from the playList and validates that Tracks
	 * are successfully removed
	 */
	@Test
	public void testRemoveTracks() throws Exception {
		int removeFromIndex = 2;
		int numberOfTracksExisting =5;
		List<Integer> removeTrackIndexList = new ArrayList<Integer>();
		removeTrackIndexList.add(removeFromIndex);
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        Track trackToBeRemoved = oldPlayList.get(removeFromIndex).getTrack();
        int oldPlayListSize = oldPlayList.getNrOfTracks();
        List<PlayListTrack> removedTracks = playlistBusinessBean.removeTracks(randomUUID,removeTrackIndexList);
        int newPlayListSize = oldPlayList.getNrOfTracks();
        Track trackRemoved =  removedTracks.get(0).getTrack();
        assertEquals(oldPlayListSize-removedTracks.size(), newPlayListSize);
	    assertTrackEqual(trackRemoved, trackToBeRemoved);
	}
	
	/**
	 * Test case that validates playList duration is updated after removal of
	 * tracks.
	 */
	@Test
	public void testRemoveTracks_VerifyPlayListDuration() throws Exception {
		int removeFromIndex = 2;
		int numberOfTracksExisting =5;
		float oldPlayListDuration=0;
		float newPlayListDuration=0;
		float durationOfTracksRemoved=0;
		List<Integer> removeTrackIndexList = new ArrayList<Integer>();
		removeTrackIndexList.add(removeFromIndex);
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        oldPlayListDuration = oldPlayList.getDuration();
        List<PlayListTrack> removedTracks = playlistBusinessBean.removeTracks(randomUUID,removeTrackIndexList);
        newPlayListDuration = oldPlayList.getDuration();
        for(PlayListTrack playListTrack: removedTracks){
        	durationOfTracksRemoved = durationOfTracksRemoved+playListTrack.getTrack().getDuration();
        }
		assertEquals(oldPlayListDuration - durationOfTracksRemoved, newPlayListDuration);
	}
	
    @Test(expectedExceptions = PlaylistException.class)
    public void testRemoveTracks_ValidationErrorForInvalidIndex(){
    	int invalidIndex = 8;
    	int numberOfTracksExisting =5;
		List<Integer> removeTrackIndexList = new ArrayList<Integer>();
		removeTrackIndexList.add(invalidIndex);
        PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        playlistBusinessBean.removeTracks(randomUUID,removeTrackIndexList);
    }
	
	/**
	 * Test case that validates reIndexing happened properly after removal of
	 * tracks.
	 */
    @Test
    public void testRemoveTracks__ValidateReIndexing(){
		int removeFromIndex = 2;
		int numberOfTracksExisting =10;
		List<Integer> removeTrackIndexList = new ArrayList<Integer>();
		removeTrackIndexList.add(removeFromIndex);
		removeTrackIndexList.add(removeFromIndex+4);
        PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        playlistBusinessBean.removeTracks(randomUUID,removeTrackIndexList);
        int[]result= new int[8];
        int[]expected={0,1,2,3,4,5,6,7};
        Iterator<PlayListTrack> trackIterator=oldPlayList.iterator();
        int i=0;
        while (trackIterator.hasNext()){
            result[i++]=trackIterator.next().getIndex();
        }
        assertEquals(result,expected);
    }
    
	/**
	 * Test case that validates performance of AddTracks method 
	 */
    @Test
    public void testAddTracks_ValidatePerformance(){
        int numberOfTracksToBeAdded =300;
    	int numberOfTracksExisting =200;
    	int index =5;
    	PlayList oldPlayList=createPlayList(randomUUID,numberOfTracksExisting);
        Mockito.when(playlistDaoBean.getPlaylistByUUID(randomUUID)).thenReturn(oldPlayList);
        List<Track> tracksToBeAdded=createSampleTrackListToAdd(numberOfTracksToBeAdded);
        int oldPlayListSize = oldPlayList.getNrOfTracks();
        long startTime = System.nanoTime();
        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(randomUUID,tracksToBeAdded , index);
        long endTime = System.nanoTime();
        int newPlayListSize = oldPlayList.getNrOfTracks();
        assertEquals(oldPlayListSize+playListTracks.size(),newPlayListSize);
        Assert.assertTrue((endTime - startTime)<50000000);
    }

	private void assertTrackEqual(Track newAddedTrack, Track expectedTrack) {
		assertTrue(newAddedTrack.equals(expectedTrack));
	}
	
    private List<Track> createSampleTrackListToAdd(int numberOfTracks){
        List<Track> trackList=new ArrayList<>();
        for(int i=1;i<=numberOfTracks;i++){
            trackList.add(getTrack(76868+i));
        }
        return trackList;
    }

    private PlayList createPlayList(String uuid,int numberOfTracks) {
    	PlayList trackPlayList = new PlayList();
        trackPlayList.setDeleted(false);
        trackPlayList.setDuration((float) (60 * 60 * 2));
        trackPlayList.setId(49834);
        trackPlayList.setLastUpdated(new Date());
        trackPlayList.setNrOfTracks(376);
        trackPlayList.setPlayListName("Collection of great songs");
        trackPlayList.setPlayListTracks(getPlaylistTracks(numberOfTracks));
        trackPlayList.setUuid(uuid);
        return trackPlayList;
    }

    private static PlayListTrackList getPlaylistTracks(int numberOfTracks) {
    	PlayListTrackList playListTracks= new PlayListTrackList();
        for (int i = 0; i < numberOfTracks; i++) {
            PlayListTrack playListTrack = new PlayListTrack();
            playListTrack.setDateAdded(new Date());
            playListTrack.setId(i + 1);
            playListTrack.setIndex(i);
            playListTrack.setTrack(getTrack(i+1));
            playListTracks.add(playListTrack);
        }
        return playListTracks;
    }

    private static Track getTrack(int id) {
        Random randomGenerator = new Random();
        Track track = new Track();
        track.setArtistId(randomGenerator.nextInt(10000));
        track.setDuration(60 * 3);
        int trackNumber = randomGenerator.nextInt(15);
        track.setTitle("Track no: " + trackNumber);
        track.setId(id);
        return track;
    }
}