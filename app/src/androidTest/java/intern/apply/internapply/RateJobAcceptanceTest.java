package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.RatingBar;

import com.robotium.solo.Solo;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.JobRating;
import intern.apply.internapply.view.mainactivity.MainActivity;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RateJobAcceptanceTest extends ActivityInstrumentationTestCase2<ViewJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String RATING_ERROR = "error with stars";
    private static final String VOTES_ERROR = "error with votes number";
    private Solo solo;
    private final InternAPI api;
    private JobRating jobRating;
    RatingBar ratingBar;

    public RateJobAcceptanceTest(){
        super(ViewJobActivity.class);
        api = mock(InternAPI.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        jobRating = new JobRating(1.00, 1);
        setActivityIntent(new Intent().putExtra("TEST", true));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }


    public void testRateJob() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        Observable<JobRating> output = Observable.just(jobRating);
        when(api.rateJob(anyInt(), eq(jobRating))).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnView(solo.getView(R.id.ratingBar));

        ratingBar = (RatingBar)solo.getView(R.id.ratingBar);
        assertTrue(RATING_ERROR,ratingBar.getRating() == jobRating.getScore());
        assertTrue(VOTES_ERROR, solo.searchText(jobRating + " votes"));
    }



}
