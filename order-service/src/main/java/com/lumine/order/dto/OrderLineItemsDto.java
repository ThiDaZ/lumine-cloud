package com.lumine.order.dto;

import java.math.BigDecimal;

public record OrderLineItemsDto(Long id , String skuCode, BigDecimal price, Integer quantity) {
}
