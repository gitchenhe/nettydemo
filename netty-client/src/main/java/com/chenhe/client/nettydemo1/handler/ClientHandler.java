package com.chenhe.client.nettydemo1.handler;

import com.chenhe.entity.MessageBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author chenhe
 * @date 2019-05-09 15:08
 * @desc
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    Charset charset = Charset.forName("UTF-8");

    Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("连接成功...");
        MessageBean messageBean = new MessageBean();
        String message = "hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world 123";
        byte[] bytes = message.getBytes(charset);
        messageBean.setContent(message);
        messageBean.setLength(bytes.length);
        messageBean.setType("TCP");
        messageBean.setVersion("1.0");
        ctx.writeAndFlush(messageBean);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
