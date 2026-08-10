package com.nemesis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.OpenableColumns;

public class AppBuildActivity extends AppCompatActivity {

    private TextView tvApkName, tvStatus;
    private Button btnPickApk, btnInject;
    private Uri selectedApkUri;
    private String selectedApkName;

    private final ActivityResultLauncher<Intent> pickApkLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                selectedApkUri = result.getData().getData();
                if (selectedApkUri != null) {
                    selectedApkName = getFileName(selectedApkUri);
                    tvApkName.setText(selectedApkName);
                    tvStatus.setText("◆ APK siap di-inject");
                    btnInject.setEnabled(true);
                    Toast.makeText(this, "APK dipilih: " + selectedApkName, Toast.LENGTH_SHORT).show();
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_build);

        tvApkName = findViewById(R.id.tvApkName);
        tvStatus = findViewById(R.id.tvStatus);
        btnPickApk = findViewById(R.id.btnPickApk);
        btnInject = findViewById(R.id.btnInject);

        btnPickApk.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.android.package-archive");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            pickApkLauncher.launch(intent);
        });

        btnInject.setOnClickListener(v -> {
            if (selectedApkUri == null) {
                Toast.makeText(this, "Pilih APK dulu!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/vnd.android.package-archive");
            shareIntent.putExtra(Intent.EXTRA_STREAM, selectedApkUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Kirim APK ke Target"));
        });
    }

    private String getFileName(Uri uri) {
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    cursor.close();
                    return name;
                }
                cursor.close();
            }
        }
        String path = uri.getPath();
        if (path != null) {
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash != -1) return path.substring(lastSlash + 1);
        }
        return "unknown.apk";
    }
}
