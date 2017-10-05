package com.tradair.mock.fix;

import com.tradair.common.serialization.AbstractSerializer;
import com.tradair.common.serialization.kryo.KryoSerializer;
import com.tradair.mock.managers.IFixManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.*;

@Component
public class Acceptor implements Application {

    @Autowired
    private IFixManager fixManager;

    @Override
    public void onCreate(SessionID sessionID) {

    }

    private static final AbstractSerializer serializer = new KryoSerializer();

    @Override
    public void onLogon(SessionID sessionID) {
        System.err.println("ON LOGONNNNNNNNNNNNNNNN");

    }

    @Override
    public void onLogout(SessionID sessionID) {
        fixManager.onLogout(sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {

    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        fixManager.newIncomingMessage(message, sessionID);
    }
}
