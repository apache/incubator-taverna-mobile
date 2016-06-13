package org.apache.taverna.mobile.tavernamobile;

/**
 * Created by Larry Akah on 6/13/15.
 * Workflow runs for a given workflow
 */
public class Runs {
    private long run_id;
    private long run_workflow_id;
    private String run_name;
    private String run_started_date;
    private String run_ended_date;
    private String state;
    private String run_author;

    public Runs(String run_name, String run_started_date, String run_ended_date, String state) {
        this.run_name = run_name;
        this.run_started_date = run_started_date;
        this.run_ended_date = run_ended_date;
        this.state = state;
    }

    ;

    public long getRunId() {
        return run_id;
    }

    public void setrunId(long run_id) {
        this.run_id = run_id;
    }

    public long getRunWorkflowId() {
        return run_workflow_id;
    }

    public void setRunWorkflowId(long run_workflow_id) {
        this.run_workflow_id = run_workflow_id;
    }

    public RunState getState() {
        if (state.equalsIgnoreCase("finished")) {
            return RunState.FINISHED;
        } else if (state.equalsIgnoreCase("failed")) {
            return RunState.FAILED;
        } else {
            return RunState.RUNNING;
        }
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRunName() {
        return run_name;
    }

    public void setRunName(String run_name) {
        this.run_name = run_name;
    }

    public String getRunStartedDate() {
        return run_started_date;
    }

    public void setRunStartedDate(String run_started_date) {
        this.run_started_date = run_started_date;
    }

    public String getRunAuthor() {
        return run_author;
    }

    public void setRunAuthor(String run_author) {
        this.run_author = run_author;
    }

    public String getRunEndedDate() {
        return run_ended_date;
    }

    public void setRunEndedDate(String run_ended_date) {
        this.run_ended_date = run_ended_date;
    }

    public static enum RunState { FAILED, FINISHED, RUNNING }
}
