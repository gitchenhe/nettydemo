package com.chenhe.server.nettydemo2.handler;

import com.chenhe.netty2demo.codec.DefaultByteToMessageDecoder;
import com.chenhe.netty2demo.codec.DefaultMessageToMessageEncoder;
import com.chenhe.netty2demo.codec.MessageToObjectDecoder;
import com.chenhe.netty2demo.codec.ObjectToMessageEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenhe
 * @date 2019-05-10 09:39
 * @desc 服务端解码编码流水线
 */
public class ServerChannelInitHandler extends ChannelInitializer {

    Logger logger = LoggerFactory.getLogger(ServerChannelInitHandler.class);

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder",new DefaultByteToMessageDecoder(2048));
        pipeline.addLast("resultHandler",new MessageToObjectDecoder());
        pipeline.addLast(new ServerResponseHandler());
        pipeline.addLast(new DefaultMessageToMessageEncoder());
        pipeline.addLast(new ObjectToMessageEncoder());

        logger.info("============= initChannel ===============");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("============= channelActive ===============");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("============= channelInactive ===============");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("============= channelRead ===============");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("============= channelReadComplete ===============");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("============= channelUnregistered ===============");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("============= channelWritabilityChanged ===============");
        super.channelWritabilityChanged(ctx);
    }
}
