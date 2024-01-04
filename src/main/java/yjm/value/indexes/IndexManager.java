package yjm.value.indexes;

import yjm.value.time.TimeSeries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Index仓库，提供方法提取给定日期的index数据
 */
public class IndexManager {

    private static final long serialVersionUID = -9204254124065694863L;

	/**
	 * HashMap存储index数据，Key为index名，Value为index的时间序列
	 */
    private static Map<String, TimeSeries<Double>> data;

	/**
	 * Singleton实例，确保类只有一个实例，自行实例化并向整个系统提供实例
	 */
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
	 * 返回历史index时间序列
	 */
	public TimeSeries<Double> getHistory(final String name) {
		return data.get(name);
	}

	/**
	 * 修改历史index时间序列
	 */
	public void setHistory(final String name, final TimeSeries<Double> history) {
		data.put(name, history);
	}

	/**
	 * 清除指定index
	 */
	public void clearHistory(final String name) {
		data.remove(name);
	}

	/**
	 * 清除所有index数据
	 */
	public void clearHistories() {
		data.clear();
	}

}
