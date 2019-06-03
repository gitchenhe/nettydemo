package com.chenhe.client.nettydemo2;

import com.chenhe.client.nettydemo2.handler.ClientHandler;
import com.chenhe.client.nettydemo2.listener.ChannelCloseListener;
import com.chenhe.netty2demo.codec.DefaultByteToMessageDecoder;
import com.chenhe.netty2demo.codec.DefaultMessageToMessageEncoder;
import com.chenhe.netty2demo.codec.MessageToObjectDecoder;
import com.chenhe.netty2demo.codec.ObjectToMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author chenhe
 * @date 2019-05-10 11:40
 * @desc
 */
public class Netty2Client implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(Netty2Client.class);

    private static Channel clientChannel;

    private static ClientHandler clientHandler = new ClientHandler();


    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("localhost", 1234))
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        logger.info("初始连接......");
                        ch.pipeline()
                                .addLast(new DefaultMessageToMessageEncoder())
                                .addLast(new ObjectToMessageEncoder())

                                .addLast("decoder", new DefaultByteToMessageDecoder(2048))
                                .addLast("resultHandler", new MessageToObjectDecoder())
                                .addLast(clientHandler);
                    }
                });

        //发起同步连接操作
        ChannelFuture channelFuture = bootstrap.connect();


        //发起连接事件
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                logger.info("客户端[" + channelFuture.channel().localAddress().toString() + "]已连接...");
                clientChannel = future.channel();
            } else {
                if (channelFuture.channel() != null && channelFuture.channel().localAddress() != null) {
                    logger.info("客户端[" + channelFuture.channel().localAddress().toString() + "]断开连接,重新连接...");
                    if (clientChannel != null) {
                        channelFuture.channel().close();
                    }
                } else {
                    logger.info("客户端断开连接,重新连接....");
                }

                bootstrap.connect();
            }
        });

        //注册关闭事件
        channelFuture.channel().closeFuture().addListener(new ChannelCloseListener(bootstrap, channelFuture));

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入消息");
        while (true){
            String text = scanner.nextLine();
            if (text == null || "".equals(text)){
                continue;
            }

            Promise promise = clientHandler.sendMessage(text);
            promise.await(3, TimeUnit.SECONDS);
            //等待服务返回
            System.out.println(clientHandler.getResult());

        }
    }

}
