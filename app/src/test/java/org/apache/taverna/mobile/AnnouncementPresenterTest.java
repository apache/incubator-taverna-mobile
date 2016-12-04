package org.apache.taverna.mobile;


import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.Announcements;
import org.apache.taverna.mobile.data.model.DetailAnnouncement;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementMvpView;
import org.apache.taverna.mobile.ui.anouncements.AnnouncementPresenter;
import org.apache.taverna.mobile.utils.RxSchedulersOverrideRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnouncementPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new
            RxSchedulersOverrideRule();
    @Mock
    DataManager dataManager;

    @Mock
    AnnouncementMvpView announcementMvpView;
    Announcements announcements;
    DetailAnnouncement announcement;
    private AnnouncementPresenter announcementPresenter;

    @Before
    public void setUp() {

        announcementPresenter = new AnnouncementPresenter(dataManager);
        announcementPresenter.attachView(announcementMvpView);

        announcements = FakeRemoteDataSource.getAnnouncements();
        announcement = FakeRemoteDataSource.getAnnouncement();

    }

    @After
    public void tearDown() {
        announcementPresenter.detachView();
    }

    @Test
    public void loadAllAnnouncement_validAnnouncementsData_ReturnsResults() {

        when(dataManager.getAllAnnouncement(1)).thenReturn
                (Observable.just(announcements));

        announcementPresenter.loadAllAnnouncement(1);

        verify(announcementMvpView, never()).showSnackBar(R.string.no_more_announcement_available);
        verify(announcementMvpView, never()).removeLoadMoreProgressBar();
        verify(announcementMvpView).showAllAnnouncement(announcements);
        verify(announcementMvpView, never()).showSnackBar(R.string.failed_to_fetch_announcement);
    }

    @Test
    public void loadAllAnnouncement_NULLAnnouncementsData_RemoveLoadMore() {

        Announcements announcements = new Announcements();
        when(dataManager.getAllAnnouncement(1)).thenReturn
                (Observable.just(announcements));

        announcementPresenter.loadAllAnnouncement(1);

        verify(announcementMvpView).showSnackBar(R.string.no_more_announcement_available);
        verify(announcementMvpView).removeLoadMoreProgressBar();
        verify(announcementMvpView, never()).showAllAnnouncement(announcements);
        verify(announcementMvpView, never()).showSnackBar(R.string.failed_to_fetch_announcement);
    }

    @Test
    public void loadAllAnnouncement_RuntimeError_ShowError() {


        when(dataManager.getAllAnnouncement(1)).thenReturn(Observable.<Announcements>error(new
                RuntimeException()));

        announcementPresenter.loadAllAnnouncement(1);

        verify(announcementMvpView, never()).showSnackBar(R.string.no_more_announcement_available);
        verify(announcementMvpView, never()).removeLoadMoreProgressBar();
        verify(announcementMvpView, never()).showAllAnnouncement(announcements);
        verify(announcementMvpView).showSnackBar(R.string.failed_to_fetch_announcement);
    }

    @Test
    public void loadAnnouncementDetails_validAnnouncementData_ReturnsResults() {

        when(dataManager.getAnnouncementDetail("1")).thenReturn(Observable.just(announcement));

        announcementPresenter.loadAnnouncementDetails("1");

        verify(announcementMvpView).showAnnouncementDetail(announcement);
        verify(announcementMvpView, never()).showSnackBar(R.string.failed_to_fetch_announcement);

    }

    @Test
    public void loadAnnouncementDetails_RuntimeError_ShowError() {

        DetailAnnouncement detailAnnouncement = new DetailAnnouncement();

        when(dataManager.getAnnouncementDetail("1")).thenReturn(Observable
                .<DetailAnnouncement>error(new RuntimeException()));

        announcementPresenter.loadAnnouncementDetails("1");

        verify(announcementMvpView, never()).showAnnouncementDetail(detailAnnouncement);
        verify(announcementMvpView).showSnackBar(R.string.failed_to_fetch_announcement);

    }
}

