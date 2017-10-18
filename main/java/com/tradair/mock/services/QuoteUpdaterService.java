package com.tradair.mock.services;

import com.tradair.mock.model.streaming.UpdaterThread;
import com.tradair.mock.pricing.IQuoteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class QuoteUpdaterService {

    private final Logger logger = LoggerFactory.getLogger(QuoteUpdaterService.class);
    private ScheduledExecutorService executor;
    private UpdaterThread updaterThread;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IQuoteManager quoteManager;

    @PostConstruct
    private void init() {
        updaterThread = applicationContext.getBean(UpdaterThread.class);
    }

    public void startSymbolUpdater(String symbol) {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(updaterThread, 0, 5, TimeUnit.SECONDS);
        }
        updaterThread.addSymbol(symbol);
        logger.info("Symbol added to Yahoo updater API : " + symbol);
    }

    public void stopSymbolUpdater(String symbol) {
        updaterThread.removeSymbol(symbol);
        logger.info("Symbol removed from Yahoo updater API : " + symbol);
    }
}
