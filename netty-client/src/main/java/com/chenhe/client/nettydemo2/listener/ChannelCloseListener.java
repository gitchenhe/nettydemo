package com.chenhe.client.nettydemo2.listener;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author chenhe
 * @date 2019-06-03 16:59
 * @desc
 */
public class ChannelCloseListener implements GenericFutureListener {
    Logger logger = LoggerFactory.getLogger(ChannelCloseListener.class);


    Bootstrap bootstrap;
    ChannelFuture channelFuture;
    public ChannelCloseListener(Bootstrap bootstrap, ChannelFuture channelFuture) {
        this.bootstrap = bootstrap;
        this.channelFuture = channelFuture;
    }

    @Override
    public void operationComplete(Future future) throws Exception {

        boolean isNotConnect = true;
        while (isNotConnect){
            logger.info("客户端[" + channelFuture.channel().localAddress().toString() + "]断开重连...");
            try{
                ChannelFuture channelFuture1 =  bootstrap.connect().sync();
                if(channelFuture1.channel().isActive()){
                    isNotConnect=false;
                    channelFuture1.channel().closeFuture().addListener(new ChannelCloseListener(bootstrap,channelFuture));
                    logger.info("客户端[" + channelFuture.channel().localAddress().toString() + "]连接成功...");
                }
            }catch (Exception e){
                isNotConnect = true;
            }
            TimeUnit.SECONDS.sleep(3);
        }
    }
}
