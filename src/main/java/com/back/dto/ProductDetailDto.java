package com.back.dto;

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
    private Integer price;   // 팝업에 표시할 가격
}
