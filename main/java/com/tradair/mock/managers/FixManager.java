package com.tradair.mock.managers;

import com.tradair.mock.services.IFixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

@Service
public class FixManager implements IFixManager {

    @Autowired
    private IFixService fixService;

    @Override
    public void newIncomingMessage(Message message, SessionID sessionId) throws FieldNotFound, UnsupportedMessageType {
        fixService.newIncomingMessage(message, sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        fixService.onLogout(sessionId);
    }
}
