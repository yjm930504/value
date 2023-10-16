

package yjm.value.termstructures;

import yjm.value.daycounters.DayCounter;
import yjm.value.math.interpolations.Extrapolator;
import yjm.value.time.Calendar;
import yjm.value.time.Date;
import yjm.value.util.Observable;
import yjm.value.util.Observer;

/**
 * Interface for term structures
 *
 * @author Richard Gomes
 */
public abstract interface TermStructure extends Extrapolator, Observer, Observable {

    /**
     * @return the latest date for which the curve can return values
     */
    public abstract Date maxDate() /* @ReadOnly */;

    /**
     * Return the calendar used for reference date calculation
     *
     * @category Dates and Time
     * @return the calendar used for reference date calculation
     */
    public abstract Calendar calendar() /* @ReadOnly */;


    /**
     * Returns the settlementDays used for reference date calculation
     *
     * @category Dates and Time
     * @return the settlementDays used for reference date calculation
     */
    public /*@Natural*/ int settlementDays() /* @ReadOnly */;


    /**
     * This method performs a date to double conversion which represents
     * the fraction of the year between the reference date and the date passed as parameter.
     *
     * @category Dates and Time
     * @param date
     * @return the fraction of the year as a double
     */
    public abstract /*@Time*/ double timeFromReference(final Date date) /* @ReadOnly */;

    /**
     * Return the day counter used for date/double conversion
     *
     * @category Dates and Time
     * @return the day counter used for date/double conversion
     */
    public abstract DayCounter dayCounter() /* @ReadOnly */;

    /**
     * Returns the latest double for which the curve can return values
     *
     * @category Dates and Time
     * @return the latest double for which the curve can return values
     */
    public abstract /*@Time*/ double maxTime() /* @ReadOnly */;

    /**
     * Returns the Date at which discount = 1.0 and/or variance = 0.0
     *
     * @note Term structures initialized by means of this
     * constructor must manage their own reference date
     * by overriding the getReferenceDate() method.
     *
     * @category Dates and Time
     * @returns the Date at which discount = 1.0 and/or variance = 0.0
     */
    public abstract Date referenceDate() /* @ReadOnly */;

}