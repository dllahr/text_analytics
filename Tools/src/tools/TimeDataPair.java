package tools;

public class TimeDataPair<T, X> {
	private T timeValue;
	private X dataValue;
	
	public TimeDataPair(T timeValue, X dataValue) {
		this.timeValue = timeValue;
		this.dataValue = dataValue;
	}

	public T getTimeValue() {
		return timeValue;
	}

	public X getDataValue() {
		return dataValue;
	}
}
