package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.view.jobcommentsactivity.JobCommentsActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobCommentsAcceptanceTest extends ActivityInstrumentationTestCase2<JobCommentsActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPI api;

    private String[] singleCommentData;
    private String[] multipleCommentsData;
    private List<Comment> singleComment;
    private List<Comment> multipleComments;

    public JobCommentsAcceptanceTest() {
        super(JobCommentsActivity.class);
        api = mock(InternAPI.class);
        populateFakeComments();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityIntent(new Intent().putExtra("TEST", true));
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testOneCommentShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, JobCommentsActivity.class);
        solo.waitForActivity(JobCommentsActivity.class);

        Observable<List<Comment>> output = Observable.fromArray(singleComment);
        when(api.getJobComments(anyInt())).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.commentsListView);
        findStrings(singleCommentData);
    }

    public void testMultipleJobsShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, JobCommentsActivity.class);
        solo.waitForActivity(JobCommentsActivity.class);

        Observable<List<Comment>> output = Observable.fromArray(multipleComments);
        when(api.getJobComments(anyInt())).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.commentsListView);
        findStrings(multipleCommentsData);
    }

    public void testEmptyJobListShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, JobCommentsActivity.class);
        solo.waitForActivity(JobCommentsActivity.class);

        Observable<List<Comment>> output = Observable.fromArray(new ArrayList<Comment>());
        when(api.getJobComments(anyInt())).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.commentsListView);
    }

    private void populateFakeComments() {
        singleCommentData = new String[]{
                "first fake message", "dima"
        };
        singleComment = new ArrayList<>();
        for (int i = 0; i < singleCommentData.length; i += 2)
            singleComment.add(new Comment(singleCommentData[i], singleCommentData[i + 1]));

        multipleCommentsData = new String[]{
                "message1", "author1",
                "another message", "another author",
                "Who is the best?", "dima"
        };
        multipleComments = new ArrayList<>();
        for (int i = 0; i < multipleCommentsData.length; i += 2)
            multipleComments.add(new Comment(multipleCommentsData[i], multipleCommentsData[i + 1]));
    }

    private void findStrings(String[] expectedStrings) {
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND, solo.waitForText(s));
        }
    }
}
