package com.chenhe.client.nettydemo2.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author chenhe
 * @date 2019-05-13 17:37
 * @desc
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    ChannelHandlerContext channelHandlerContext;
    ChannelPromise promise;
    Object result;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channelHandlerContext = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg != null){
            result = msg;
            promise.setSuccess();
        }else {
            //@ todo
            ctx.fireChannelRead(msg);
        }
    }

    public synchronized ChannelPromise sendMessage(Object message){
        while (channelHandlerContext == null){
            try{
                TimeUnit.MILLISECONDS.sleep(1);
            }catch (InterruptedException e){
                logger.error("等待ChannelHandlerContext实例化过程中出错",e);
            }
        }

        promise = channelHandlerContext.newPromise();
        channelHandlerContext.writeAndFlush(message);
        return promise;
    }

    public Object getResult() {
        return result;
    }
}
