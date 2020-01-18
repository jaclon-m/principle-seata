package com.jaclon.learning.tm.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jaclon.learning.tm.transactional.DistributeTransaction;
import com.jaclon.learning.tm.transactional.DistributeTransactionManager;
import com.jaclon.learning.tm.transactional.TransactionType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受数据:" + msg.toString());
        JSONObject jsonObject = JSON.parseObject((String) msg);

        String groupId = jsonObject.getString("groupId");
        String command = jsonObject.getString("command");

        System.out.println("接收command:" + command);
        // 对事务进行操作
        DistributeTransaction distributeTransaction =
                DistributeTransactionManager.getDistributeTransaction(groupId);
        if(command.equals("rollback")){
            distributeTransaction.setTransactionType(TransactionType.rollback);
        }else if(command.equals("commit")){
            distributeTransaction.setTransactionType(TransactionType.commit);
        }
        distributeTransaction.getTask().signalTask();

    }

    public synchronized Object call(JSONObject data) throws Exception {
        context.writeAndFlush(data.toJSONString());
        return null;
    }
}
