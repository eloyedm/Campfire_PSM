package com.example.android.campfire;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Networking extends AsyncTask<Object, Integer, Object> {
    static final String SERVER_PATH = "http://192.168.1.65/campfire/services.php";
    static final int TIMEOUT = 3000;

    Context m_context;
    ProgressDialog m_progressDialog;

    public Networking(Context m_context){
        this.m_context = m_context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        m_progressDialog = new ProgressDialog(m_context);
        m_progressDialog.setTitle("Conectando");
        m_progressDialog.setMessage("Espera...");
        m_progressDialog.setCancelable(false);
        m_progressDialog.show();
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        m_progressDialog.dismiss();
    }

    @Override
    protected Object doInBackground(Object... params) {
        String action = (String)params[0];
        if (action.equals("getPlaces")){
            List<Place> places = getPLaces();
            NetCallback netCallback = (NetCallback) params[1];
            netCallback.onWorkFinish(places);
        }
        else if(action.equals("signup")){
            int signupCode = validateSignup((String)params[1], (String)params[2], (String)params[3]);
            NetCallback netCallback = (NetCallback) params[4];
            netCallback.onWorkFinish(signupCode);
        }
        return null;
    }

    public List<Place> getPLaces(){
        List<Place> places = new ArrayList<>();
        String postParams = "&action=getplaces";
        String response = "";
        HttpURLConnection conn = null;
        URL url = null;
        try{
            url = new URL(SERVER_PATH);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(postParams.getBytes().length);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(postParams.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String jsonResponse = inputStreamToString(in);
            try {
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String name = object.getString("name");
                    Double lat = object.getDouble("latitud");
                    Double lng = object.getDouble("longitud");
                    String creator = object.getString("username");
                    List<Feature> features = new ArrayList<>();
                    features.add(new Feature("Baño", 1));
                    features.add(new Feature("Agua", 3));
                    places.add(new Place(name, new LatLng(lat, lng), creator, features, ""));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
         catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }

    public int validateSignup(String user, String mail, String password){
        int signupCode = 0;
        String postParams = "&action=signup";
        postParams += "&user=" + user;
        postParams += "&mail=" + mail;
        postParams += "&password=" + password;
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(SERVER_PATH);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(postParams.getBytes().length);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(postParams.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String jsonResponse = inputStreamToString(in);
            try {

                JSONObject jsonObject = new JSONObject(jsonResponse);
                signupCode = jsonObject.getInt("code");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
         catch (IOException e) {
            e.printStackTrace();
        }
        return signupCode;
    }

    private String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder response = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while((rLine = rd.readLine()) != null)
            {
                response.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
