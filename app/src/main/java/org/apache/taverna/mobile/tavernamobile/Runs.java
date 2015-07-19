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
    public static enum RUN_STATE { FAILED,FINISHED,RUNNING};


    public Runs(String run_name, String run_started_date, String run_ended_date, String state) {
        this.run_name = run_name;
        this.run_started_date = run_started_date;
        this.run_ended_date = run_ended_date;
        this.state=state;
    }

    public long getRun_id() {
        return run_id;
    }

    public void setRun_id(long run_id) {
        this.run_id = run_id;
    }

    public long getRun_workflow_id() {
        return run_workflow_id;
    }

    public void setRun_workflow_id(long run_workflow_id) {
        this.run_workflow_id = run_workflow_id;
    }

    public RUN_STATE getState() {
        if(state.equalsIgnoreCase("finished"))
            return RUN_STATE.FINISHED;
        else if (state.equalsIgnoreCase("failed"))
            return RUN_STATE.FAILED;
        else
            return RUN_STATE.RUNNING;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRun_name() {
        return run_name;
    }

    public void setRun_name(String run_name) {
        this.run_name = run_name;
    }

    public String getRun_started_date() {
        return run_started_date;
    }

    public void setRun_started_date(String run_started_date) {
        this.run_started_date = run_started_date;
    }

    public String getRun_author() {
        return run_author;
    }

    public void setRun_author(String run_author) {
        this.run_author = run_author;
    }

    public String getRun_ended_date() {
        return run_ended_date;
    }

    public void setRun_ended_date(String run_ended_date) {
        this.run_ended_date = run_ended_date;
    }
}
