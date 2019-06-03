package com.chenhe.server.nettydemo2.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author chenhe
 * @date 2019-06-03 17:25
 * @desc 服务端响应客户端数据
 */
public class ServerResponseHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().writeAndFlush("response:" + msg);
    }
}
