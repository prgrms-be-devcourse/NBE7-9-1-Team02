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
    private Long price;   // 팝업에 표시할 가격
}
