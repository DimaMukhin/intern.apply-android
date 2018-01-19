package intern.apply.internapply.api.service;


import java.util.List;

import intern.apply.internapply.api.model.Job;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Unknown on 2018-01-13.
 */

public interface InternAPIClient {
    @GET("/api/job")
    Observable<List<Job>> getAllJobs();
}
