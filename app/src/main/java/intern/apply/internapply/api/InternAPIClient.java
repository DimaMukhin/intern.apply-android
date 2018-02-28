package intern.apply.internapply.api;


import java.util.List;

import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobRating;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface InternAPIClient {
    @GET("/api/job")
    Observable<List<Job>> getAllJobs();

    @POST("/api/job")
    Observable<Job> addJob(@Body Job job);

    @POST("/api/contactMessage")
    Observable<ContactMessage> sendContactMessage(@Body ContactMessage cm);

    @GET("/api/job/{id}")
    Observable<List<Job>> getJob(@Path("id") int jobId);

    @GET("/api/job/{id}/comments")
    Observable<List<Comment>> getJobComments(@Path("id") int jobId);

    @POST("/api/comment")
    Observable<Comment> addJobComment(@Body Comment comment);

    @GET("/api/job/{id}/rating")
    Observable<List<JobRating>> getJobRating(@Path("id") int jobId);

    @POST("/api/job/{id}/rating")
    Observable<JobRating> rateJob(@Path("id") int jobId, @Body JobRating jobRating);
}
