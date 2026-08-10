package com.nemesis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent lock = new Intent(context, LockscreenActivity.class);
            lock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lock);
            context.startService(new Intent(context, FirebaseService.class));
        }
    }
}
