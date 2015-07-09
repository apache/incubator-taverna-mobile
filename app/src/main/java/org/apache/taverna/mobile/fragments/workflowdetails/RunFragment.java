package org.apache.taverna.mobile.fragments.workflowdetails;


import android.os.Bundle;
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

import org.apache.taverna.mobile.R;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private static int RUNID;
    private TextView runIdTextView,runNameTextView;
    private ImageButton status;
    private  TextView runStateTextView, runStartTime,runEndTime, runInputsText;
    private  Button downloadOutput,downloadLogs;

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
        RUNID = 0 ;
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
            String runId = ""+resultObject.get("id");
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
            //TODO start task to retrieve current status of workflow and further details
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadRunResult(){
        //TODO reload the run results when refreshed
    }

    @Override
    public void onClick(View view) {

    }
}
