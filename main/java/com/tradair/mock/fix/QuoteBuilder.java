package com.tradair.mock.fix;

import com.tradair.mock.model.subscription.RfsSubscription;
import com.tradair.mock.model.subscription.StreamingSubscription;
import com.tradair.mock.model.subscription.Subscription;
import com.tradair.mock.pricing.IQuoteManager;
import com.tradair.mock.pricing.Quote;
import com.tradair.mock.util.PerformanceConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.*;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class QuoteBuilder {

    private final Map<String, Integer> marketDepthMapByRequestId = new ConcurrentHashMap<>(PerformanceConst.STREAMING_MAX_SIZE_100);

    @Autowired
    private IQuoteManager quoteManager;

    public Message createMarket(Subscription subscription) {
        Quote quote = quoteManager.getQuote(subscription.getSymbol());
        return subscription.getType() == Subscription.Type.STREAMING
                ? toMarketFixMessage((StreamingSubscription) subscription, quote)
                : toRfsQuoteFixMessage((RfsSubscription) subscription, quote);
    }

    public void clearMarketMetaData(String mdReqId) {
        marketDepthMapByRequestId.remove(mdReqId);
    }

    private Message toMarketFixMessage(StreamingSubscription subscription, Quote quote) {
        MarketDataSnapshotFullRefresh market = new MarketDataSnapshotFullRefresh();
        market.setField(new MDReqID(subscription.getRequestId()));
        market.setField(new Symbol(quote.getSymbol()));
        setNoMdEntries(market, quote, subscription);
        market.setField(new TransactTime(Calendar.getInstance().getTime()));
        return market;
    }

    private Message toRfsQuoteFixMessage(RfsSubscription subscription, Quote quote) {
        quickfix.fix44.Quote fixQuote = new quickfix.fix44.Quote();
        fixQuote.setField(new QuoteReqID(subscription.getRequestId()));
        fixQuote.setField(new QuoteID(subscription.getRequestId() + "_" + System.currentTimeMillis()));
        fixQuote.setField(new OrderQty(subscription.getNearAmount()));
        fixQuote.setField(new SettlDate(subscription.getNearDate().format(DateTimeFormatter.BASIC_ISO_DATE)));
        fixQuote.setField(new BidSpotRate(quote.getBid() - getRandom()));
        fixQuote.setField(new OfferSpotRate(quote.getOffer() + getRandom()));
        fixQuote.setField(new TransactTime(Calendar.getInstance().getTime()));
        double nearOfferPoint = getRandom();
        fixQuote.setField(new OfferForwardPoints(nearOfferPoint));
        double nearBidPoint = nearOfferPoint - getRandom();
        fixQuote.setField(new BidForwardPoints(nearBidPoint));
        if (subscription.isSwap()) {
            double farOfferPoint = nearOfferPoint + getRandom();
            fixQuote.setField(new OfferForwardPoints2(farOfferPoint));
            double farBidPoint = farOfferPoint - getRandom();
            fixQuote.setField(new BidForwardPoints2(farBidPoint));
            fixQuote.setField(new OrderQty2(subscription.getFarAmount()));
            fixQuote.setField(new SettlDate2(subscription.getFarDate().format(DateTimeFormatter.BASIC_ISO_DATE)));
        }
        return fixQuote;

    }

    private void setNoMdEntries(MarketDataSnapshotFullRefresh market, Quote quote, StreamingSubscription subscription) {
        int marketDepth = getMarketDepth(subscription);
        addEntries(market, quote, marketDepth);
    }

    private int getMarketDepth(StreamingSubscription subscription) {
        int marketDepth = subscription.getMarketDepth();
        if (marketDepth < 0) {
            marketDepth = marketDepthMapByRequestId.getOrDefault(subscription.getRequestId(), 1) % 6;
            if (marketDepth == 0) {
                marketDepth = 1;
            }
            marketDepthMapByRequestId.put(subscription.getRequestId(), marketDepth + 1);
        }
        return marketDepth;
    }

    private void addEntries(MarketDataSnapshotFullRefresh market, Quote quote, int marketDepth) {
        double bidPrice = quote.getBid();
        double offerPrice = quote.getOffer();
        for (int i = 0; i < marketDepth; i++) {
            double amount = quote.getAmount() * (i + 1);
            Group offerGrp = new MarketDataSnapshotFullRefresh.NoMDEntries();
            offerGrp.setField(new MDEntryType(MDEntryType.OFFER));
            offerGrp.setField(new QuoteEntryID(amount + "_" + quote.getOffer()));
            offerGrp.setField(new MDEntrySize(amount));
            offerPrice = offerPrice + getRandom();
            offerGrp.setField(new MDEntryPx(offerPrice));
            offerGrp.setField(new QuoteCondition(QuoteCondition.OPEN_ACTIVE));
            market.addGroup(offerGrp);
            Group bidGrp = new MarketDataSnapshotFullRefresh.NoMDEntries();
            bidGrp.setField(new MDEntryType(MDEntryType.BID));
            bidGrp.setField(new QuoteEntryID(amount + "_" + quote.getBid()));
            bidGrp.setField(new MDEntrySize(amount));
            bidPrice = bidPrice - getRandom();
            bidGrp.setField(new MDEntryPx(bidPrice));
            offerGrp.setField(new QuoteCondition(QuoteCondition.OPEN_ACTIVE));
            market.addGroup(bidGrp);
        }
    }

    private double getRandom() {
        double min = 0.0001;
        double max = 0.01;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
