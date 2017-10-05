package com.tradair.mock.model.streaming;

import com.tradair.mock.model.sender.Sender;
import com.tradair.mock.model.subscription.Subscription;

public class StreamingCtx {

    private Subscription subscription;
    private Sender sender;

    public StreamingCtx(Subscription subscription, Sender sender) {
        this.subscription = subscription;
        this.sender = sender;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public Sender getSender() {
        return sender;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StreamingCtx{");
        sb.append("subscription=").append(subscription);
        sb.append(", sender=").append(sender);
        sb.append('}');
        return sb.toString();
    }
}
