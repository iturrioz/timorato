package net.iturrioz.timorato;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import net.iturrioz.timorato.domain.Eskue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KoadernoaActivity extends ActionBarActivity {
    final int[] JOKALARIK = {R.id.bat, R.id.bi, R.id.hiru, R.id.lau, R.id.bost};
    final int[] ESKUK = {R.id.bat_gora, R.id.bi_gora, R.id.hiru_gora, R.id.lau_gora, R.id.bost_gora, R.id.sei_gora,
            R.id.zazpi_gora, R.id.zortzi_urrek, R.id.zortzi_kopak, R.id.zortzi_ezpatak, R.id.zortzi_bastok,
            R.id.zazpi_behera, R.id.sei_behera, R.id.bost_behera, R.id.lau_behera, R.id.hiru_behera, R.id.bi_behera,
            R.id.bat_behera};

    Eskue[] eskuk = new Eskue[18];
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koadernoa);

        Bundle extras = getIntent().getExtras();
        String json = extras != null ? extras.getString("ESKUK") : null;
        if (json != null) {
            try {
                JSONObject obj = new JSONObject(json);
                JSONArray jsonArray = obj.getJSONArray("ESKUK");
                for (int i = 0; i < jsonArray.length(); i++) {
                    eskuk[i] = new Eskue(jsonArray.getString(i));
                    zerrendaEguneratu();
                    count++;
                }
            } catch (JSONException je) {
                Log.e("TIMORATO", "Ezin izan da gordetakoa ireki", je);
            }
        }

        Button hasi = (Button) findViewById(R.id.button);
        hasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gehituPuntuk();
            }
        });

        automatikokiJauziEgin();
    }

    private void automatikokiJauziEgin() {
        for (int i=0; i<5; i++) {
            gehituJauzia(i, R.id.eskatutakok);
            gehituJauzia(i, R.id.indakok);
        }
    }

    private void gehituJauzia(final int zenbakie, final int id) {
        ((EditText) jokalarinEskatuHartu(zenbakie).findViewById(id)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    jokalarinEskatuHartu(zenbakie < 4 ? zenbakie + 1 : 0).findViewById(id).requestFocus();
                }
            }
        });
    }

    private void gehituPuntuk() {
        int[] aurrekok = aurrekoPuntukHartu();
        int[] berrik = new int[5];
        int[] eskatutakok = new int[5];
        try {
            for (int i=0; i<5; i++) {
                int gaizkiIndakok = zenbatGaizki(i);
                eskatutakok[i] = eskatutakokHartu(i);
                if (gaizkiIndakok == 0) {
                    int indakok = indakokHartu(i);
                    berrik[i] = aurrekok[i] + (10 + indakok * 5) * urrenBiderkatzailea();
                } else {
                    berrik[i] = aurrekok[i] + gaizkiIndakok * 5 * urrenBiderkatzailea();
                }
            }
            eskuk[count] = new Eskue(eskatutakok, berrik);
            zerrendaEguneratu();
            count++;
            garbituEskatu();
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Begiratu ea zenbakiren bat falta den edo gaizki dagoen.", Toast.LENGTH_SHORT).show();
        }
    }

    private void zerrendaEguneratu() {
        View lerroa = findViewById(ESKUK[count]);
        Eskue eskue = eskuk[count];
        for (int i=0; i<5; i++) {
            zutabeaEguneratu(lerroa.findViewById(JOKALARIK[i]), eskue.getEskatutakok()[i], eskue.getPuntuk()[i]);
        }
        if (count == eskuk.length) {
            findViewById(R.id.button).setEnabled(false);
        }
    }

    private void garbituEskatu() {
        ((CheckBox)findViewById(R.id.urrek)).setChecked(false);
        for (int i=0; i<5; i++) {
            View eskatu = jokalarinEskatuHartu(i);
            ((TextView) eskatu.findViewById(R.id.eskatutakok)).setText("");
            ((TextView) eskatu.findViewById(R.id.indakok)).setText("");
        }
    }

    private void zutabeaEguneratu(View zutabea, int eskatutakok, int puntuk) {
        setTextViewText(zutabea, R.id.eskatuta, eskatutakok);
        setTextViewText(zutabea, R.id.puntuk, puntuk);
    }

    private void setTextViewText(View view, int id, int value) {
        ((TextView)view.findViewById(id)).setText(value + "");
    }

    private int urrenBiderkatzailea() {
        return ((CheckBox)findViewById(R.id.urrek)).isChecked() ? 2 : 1;
    }

    private int indakokHartu(int zenbakie) {
        return getEditViewValue(jokalarinEskatuHartu(zenbakie).findViewById(R.id.indakok));
    }

    private int eskatutakokHartu(int zenbakie) {
        return getEditViewValue(jokalarinEskatuHartu(zenbakie).findViewById(R.id.eskatutakok));
    }

    private View jokalarinEskatuHartu(int zenbakie) {
        return findViewById(R.id.eskatzen).findViewById(JOKALARIK[zenbakie]);
    }

    private int zenbatGaizki(int zenbakie) {
        int eskatutakok = eskatutakokHartu(zenbakie);
        int indakok = indakokHartu(zenbakie);
        return eskatutakok < indakok ? eskatutakok - indakok : indakok - eskatutakok;
    }

    private int getEditViewValue(View editView) {
        return Integer.parseInt(((TextView) editView).getText().toString());
    }

    private int[] aurrekoPuntukHartu() {
        return count == 0 ? new int[]{0,0,0,0,0} : eskuk[count - 1].getPuntuk();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (Eskue eskue : eskuk) {
                if (eskue != null) {
                    jsonArray.put(eskue.toJson());
                }
            }
            json.put("ESKUK", jsonArray);
            SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putString("ESKUK", json.toString()).apply();
        } catch (JSONException je) {
            Log.e("TIMORATO", "Ezin izan da gorde", je);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.laguntza);
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}