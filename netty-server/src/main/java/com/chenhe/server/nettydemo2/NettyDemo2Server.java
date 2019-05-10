package com.chenhe.server.nettydemo2;

import com.chenhe.server.nettydemo2.handler.ServerChannelInitHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author chenhe
 * @date 2019-05-10 09:28
 * @desc
 */
public class NettyDemo2Server {
    static int port = 1234;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup client = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, client);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ServerChannelInitHandler());
        serverBootstrap.bind(port).channel().closeFuture().sync();

        System.out.println("list on port " + port);
    }
}
