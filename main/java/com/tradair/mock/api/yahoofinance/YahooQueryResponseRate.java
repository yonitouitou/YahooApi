package com.tradair.mock.api.yahoofinance;

import com.fasterxml.jackson.annotation.JsonSetter;

public class YahooQueryResponseRate {

	private String id;
	private double Ask;
	private double Bid;

	public YahooQueryResponseRate() {
		this(null, 0, 0);
	}

	public YahooQueryResponseRate(String id, double Ask, double Bid) {
		this.id = null;
		this.Ask = Ask;
		this.Bid = Bid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getAsk() {
		return Ask;
	}

	@JsonSetter("Ask")
	public void setAsk(double ask) {
		this.Ask = ask;
	}

	public double getBid() {
		return Bid;
	}
	@JsonSetter("Bid")
	public void setBid(double bid) {
		this.Bid = bid;
	}


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("YahooQueryResponseRate{");
		sb.append("id='").append(id).append('\'');
		sb.append(", Ask=").append(Ask);
		sb.append(", Bid=").append(Bid);
		sb.append('}');
		return sb.toString();
	}
}
