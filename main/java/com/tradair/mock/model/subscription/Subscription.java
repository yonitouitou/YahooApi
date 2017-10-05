package com.tradair.mock.model.subscription;

public abstract class Subscription {

    protected long id;
    protected Type type;
    protected boolean isSubscription;
    protected String requestId;
    protected String symbol;
    protected int rythme;

    public Subscription(long id, Type type, boolean isSubscription, String requestId, String symbol, int rythme) {
        this.id = id;
        this.type = type;
        this.isSubscription = isSubscription;
        this.requestId = requestId;
        this.symbol = symbol;
        this.rythme = rythme;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setSubscription(boolean subscription) {
        isSubscription = subscription;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getRythme() {
        return rythme;
    }

    public void setRythme(int rythme) {
        this.rythme = rythme;
    }

    public Type getType() {
        return type;
    }


    public enum Type {
        STREAMING,
        RFS;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Subscription{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", isSubscription=").append(isSubscription);
        sb.append(", requestId='").append(requestId).append('\'');
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", rythme=").append(rythme);
        sb.append('}');
        return sb.toString();
    }
}
