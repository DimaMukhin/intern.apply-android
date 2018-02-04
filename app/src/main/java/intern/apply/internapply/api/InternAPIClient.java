package intern.apply.internapply.api;


import java.util.List;

import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InternAPIClient {
    @GET("/api/job")
    Observable<List<Job>> getAllJobs();

    @POST("/api/contactMessage")
    Observable<ContactMessage> sendContactMessage(@Body ContactMessage cm);

    @POST("/api/reg")
    Observable<User> register(@Body User user);
}
