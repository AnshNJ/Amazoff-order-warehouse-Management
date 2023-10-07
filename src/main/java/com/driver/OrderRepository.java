
package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;
@Repository
public class OrderRepository {

    //database//Hashmap
    HashMap<String,Order> orderMap;
    HashMap<String,DeliveryPartner> partnerMap;
    HashMap<String, List<String>> pairMap;
    HashSet<String> unassignedOrders;

    public OrderRepository() {
        orderMap=new HashMap<>();
        partnerMap=new HashMap<>();
        pairMap=new HashMap<>();
        unassignedOrders=new HashSet<>();

    }

    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
        unassignedOrders.add(order.getId());
    }

    public void addPartner(String partnerId) {
        DeliveryPartner d1=new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,d1);
    }


    public void addOrderPartnerPair(String orderId, String partnerId) {
        List<String> list=pairMap.getOrDefault(partnerId,new ArrayList<>());
        list.add(orderId);
        pairMap.put(partnerId,list);
        partnerMap.get(partnerId).setNumberOfOrders(partnerMap.get(partnerId).getNumberOfOrders()+1);

        unassignedOrders.remove(orderId);
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }


    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
//        return partnerMap.get(partnerId).getNumberOfOrders();
        return pairMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> list=pairMap.getOrDefault(partnerId,new ArrayList<>());
        return list;
    }

    public List<String> getAllOrders() {
        List<String> list=new ArrayList<>();
        for(String s: orderMap.keySet()){
            list.add(s);
        }
        return list;
    }

    public Integer getCountOfUnassignedOrders() {
        return unassignedOrders.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer count=0;
        //converting given string time to integer
        String arr[]=time.split(":"); //12:45
        int hr=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);

        int total=(hr*60+min);

        List<String> list=pairMap.getOrDefault(partnerId,new ArrayList<>());
        if(list.size()==0)return 0; //no order assigned to partnerId

        for(String s: list){
            Order currentOrder=orderMap.get(s);
            if(currentOrder.getDeliveryTime()>total){
                count++;
            }
        }

        return count;

    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        //return in HH:MM format
        String str="00:00";
        int max=0;

        List<String>list=pairMap.getOrDefault(partnerId,new ArrayList<>());
        if(list.size()==0)return str;
        for(String s: list){
            Order currentOrder=orderMap.get(s);
            max=Math.max(max,currentOrder.getDeliveryTime());
        }
        //convert int to string (140-> 02:20)
        int hr=max/60;
        int min=max%60;

        if(hr<10){
            str="0"+hr+":";
        }else{
            str=hr+":";
        }

        if(min<10){
            str+="0"+min;
        }
        else{
            str+=min;
        }
        return str;


    }

    public void deletePartnerById(String partnerId) {
        if(!pairMap.isEmpty()){
            unassignedOrders.addAll(pairMap.get(partnerId));
        }

        //removing form partnerMap
        partnerMap.remove(partnerId);


        //remove form the pairMap
        pairMap.remove(partnerId);

    }

    public void deleteOrderById(String orderId) {
        //Delete an order and the corresponding partner should be unassigned
        if(orderMap.containsKey(orderId)){
            if(unassignedOrders.contains(orderId)){
                unassignedOrders.remove(orderId);
            }
            else{

                for(String str : pairMap.keySet()){
                    List<String> list=pairMap.get(str);
                    if(list.contains(orderId)){
                        list.remove(orderId);
                    }
                }
            }
        }


    }
}