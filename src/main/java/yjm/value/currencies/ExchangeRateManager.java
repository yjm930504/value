package yjm.value.currencies;

import yjm.value.QL;
import yjm.value.Settings;
import yjm.value.currencies.America.PEHCurrency;
import yjm.value.currencies.America.PEICurrency;
import yjm.value.currencies.America.PENCurrency;
import yjm.value.currencies.Europe.*;
import yjm.value.lang.exceptions.LibraryException;
import yjm.value.lang.iterators.Iterables;
import yjm.value.time.Date;
import yjm.value.time.Month;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 汇率仓库，提供方法提取给定日期的汇率数据
 */
public class ExchangeRateManager {

    /**
     * Singleton实例，确保类只有一个实例，自行实例化并向整个系统提供实例
     */
    private static volatile ExchangeRateManager instance = null;

    /**
     * HashMap存储所有的汇率，Key为Hash值，Value为汇率实体，Entry中有起始和到期日
     */
    private final HashMap<Object, List<Entry>> data_ = new HashMap<Object, List<Entry>>();

    private ExchangeRateManager() {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        addKnownRates();
    }

    public static ExchangeRateManager getInstance() {
        if (instance == null) {
            synchronized (ExchangeRateManager.class) {
                if (instance == null) {
                    instance = new ExchangeRateManager();
                }
            }
        }
        return instance;
    }

    /**
     * 新增汇率信息，存储到HashMap
     * <p>
     * 如果在相同的货币之间给出了两个日期范围重叠的汇率，则在查找过程中，最新添加的汇率优先。
     */
    public void add(final ExchangeRate rate, final Date startDate, final Date endDate) {
        final int k = hash(rate.source(), rate.target());
        if (data_.get(k) == null) {
            data_.put(k, new ArrayList<Entry>());
        }
        data_.get(k).add(0, new Entry(rate, startDate, endDate));
    }

    /**
     * 新增汇率信息，存储到HashMap
     */
    public void add(final ExchangeRate rate) {
        add(rate, Date.minDate(), Date.maxDate());
    }

    /**
     * 获取汇率信息
     */
    public ExchangeRate lookup(final Currency source, final Currency target) {
        return lookup(source, target, Date.todaysDate(), ExchangeRate.Type.Derived);
    }

    /**
     * 获取汇率信息
     */
    public ExchangeRate lookup(final Currency source, final Currency target, final Date date) {
        return lookup(source, target, date, ExchangeRate.Type.Derived);
    }

    /**
     * 查找给定日期两种货币之间的汇率。
     * <p>
     * 如果给定类型为直接汇率，则仅返回直接汇率（如果可用）；如果是衍生利率，则仍然首选直接利率，但允许使用衍生利率。
     */
    public ExchangeRate lookup(final Currency source, final Currency target, Date date, final ExchangeRate.Type type) {

        if (source.eq(target))
            return new ExchangeRate(source, target, 1.0);

        if (date.isToday()) {
            date = new Settings().evaluationDate();
        }
        // 汇率类型为Direct,直接获取
        if (type == ExchangeRate.Type.Direct)
            return directLookup(source, target, date);
        // 判断source是否存在三角转换币种
        else if (!source.triangulationCurrency().empty()) {
            final Currency link = source.triangulationCurrency();
            if (link.eq(target))
                return directLookup(source, link, date);
            else
                // 根据link找与source和target的汇率，生成source和target的汇率对象
                return ExchangeRate.chain(directLookup(source, link, date), lookup(link, target, date));
            // 判断target是否存在三角转换币种
        } else if (!target.triangulationCurrency().empty()) {
            final Currency link = target.triangulationCurrency();
            if (source.eq(link))
                return directLookup(link, target, date);
            else
                return ExchangeRate.chain(lookup(source, link, date), directLookup(link, target, date));
        } else
            return smartLookup(source, target, date);
    }

    /**
     * 移除所有人工添加的汇率数据
     */
    public void clear() {
        data_.clear();
        addKnownRates();
    }

    /**
     * 创建货币对唯一标识
     */
    public int hash(final Currency c1, final Currency c2) {
        return Math.min(c1.numericCode(), c2.numericCode()) * 1000 + Math.max(c1.numericCode(), c2.numericCode());
    }

    /**
     * 判断hash值是否包含Currency
     */
    public boolean hashes(final int k, final Currency c) {
        return c.numericCode() == k % 1000 || c.numericCode() == k / 1000;
    }

    /**
     * 将已经不使用的汇率数据添加到data_中
     */
    private void addKnownRates() {
        final Date maxDate = Date.maxDate();

        add(new ExchangeRate(
                        new EURCurrency(),
                        new ATSCurrency(), 13.7603),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new BEFCurrency(), 40.3399),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new DEMCurrency(), 1.95583),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new ESPCurrency(), 166.386),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new FIMCurrency(), 5.94573),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new FRFCurrency(), 6.55957),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new GRDCurrency(), 340.750),
                new Date(1, Month.January, 2001),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new IEPCurrency(), 0.787564),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new ITLCurrency(), 1936.27),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new LUFCurrency(), 40.3399),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new NLGCurrency(), 2.20371),
                new Date(1, Month.January, 1999),
                maxDate);
        add(new ExchangeRate(
                        new EURCurrency(),
                        new PTECurrency(), 200.482),
                new Date(1, Month.January, 1999),
                maxDate);
        // other obsoleted currencies
        add(new ExchangeRate(
                        new TRYCurrency(),
                        new TRLCurrency(), 1000000.0),
                new Date(1, Month.January, 2005),
                maxDate);
        add(new ExchangeRate(
                        new RONCurrency(),
                        new ROLCurrency(), 10000.0),
                new Date(1, Month.July, 2005),
                maxDate);
        add(new ExchangeRate(
                        new PENCurrency(),
                        new PEICurrency(), 1000000.0),
                new Date(1, Month.July, 1991),
                maxDate);
        add(new ExchangeRate(
                        new PEICurrency(),
                        new PEHCurrency(), 1000.0),
                new Date(1, Month.February, 1985),
                maxDate);
    }

    /**
     * 直接获取汇率数据
     */
    private ExchangeRate directLookup(final Currency source, final Currency target, final Date date) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");

        ExchangeRate rate = null;
        QL.require(((rate = fetch(source, target, date)) != null), "没有可用的直接汇率");
        return rate;
    }

    /**
     * 智能获取汇率数据
     */
    private ExchangeRate smartLookup(final Currency source, final Currency target, final Date date) {
        return smartLookup(source, target, date, new int[0]);
    }

    /**
     * 智能获取汇率数据
     */
    private ExchangeRate smartLookup(final Currency source, final Currency target, final Date date, int[] forbidden) {
        // 优先直接获取汇率
        final ExchangeRate direct = fetch(source, target, date);
        if (direct != null)
            return direct;

        final int temp[] = forbidden.clone();
        forbidden = new int[temp.length + 1];
        System.arraycopy(temp, 0, forbidden, 0, temp.length);
        // forbidden的尾部增加货币code,防止循环招
        forbidden[forbidden.length - 1] = (source.numericCode());

        for (final Object key : Iterables.unmodifiableIterable(data_.keySet())) {
            // 寻找包含source的汇率数据
            if (hashes((Integer) key, source) && !(data_.get(key).isEmpty())) {
                final Entry e = data_.get(key).get(0);

                // 如果找到包含source的汇率数据，找到汇率另一个币种
                final Currency other = (source == e.rate.source()) ? e.rate.target() : e.rate.source();

                // 如果另一个币种不在forbidden的列表中
                if (match(forbidden, other.numericCode()) == (forbidden.length - 1)) {

                    // 根据source和other找汇率数据
                    final ExchangeRate head = fetch(source, other, date);
                    try {
                        if (head != null) {
                            // 找other和target的汇率数据
                            final ExchangeRate tail = smartLookup(other, target, date, forbidden);
                            // 根据head和tail生成汇率对象，得到source和target的汇率对象
                            return ExchangeRate.chain(head, tail);
                        }
                    } catch (final Exception ex) {
                    }
                }
            }
        }

        // 循环完之后都没有找到，报错
        throw new LibraryException("找不到汇率数据");
    }

    /**
     * 从HashMap中获取指定日期的汇率，返回汇率对象
     */
    public ExchangeRate fetch(final Currency source, final Currency target, final Date date) {
        // 所有日期的汇率
        final List<Entry> rates = data_.get(hash(source, target));
        // 日期有效的索引，均无效返回-1
        final int i = matchValidateAt(rates, date);
        // 返回汇率
        return i == rates.size() - 1 ? rates.get(i).rate : null;
    }

    /**
     * 返回第一个等于特定数值的索引
     */
    private int match(final int[] list, final int value) {
        for (int i = 0; i < list.length; i++) {
            if (value == list[i])
                return i;
        }
        return -1;
    }

    /**
     * 返回第一个有效日期的索引
     */
    private int matchValidateAt(final List<Entry> rates, final Date date) {
        final validAt va = new validAt(date);
        for (int i = 0; i < rates.size(); i++) {
            if (va.operator(rates.get(i)))
                return i;
        }
        return -1;
    }

    /**
     * 用于判断日期是否在范围内
     */
    public static class validAt {
        Date d;
        public validAt(final Date d) {
            this.d = d;
        }

        public boolean operator(final Entry e) {
            return d.ge(e.startDate) && d.le(e.endDate);
        }
    }

    /**
     * 存储汇率数据的实体
     */
    public static class Entry {

        public ExchangeRate rate;

        public Date startDate, endDate;

        public Entry(final ExchangeRate rate, final Date start, final Date end) {
            this.rate = (rate);
            this.startDate = (start);
            this.endDate = (end);
        }
    }
}
