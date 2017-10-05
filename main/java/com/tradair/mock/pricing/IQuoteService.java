package com.tradair.mock.pricing;

public interface IQuoteService {

    Quote getQuote(String symbol);

    void updateQuote(String symbol, Quote quote);
}
