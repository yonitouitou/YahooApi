package com.tradair.mock.managers;

import com.tradair.mock.model.streaming.StreamingCtx;

public interface IStreamingManager {

    /**
     * Starts a market stream
     * @param ctx
     */
    void startStreaming(StreamingCtx ctx);
}
