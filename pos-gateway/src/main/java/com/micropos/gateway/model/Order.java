package com.micropos.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class Order implements Serializable {
    private List<Item> cart;
    private double orderTotalPrice;
    private String userID;
    private String deliveryState;
    private String deliveryID;
}
