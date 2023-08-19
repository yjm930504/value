package org.yjm.time;
import org.yjm.QL;
import org.yjm.time.calendars.china;
import org.yjm.time.calendars.nullcalendar;
import org.yjm.lang.LibraryException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiaming Yan
 * @description: Schedule类
 */
public class Schedule {

    private final boolean fullInterface_;
    private final Calendar calendar_;
    private final BusinessDayConvention convention_;
    private final BusinessDayConvention terminationDateConvention_;

    //如果起始日期在月末，是否要求其他日期也要安排在月末（除最后一个日期外）。
    private final boolean endOfMonth_;
    private final List<Date> dates_;
    private final List<Boolean> isRegular_;

    private Period tenor_;
    private DateGeneration.Rule rule_;

    //以下两个日期用于计息区间生成规则
    private Date firstDate_;
    private Date nextToLastDate_;

    /**
     * @author Jiaming Yan
     * @description: 构造函数，默认无工作日调整和中国日历
     */
    public Schedule(final List<Date> dates) {
        this(dates, new china(), BusinessDayConvention.Unadjusted);
    }

    /**
     * @author Jiaming Yan
     * @description: 构造函数
     */
    public Schedule(final List<Date> dates, final Calendar calendar) {
        this(dates, calendar, BusinessDayConvention.Unadjusted);
    }

    /**
     * @author Jiaming Yan
     * @description: 构造函数
     */
    public Schedule(final List<Date> dates, final Calendar calendar, final BusinessDayConvention convention) {
        this.dates_ = dates;
        this.isRegular_ = new ArrayList<Boolean>();
        this.calendar_ = calendar;
        this.convention_ = convention;
        this.fullInterface_ = false;
        this.tenor_ = new Period();
        this.terminationDateConvention_ = convention;
        this.rule_ = DateGeneration.Rule.Forward;
        this.endOfMonth_ = false;
    }

    /**
     * @author Jiaming Yan
     * @description: 构造函数
     */
    public Schedule(final Date effectiveDate,
                    final Date terminationDate,
                    final Period tenor,
                    final Calendar calendar,
                    final BusinessDayConvention convention,
                    final BusinessDayConvention terminationDateConvention,
                    final DateGeneration.Rule rule,
                    final boolean endOfMonth) {
        this(effectiveDate, terminationDate, tenor, calendar, convention, terminationDateConvention, rule, endOfMonth, new Date(), new Date());
    }

    /**
     * @author Jiaming Yan
     * @description: 构造函数，根据起息日、到期日、计息频率、计息区间生成规则以及工作日调整规则生成计息区间dates_
     */
    public Schedule(final Date effectiveDate,
                    final Date terminationDate,
                    final Period tenor,
                    final Calendar calendar,
                    BusinessDayConvention convention,
                    final BusinessDayConvention terminationDateConvention,
                    final DateGeneration.Rule rule,
                    final boolean endOfMonth,
                    final Date firstDate,
                    final Date nextToLastDate) {

        this.dates_ = new ArrayList<Date>();
        this.isRegular_ = new ArrayList<Boolean>();
        this.fullInterface_ = true;
        this.tenor_ = tenor;
        this.calendar_ = calendar;
        this.convention_ = convention;
        this.terminationDateConvention_ = terminationDateConvention;
        this.rule_ = rule;
        this.endOfMonth_ = endOfMonth;
        this.firstDate_ = firstDate;
        this.nextToLastDate_ = nextToLastDate;


        if (firstDate.eq(effectiveDate)) {
            this.firstDate_ = new Date();
        } else {
            this.firstDate_ = firstDate;
        }

        if (nextToLastDate.eq(terminationDate)) {
            this.nextToLastDate_ = new Date();
        } else {
            this.nextToLastDate_ = nextToLastDate;
        }

        QL.require(effectiveDate != null && !effectiveDate.isNull(), "起息日不能为空");
        QL.require(terminationDate != null && !terminationDate.isNull(), "到期日不能为空");
        QL.require(effectiveDate.lt(terminationDate),
                "起息日(" + effectiveDate
                        + ")在到期日("
                        + terminationDate + ")后");

        if (tenor.length() == 0) {
            rule_ = DateGeneration.Rule.Zero;
        } else {
            QL.require(tenor.length() > 0,
                    "计息周期的长度(" + tenor + ")不能为负");

            if (firstDate != null && !firstDate.isNull()) {
                switch (rule_) {
                    case Backward:
                    case Forward:
                        QL.require(firstDate.gt(effectiveDate) &&
                                        firstDate.lt(terminationDate),
                                "firstDate_(" + firstDate +
                                        ") 在[起息日(" + effectiveDate +
                                        "), 到期日(" + terminationDate +
                                        ")]的区间之外");
                        break;
                    case ThirdWednesday:
                        break;
                    case Zero:
                        String errMsg = "firstDate_与" + rule_ + "的计息区间调整规则不兼容";
                        throw new LibraryException(errMsg);
                    default:
                        errMsg = "不支持的计息区间调整规则(" + rule_ + ")";
                        throw new LibraryException(errMsg);
                }
            }

            if (nextToLastDate != null && !nextToLastDate.isNull()) {
                switch (rule_) {
                    case Backward:
                    case Forward:
                        QL.require(nextToLastDate.gt(effectiveDate) &&
                                        nextToLastDate.lt(terminationDate),
                                "nextToLastDate_(" + nextToLastDate +
                                        ")在[起息日(" + effectiveDate +
                                        "),到期日(" + terminationDate +
                                        ")]区间之外");
                        break;
                    case ThirdWednesday:
                        String errMsg = "nextToLastDate_与" + rule_ + "的计息区间调整规则不兼容";
                        throw new LibraryException(errMsg);
                    default:
                        errMsg = "不支持的计息区间调整规则(" + rule_ + ")";
                        throw new LibraryException(errMsg);
                }
            }

            final Calendar nullCalendar = new nullcalendar();

            int periods = 1;

            Date seed, exitDate;

            //根据计息区间规则，生成未经节假日调整的计息区间dates_
            switch (rule_) {
                case Zero:
                    tenor_ = new Period(0, TimeUnit.Days);
                    dates_.add(effectiveDate);
                    dates_.add(terminationDate);
                    isRegular_.add(true);
                    break;
                case Backward:
                    dates_.add(terminationDate);
                    seed = terminationDate.clone();
                    if (nextToLastDate != null && !nextToLastDate.isNull()) {
                        dates_.add(0, nextToLastDate);
                        final Date temp = nullCalendar.advance(seed, tenor_.mul(periods).negative(), convention, endOfMonth);
                        if (temp.ne(nextToLastDate)) {
                            isRegular_.add(0, false);
                        } else {
                            isRegular_.add(0, true);
                        }
                        seed = nextToLastDate.clone();
                    }
                    exitDate = effectiveDate.clone();
                    if (firstDate != null && !firstDate.isNull()) {
                        exitDate = firstDate.clone();
                    }
                    while (true) {
                        final Date temp = nullCalendar.advance(seed, tenor_.mul(periods).negative(), convention, endOfMonth);
                        if (temp.lt(exitDate)) {
                            break;
                        } else {
                            dates_.add(0, temp);
                            isRegular_.add(0, true);
                            ++periods;
                        }
                    }
                    if (endOfMonth && calendar.isEndOfMonth(seed)) {
                        convention = BusinessDayConvention.Preceding;
                    }
                    if (calendar.adjust(dates_.get(0), convention).ne(
                            calendar.adjust(effectiveDate, convention))) {
                        dates_.add(0, effectiveDate);
                        isRegular_.add(0, false);
                    }
                    break;
                case ThirdWednesday:
                    QL.require(!endOfMonth,
                            "endOfMonth与" + rule_ +
                                    "的计息区间调整规则不兼容");
                case Forward:
                    dates_.add(effectiveDate);
                    seed = effectiveDate.clone();
                    if (firstDate != null && !firstDate.isNull()) {
                        dates_.add(firstDate);
                        final Date temp = nullCalendar.advance(seed, tenor_.mul(periods), convention, endOfMonth);
                        if (temp.ne(firstDate)) {
                            isRegular_.add(false);
                        } else {
                            isRegular_.add(true);
                        }
                        seed = firstDate.clone();
                    }
                    exitDate = terminationDate.clone();
                    if (nextToLastDate != null && !nextToLastDate.isNull()) {
                        exitDate = nextToLastDate.clone();
                    }
                    while (true) {
                        final Date temp = nullCalendar.advance(seed, tenor_.mul(periods), convention, endOfMonth);
                        if (temp.gt(exitDate)) {
                            break;
                        } else {
                            dates_.add(temp);
                            isRegular_.add(true);
                            ++periods;
                        }
                    }
                    if (endOfMonth && calendar.isEndOfMonth(seed)) {
                        convention = BusinessDayConvention.Preceding;
                    }
                    if (calendar.adjust(dates_.get(dates_.size() - 1), terminationDateConvention).ne(
                            calendar.adjust(terminationDate, terminationDateConvention))) {
                        dates_.add(terminationDate);
                        isRegular_.add(Boolean.valueOf(false));
                    }
                    break;
                default:
                    final String errMsg = "不支持的计息区间调整规则(" + rule_ + ")";
                    throw new LibraryException(errMsg);
            }

            if (rule_ == DateGeneration.Rule.ThirdWednesday) {
                for (int i = 1; i < dates_.size() - 1; ++i) {
                    dates_.set(i, Date.nthWeekday(3, Weekday.Wednesday,
                            dates_.get(i).month(),
                            dates_.get(i).year()));
                }
            }

            //根据日历和工作日调整规则，进行节假日调整
            for (int i = 0; i < dates_.size() - 1; ++i) {
                dates_.set(i, calendar.adjust(dates_.get(i), convention));
            }

            if (terminationDateConvention != BusinessDayConvention.Unadjusted) {
                dates_.set(dates_.size() - 1, calendar.adjust(dates_.get(dates_.size() - 1),
                        terminationDateConvention));
            }
        }

    }

    /**
     * @author Jiaming Yan
     * @description: 返回dates_的大小
     */
    public int size() {
        return dates_.size();
    }

    /**
     * @author Jiaming Yan
     * @description: 返回dates_的第i个日期
     */
    public final Date at(final int i) {
        return dates_.get(i);
    }

    /**
     * @author Jiaming Yan
     * @description: 返回dates_的第i个日期
     */
    public final Date date(final int i) {
        return dates_.get(i);
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schelue中refDate之前的日期
     */
    public Date previousDate(final Date refDate) {
        final int index = Date.lowerBound(dates_, refDate);
        if ( index > 0 )
            return dates_.get(index-1).clone();
        else
            return new Date();

    }

    /**
     * @author Jiaming Yan
     * @description: 返回schelue中refDate周后的日期
     */
    public Date nextDate(final Date  refDate) {
        final int index = Date.lowerBound(dates_, refDate);
        if ( index < dates_.size() )
            return dates_.get(index).clone();
        else
            return new Date();
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的dates_
     */
    public List<Date> dates() {
        return dates_;
    }

    /**
     * @author Jiaming Yan
     * @description:
     */
    public boolean isRegular(final int i) {
        QL.require(fullInterface_, "full interface not available");
        QL.require(i<=isRegular_.size() && i>0,
                "index (" + i + ") must be in [1, " +
                        isRegular_.size() +"]");
        return isRegular_.get(i-1);
    }

    /**
     * @author Jiaming Yan
     * @description: 判断dates_是否为空
     */
    public boolean empty() {
        return  dates_.isEmpty();
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的calendar_
     */
    public final Calendar calendar() {
        return calendar_;
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的第一个日期
     */
    public final Date startDate(){
        return dates_.isEmpty() ? null : dates_.get(0);
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的最后一个日期
     */
    public final Date endDate() {
        return dates_.isEmpty() ? null : dates_.get(dates_.size()-1);
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的tenor_(计息周期)
     */
    public final Period tenor() {
        QL.require(fullInterface_, "full interface not available");
        return tenor_;
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的工作日调整规则
     */
    public BusinessDayConvention businessDayConvention() /* @ReadOnly */ {
        return convention_;
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的结束日的工作日调整规则
     */
    public BusinessDayConvention terminationDateBusinessDayConvention() {
        QL.require(fullInterface_, "full interface not available"); // TODO: message
        return terminationDateConvention_;
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的计息区间生成规则
     */
    public DateGeneration.Rule rule() /* @ReadOnly */ {
        QL.require(fullInterface_, "full interface not available"); // TODO: message
        return rule_;
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule的endOfMonth_
     */
    public boolean endOfMonth() {
        QL.require(fullInterface_, "full interface not available"); // TODO: message
        return endOfMonth_;
    }

    /**
     * @author Jiaming Yan
     * @description:
     */
    public int lowerBound() {
        return lowerBound(new Date());
    }

    /**
     * @author Jiaming Yan
     * @description: 返回schedule中第一个大于等于refDate日期的index
     */
    public int lowerBound(final Date refDate) {
        if (refDate.gt(dates_.get(dates_.size() - 1)) || refDate.lt(dates_.get(0))){
            throw new LibraryException("比较日期超过Schedule范围");
        }
        return Date.lowerBound(dates_, refDate.clone());
    }

}

