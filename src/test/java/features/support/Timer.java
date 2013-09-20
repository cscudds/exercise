package features.support;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Timer {
    private DateTime start;

    public void start() {
        start = new DateTime();
    }

    public long stop() {
        return new Duration(DateTime.now().getMillis() - start.getMillis()).getStandardSeconds();
    }
}
