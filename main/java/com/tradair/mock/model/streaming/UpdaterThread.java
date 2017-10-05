package com.tradair.mock.model.streaming;

import com.tradair.mock.api.yahoofinance.YahooFinanceApi;
import com.tradair.mock.pricing.IQuoteManager;
import com.tradair.mock.pricing.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdaterThread implements Runnable {

    @Autowired
    private YahooFinanceApi yahooFinanceApi;
    @Autowired
    private IQuoteManager quoteManager;

    private String symbol;

    @Override
    public void run() {
        Quote quote = yahooFinanceApi.getPrices(symbol);
        if (quote != null) {
            quoteManager.updateQuote(symbol, quote);
            System.out.println(quote);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
