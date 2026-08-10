package com.nemesis;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class DashboardActivity extends AppCompatActivity {

    private EditText etNewPin;
    private Button btnSavePin;
    private TextView tvStatus, tvCurrentPin;
    private DatabaseReference settingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        etNewPin = findViewById(R.id.etNewPin);
        btnSavePin = findViewById(R.id.btnSavePin);
        tvStatus = findViewById(R.id.tvStatus);
        tvCurrentPin = findViewById(R.id.tvCurrentPin);

        String url = "https://nemesisc2-default-rtdb.firebaseio.com/";
        settingsRef = FirebaseDatabase.getInstance(url).getReference("settings");

        loadCurrentPin();

        btnSavePin.setOnClickListener(v -> {
            String newPin = etNewPin.getText().toString().trim();
            if (newPin.isEmpty() || newPin.length() < 4) {
                tvStatus.setText("❌ PIN minimal 4 digit!");
                return;
            }

            settingsRef.child("pin").setValue(newPin)
                .addOnSuccessListener(aVoid -> {
                    tvStatus.setText("✅ PIN berhasil diubah!");
                    tvCurrentPin.setText("PIN saat ini: " + newPin);
                    etNewPin.setText("");
                    Toast.makeText(DashboardActivity.this, "PIN disimpan!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> tvStatus.setText("❌ Gagal: " + e.getMessage()));
        });
    }

    private void loadCurrentPin() {
        settingsRef.child("pin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String pin = snapshot.getValue(String.class);
                if (pin != null) tvCurrentPin.setText("PIN saat ini: " + pin);
                else settingsRef.child("pin").setValue("123456");
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
