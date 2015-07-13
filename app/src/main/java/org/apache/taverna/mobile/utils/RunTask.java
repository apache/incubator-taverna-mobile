package org.apache.taverna.mobile.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.taverna.mobile.activities.RunResult;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 7/11/15.
 */
public class RunTask extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog progressDialog;

    public RunTask(Context ctx) {
        this.context = ctx;
        progressDialog = new ProgressDialog(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
            progressDialog.setMessage("Creating new run for the workflow");
            progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuffer sb = new StringBuffer();
        try {
            TavernaPlayerAPI tavernaPlayerAPI = new TavernaPlayerAPI();
            URL workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_RUN_URL);
            HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();
            String userpass = tavernaPlayerAPI.getPlayerUserName(this.context) + ":" + tavernaPlayerAPI.getPlayerUserPassword(this.context);
            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);

            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            // connection.setDoInput(true);
            //  connection.setDoOutput(true);
            connection.connect(); //send request

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(params[0]);//write post data which is a formatted json data representing inputs to a run

            dos.flush();
            dos.close();

            InputStream dis = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));

            String jsonData = "";
            while ((jsonData = br.readLine()) != null) {
                sb.append(jsonData);
                //
            }
            dis.close();
            br.close();

            return sb.toString();

        }catch (IOException ex){
            ex.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("RUN OutPut", s);
            progressDialog.dismiss();
        Intent runIntent = new Intent();
        runIntent.setClass(this.context, RunResult.class);
        runIntent.putExtra("runresult", s);
        this.context.startActivity(runIntent);
    }
}
