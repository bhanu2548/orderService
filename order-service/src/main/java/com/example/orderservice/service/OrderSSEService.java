package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repo.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.*;
import java.util.stream.*;

/**
 * Created by bhanu on 14/01/23
 */
@Service
@AllArgsConstructor
public class OrderSSEService {
    private OrderRepository orderRepository;

    public Flux<ServerSentEvent<Map<Integer,List<Order>>>> getOrdersGroupByShipPin() {
        return Flux.interval(Duration.ofSeconds(1))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> ServerSentEvent.<Map<Integer,List<Order>>>builder()
                        .event("order-update")
                        .data(this.orderRepository.findAll()
                                .stream().collect(Collectors.groupingBy(Order::getShipToPin))
                                .entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2)->e2, LinkedHashMap::new)))
                        .build());
    }
}
