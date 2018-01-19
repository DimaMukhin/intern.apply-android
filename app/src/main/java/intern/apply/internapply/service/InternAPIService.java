package intern.apply.internapply.service;

import java.util.List;

import intern.apply.internapply.api.model.Job;
import intern.apply.internapply.api.service.InternAPI;
import io.reactivex.Observable;

/**
 * Created by Unknown on 2018-01-13.
 */

public class InternAPIService {

    private InternAPI internAPI;

    public InternAPIService() {
        internAPI = InternAPI.getAPI();
    }

    public Observable<List<Job>> getAllJobs() {
        return internAPI.getAllJobs();
    }

    public Observable<Job> getFirstJob() {
        return internAPI.getAllJobs().map(response -> {
           return response.get(0);
        });
    }
}
