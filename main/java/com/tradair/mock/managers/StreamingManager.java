package com.tradair.mock.managers;

import com.tradair.mock.model.streaming.StreamingCtx;
import com.tradair.mock.services.IStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamingManager implements IStreamingManager {

    @Autowired
    private IStreamingService streamingService;

    @Override
    public void startStreaming(StreamingCtx ctx) {
        streamingService.startStopStreaming(ctx);
    }

}
