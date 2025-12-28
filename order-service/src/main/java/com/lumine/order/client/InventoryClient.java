package com.lumine.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//must match the name in Eureka
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/{sku-code}")
    boolean checkStock(@PathVariable("sku-code") String skuCode);

}
