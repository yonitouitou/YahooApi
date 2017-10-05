package com.tradair.mock.services;

import com.tradair.mock.model.order.Order;
import com.tradair.mock.model.order.SpotOrder;
import com.tradair.mock.model.sender.QuickFixSender;
import com.tradair.mock.model.streaming.StreamingCtx;
import com.tradair.mock.model.subscription.RfsSubscription;
import com.tradair.mock.model.subscription.StreamingSubscription;
import com.tradair.mock.model.subscription.Subscription;
import com.tradair.mock.model.trade.SpotTrade;
import com.tradair.mock.model.trade.Trade;
import com.tradair.mock.util.FixUtil;
import com.tradair.mock.util.PerformanceConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FixService implements IFixService {

    private final Logger logger = LoggerFactory.getLogger(FixService.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private Map<String, StreamingCtx> streamingCtxMapBySession = new ConcurrentHashMap<>(PerformanceConst.SESSION_MAX_SIZE_100);

    @Autowired
    private IStreamingService streamingService;
    @Autowired
    private ITradingService tradingService;

    @Override
    public void newIncomingMessage(Message message, SessionID sessionId) throws FieldNotFound, UnsupportedMessageType {
        String msgType = message.getHeader().getString(MsgType.FIELD);
        switch (msgType) {
            case MsgType.MARKET_DATA_REQUEST:
                handleMarketRequest(message, sessionId);
                break;
            case MsgType.QUOTE_REQUEST:
                handleQuoteRequest(message, sessionId);
                break;
            case MsgType.ORDER_SINGLE:
                handleOrderSingle(message, sessionId);
                break;
            default:
                throw new UnsupportedMessageType();
        }
    }

    @Override
    public void onLogout(SessionID sessionId) {
        Session session = Session.lookupSession(sessionId);
        streamingService.stopAllStreaming(new QuickFixSender(session));
    }

    private void handleMarketRequest(Message message, SessionID sessionId) throws FieldNotFound {
        Subscription subscription = toStreamingSubscription(message);
        Session session = Session.lookupSession(sessionId);
        StreamingCtx ctx = new StreamingCtx(subscription, new QuickFixSender(session));
        streamingService.startStopStreaming(ctx);
    }

    private Subscription toStreamingSubscription(Message message) throws FieldNotFound {
        String mdReqId = message.getString(MDReqID.FIELD);
        Group noRelatedSymGrp = message.getGroup(NoRelatedSym.FIELD, 0);
        String symbol = noRelatedSymGrp.getString(Symbol.FIELD).replaceAll("/", "");
        int marketDepth = message.isSetField(MarketDepth.FIELD) ? message.getInt(MarketDepth.FIELD) : 5;
        if (marketDepth == 0) {
            marketDepth = 5;
        }
        int rythme = getRythme(message);
        boolean isSubscription = message.getChar(SubscriptionRequestType.FIELD) == SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES;
        return new StreamingSubscription(System.currentTimeMillis(), isSubscription, mdReqId, symbol, rythme, marketDepth);
    }

    private int getRythme(Message message) throws FieldNotFound {
        int rythme = message.isSetField(9000) ? message.getInt(9000) : 5;
        if (rythme > 1000) {
            rythme = 1000;
        }
        return rythme;
    }

    private void handleQuoteRequest(Message message, SessionID sessionId) throws FieldNotFound {
        Subscription subscription = toRfsSubscription(message);
        Session session = Session.lookupSession(sessionId);
        StreamingCtx ctx = new StreamingCtx(subscription, new QuickFixSender(session));
        streamingService.startStopStreaming(ctx);
    }

    private Subscription toRfsSubscription(Message message) throws FieldNotFound {
        String quoteReqId = message.getString(QuoteReqID.FIELD);
        int rythme = getRythme(message);
        Group noRelatedSymGrp = message.getGroup(NoRelatedSym.FIELD, 0);
        String symbol = noRelatedSymGrp.getString(Symbol.FIELD).replaceAll("/", "");
        double nearAmount = noRelatedSymGrp.getDouble(OrderQty.FIELD);
        double farAmount = noRelatedSymGrp.isSetField(OrderQty2.FIELD) ? noRelatedSymGrp.getDouble(OrderQty2.FIELD) : -1;
        ZonedDateTime nearDate = ZonedDateTime.parse(noRelatedSymGrp.getString(SettlDate.FIELD), DateTimeFormatter.BASIC_ISO_DATE).withZoneSameInstant(ZoneId.of("GMT"));
        ZonedDateTime farDate = noRelatedSymGrp.isSetField(SettlDate2.FIELD) ?
                ZonedDateTime.parse(noRelatedSymGrp.getString(SettlDate2.FIELD), DateTimeFormatter.BASIC_ISO_DATE).withZoneSameInstant(ZoneId.of("GMT"))
                : null;
        return new RfsSubscription(System.currentTimeMillis(), true, quoteReqId, symbol, rythme,
                nearAmount, farAmount, nearDate, farDate);
    }

    private void handleOrderSingle(Message message, SessionID sessionId) throws FieldNotFound {
        Order order = toOrder(message);
        logger.info(order.toString());
        Session session = Session.lookupSession(sessionId);
        session.send(toNewExecutionReport((SpotOrder) order));
        List<Trade> trades = tradingService.newOrder(order);
        trades.forEach(trade -> {
            Message executionReport = toExecutionReport((SpotTrade) trade);
            session.send(executionReport);
            logger.info(trade.toString());
        });
    }

    private Order toOrder(Message message) throws FieldNotFound {
        String externalId = message.getString(ClOrdID.FIELD);
        SpotOrder.OrderType orderType = toOrderType(message.getChar(OrdType.FIELD));
        SpotOrder.TimeInForce timeInForce = toTimeInForce(message.getChar(TimeInForce.FIELD));
        SpotOrder.Direction direction = toDirection(message.getChar(Side.FIELD));
        double amount = message.getDouble(OrderQty.FIELD);
        double price = message.isSetField(Price.FIELD) ? message.getDouble(Price.FIELD) : -1;
        String symbol = message.getString(Symbol.FIELD).replaceAll("/", "");
        String currency = message.getString(Currency.FIELD);
        String account = message.isSetField(Account.FIELD) ? message.getString(Account.FIELD) : null;
        ZonedDateTime valueDate = ZonedDateTime.of(LocalDate.parse(message.getString(SettlDate.FIELD), DateTimeFormatter.BASIC_ISO_DATE), LocalTime.MIN, ZoneId.of("GMT"));
        return new SpotOrder(System.currentTimeMillis(), externalId, symbol, currency, account, orderType, timeInForce, amount, price, direction, valueDate);
    }

    private SpotOrder.Direction toDirection(char directionSymbol) {
        return directionSymbol == Side.BUY ? SpotOrder.Direction.BUY : SpotOrder.Direction.SELL;
    }


    private SpotOrder.OrderType toOrderType(char orderTypeSymbol) {
        return orderTypeSymbol == OrdType.MARKET ? SpotOrder.OrderType.MARKET : SpotOrder.OrderType.LIMIT;
    }

    private SpotOrder.TimeInForce toTimeInForce(char timeInForceSymbol) {
        return timeInForceSymbol == TimeInForce.FILL_OR_KILL ? SpotOrder.TimeInForce.FILL_OR_KILL : SpotOrder.TimeInForce.IOC;
    }

    private Message toExecutionReport(SpotTrade trade) {
        ExecutionReport execution = new ExecutionReport();
        execution.setField(new OrderID(String.valueOf(trade.getOrderId())));
        execution.setField(new ClOrdID(trade.getExternalOrderId()));
        execution.setField(new ExecID(String.valueOf(trade.getId())));
        execution.setField(new OrdStatus(getOrderStatusFixSymbol(trade.getStatus())));
        execution.setField(new LastPx(trade.getPrice()));
        execution.setField(new Side(trade.getDirection() == SpotOrder.Direction.BUY ? Side.BUY : Side.SELL));
        execution.setField(new LastShares(trade.getAmount()));
        execution.setField(new Symbol(FixUtil.getSymbolWithSlash(trade.getSymbol())));
        execution.setField(new CumQty(trade.getCumQty()));
        execution.setField(new LeavesQty(trade.getLeaveQty()));
        execution.setField(new TransactTime(Calendar.getInstance().getTime()));
        if (trade.getTradeDate() != null) {
            execution.setField(new TradeDate(formatter.format(trade.getTradeDate())));
        }
        if (trade.getValueDate() != null) {
            execution.setField(new SettlDate(formatter.format(trade.getValueDate())));
        }
        if (trade.getPrice() > 0) {
            execution.setField(new Price(trade.getPrice()));
        }
        if (!StringUtils.isEmpty(trade.getText())) {
            execution.setField(new Text(trade.getText()));
        }
        if (! StringUtils.isEmpty(trade.getAccount())) {
            execution.setField(new Account(trade.getAccount()));
        }
        return execution;
    }

    private Message toNewExecutionReport(SpotOrder order) {
        ExecutionReport execution = new ExecutionReport();
        execution.setField(new OrderID(String.valueOf(order.getId())));
        execution.setField(new ClOrdID(order.getExternalId()));
        execution.setField(new OrdStatus(OrdStatus.NEW));
        execution.setField(new Symbol(FixUtil.getSymbolWithSlash(order.getSymbol())));
        execution.setField(new OrderQty(order.getAmount()));
        execution.setField(new Side(order.getDirection() == SpotOrder.Direction.BUY ? Side.BUY : Side.SELL));
        execution.setField(new TransactTime(Calendar.getInstance().getTime()));
        if (! StringUtils.isEmpty(order.getAccount())) {
            execution.setField(new Account(order.getAccount()));
        }
        if (order.getPrice() > 0) {
            execution.setField(new Price(order.getPrice()));
        }
        return execution;
    }

    private char getOrderStatusFixSymbol(Trade.OrderStatus status) {
        char st = OrdStatus.FILLED;
        switch (status) {
            case PARTIALLY_FILLED:
                st = OrdStatus.PARTIALLY_FILLED;
                break;
            case CANCELLED:
                st = OrdStatus.CANCELED;
                break;
            case REJECTED:
                st = OrdStatus.REJECTED;
                break;
        }
        return st;
    }
}
