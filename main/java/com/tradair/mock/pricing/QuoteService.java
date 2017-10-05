package com.tradair.mock.pricing;

import com.tradair.mock.util.PerformanceConst;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuoteService implements IQuoteService {

    private Map<String, Quote> quoteMapBySymbol = new ConcurrentHashMap<>(PerformanceConst.STREAMING_MAX_SIZE_100);

    @Override
    public Quote getQuote(String symbol) {
        return quoteMapBySymbol.get(symbol);
    }

    @Override
    public void updateQuote(String symbol, Quote quote) {
        quoteMapBySymbol.put(symbol, quote);
    }
}
