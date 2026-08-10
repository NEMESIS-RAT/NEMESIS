package com.nemesis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, FirebaseService.class));

        Button btnLock = findViewById(R.id.btnLock);
        Button btnRansom = findViewById(R.id.btnRansom);
        Button btnAppBuild = findViewById(R.id.btnAppBuild);
        Button btnDashboard = findViewById(R.id.btnDashboard);

        btnLock.setOnClickListener(v -> startActivity(new Intent(this, LockscreenActivity.class)));
        btnRansom.setOnClickListener(v -> startActivity(new Intent(this, RansomActivity.class)));
        btnAppBuild.setOnClickListener(v -> startActivity(new Intent(this, AppBuildActivity.class)));
        btnDashboard.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
    }
}
