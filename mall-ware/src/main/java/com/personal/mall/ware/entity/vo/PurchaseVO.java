package com.personal.mall.ware.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseVO {
    private Long purchaseId;
    private List<PurchaseDetailVO> items;

    @Data
    public static class PurchaseDetailVO {
        private Long id;
        private Integer status;
        private String reason;
    }
}
