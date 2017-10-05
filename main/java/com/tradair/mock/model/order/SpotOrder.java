package com.tradair.mock.model.order;

import java.time.ZonedDateTime;

public class SpotOrder extends Order {

    protected final OrderType orderType;
    protected final TimeInForce timeInForce;
    protected final Direction direction;
    protected final String currency;
    protected final double amount;
    protected final double price;
    protected final ZonedDateTime valueDate;

    public SpotOrder(long id, String externalId, String symbol, String currency, String account, OrderType orderType,
                     TimeInForce timeInForce, double amount, double price,
                     Direction direction, ZonedDateTime valueDate) {
        super(id, externalId, symbol, account);
        this.orderType = orderType;
        this.timeInForce = timeInForce;
        this.amount = amount;
        this.price = price;
        this.currency = currency;
        this.direction = direction;
        this.valueDate = valueDate;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public TimeInForce getTimeInForce() {
        return timeInForce;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public ZonedDateTime getValueDate() {
        return valueDate;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getCurrency() {
        return currency;
    }

    public enum OrderType {
        MARKET,
        LIMIT;
    }

    public enum TimeInForce {
        IOC,
        FILL_OR_KILL;
    }

    public enum Direction {
        BUY,
        SELL;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SpotOrder{");
        sb.append("orderType=").append(orderType);
        sb.append(", timeInForce=").append(timeInForce);
        sb.append(", direction=").append(direction);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", price=").append(price);
        sb.append(", valueDate=").append(valueDate);
        sb.append(", id=").append(id);
        sb.append(", externalId='").append(externalId).append('\'');
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
