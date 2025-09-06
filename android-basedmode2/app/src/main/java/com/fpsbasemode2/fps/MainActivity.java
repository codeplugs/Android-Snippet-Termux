package com.fpsbasemode2.fps;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    TextView statusText;
    Button runButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        runButton = findViewById(R.id.runButton);

        runButton.setOnClickListener(v -> {
            statusText.setText("Running...");
            new Thread(this::overrideBaseMode).start();
        });
    }

    private void overrideBaseMode() {
        try {
            // Jalankan perintah root
            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "service call SurfaceFlinger 13 i32 2"});
            p.waitFor();

            // Cek baseModeId
            Process dump = Runtime.getRuntime().exec(new String[]{"su", "-c", "dumpsys display | grep -i baseModeId"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(dump.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            runOnUiThread(() -> statusText.setText("Done:\n" + output.toString()));
        } catch (Exception e) {
            runOnUiThread(() -> statusText.setText("Error:\n" + e.getMessage()));
        }
    }
}