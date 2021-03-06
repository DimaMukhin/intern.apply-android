package intern.apply.internapply.api;

import java.util.List;

import intern.apply.internapply.model.Answer;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.CompletedSurvey;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobRating;
import intern.apply.internapply.model.Salary;
import intern.apply.internapply.model.SurveyQuestion;
import io.reactivex.Observable;

public interface InternAPIProvider {

    /**
     * get all jobs
     *
     * @return list of jobs
     */
    Observable<List<Job>> getAllJobs();

    /**
     * get all filtered jobs
     *
     * @param filter the string filter
     * @return a list of jobs
     */
    Observable<List<Job>> getAllJobs(String filter);

    /**
     * get the survey questions with allowed responses
     *
     * @return the survey questions with their allowed responses
     */
    Observable<List<SurveyQuestion>> getSurvey();

    /**
     * send a completed survey
     *
     * @param survey the completed survey
     * @return the survey sent
     */
    Observable<CompletedSurvey> sendCompletedSurvey(CompletedSurvey survey);

    /**
     * send a "contact-us" message
     *
     * @param cm the contact-us message
     * @return the message sent
     */
    Observable<ContactMessage> sendContactMessage(ContactMessage cm);


    /**
     * Sends a job
     *
     * @param job the new job
     * @return servers response
     */
    Observable<Job> addJob(Job job);

    /**
     * get a specific job by id
     *
     * @param jobId the id of the job
     * @return the job with the given id
     */
    Observable<List<Job>> getJob(int jobId);

    /**
     * get all the comments of a specific job
     *
     * @param jobId the id of the job
     * @return  a list of comments of the specified job
     */
    Observable<List<Comment>> getJobComments(int jobId);


    /**
     * Add a comment to a job
     *
     * @param comment   the comment to add
     * @return  the added comment
     */
    Observable<Comment> addJobComment(Comment comment);

    /**
     * Add a salary to a job
     *
     * @param salary the salary to add
     * @return the added comment

     */
    Observable<Salary> addJobSalary(Salary salary);

    /**
     * getting the rating of a specific job
     * @param jobId
     * @return
     */
    Observable<List<JobRating>> getJobRating(int jobId);


    /**
     * rate a job
     * @param jobId
     * @param jobRating
     * @return
     */
    Observable<JobRating> rateJob(int jobId, JobRating jobRating);

    /**
     * get answer for a question
     *
     * @param questionId
     * @return
     */
    Observable<List<Answer>> getAnswers(int questionId);

    /**
     * add an answer to a question
     *
     * @param questionId
     * @param answer
     * @return
     */
    Observable<Answer> addAnswer(int questionId, Answer answer);
}
