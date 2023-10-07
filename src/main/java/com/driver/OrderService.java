package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepo;
    public void addOrder(Order order) {
        orderRepo.addOrder(order);
    }

    public void addPartner(String partnerId) {
        orderRepo.addPartner(partnerId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepo.getPartnerById(partnerId);
    }

    public void addPartnerOrderPair(String orderId, String partnerId) {
        orderRepo.addPartnerOrderPair(orderId , partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderRepo.getOrderById(orderId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return orderRepo.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepo.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return orderRepo.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        return orderRepo.getCountOfUnassignedOrders();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        return orderRepo.getOrdersLeftAfterGivenTimeByPartnerId(time , partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return orderRepo.getLastDeliveryTimeByPartnerId(partnerId);
    }

    public void deletePartnerById(String partnerId) {
        orderRepo.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepo.deleteOrderById(orderId);
    }
}
