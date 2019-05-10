package com.chenhe.client.nettydemo1;

import com.chenhe.client.nettydemo1.handler.ClientHandler;
import com.chenhe.netty.codec.MessgeEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author chenhe
 * @date 2019-05-09 14:32
 * @desc
 */
public class NettyClientApp {

    static Logger logger = LoggerFactory.getLogger(NettyClientApp.class);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("localhost", 1234))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                //.addLast(new MessgeEncoder2())
                                .addLast(new MessgeEncoder())
                                .addLast(new ClientHandler());
                    }
                });
        ChannelFuture cf = bootstrap.connect().sync(); // 异步连接服务器

        cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
        System.out.println("closed.."); // 关闭完成
    }
}
