/**
 * Juzifenqi.com Inc.
 * Copyright (c) 2019-2029 All Rights Reserved.
 */
package com.jaclon.learning.tm.aspect;

import com.jaclon.learning.tm.annotation.DistributeTransactional;
import com.jaclon.learning.tm.transactional.DistributeTransaction;
import com.jaclon.learning.tm.transactional.DistributeTransactionManager;
import com.jaclon.learning.tm.transactional.TransactionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Classname SeataTransactionAspect
 * @Description TODO
 *
 * @author jaclon
 * @date 2020/1/17
 */
@Aspect
@Component
public class DistributeTransactionAspect implements Ordered {

    @Around("@annotation(com.jaclon.learning.tm.annotation.DistributeTransactional)")
    public  void invoke(ProceedingJoinPoint point){

        //before
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        DistributeTransactional annotation = method.getAnnotation(DistributeTransactional.class);
        //获取groupId
        String groupId = "";
        if(annotation.isStart()){
            groupId = DistributeTransactionManager.createTransactionGroup();
        }else {
            groupId = DistributeTransactionManager.getCurrentGroupId();
        }
        //创建事务
        DistributeTransaction distributeTransaction = DistributeTransactionManager.createTransaction(groupId);

        try {
            point.proceed();
            //after
            DistributeTransactionManager.addTransaction(distributeTransaction,annotation.isEnd(), TransactionType.commit);
        }catch(Exception e){
            DistributeTransactionManager.addTransaction(distributeTransaction,annotation.isEnd(),TransactionType.rollback);
            e.printStackTrace();
        } catch (Throwable throwable) {
            DistributeTransactionManager.addTransaction(distributeTransaction,annotation.isEnd(),TransactionType.rollback);
            throwable.printStackTrace();
        }finally {
            //finally
        }

    }
    @Override
    public int getOrder() {
        //数量越大优先级越高
        return 10000;
    }
}
