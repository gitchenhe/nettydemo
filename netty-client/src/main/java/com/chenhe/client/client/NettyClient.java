package com.chenhe.client.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author chenhe
 * @date 2019-05-07 15:32
 * @desc
 */
public final class NettyClient {

    static Logger logger = LoggerFactory.getLogger(NettyClient.class);
    static SocketChannel socketChannel;
    public static SocketChannel init(String host,int ip){
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host,ip));
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            logger.error("服务器连接异常",e);
        }
        return socketChannel;
    }

    public static void send(String msg){
        if (socketChannel == null){
            logger.error("与服务器断开连接");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes(Charset.defaultCharset()));
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            logger.error("消息发送失败",e);
        }

    }

    public static void close(){
        try {
            socketChannel.close();
        } catch (IOException e) {
            logger.error("服务器断开连接异常", e);
        }
    }
}
