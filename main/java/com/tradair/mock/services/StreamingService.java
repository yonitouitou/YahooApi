package com.tradair.mock.services;

import com.tradair.mock.fix.QuoteBuilder;
import com.tradair.mock.model.sender.Sender;
import com.tradair.mock.model.streaming.StreamingCtx;
import com.tradair.mock.model.streaming.StreamingThread;
import com.tradair.mock.model.subscription.Subscription;
import com.tradair.mock.util.PerformanceConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import quickfix.Message;
import quickfix.field.MDReqID;
import quickfix.field.Text;
import quickfix.fix42.MarketDataRequestReject;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamingService implements IStreamingService {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private QuoteBuilder quoteBuilder;
    @Autowired
    private QuoteUpdaterService quoteUpdaterService;

    private final Map<String, Map<String, ScheduledFuture<?>>> activeStreamingByExternalSenderId = new ConcurrentHashMap<>(PerformanceConst.STREAMING_MAX_SIZE_100);
    private final Map<String, AtomicInteger> subscriptionsCounterMapBySymbol = new ConcurrentHashMap<>(PerformanceConst.STREAMING_MAX_SIZE_100);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(PerformanceConst.STREAMING_MAX_SIZE_100);

    @Override
    public void startStopStreaming(StreamingCtx streamingCtx) {
        Subscription subscription = streamingCtx.getSubscription();
        if (subscription.isSubscription()) {
            newActiveStreaming(streamingCtx);
        } else {
            stopActiveStreaming(streamingCtx);
        }
    }

    @Override
    public void stopAllStreaming(Sender sender) {
        Map<String, ScheduledFuture<?>> activeStreamingMapByRequestid = activeStreamingByExternalSenderId.remove(sender.getExternalId());
        if (activeStreamingMapByRequestid != null) {
            activeStreamingMapByRequestid.values()
                    .stream()
                    .forEach(streamingThread -> streamingThread.cancel(false));
        }
    }

    private void newActiveStreaming(StreamingCtx streamingCtx) {
        Subscription subscription = streamingCtx.getSubscription();
        if (isActiveStreaming(streamingCtx)) {
            Message reject = createSubscriptionReject(subscription.getRequestId(), "Streaming " + subscription.getRequestId() + " already active");
            streamingCtx.getSender().send(reject);
        } else if (subscriptionNumberExceeded()) {
            Message reject = createSubscriptionReject(subscription.getRequestId(), "Active Streaming number exceeded : " + PerformanceConst.STREAMING_MAX_SIZE_100);
            streamingCtx.getSender().send(reject);
        } else {
            AtomicInteger counter = subscriptionsCounterMapBySymbol.computeIfAbsent(subscription.getSymbol(), l -> new AtomicInteger(0));
            counter.incrementAndGet();
            quoteUpdaterService.updatePrice(subscription.getSymbol());
            StreamingThread streamingThread = applicationContext.getBean(StreamingThread.class);
            streamingThread.setCtx(streamingCtx);
            ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(streamingThread, 0, 1000 / subscription.getRythme(), TimeUnit.MILLISECONDS);
            saveActiveStreaming(streamingCtx, scheduledFuture);
        }
    }

    private void stopActiveStreaming(StreamingCtx ctx) {
        Subscription subscription = ctx.getSubscription();
        String requestIdToStop = subscription.getRequestId();
        Sender sender = ctx.getSender();
        Map<String, ScheduledFuture<?>> activeStreamingMapByRequestId = activeStreamingByExternalSenderId.get(sender.getExternalId());
        ScheduledFuture<?> activeStreaming = null;
        if (activeStreamingMapByRequestId != null) {
            activeStreaming = activeStreamingMapByRequestId.get(requestIdToStop);
        }
        if (activeStreamingMapByRequestId == null || activeStreaming == null) {
            Message reject = createSubscriptionReject(requestIdToStop, "Active streaming not found : " + requestIdToStop);
            sender.send(reject);
        } else {
            removeActiveStreamingByMdReqId(ctx);
            activeStreaming.cancel(false);
            quoteBuilder.clearMarketMetaData(requestIdToStop);
            AtomicInteger counter = subscriptionsCounterMapBySymbol.get(subscription.getSymbol());
            counter.decrementAndGet();
            if (counter.get() == 0) {
                quoteUpdaterService.stopUpdaterThread(subscription.getSymbol());
            }
        }
    }

    private boolean isActiveStreaming(StreamingCtx ctx) {
        Map<String, ScheduledFuture<?>> activeStreamingMapByRequestId = activeStreamingByExternalSenderId.get(ctx.getSender().getExternalId());
        return activeStreamingMapByRequestId != null && activeStreamingMapByRequestId.get(ctx.getSubscription().getRequestId()) != null;
    }

    private boolean subscriptionNumberExceeded() {
        return activeStreamingByExternalSenderId.values()
                .stream()
                .flatMap(l -> l.values().stream())
                .count() >= 100;
    }

    private void saveActiveStreaming(StreamingCtx ctx, ScheduledFuture<?> streamingThread) {
        Subscription subscription = ctx.getSubscription();
        Sender sender = ctx.getSender();
        Map<String, ScheduledFuture<?>> activeStreamingMapByRequestId = activeStreamingByExternalSenderId.computeIfAbsent(sender.getExternalId(), map -> new ConcurrentHashMap<>());
        activeStreamingMapByRequestId.put(subscription.getRequestId(), streamingThread);
    }

    private void removeActiveStreamingByMdReqId(StreamingCtx ctx) {
        Subscription subscription = ctx.getSubscription();
        Sender sender = ctx.getSender();
        Map<String, ScheduledFuture<?>> activeStreamingMapByRequestId = activeStreamingByExternalSenderId.get(sender.getExternalId());
        if (activeStreamingMapByRequestId != null) {
            activeStreamingMapByRequestId.remove(subscription.getRequestId());
            if (activeStreamingMapByRequestId.isEmpty()) {
                activeStreamingByExternalSenderId.remove(sender.getExternalId());
            }
        }
    }

    private Message createSubscriptionReject(String mdReqId, String reason) {
        MarketDataRequestReject reject = new MarketDataRequestReject();
        reject.setString(MDReqID.FIELD, mdReqId);
        if (! StringUtils.isEmpty(reason)) {
            reject.setString(Text.FIELD, reason);
        }
        return reject;
    }
}
