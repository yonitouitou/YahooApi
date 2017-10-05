package com.tradair.mock.pricing;

public class Quote {

    protected final String id;
    protected final String symbol;
    protected final double amount;
    protected final double offer;
    protected final double bid;

    public Quote(String id, String symbol, double amount, double offer, double bid) {
        this.id = id;
        this.symbol = symbol;
        this.amount = amount;
        this.offer = offer;
        this.bid = bid;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getOffer() {
        return offer;
    }

    public double getBid() {
        return bid;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Quote{");
        sb.append("id='").append(id).append('\'');
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", offer=").append(offer);
        sb.append(", bid=").append(bid);
        sb.append('}');
        return sb.toString();
    }
}
