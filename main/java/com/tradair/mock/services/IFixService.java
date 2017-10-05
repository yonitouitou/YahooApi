package com.tradair.mock.services;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

public interface IFixService {

    /**
     * Handles a new incoming FIX message
     * @param message
     * @param sessionId
     * @throws FieldNotFound
     * @throws UnsupportedMessageType
     */
    void newIncomingMessage(Message message, SessionID sessionId) throws FieldNotFound, UnsupportedMessageType;

    /**
     * On logout process
     * @param sessionId
     */
    void onLogout(SessionID sessionId);
}
