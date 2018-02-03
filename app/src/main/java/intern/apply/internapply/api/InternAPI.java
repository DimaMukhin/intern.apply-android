package intern.apply.internapply.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


import java.util.List;

import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.User;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    /**
     * intern api singleton provider method
     * @return an intern api singleton object
     */
    public static InternAPI getAPI() {
        if (instance == null)
            instance = new InternAPI();
        return instance;
    }

    //region Transit API public calls

    /**
     * get all jobs from the server
     * @return list of jobs
     */
    public Observable<List<Job>> getAllJobs() {
        return internAPIClient.getAllJobs();
    }

    /**
     * send a "contact-us" message to the server
     * @param cm the contact-us message
     * @return the message sent to the server
     */
    public Observable<ContactMessage> sendContactMessage(ContactMessage cm) { return internAPIClient.sendContactMessage(cm); }

    /**
     * send a new user to the server
     * @param user details of the new user
     * @return confirmation response from the server
     */
    public Observable<User> register(User user) { return internAPIClient.regiser(user); }

    //endregion
}
