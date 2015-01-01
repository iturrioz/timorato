package net.iturrioz.timorato;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    static final String PREFS_NAME = "Timorato";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Button hasi = (Button) findViewById(R.id.hasi);
        hasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().remove("ESKUK").apply();
                irekiKoadernoa(null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String aurrekoa = prefs.getString("ESKUK", null);
        if (aurrekoa != null) {
            Button segi = (Button) findViewById(R.id.segi);
            segi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irekiKoadernoa(aurrekoa);
                }
            });
            segi.setEnabled(true);
        }
    }

    private void irekiKoadernoa(String partida) {
        Intent intent = new Intent(MainActivity.this, KoadernoaActivity.class);
        if (partida != null) {
            intent.putExtra("ESKUK", partida);
        }
        startActivity(intent);
    }
}
