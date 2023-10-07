package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private HashMap<String , Order> orderMap;
    private HashMap<String , DeliveryPartner> partnerMap;
    private HashMap<String , List<String>> partnerOrderMap;
    private HashSet<String> assignedOrder;

    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.partnerOrderMap = new HashMap<>();
        this.assignedOrder = new HashSet<>();
    }

    public void addOrder(Order order) {
        if(!orderMap.containsKey(order.getId())) orderMap.put(order.getId() , order);
    }

    public void addPartner(String partnerId) {
        if(!partnerMap.containsKey(partnerId)) partnerMap.put(partnerId , new DeliveryPartner(partnerId));
    }

    public DeliveryPartner getPartnerId(String partnerId) {
        return partnerMap.getOrDefault(partnerId , null);
    }

    public void addPartnerOrderPair(String orderId, String partnerId) {
        if(!partnerOrderMap.containsKey(partnerId)){
            //Del. partner had no order assigned
            List<String> newList = new ArrayList<>();
            newList.add(orderId);
            partnerOrderMap.put(partnerId , newList);
        } else {
            //Del. partner already had some orders assigned
            List<String> oldList = partnerOrderMap.get(partnerId);
            oldList.add(orderId);
            partnerOrderMap.put(partnerId , oldList);
        }
        //Update number of orders for partner and assigned orders
        assignedOrder.add(orderId);
        partnerMap.get(partnerId).setNumberOfOrders(partnerOrderMap.get(partnerId).size());

    }

    public Order getOrderById(String orderId) {
        return orderMap.getOrDefault(orderId, null);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerOrderMap.containsKey(partnerId) ? partnerOrderMap.get(partnerId).size() : 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> allOrders = new ArrayList<>();
        for(Order order : orderMap.values()){
            allOrders.add(order.getId());
        }
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderMap.size() - assignedOrder.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        //Convert String time to int time
        Integer count=0;
        //converting given string time to integer
        String arr[]=time.split(":"); //12:45
        int hr=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);

        int total=(hr*60+min);

        List<String> list=partnerOrderMap.getOrDefault(partnerId,new ArrayList<>());
        if(list.size()==0) return 0; //no order assigned to partnerId

        for(String orderId: list){
            Order currentOrder = orderMap.get(orderId);
            if(currentOrder.getDeliveryTime()>total){
                count++;
            }
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String time = "00:00";
        int max = -1;
        List<String> orders = partnerOrderMap.get(partnerId);
        if(orders.size() == 0) return time;
        //Finding order with the longest del. time
        for(String orderId : orders){
            if(orderMap.get(orderId).getDeliveryTime() > max){
                max = orderMap.get(orderId).getDeliveryTime();
            }
        }

        int hr = max / 60;
        int min = max % 60;

        if(hr<10){
            time = "0"+hr+":";
        }else{
            time = hr+":";
        }

        if(min<10){
            time += "0"+min;
        }
        else{
            time += min;
        }
        return time;

    }

    public void deletePartnerById(String partnerId) {
        List<String> assignedOrderList = partnerOrderMap.get(partnerId);
        partnerOrderMap.remove(partnerId);
        partnerMap.remove(partnerId);
        for(String orderId : assignedOrderList){
            if(assignedOrder.contains(orderId)) assignedOrder.remove(orderId);
        }
    }

    public void deleteOrderById(String orderId) {
        for(List<String> orders : partnerOrderMap.values()){
            if(orders.contains(orderId)) orders.remove(orderId);
        }
        orderMap.remove(orderId);
    }
}
