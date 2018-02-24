package intern.apply.internapply.api;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternAPI {
    private static InternAPI instance = null;


    //private final String BASE_URL = "http://192.168.0.14:3000";
    private InternAPIClient internAPIClient;

    private InternAPI() {
        String BASE_URL = "http://192.168.0.14:3000";
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.build();
        internAPIClient = retrofit.create(InternAPIClient.class);
    }

    /**
     * intern api singleton provider method
     *
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
     *
     * @return list of jobs
     */
    public Observable<List<Job>> getAllJobs() {
        return internAPIClient.getAllJobs();
    }

    public Observable<List<Job>> getAllJobs(String filter) { return internAPIClient.getAllJobs(filter); }

    /**
     * send a "contact-us" message to the server
     *
     * @param cm the contact-us message
     * @return the message sent to the server
     */
    public Observable<ContactMessage> sendContactMessage(ContactMessage cm) {
        return internAPIClient.sendContactMessage(cm);
    }


    /**
     * Sends a job the server
     *
     * @param job the new job
     * @return servers response
     */
    public Observable<Job> addJob(Job job) {
        return internAPIClient.addJob((job));
    }

    /**
     * get a specific job by id
     *
     * @param jobId the id of the job
     * @return the job with the given id
     */
    public Observable<List<Job>> getJob(int jobId) {
        return internAPIClient.getJob(jobId);
    }

    /**
     * get all the comments of a specific job
     *
     * @param jobId the id of the job
     * @return  a list of comments of the specified job
     */
    public Observable<List<Comment>> getJobComments(int jobId) { return internAPIClient.getJobComments(jobId); }

    /**
     * Add a comment to a job
     * @param comment   the comment to add
     * @return  the added comment
     */
    public Observable<Comment> addJobComment(Comment comment) { return internAPIClient.addJobComment(comment); }
    //endregion
}
