package com.lumine.product.dto;

import java.math.BigDecimal;

public record ProductResponse(String id , String name, String Description, BigDecimal price) {
}
