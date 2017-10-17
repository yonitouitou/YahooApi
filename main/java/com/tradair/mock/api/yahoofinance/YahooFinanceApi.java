package com.tradair.mock.api.yahoofinance;

import com.tradair.mock.pricing.Quote;
import com.tradair.mock.util.HttpClient;
import com.tradair.mock.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class YahooFinanceApi {

    private static final String QUERY = "select Ask,Bid,id from yahoo.finance.xchange where pair in ";
    private static final String LAST_PRICES_FILE = "yahooPrices.txt";
    private static final String FORMATTED_REQUEST = "q=%s&format=%s&env=%s&callback=";
    private static final String DEFAULT_FORMAT = "json";
    private static final String DEFAULT_ENV = "store://datatables.org/alltableswithkeys";

    private static final Logger logger = LoggerFactory.getLogger(YahooFinanceApi.class);

    public static final String YQL_URL = "http://query.yahooapis.com/v1/public/yql";

    private String format;
    private String env;
    private String configFolder;

    public Quote getPrices(String symbol) {
        Set<String> ccpairs = new HashSet<>();
        ccpairs.add(symbol.replaceAll("/", ""));
        String request = buildEncodedQueryString(ccpairs);
        String response = HttpClient.sendGetRequest(YQL_URL, request);
        YahooQueryResponse yahooResponse = JsonUtil.fromJson(response, YahooQueryResponse.class);
        Quote quote = null;
        if (yahooResponse != null) {
            quote = toQuote(yahooResponse, symbol);
        }
        return quote;
    }

    private Quote toQuote(YahooQueryResponse response, String symbol) {
        List<YahooQueryResponseRate> rate = response.getQuery().getResults().getRate();
        YahooQueryResponseRate res = rate.get(0);
        return new Quote(res.getId(), symbol, 1000000, res.getAsk(), res.getBid());
    }

    private String buildEncodedQueryString(Set<String> ccPairSet) {
        String query = buildEncodedQuery(ccPairSet);
        String result = null;
        try {
            result = String.format(FORMATTED_REQUEST, query, URLEncoder.encode(DEFAULT_FORMAT, "UTF-8"), URLEncoder.encode(DEFAULT_ENV, "UTF-8"));
            return result;
        }
        catch (UnsupportedEncodingException e) {
            logger.error("error on encoding", e);
        }
        return result;
    }

    private String buildEncodedQuery(Set<String> ccPairSet) {
        String result = null;
        StringBuilder symbols = new StringBuilder();
        for (String symbol : ccPairSet) {
            symbols.append('\"').append(symbol).append('\"').append(',');
        }
        if (symbols.toString().endsWith(",")) {
            symbols.deleteCharAt(symbols.lastIndexOf(","));
        }
        try {
            String encodedQuery = URLEncoder.encode(QUERY, "UTF-8");
            String encodedSymbols = '(' + URLEncoder.encode(symbols.toString(), "UTF-8") + ')';
            result = encodedQuery + encodedSymbols;
        }
        catch (UnsupportedEncodingException e) {
            logger.error("error on encoding", e);
        }
        return result;
    }
}
