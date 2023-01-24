package com.example.orderservice.controller;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repo.OrderRepository;
import com.example.orderservice.service.OrderSSEService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.codec.ServerSentEvent;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
@CrossOrigin("*")
@Log4j2
public class OrderController {

    private final OrderRepository orderRepository ;

    private final OrderSSEService orderSSEService;

    public OrderController(OrderRepository orderRepository, OrderSSEService orderSSEService) {
		super();
		this.orderRepository = orderRepository;
		this.orderSSEService = orderSSEService;
	}

	@GetMapping(value = "/latest")
    public Flux<ServerSentEvent<Map<Integer,List<Order>>>> getOrders() {
        return this.orderSSEService.getOrdersGroupByShipPin();
    }

    @PostMapping()
    public Order createOrder(@RequestBody Order order) {
        order = orderRepository.save(order);
      //  log.info("Order saved successfully order id : {}",order.getOrderId());
        return order;
    }
}
