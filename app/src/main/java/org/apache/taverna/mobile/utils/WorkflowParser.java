package org.apache.taverna.mobile.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.XMLParserException;
import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.adapters.WorkflowAdapter;
import org.apache.taverna.mobile.fragments.WorkflowItemFragment;
import org.apache.taverna.mobile.tavernamobile.Workflow;

import java.util.List;

/**
 * Workflow end document class for detecting when the complete list of workflows have been read out
 * Created by Larry Akah on 6/24/15.
 */
public class WorkflowParser extends XMLParser implements WorkflowDataCallback{

    private RecyclerView recyclerView;
    private Context mcontext;

    public WorkflowParser(IRule[] rules) throws IllegalArgumentException, XMLParserException {
        super(rules);
    }

    public WorkflowParser(IRule[] rules, RecyclerView recyclerv, Context c){
        super(rules);
        this.recyclerView = recyclerv;
        this.mcontext = c;
    }

    @Override
    protected void doEndDocument(Object userObject) {
  //      super.doEndDocument(userObject);
    //bind workflows to the adapter;
//        onWorkflowDataReady((List<Workflow>) userObject);
        WorkflowItemFragment.updateWorkflowUI((List<Workflow>) userObject);
    }

    @Override
    public void onWorkflowDataReady(List<Workflow> data) {
     //   this.recyclerView.setAdapter(new WorkflowAdapter(mcontext, data));
        for(Workflow w:data){
            System.out.println(w.getId());
        }
    }
}
