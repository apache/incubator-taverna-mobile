package org.apache.taverna.mobile;

import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflows;


public class FakeRemoteDataSource {

    private static TestDataFactory mTestDataFactory = new TestDataFactory();

    public static Announcements getAnnouncements() {
        return mTestDataFactory.getObjectTypeBean(Announcements.class, FakeXMLName
                .ANNOUNCEMENTS_XML);
    }

    public static DetailAnnouncement getAnnouncement() {
        return mTestDataFactory.getObjectTypeBean(DetailAnnouncement.class,
                FakeXMLName.ANNOUNCEMENT_XML);
    }

    public static Workflows getWorkflowList() {
        return mTestDataFactory.getObjectTypeBean(Workflows.class, FakeXMLName.WORKFLOWS_XML);
    }

    public static User getLoginUser() {
        return mTestDataFactory.getObjectTypeBean(User.class, FakeXMLName.USER_XML);
    }
}