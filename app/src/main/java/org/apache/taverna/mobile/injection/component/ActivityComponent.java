package org.apache.taverna.mobile.injection.component;

import org.apache.taverna.mobile.injection.PerActivity;
import org.apache.taverna.mobile.injection.module.ActivityModule;
import org.apache.taverna.mobile.ui.DashboardActivity;
import org.apache.taverna.mobile.ui.FlashScreenActivity;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementFragment;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsActivity;
import org.apache.taverna.mobile.ui.favouriteworkflow.FavouriteWorkflowsFragment;
import org.apache.taverna.mobile.ui.favouriteworkflowdetail.FavouriteWorkflowDetailActivity;
import org.apache.taverna.mobile.ui.favouriteworkflowdetail.FavouriteWorkflowDetailFragment;
import org.apache.taverna.mobile.ui.imagezoom.ImageZoomActivity;
import org.apache.taverna.mobile.ui.imagezoom.ImageZoomFragment;
import org.apache.taverna.mobile.ui.login.LoginActivity;
import org.apache.taverna.mobile.ui.login.LoginFragment;
import org.apache.taverna.mobile.ui.myworkflows.MyWorkflowActivity;
import org.apache.taverna.mobile.ui.myworkflows.MyWorkflowFragment;
import org.apache.taverna.mobile.ui.playerlogin.PlayerLoginFragment;
import org.apache.taverna.mobile.ui.tutorial.TutorialActivity;
import org.apache.taverna.mobile.ui.usage.UsageActivity;
import org.apache.taverna.mobile.ui.userprofile.UserProfileActivity;
import org.apache.taverna.mobile.ui.userprofile.UserProfileFragment;
import org.apache.taverna.mobile.ui.workflow.WorkflowFragment;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailActivity;
import org.apache.taverna.mobile.ui.workflowdetail.WorkflowDetailFragment;
import org.apache.taverna.mobile.ui.workflowrun.WorkflowRunActivity;

import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(DashboardActivity dashboardActivity);

    void inject(FavouriteWorkflowsActivity favouriteWorkflowsActivity);

    void inject(FavouriteWorkflowDetailActivity favouriteWorkflowDetailActivity);

    void inject(ImageZoomActivity imageZoomActivity);

    void inject(LoginActivity loginActivity);

    void inject(MyWorkflowActivity myWorkflowActivity);

    void inject(TutorialActivity tutorialActivity);

    void inject(UsageActivity usageActivity);

    void inject(UserProfileActivity userProfileActivity);

    void inject(WorkflowDetailActivity workflowDetailActivity);

    void inject(AnnouncementFragment announcementFragment);

    void inject(FavouriteWorkflowsFragment favouriteWorkflowsFragment);

    void inject(FavouriteWorkflowDetailFragment favouriteWorkflowDetailFragment);

    void inject(ImageZoomFragment imageZoomFragment);

    void inject(LoginFragment loginFragment);

    void inject(MyWorkflowFragment myWorkflowFragment);

    void inject(PlayerLoginFragment playerLoginFragment);

    void inject(UserProfileFragment userProfileFragment);

    void inject(WorkflowFragment workflowFragment);

    void inject(WorkflowDetailFragment workflowDetailFragment);

    void inject(WorkflowRunActivity workflowRunActivity);

    void inject(FlashScreenActivity flashScreenActivity);
}
