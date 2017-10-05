package com.tradair.mock.api.yahoofinance;

import java.util.ArrayList;
import java.util.List;

public class YahooQueryResults {

	private List<YahooQueryResponseRate> rate;

	public YahooQueryResults() {
		this(new ArrayList<YahooQueryResponseRate>());
	}

	public YahooQueryResults(List<YahooQueryResponseRate> rate) {
		this.rate = rate;
	}

	public List<YahooQueryResponseRate> getRate() {
		return rate;
	}

	public void setRate(List<YahooQueryResponseRate> rate) {
		this.rate = rate;
	}


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("YahooQueryResults{");
		sb.append("rate=").append(rate);
		sb.append('}');
		return sb.toString();
	}
}
