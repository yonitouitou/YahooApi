package com.tradair.mock.model.subscription;

public class StreamingSubscription extends Subscription {

    protected int marketDepth;

    public StreamingSubscription(long id, boolean isSubscription, String mdReqId, String symbol, int rythme, int marketDepth) {
        super(id, Type.STREAMING, isSubscription, mdReqId, symbol, rythme);
        this.marketDepth = marketDepth;
    }

    public int getMarketDepth() {
        return marketDepth;
    }

    public void setMarketDepth(int marketDepth) {
        this.marketDepth = marketDepth;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StreamingSubscription{");
        sb.append("marketDepth=").append(marketDepth);
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", isSubscription=").append(isSubscription);
        sb.append(", requestId='").append(requestId).append('\'');
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", rythme=").append(rythme);
        sb.append('}');
        return sb.toString();
    }
}
