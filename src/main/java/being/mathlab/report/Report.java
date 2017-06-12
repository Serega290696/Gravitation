package being.mathlab.report;

import java.util.concurrent.TimeUnit;

public class Report<R> {
    private boolean isSuccessful;
    private boolean isTimeLimitExceed;
    private R result;
    private long elapsedTime;

    private TimeUnit elapsedTimeUnit = TimeUnit.MILLISECONDS;

    public Report(ReportBuilder<R> builder) {
        this.isSuccessful = builder.isSuccessful();
        this.isTimeLimitExceed = builder.isTimeLimitExceed();
        this.result = builder.getResult();
        this.elapsedTime = builder.getElapsedTime();
    }

    public long getSpentTime() {
        return elapsedTime;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public TimeUnit getElapsedTimeUnit() {
        return elapsedTimeUnit;
    }

    public boolean isTimeLimitExceed() {
        return isTimeLimitExceed;
    }

    public R getResult() {
        return result;
    }

    public static class ReportBuilder<R>{

        private boolean isSuccessful;
        private boolean isTimeLimitExceed;
        private R result;
        private long elapsedTime;

        public long getElapsedTime() {
            return elapsedTime;
        }

        public ReportBuilder<R> setElapsedTime(long elapsedTime) {
            this.elapsedTime = elapsedTime;
            return this;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }

        public ReportBuilder<R> setIsSuccessful(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
            return this;
        }

        public boolean isTimeLimitExceed() {
            return isTimeLimitExceed;
        }

        public ReportBuilder<R> setIsTimeLimitExceed(boolean isTimeLimitExceed) {
            this.isTimeLimitExceed = isTimeLimitExceed;
            return this;
        }

        public R getResult() {
            return result;
        }

        public ReportBuilder<R> setResult(R result) {
            this.result = result;
            return this;
        }

        public Report<R> build() {
            return new Report<R>(this);
        }
    }
}
