package net.iturrioz.timorato.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Eskue {
    int[] eskatutakok;

    int[] puntuk;

    public Eskue(int[] eskatutakok, int[] puntuk) {
        this.eskatutakok = eskatutakok;
        this.puntuk = puntuk;
    }

    public Eskue(final String json) throws JSONException {
        this.eskatutakok = new int[5];
        this.puntuk = new int[5];

        JSONObject obj = new JSONObject(json);
        JSONArray eskatutakokJson = obj.getJSONArray("ESKATUTAKOK");
        JSONArray puntukJson = obj.getJSONArray("PUNTUK");
        for (int i = 0; i < 5; i++) {
            eskatutakok[i] = eskatutakokJson.getInt(i);
            puntuk[i] = puntukJson.getInt(i);
        }
    }

    public int[] getEskatutakok() {
        return eskatutakok;
    }

    public int[] getPuntuk() {
        return puntuk;
    }

    public String toJson() throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray eskatutakokJson = new JSONArray();
        JSONArray puntukJson = new JSONArray();
        for (int eskatu : eskatutakok) {
            eskatutakokJson.put(eskatu);
        }
        for (int puntu : puntuk) {
            puntukJson.put(puntu);
        }
        json.put("ESKATUTAKOK", eskatutakokJson);
        json.put("PUNTUK", puntukJson);
        return json.toString();
    }
}
