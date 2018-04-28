package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "workflows")

public class Search {

    @Attribute(name = "query", required = false)
    String query;

    @Attribute(name = "type", required = false)
    String type;

    @ElementList(name = "workflow", inline = true, required = false)
    List<Workflow> WorkflowList;

    public String getQuery() {
        return query;
    }

    public String getType() {
        return type;
    }

    public List<Workflow> getWorkflowList() {
        return WorkflowList;
    }
}
