package intern.apply.internapply.api;


import java.util.List;

import intern.apply.internapply.model.CompletedSurvey;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.SurveyQuestion;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Unknown on 2018-01-13.
 */

public interface InternAPIClient {
    @GET("api/job")
    Observable<List<Job>> getAllJobs();

    @GET("api/survey")
    Observable<List<SurveyQuestion>> getSurvey();

    @POST("api/survey")
    Observable<CompletedSurvey> sendCompletedSurvey(@Body CompletedSurvey survey);
}
