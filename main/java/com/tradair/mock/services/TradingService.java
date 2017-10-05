package com.tradair.mock.services;

import com.tradair.mock.model.order.Order;
import com.tradair.mock.model.order.SpotOrder;
import com.tradair.mock.model.trade.SpotTrade;
import com.tradair.mock.model.trade.Trade;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TradingService implements ITradingService {

    private static int orderCounter = 0;
    private static int improvementPriceCounter = 0;


    @Override
    public List<Trade> newOrder(Order order) {
        SpotOrder spotOrder = (SpotOrder) order;
        incrementCounters();
        List<Trade> trades = null;
        Trade.OrderStatus status = getOrderStatus(spotOrder.getTimeInForce());
        switch (status) {
            case FILLED:
                trades = createFilledTrade(spotOrder);
                break;
            case PARTIALLY_FILLED:
                trades = createPartiallyFilledTrade(spotOrder);
                break;
            case CANCELLED:
                trades = createCancelledTrade(spotOrder);
                break;
            default:
                break;
        }
        return trades;
    }

    private void incrementCounters() {
        orderCounter++;
        improvementPriceCounter++;
    }

    private Trade.OrderStatus getOrderStatus(SpotOrder.TimeInForce tif) {
        Trade.OrderStatus status = Trade.OrderStatus.FILLED;
        if (orderCounter % 3 == 0 && tif != SpotOrder.TimeInForce.FILL_OR_KILL) {
            status = Trade.OrderStatus.PARTIALLY_FILLED;
        } else if (orderCounter % 5 == 0) {
            status = Trade.OrderStatus.CANCELLED;
        }
        return status;
    }

    private double getTradePrice(double requestedPrice, SpotOrder.OrderType orderType) {
        double tradePrice = requestedPrice;
        if (orderType == SpotOrder.OrderType.MARKET) {
            tradePrice = requestedPrice + getRandom();
        } else if (improvementPriceCounter % 5 == 0) {
            tradePrice = requestedPrice - getRandom();
        }
        return tradePrice;
    }

    private double getRandom() {
        double min = 0.0001;
        double max = 0.03;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private double getRandomPourcent() {
        int min = 1;
        int max = 100;
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private List<Trade> createFilledTrade(SpotOrder order) {
        return Arrays.asList(new SpotTrade(System.currentTimeMillis(), order.getId(), order.getExternalId(), Trade.OrderStatus.FILLED, null,
                order.getAccount(), order.getSymbol(), order.getDirection(), order.getPrice(), getTradePrice(order.getPrice(), order.getOrderType()), order.getAmount(),
                order.getAmount(), 0, ZonedDateTime.now(ZoneId.of("GMT")), order.getValueDate()));
    }

    private List<Trade> createPartiallyFilledTrade(SpotOrder order) {
        double partialAmount = order.getAmount() - ((order.getAmount() * getRandomPourcent()) / 100);
        Trade tradePf = new SpotTrade(System.currentTimeMillis(), order.getId(), order.getExternalId(), Trade.OrderStatus.PARTIALLY_FILLED, null,
                order.getAccount(), order.getSymbol(), order.getDirection(), order.getPrice(), getTradePrice(order.getPrice(), order.getOrderType()), partialAmount, partialAmount, order.getAmount() - partialAmount,
                ZonedDateTime.now(ZoneId.of("GMT")), order.getValueDate());
        double cumQty = order.getAmount() - partialAmount;
        Trade cancelledTrade = new SpotTrade(System.currentTimeMillis(), order.getId(), order.getExternalId(), Trade.OrderStatus.CANCELLED, null,
                order.getAccount(), order.getSymbol(), order.getDirection(), order.getPrice(), 0, cumQty, cumQty, 0, null, null);
        return Arrays.asList(tradePf, cancelledTrade);
    }

    private List<Trade> createCancelledTrade(SpotOrder order) {
        return Arrays.asList(new SpotTrade(System.currentTimeMillis(), order.getId(), order.getExternalId(), Trade.OrderStatus.CANCELLED, null,
                order.getAccount(), order.getSymbol(), order.getDirection(), order.getPrice(), 0, order.getAmount(), 0, order.getAmount(), null, null));
    }
}
