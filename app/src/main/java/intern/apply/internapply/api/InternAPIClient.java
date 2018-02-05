package intern.apply.internapply.api;


import java.util.List;

import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InternAPIClient {
    @GET("/api/job")
    Observable<List<Job>> getAllJobs();

    @POST("/api/job")
    Observable<Job> addJob(@Body Job job);

    @POST("/api/contactMessage")
    Observable<ContactMessage> sendContactMessage(@Body ContactMessage cm);
}
