package com.tradair.mock.model.streaming;

import com.tradair.mock.api.yahoofinance.YahooFinanceApi;
import com.tradair.mock.pricing.IQuoteManager;
import com.tradair.mock.pricing.Quote;
import com.tradair.mock.util.PerformanceConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class UpdaterThread implements Runnable {

    @Autowired
    private YahooFinanceApi yahooFinanceApi;
    @Autowired
    private IQuoteManager quoteManager;

    private String symbol;
    private Lock lock = new ReentrantLock();
    private Set<String> symbols = new LinkedHashSet<>(PerformanceConst.STREAMING_MAX_SIZE_100);

    @Override
    public void run() {
        symbols.parallelStream()
                .forEach(symbol -> {
                    Quote quote = yahooFinanceApi.getPrices(symbol);
                    if (quote != null) {
                        quoteManager.updateQuote(symbol, quote);
                        System.out.println(quote);
                    }
                });
    }

    public void addSymbol(String symbol) {
        try {
            lock.lock();
            symbols.add(symbol);
        } finally {
            lock.unlock();
        }
    }

    public void removeSymbol(String symbol) {
        try {
            lock.lock();
            symbols.remove(symbol);
        } finally {
            lock.unlock();
        }
    }
}
