/**
 * Juzifenqi.com Inc.
 * Copyright (c) 2019-2029 All Rights Reserved.
 */
package com.jaclon.learning.tm.transactional;

import com.jaclon.learning.tm.util.Task;
import lombok.Data;

/**
 * @Classname DistributeConnection
 * @Description TODO
 *
 * @author jaclon
 * @date 2020/1/17
 */
@Data
public class DistributeTransaction {

    private String groupId;
    private String transactionId;
    private TransactionType transactionType;
    private Task task = new Task();

    public DistributeTransaction(String groupId,String transactionId){
        this.groupId = groupId;
        this.transactionId = transactionId;
    }
}
