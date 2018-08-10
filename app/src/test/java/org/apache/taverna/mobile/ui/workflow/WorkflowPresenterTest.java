package org.apache.taverna.mobile.ui.workflow;


import org.apache.taverna.mobile.FakeRemoteDataSource;
import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Workflows;
import org.apache.taverna.mobile.utils.RxSchedulersOverrideRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new
            RxSchedulersOverrideRule();

    @Mock
    DataManager dataManager;

    @Mock
    WorkflowMvpView workflowMvpView;

    private Workflows workflows;
    private WorkflowPresenter workflowPresenter;
    private Map<String, String> optionPage1;
    private Map<String, String> optionPage2;

    @Before
    public void setUp() {

        workflowPresenter = new WorkflowPresenter(dataManager);
        workflowPresenter.attachView(workflowMvpView);

        workflows = FakeRemoteDataSource.getWorkflowList();

        optionPage1 = new HashMap<>();
        optionPage1.put("elements", "title,type,uploader,preview,created-at");
        optionPage1.put("page", String.valueOf(1));
        optionPage1.put("num", String.valueOf(10));
        optionPage1.put("order", "reverse");

        optionPage2 = new HashMap<>();
        optionPage2.put("elements", "title,type,uploader,preview,created-at");
        optionPage2.put("page", String.valueOf(2));
        optionPage2.put("num", String.valueOf(10));
        optionPage2.put("order", "reverse");
    }

    @After
    public void tearDown() {

        workflowPresenter.detachView();
    }

    @Test
    public void loadAllWorkflow_validWorkflowList_ReturnsResults() {

        when(dataManager.getAllWorkflow(optionPage1)).thenReturn(
                Observable.just(workflows));

        workflowPresenter.loadAllWorkflow(1);

        verify(workflowMvpView).showProgressbar(true);
        verify(workflowMvpView).removeLoadMoreProgressbar();
        verify(workflowMvpView).showWorkflows(workflows);
        verify(workflowMvpView, never()).showSnackBar(R.string.error_failed_to_fetch_workflow);
    }

    @Test
    public void loadAllWorkflow_EmptyWorkflow_ReturnNoWorkflowResults() {

        Workflows workflows = new Workflows();
        when(dataManager.getAllWorkflow(optionPage1)).thenReturn(
                Observable.just(workflows));

        workflowPresenter.loadAllWorkflow(1);

        verify(workflowMvpView).showProgressbar(true);
        verify(workflowMvpView).removeLoadMoreProgressbar();
        verify(workflowMvpView).showSnackBar(R.string.no_workflows_found);
        verify(workflowMvpView, never()).showWorkflows(workflows);
        verify(workflowMvpView, never()).showSnackBar(R.string.error_failed_to_fetch_workflow);
    }

    @Test
    public void loadAllWorkflow_page2_EmptyWorkflow_ReturnNoMoreWorkflowResults() {

        Workflows workflows = new Workflows();
        when(dataManager.getAllWorkflow(optionPage2)).thenReturn(
                Observable.just(workflows));

        workflowPresenter.loadAllWorkflow(2);

        verify(workflowMvpView).showProgressbar(true);
        verify(workflowMvpView).removeLoadMoreProgressbar();
        verify(workflowMvpView).showSnackBar(R.string.no_more_workflows_avialable);
        verify(workflowMvpView, never()).showWorkflows(workflows);
        verify(workflowMvpView, never()).showSnackBar(R.string.no_workflows_found);
        verify(workflowMvpView, never()).showSnackBar(R.string.error_failed_to_fetch_workflow);
    }

    @Test
    public void loadAllWorkflow_RuntimeError_showError() {

        when(dataManager.getAllWorkflow(optionPage1)).thenReturn(
                Observable.<Workflows>error(new RuntimeException()));

        workflowPresenter.loadAllWorkflow(1);

        verify(workflowMvpView).showProgressbar(false);
        verify(workflowMvpView).removeLoadMoreProgressbar();
        verify(workflowMvpView, never()).showWorkflows(workflows);
        verify(workflowMvpView).showSnackBar(R.string.error_failed_to_fetch_workflow);
    }

}