
package yjm.value.time;


/**
 * 存储历史数据
 */
public class TimeSeries<V> extends Series<Date,V> { 
	
	public TimeSeries(final Class<V> classV) {
		super(Date.class, classV);
	}
	
}
