package com.tidal.refactoring.playlist.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PlayListTrackList {
    private List<PlayListTrack> list= new ArrayList<>();

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public PlayListTrack get(int index){
        return list.get(index);
    }

    public boolean contains(PlayListTrack o) {
        return list.contains(o);
    }

    public Iterator<PlayListTrack> iterator() {
        return list.iterator();
    }

    public PlayListTrack[] toArray(PlayListTrack[] a) {
        return list.toArray(a);
    }

    public void add(PlayListTrack e) {
        add(list.size(),e);
    }
    
    public void add(int index,PlayListTrack e) {
        int existingIndex=list.indexOf(e);
        if(existingIndex==-1){
        	list.add(index,e);
        }
        reIndex();
    }

    private void reIndex() {
        for(int i=0;i<list.size();i++){
            list.get(i).setIndex(i);
        }
    }

    public PlayListTrack remove(int index) {
        PlayListTrack removed =list.remove(index);
        reIndex();
        return removed;
    }

    public boolean remove(PlayListTrack e) {
        boolean flag=list.remove(e);
        reIndex();
        return flag;
    }

    public boolean containsAll(Collection<PlayListTrack> c) {
        return list.containsAll(c);
    }

    public void clear() {
        list.clear();
    }
}
