package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
/**
 * Created by ian on 16/01/17.
 */

@Root(name="inputDescription")
public class Inputs {

    @Attribute(name = "workflowId", required = false)
    private String workflowId;

    @Attribute(name = "workflowRun", required = false)
    private String workflowRun;

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public void setWorkflowRun(String workflowRun) {
        this.workflowRun = workflowRun;
    }

    public void setWorkflowRunId(String workflowRunId) {
        this.workflowRunId = workflowRunId;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    @Attribute(name = "workflowRunId", required = false)
    private String workflowRunId;

    public List<Input> getInputs() {
        return inputs;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public String getWorkflowRun() {
        return workflowRun;
    }

    public String getWorkflowRunId() {
        return workflowRunId;
    }

    @ElementList(inline = true, required = false)
    private List<Input> inputs;


}
