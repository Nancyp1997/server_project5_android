package com.nancy.cs478_p5_musiccentral;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.nancy.cs478_p5_musicaidl.MusicAIDL;

public class MusicService extends Service {
    String CHANNEL_ID = "MusicPlayer";
    int NOTIFICATION_ID = 999;
    Notification notification;
       String[]  urls   = {
               "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
               "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-16.mp3","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-15.mp3"};
//            "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.01.mp3",
//            "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.02.mp3",
//            "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.03.mp3",
//            "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.04.mp3",
//            "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.05.mp3",
//            "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.06.mp3",
//            "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.07.mp3"};
    private int songImgs[] =  {
            R.drawable.img1,
            R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5,
//            R.drawable.img6, R.drawable.img7
    };
    String[] song_names = {"Foundation Rock 1",
//            "Foundation Rock 2","Foundation Rock 3",
            "Foundation Rock 4","Foundation Rock 5","Foundation Rock 6","Foundation Rock 7"};
    String[] singers = {"Walter Everett",
//            "Walter Everett","Walter Everett",
            "Walter Everett","Walter Everett","Walter Everett","Walter Everett"};

    private final MusicAIDL.Stub mBinder = new MusicAIDL.Stub() {
        @Override
        public String getSongUrl(int id) throws RemoteException {
            synchronized (this) {
                return urls[id];
            }
        }

        //ALL SONG INFORMATION
        @Override
        public Bundle getAllSongs() throws RemoteException {
            synchronized (this) {
                Bundle bundle = new Bundle();
                //Convert Image into bitmap
                Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.img1);
                Bitmap icon2 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.img2);
                Bitmap icon3 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.img3);
                Bitmap icon4 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.img4);
                Bitmap icon5 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.img5);
//                Bitmap icon6 = BitmapFactory.decodeResource(getResources(),
//                        R.drawable.img6);
//                Bitmap icon7 = BitmapFactory.decodeResource(getResources(),
//                        R.drawable.img7);

                //Adding data to bundle
                bundle.putParcelable("img1", icon1);
                bundle.putParcelable("img2", icon2);
                bundle.putParcelable("img3", icon3);
                bundle.putParcelable("img4", icon4);
                bundle.putParcelable("img5", icon5);
//                bundle.putParcelable("img6", icon5);
//                bundle.putParcelable("img7", icon5);
                bundle.putStringArray("songnames", song_names);
                bundle.putStringArray("singername", singers);
                bundle.putStringArray("songurl", urls);
                return bundle;
            }
        }

        //Get One Sog Information
        @Override
        public Bundle getSingleSong(int index) throws RemoteException {
            synchronized (this) {
                Bundle bundle = new Bundle();
                //Adding song data into bundle
                Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                        songImgs[index]);
                bundle.putParcelable("oneimage", icon1);
                bundle.putString("songnames", song_names[index]);
                bundle.putString("singername", singers[index]);
                bundle.putString("SONGURL", urls[index]);
                return bundle;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("NANCY","Inside onbind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.createNotificationChannel();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.nancy.cs478_p5_musicclient",
                "com.nancy.cs478_p5_musicclient.MainActivity"));
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0) ;
        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setTicker("Music is playing!")
                .build();
        startForeground(NOTIFICATION_ID,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        CharSequence name = "MP notfcn";
        String description = "Channel for music player notfcns";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(description);
        }
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}