package com.lumine.order.service;

import com.lumine.order.client.InventoryClient;
import com.lumine.order.dto.OrderLineItemsDto;
import com.lumine.order.dto.OrderRequest;
import com.lumine.order.model.Order;
import com.lumine.order.model.OrderLineItems;
import com.lumine.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    /**
     * Places order if all products are in stock
     */
    public void placeOrder(OrderRequest orderRequest){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        boolean allProductsInStock = order.getOrderLineItemsList().stream()
                .allMatch(item -> inventoryClient.checkStock(item.getSkuCode()));

        if(allProductsInStock){
            orderRepository.save(order);
            log.info("Order placed Successfully");
        }else{
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.price());
        orderLineItems.setQuantity(orderLineItemsDto.quantity());
        orderLineItems.setSkuCode(orderLineItemsDto.skuCode());
        return orderLineItems;
    }

}
