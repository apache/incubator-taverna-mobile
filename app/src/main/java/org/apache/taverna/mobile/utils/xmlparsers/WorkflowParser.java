package org.apache.taverna.mobile.utils.xmlparsers;

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.XMLParserException;
import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.fragments.WorkflowItemFragment;
import org.apache.taverna.mobile.tavernamobile.Workflow;

import java.util.List;

/**
 * Workflow end document class for detecting when the complete list of workflows have been read out
 * Created by Larry Akah on 6/24/15.
 */

public class WorkflowParser extends XMLParser {

    public WorkflowParser(IRule[] rules) throws IllegalArgumentException, XMLParserException {
        super(rules);
    }

    @Override
    protected void doEndDocument(Object userObject) {
        WorkflowItemFragment.updateWorkflowUI((List<Workflow>) userObject);
    }
}
