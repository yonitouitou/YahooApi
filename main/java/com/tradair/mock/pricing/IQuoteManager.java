package com.tradair.mock.pricing;

public interface IQuoteManager {

    Quote getQuote(String symbol);

    void updateQuote(String symbol, Quote quote);
}
