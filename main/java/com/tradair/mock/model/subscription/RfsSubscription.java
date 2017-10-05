package com.tradair.mock.model.subscription;

import java.time.ZonedDateTime;

public class RfsSubscription extends Subscription {

    protected double nearAmount;
    protected double farAmount;
    protected ZonedDateTime nearDate;
    protected ZonedDateTime farDate;

    public RfsSubscription(long id, boolean isSubscription, String requestId,
                           String symbol, int rythme, double nearAmount,
                           double farAmount, ZonedDateTime nearDate, ZonedDateTime farDate) {
        super(id, Type.RFS, isSubscription, requestId, symbol, rythme);
        this.nearAmount = nearAmount;
        this.farAmount = farAmount;
        this.nearDate = nearDate;
        this.farDate = farDate;
    }

    public double getNearAmount() {
        return nearAmount;
    }

    public void setNearAmount(double nearAmount) {
        this.nearAmount = nearAmount;
    }

    public double getFarAmount() {
        return farAmount;
    }

    public void setFarAmount(double farAmount) {
        this.farAmount = farAmount;
    }

    public ZonedDateTime getNearDate() {
        return nearDate;
    }

    public void setNearDate(ZonedDateTime nearDate) {
        this.nearDate = nearDate;
    }

    public ZonedDateTime getFarDate() {
        return farDate;
    }

    public void setFarDate(ZonedDateTime farDate) {
        this.farDate = farDate;
    }

    public boolean isSwap() {
        return farDate != null;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RfsSubscription{");
        sb.append("nearAmount=").append(nearAmount);
        sb.append(", farAmount=").append(farAmount);
        sb.append(", nearDate=").append(nearDate);
        sb.append(", farDate=").append(farDate);
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
