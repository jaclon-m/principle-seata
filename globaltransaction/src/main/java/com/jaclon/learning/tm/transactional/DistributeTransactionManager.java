/**
 * Juzifenqi.com Inc.
 * Copyright (c) 2019-2029 All Rights Reserved.
 */
package com.jaclon.learning.tm.transactional;

import com.alibaba.fastjson.JSONObject;
import com.jaclon.learning.tm.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Classname DistributeTransactionManager
 * @Description TODO
 *
 * @author jaclon
 * @date 2020/1/17
 */
@Component
public class DistributeTransactionManager {

    private static NettyClient nettyClient;
    private static ThreadLocal<DistributeTransaction> current = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<>();
    private static Map<String,DistributeTransaction> DIS_TRANSACTION_MAP = new HashMap<>();

    public static NettyClient getNettyClient() {
        return nettyClient;
    }
    @Autowired
    public static void setNettyClient(NettyClient nettyClient) {
        DistributeTransactionManager.nettyClient = nettyClient;
    }

    public static String createTransactionGroup(){
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",groupId);
        jsonObject.put("commond","create");
        nettyClient.send(jsonObject);
        System.out.println("创建事务组");
        currentGroupId.set(groupId);
        return  groupId;
    }

    public static DistributeTransaction createTransaction(String groupId){
        String transactionId = UUID.randomUUID().toString();
        DistributeTransaction transaction = new DistributeTransaction(groupId, transactionId);
        DIS_TRANSACTION_MAP.put(transactionId,transaction);
        current.set(transaction);
        addTransactionCount();
        System.out.println("创建事务");
        return transaction;
    }

    public static DistributeTransaction addTransaction(DistributeTransaction transaction,Boolean isEnd,TransactionType transactionType){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",transaction.getGroupId());
        jsonObject.put("transactionId",transaction.getTransactionId());
        jsonObject.put("transactionType",transactionType);
        jsonObject.put("command","add");
        jsonObject.put("isEnd",isEnd);
        jsonObject.put("transactionCount",DistributeTransactionManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("添加事务");
        return transaction;
    }

    public static DistributeTransaction getDistributeTransaction(String groupId){
        return DIS_TRANSACTION_MAP.get(groupId);
    }

    public static DistributeTransaction getCurrent(){
        return current.get();
    }

    public static String getCurrentGroupId(){
        return currentGroupId.get();
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionCount.get();
    }
    public static void setTransactionCount(int i){
        transactionCount.set(i);
    }

    public static Integer addTransactionCount() {
        int i = (transactionCount.get() == null ? 0 : transactionCount.get()) + 1;
        transactionCount.set(i);
        return i;
    }


}
