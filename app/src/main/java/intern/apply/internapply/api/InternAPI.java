package intern.apply.internapply.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import intern.apply.internapply.model.Job;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Unknown on 2018-01-13.
 */

public class InternAPI {
    private static InternAPI instance = null;

    private final String BASE_URL = "https://intern-apply.herokuapp.com/";
    private InternAPIClient internAPIClient;

    private InternAPI() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.build();
        internAPIClient = retrofit.create(InternAPIClient.class);
    }

    public static InternAPI getAPI() {
        if (instance == null)
            instance = new InternAPI();
        return instance;
    }

    //region Transit API public calls

    public Observable<List<Job>> getAllJobs() {
        return internAPIClient.getAllJobs();
    }

    //endregion
}
