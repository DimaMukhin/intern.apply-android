package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.view.jobcommentsactivity.JobCommentsActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobCommentsAcceptanceTest extends ActivityInstrumentationTestCase2<JobCommentsActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private final InternAPIProvider api;
    private Solo solo;
    private String[] singleCommentData;
    private String[] multipleCommentsData;
    private List<Comment> singleComment;
    private List<Comment> multipleComments;

    public JobCommentsAcceptanceTest() {
        super(JobCommentsActivity.class);
        api = mock(InternAPIProvider.class);
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
        testHelper(Observable.fromArray(singleComment), singleCommentData);
    }

    public void testMultipleJobsShowing() {
        testHelper(Observable.fromArray(multipleComments), multipleCommentsData);
    }

    public void testEmptyJobListShowing() {
        testHelper(Observable.fromArray(new ArrayList<>()), new String[0]);
    }

    private void testHelper(Observable<List<Comment>> output, String[] data) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, JobCommentsActivity.class);
        solo.waitForActivity(JobCommentsActivity.class);

        when(api.getJobComments(anyInt())).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.commentsListView);
        TestHelper.findStrings(data, this, solo);
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
}
