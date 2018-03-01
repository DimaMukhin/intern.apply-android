package intern.apply.internapply.api;


import android.content.Context;

import org.json.JSONObject;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.CompletedSurvey;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.Salary;
import intern.apply.internapply.model.SurveyQuestion;
import intern.apply.internapply.model.JobRating;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

interface InternAPIClient {
    @GET("/api/job")
    Observable<List<Job>> getAllJobs();

    @GET("api/job")
    Observable<List<Job>> getAllJobs(@Query("filter") String filter);

    @POST("/api/job")
    Observable<Job> addJob(@Body Job job);

    @GET("api/survey")
    Observable<List<SurveyQuestion>> getSurvey();

    @POST("api/survey")
    Observable<CompletedSurvey> sendCompletedSurvey(@Body CompletedSurvey survey);

    @POST("/api/contactMessage")
    Observable<ContactMessage> sendContactMessage(@Body ContactMessage cm);

    @GET("/api/job/{id}")
    Observable<List<Job>> getJob(@Path("id") int jobId);

    @GET("/api/job/{id}/comments")
    Observable<List<Comment>> getJobComments(@Path("id") int jobId);

    @POST("/api/comment")
    Observable<Comment> addJobComment(@Body Comment comment);

    @POST("/api/salary")
    Observable<Salary> addJobSalary(@Body Salary salary);

    @GET("/api/job/{id}/rating")
    Observable<List<JobRating>> getJobRating(@Path("id") int jobId);

    @POST("/api/job/{id}/rating")
    Observable<JobRating> rateJob(@Path("id") int jobId, @Body JobRating jobRating);
}
