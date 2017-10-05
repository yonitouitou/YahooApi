package com.tradair.mock.services;

import com.tradair.mock.model.sender.Sender;
import com.tradair.mock.model.streaming.StreamingCtx;

public interface IStreamingService {

    /**
     * Starts a market stream according to the subscription
     * @param streamingCtx
     */
    void startStopStreaming(StreamingCtx streamingCtx);

    /**
     * Stops all active streaming for the given sender
     * @param sender
     */
    void stopAllStreaming(Sender sender);
}
