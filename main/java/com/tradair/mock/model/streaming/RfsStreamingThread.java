package com.tradair.mock.model.streaming;

import com.tradair.mock.fix.QuoteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.Message;

@Component
public class RfsStreamingThread implements Runnable {

    private StreamingCtx ctx;

    @Autowired
    private QuoteBuilder quoteBuilder;

    @Override
    public void run() {
        Message message = quoteBuilder.createMarket(ctx.getSubscription());
        ctx.getSender().send(message);
    }

    public void setCtx(StreamingCtx ctx) {
        this.ctx = ctx;
    }
}
