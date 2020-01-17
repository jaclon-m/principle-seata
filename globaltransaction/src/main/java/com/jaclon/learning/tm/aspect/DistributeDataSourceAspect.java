/**
 * Juzifenqi.com Inc.
 * Copyright (c) 2019-2029 All Rights Reserved.
 */
package com.jaclon.learning.tm.aspect;

import com.jaclon.learning.tm.connection.DistributeConnection;
import com.jaclon.learning.tm.transactional.DistributeTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @Classname SeataDataSourceAspect
 * @Description TODO
 *
 * @author jaclon
 * @date 2020/1/17
 */
@Aspect
@Component
public class DistributeDataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection getConnection(ProceedingJoinPoint point) throws Throwable {
        if(DistributeTransactionManager.getCurrent() != null){
            return new DistributeConnection((Connection)point.proceed(),DistributeTransactionManager.getCurrent());
        }else {
            return (Connection) point.proceed();
        }

    }
}
