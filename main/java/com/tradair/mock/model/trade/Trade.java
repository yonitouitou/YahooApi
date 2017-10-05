package com.tradair.mock.model.trade;

public abstract class Trade {

    protected final long id;
    protected final long orderId;
    protected final String externalOrderId;
    protected final OrderStatus status;
    protected final String text;
    protected final String account;

    public Trade(long id, long orderId, String externalOrderId, OrderStatus status, String text, String account) {
        this.id = id;
        this.orderId = orderId;
        this.externalOrderId = externalOrderId;
        this.status = status;
        this.text = text;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    public String getAccount() {
        return account;
    }

    public long getOrderId() {
        return orderId;
    }

    public enum OrderStatus {
        FILLED,
        PARTIALLY_FILLED,
        CANCELLED,
        REJECTED;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trade{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", externalOrderId='").append(externalOrderId).append('\'');
        sb.append(", status=").append(status);
        sb.append(", text='").append(text).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
