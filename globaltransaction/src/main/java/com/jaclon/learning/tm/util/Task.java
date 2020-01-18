/**
 * Juzifenqi.com Inc.
 * Copyright (c) 2019-2029 All Rights Reserved.
 */
package com.jaclon.learning.tm.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname Task
 * @Description TODO
 *
 * @author jaclon
 * @date 2020/1/18
 */
public class Task {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void waitTask(){

        try{
            lock.lock();
            condition.await();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
           lock.unlock();
        }
    }

    public void signalTask(){
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
