// MusicAIDL.aidl
package com.nancy.cs478_p5_musicaidl;


   interface MusicAIDL {
            String getSongUrl(int id);
            Bundle getAllSongs();
            Bundle getSingleSong(int index);
             }