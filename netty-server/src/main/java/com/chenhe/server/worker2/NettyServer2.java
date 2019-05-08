package com.chenhe.server.worker2;

import com.chenhe.server.worker.ClientAcceptListener;
import com.chenhe.server.worker.ClientReadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenhe
 * @date 2019-05-07 16:04
 * @desc 启动一个服务,使用telnet做客户端聊天室
 */
public class NettyServer2 {

    static Logger logger = LoggerFactory.getLogger(NettyServer2.class);

    static Selector serverSelector, clientSelector;

    static ConcurrentHashMap<String, SocketChannel> clientChannel = new ConcurrentHashMap<>();

    public static void start(int port) {

        ServerSocketChannel serverSocketChannel;
        try {

            serverSelector = Selector.open();
            clientSelector = Selector.open();

            //启动服务监听程序
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

            logger.info("服务启动成功,端口号:{}", port);

            //另起一个线程,监听所有客户端连接
            new Thread(new ClientAcceptListener2(serverSelector, clientSelector)).start();

            //在其一个线程,监听客户端读入的程序
            new Thread(new ClientReadListener2(clientSelector,clientChannel)).start();

        } catch (IOException e) {
            logger.error("服务启动异常", e);
            return;
        }
    }
}
