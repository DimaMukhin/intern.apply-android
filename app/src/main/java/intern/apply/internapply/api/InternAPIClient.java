package intern.apply.internapply.api;


import android.content.Context;

import org.json.JSONObject;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Unknown on 2018-01-13.
 */
import retrofit2.http.POST;

public interface InternAPIClient {
    @GET("/api/job")
    Observable<List<Job>> getAllJobs();

    @GET("api/job")
    Observable<List<Job>> getAllJobs(@Query("filter") String filter);

    @POST("/api/contactMessage")
    Observable<ContactMessage> sendContactMessage(@Body ContactMessage cm);
}
