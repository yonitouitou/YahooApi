package com.tradair.mock.model.sender;

import quickfix.Message;
import quickfix.Session;

public class QuickFixSender extends Sender<Session> {

    public QuickFixSender(Session payload) {
        super(payload.getSessionID().getSenderCompID(), payload);
    }

    @Override
    public <U> boolean send(U msg) {
        return payload.send((Message) msg);
    }
}
