package com.tradair.mock.services;

import com.tradair.mock.model.order.Order;
import com.tradair.mock.model.trade.Trade;

import java.util.List;

public interface ITradingService {

    List<Trade> newOrder(Order order);
}
