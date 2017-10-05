package com.tradair.mock.api.yahoofinance;

public class YahooQueryResponse {

	private YahooQueryResponseHeader query;

	public YahooQueryResponse() {
		this(null);
	}

	public YahooQueryResponse(YahooQueryResponseHeader query) {
		this.query = query;
	}

	public YahooQueryResponseHeader getQuery() {
		return query;
	}

	public void setQuery(YahooQueryResponseHeader query) {
		this.query = query;
	}


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("YahooQueryResponse{");
		sb.append("query=").append(query);
		sb.append('}');
		return sb.toString();
	}
}
