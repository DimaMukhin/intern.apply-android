package intern.apply.internapply.model;

/*
    job rating data model
 */

public class JobRating {
    private double score;
    private int votes;

    public JobRating(double score, int votes){
        this.score = score;
        this.votes = votes;
    }

    public JobRating(double score){
        this.score = score;
    }

    public double getScore() { return score; }

    public int getVotes(){
        return votes;
    }
}
