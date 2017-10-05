package com.tradair.mock.pricing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteManager implements IQuoteManager {

    @Autowired
    private IQuoteService quoteService;

    @Override
    public Quote getQuote(String symbol) {
        return quoteService.getQuote(symbol);
    }

    @Override
    public void updateQuote(String symbol, Quote quote) {
        quoteService.updateQuote(symbol, quote);
    }
}
