package yjm.value;

import org.slf4j.Logger;
public class ValueLogger {
    static Logger logger;

    public final static void setLogger(final Logger logger) {
        ValueLogger.logger = logger;
    }

}
