package com.back.domain.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {
    private Integer productId;
    private String origin;
    private String flavorAroma;
    private String feature;
    private Integer price;
}
