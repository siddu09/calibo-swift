package ai.models;

public class DatasetSummary {

    private int totalRecords;
    private int passedRecords;
    private int failedRecords;

    private long strongMatches;
    private long likelyMatches;
    private long mismatches;

    private double averageScore;
    private double highestScore;
    private double lowestScore;
    private double passRate;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPassedRecords() {
        return passedRecords;
    }

    public void setPassedRecords(int passedRecords) {
        this.passedRecords = passedRecords;
    }

    public int getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }

    public long getStrongMatches() {
        return strongMatches;
    }

    public void setStrongMatches(long strongMatches) {
        this.strongMatches = strongMatches;
    }

    public long getLikelyMatches() {
        return likelyMatches;
    }

    public void setLikelyMatches(long likelyMatches) {
        this.likelyMatches = likelyMatches;
    }

    public long getMismatches() {
        return mismatches;
    }

    public void setMismatches(long mismatches) {
        this.mismatches = mismatches;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(double highestScore) {
        this.highestScore = highestScore;
    }

    public double getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(double lowestScore) {
        this.lowestScore = lowestScore;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }
}