package com.tradair.mock.model.trade;

import com.tradair.mock.model.order.SpotOrder;

import java.time.ZonedDateTime;

public class SpotTrade extends Trade {

    protected final SpotOrder.Direction direction;
    protected final double requestPrice;
    protected final double price;
    protected final double amount;
    protected final double cumQty;
    protected final double leaveQty;
    protected final String symbol;
    protected final ZonedDateTime tradeDate;
    protected final ZonedDateTime valueDate;

    public SpotTrade(long id, long orderId, String externalOrderId, OrderStatus status,
                     String text, String account, String symbol, SpotOrder.Direction direction, double requestPrice, double price,
                     double amount, double cumQty, double leaveQty, ZonedDateTime tradeDate, ZonedDateTime valueDate) {
        super(id, orderId, externalOrderId, status, text, account);
        this.symbol = symbol;
        this.requestPrice = requestPrice;
        this.price = price;
        this.amount = amount;
        this.cumQty = cumQty;
        this.leaveQty = leaveQty;
        this.tradeDate = tradeDate;
        this.valueDate = valueDate;
        this.direction = direction;
    }

    public SpotOrder.Direction getDirection() {
        return direction;
    }

    public double getRequestPrice() {
        return requestPrice;
    }

    public double getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }

    public ZonedDateTime getTradeDate() {
        return tradeDate;
    }

    public ZonedDateTime getValueDate() {
        return valueDate;
    }

    public double getCumQty() {
        return cumQty;
    }

    public double getLeaveQty() {
        return leaveQty;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SpotTrade{");
        sb.append("direction=").append(direction);
        sb.append(", requestPrice=").append(requestPrice);
        sb.append(", price=").append(price);
        sb.append(", amount=").append(amount);
        sb.append(", cumQty=").append(cumQty);
        sb.append(", leaveQty=").append(leaveQty);
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", tradeDate=").append(tradeDate);
        sb.append(", valueDate=").append(valueDate);
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", externalOrderId='").append(externalOrderId).append('\'');
        sb.append(", status=").append(status);
        sb.append(", text='").append(text).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
