package com.tradair.mock.api.yahoofinance;

public class YahooQueryResponseHeader {

	private int count;
	private String created;
	private String lang;
	private YahooQueryResults results;

	public YahooQueryResponseHeader() {
		this(0, null, null, null);
	}

	public YahooQueryResponseHeader(int count, String created, String lang, YahooQueryResults results) {
		this.count = count;
		this.created = created;
		this.lang = lang;
		this.results = results;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public YahooQueryResults getResults() {
		return results;
	}

	public void setResults(YahooQueryResults results) {
		this.results = results;
	}


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("YahooQueryResponseHeader{");
		sb.append("count=").append(count);
		sb.append(", created='").append(created).append('\'');
		sb.append(", lang='").append(lang).append('\'');
		sb.append(", results=").append(results);
		sb.append('}');
		return sb.toString();
	}
}
