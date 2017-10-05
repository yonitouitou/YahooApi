package com.tradair.mock.model.order;

public abstract class Order {

    protected final long id;
    protected final String externalId;
    protected final String symbol;
    protected final String account;

    public Order(long id, String externalId, String symbol, String account) {
        this.id = id;
        this.externalId = externalId;
        this.account = account;
        this.symbol = symbol;
    }

    public long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccount() {
        return account;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("id=").append(id);
        sb.append(", externalId='").append(externalId).append('\'');
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
