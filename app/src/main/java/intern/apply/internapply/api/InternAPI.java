package intern.apply.internapply.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.CompletedSurvey;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.Salary;
import intern.apply.internapply.model.SurveyQuestion;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternAPI {
    private static InternAPI instance = null;

    private final InternAPIClient internAPIClient;

    private InternAPI() {
        String BASE_URL = "https://intern-apply.herokuapp.com/";
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

    /**
     * get the survey questions with allowed responses from the server
     *
     * @return the survey questions with their allowed responses
     */
    public Observable<List<SurveyQuestion>> getSurvey() {
        return internAPIClient.getSurvey();
    }

    /**
     * send a completed survey to the server
     *
     * @param survey the completed survey
     * @return the survey sent to the server
     */
    public Observable<CompletedSurvey> sendCompletedSurvey(CompletedSurvey survey) {
        return internAPIClient.sendCompletedSurvey(survey);
    }

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

    /**
     * Add a salary to a job
     *
     * @param salary the salary to add
     * @return the added comment
     */
    public Observable<Salary> addJobSalary(Salary salary) {
        return internAPIClient.addJobSalary(salary);
    }
    //endregion
}
