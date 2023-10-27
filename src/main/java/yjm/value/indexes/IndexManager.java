package yjm.value.indexes;

import yjm.value.time.TimeSeries;
import yjm.value.util.Observable;
import yjm.value.util.ObservableValue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class IndexManager {

    private static final long serialVersionUID = -9204254124065694863L;
    
    private static Map<String, TimeSeries<Double>> data;
    private static volatile IndexManager instance;

    
    public static IndexManager getInstance() {
		if (instance == null) {
			synchronized (IndexManager.class) {
				if (instance == null) {
					instance = new IndexManager();
				}
			}
		}
		return instance;
	}

    private IndexManager() {
	    this.data = new ConcurrentHashMap<String, TimeSeries<Double>>();
	}

	/**
	 * 返回定盘数据
	 */
	public TimeSeries<Double> getHistory(final String name) {
		return data.get(name);
	}

	public void setHistory(final String name, final TimeSeries<Double> history) {
		data.put(name, history);
	}

	public void clearHistory(final String name) {
		data.remove(name);
	}

	public void clearHistories() {
		data.clear();
	}

	public Observable notifier(final String name) {
	    TimeSeries<Double> value = data.get(name);
		if (value == null){
			value = new TimeSeries<Double>(Double.class);
			data.put(name, value);
		}
		return new ObservableValue<TimeSeries<Double>>(value);
	}

}
