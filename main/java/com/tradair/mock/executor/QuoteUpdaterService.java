package com.tradair.mock.executor;

import com.tradair.mock.model.streaming.UpdaterThread;
import com.tradair.mock.pricing.IQuoteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Service
public class QuoteUpdaterService {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IQuoteManager quoteManager;

    private Map<String, ScheduledFuture<?>> threadUpdaterMapBySymbol = new ConcurrentHashMap<>(200);

    public void updatePrice(String symbol) {
        threadUpdaterMapBySymbol.computeIfAbsent(symbol, p -> startUpdaterThread(symbol));
    }

    public void stopUpdaterThread(String symbol) {
        ScheduledFuture scheduledFuture = threadUpdaterMapBySymbol.get(symbol);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    private ScheduledFuture<?> startUpdaterThread(String symbol) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        UpdaterThread updaterThread = applicationContext.getBean(UpdaterThread.class);
        updaterThread.setSymbol(symbol);
        return executor.scheduleAtFixedRate(updaterThread, 0, 5, TimeUnit.SECONDS);
    }
}
