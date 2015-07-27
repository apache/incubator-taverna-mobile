package org.apache.taverna.mobile.fragments.workflowdetails;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.tavernamobile.TavernaPlayerAPI;
import org.apache.taverna.mobile.utils.WorkflowDownloadManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static org.apache.taverna.mobile.activities.DashboardMainActivity.APP_DIRECTORY_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private TextView runIdTextView,runNameTextView;
    private ImageButton status;
    private  TextView runStateTextView, runStartTime,runEndTime, runInputsText;
    private  Button downloadOutput,downloadLogs;
    private int run_id;
    private String run_output_url = "";
    private String run_logs_url = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RunFragment.
     */
    public static RunFragment newInstance() {
        RunFragment fragment = new RunFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_run_result, container, false);
         runIdTextView = (TextView) rootView.findViewById(R.id.textview_runid);
         runNameTextView = (TextView) rootView.findViewById(R.id.textView_runName);
         status = (ImageButton) rootView.findViewById(R.id.statusButton);
         runStateTextView = (TextView) rootView.findViewById(R.id.statusTextView);
         runStartTime = (TextView) rootView.findViewById(R.id.start_time);
         runEndTime = (TextView) rootView.findViewById(R.id.runfinish);
         runInputsText = (TextView) rootView.findViewById(R.id.runinputsTextView);
         downloadOutput = (Button) rootView.findViewById(R.id.buttonWorkflowDownloadOutput);
         downloadLogs = (Button) rootView.findViewById(R.id.downloadRunLogs);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.run_result, menu);
        return;
    }

    @Override
    public void onResume() {
        super.onResume();
        String runresult = getActivity().getIntent().getStringExtra("runresult");
        try{
            JSONObject resultObject = new JSONObject(runresult);
            String runName = resultObject.getString("name");
            run_id = (int) resultObject.get("id");
            String runId = ""+run_id;
            String runState = resultObject.getString("state");
            String runStarted = resultObject.getString("start_time");
            String runEnded = resultObject.getString("finish_time");
            String runInputs = resultObject.getString("inputs");

                runIdTextView.setText(runId);
                runNameTextView.setText(runName);

                if(runState.contains("Pending"))
                    status.setImageResource(android.R.drawable.presence_busy);
            else if (runState.contains("Running"))
                    status.setImageResource(android.R.drawable.presence_away);
            else if (runState.contains("Finished"))
                    status.setImageResource(android.R.drawable.presence_online);
            else if (runState.contains("Failed"))
                    status.setImageResource(android.R.drawable.presence_offline);
            else
                status.setImageResource(android.R.drawable.presence_invisible);

                runStateTextView.setText(runState);
                runStartTime.setHint(runStarted);
                runEndTime.setHint(runEnded);
                runInputsText.setText(runInputs);
                runInputsText.setMovementMethod(new ScrollingMovementMethod());

                downloadOutput.setOnClickListener(this);
                downloadLogs.setOnClickListener(this);
            reloadRunResult();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            reloadRunResult();
            return true;
        }
        if(id == android.R.id.home){
           //getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadRunResult(){
        Timer t = new Timer();
       // t.scheduleAtFixedRate(new RunTimerTask(getActivity(), run_id), 0, 5000);
        t.schedule(new RunTimerTask(getActivity(),run_id),1);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonWorkflowDownloadOutput:
                try {
                    System.out.println("output url: "+run_output_url);
                    if(run_output_url.isEmpty()){
                        Toast.makeText(getActivity(), "No run logs available", Toast.LENGTH_LONG).show();
                    }else {
                        new WorkflowDownloadManager(getActivity()).downloadWorkflow(
                                new File(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                                        APP_DIRECTORY_NAME + "/Runoutput/outputs", "/TavernaMobile/Runouput/outputs/")),
                                new TavernaPlayerAPI(getActivity()).PLAYER_RUN_URL + run_output_url.substring(0, 5));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error downloading run output",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.downloadRunLogs:
                try {
                    System.out.println("run logs: "+run_logs_url);
                    if(run_logs_url.isEmpty()){
                        Toast.makeText(getActivity(), "No run logs available", Toast.LENGTH_LONG).show();
                    }else {
                        new WorkflowDownloadManager(getActivity()).downloadWorkflow(
                                new File(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                                        APP_DIRECTORY_NAME + "/Runoutput/logs/", "/TavernaMobile/Runoutput/logs")),
                                new TavernaPlayerAPI(getActivity()).PLAYER_RUN_URL + run_logs_url.substring(0, 5));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error downloading run logs",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void updateRun(Context context, final JSONObject runInfo){
        if(null != runInfo)
            ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    runStartTime.setHint(runInfo.getString("start_time"));
                    runEndTime.setHint(runInfo.getString("finish_time"));
                    runStateTextView.setText(runInfo.getString("status_message"));

                if(runInfo.getString("status_message").contains("Pending"))
                    status.setImageResource(android.R.drawable.presence_busy);
                else if (runInfo.getString("status_message").contains("Running"))
                    status.setImageResource(android.R.drawable.presence_away);
                else if (runInfo.getString("status_message").contains("Finished"))
                    status.setImageResource(android.R.drawable.presence_online);
                else if (runInfo.getString("status_message").contains("Failed"))
                    status.setImageResource(android.R.drawable.presence_offline);
                else
                    status.setImageResource(android.R.drawable.presence_invisible);

                run_output_url = runInfo.has("outputs_zip")? runInfo.getString("outputs_zip"):"";
                run_logs_url = runInfo.has("log")?runInfo.getString("log"):"";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class RunTimerTask extends TimerTask {

        private Context context;
        private int runid;

        public RunTimerTask(Context context, int runID) {
            this.context = context;
            this.runid = runID;
        }

        @Override
        public void run() {
            //QUERY player for the continuous status of the workflow run and update the ui with the results
            StringBuffer sb = new StringBuffer();
            try {

                URL workflowurl = new URL(new TavernaPlayerAPI(this.context).PLAYER_RUN_URL+this.runid);
                HttpURLConnection connection = (HttpURLConnection) workflowurl.openConnection();

                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");
                connection.connect(); //send request

                InputStream dis = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(dis));

                String jsonData = "";
                while ((jsonData = br.readLine()) != null) {
                //json results of the full workflow details
                    sb.append(jsonData);
                }
                dis.close();
                br.close();
                connection.disconnect();

                JSONObject runInfo = new JSONObject(sb.toString());
                updateRun(this.context, runInfo);

            }catch (IOException ex){
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
