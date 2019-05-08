package com.chenhe.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AppTest {

    Logger logger = LoggerFactory.getLogger(AppTest.class);

    Selector serverSelector = null;
    Selector clientSelector = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        AppTest appTest = new AppTest();
        appTest.test1();
    }


    public void test1() throws InterruptedException, IOException {
        serverSelector = Selector.open();
        clientSelector = Selector.open();
        Runnable runnable = () -> {
            try {
                NioServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable runnable2 = () -> {
            try {
                NioClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable2);

        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t2.start();

        t1.join();
        t2.join();

    }

    public void test2() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1025);
        byteBuffer.put("你好我是陈贺".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        socketChannel.close();
    }

    public void NioServer() throws IOException {


        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
        logger.info("服务启动 端口:{}", 9999);
        while (true) {
            try {
                //检测是否有新的连接
                if (serverSelector.select(1) > 0) {
                    logger.info("新的连接...");
                    Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        try {
                            logger.info("---------");
                            SelectionKey key = keyIterator.next();
                            if (key.isAcceptable()) {
                                logger.info("注册 clientChannel 到 clientSelector");
                                SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                clientChannel.configureBlocking(false);
                                clientChannel.register(clientSelector, SelectionKey.OP_READ);
                            }
                            logger.info("===========");
                        } finally {
                            keyIterator.remove();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void NioClient() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        while (true) {
            if (clientSelector.select(1000) > 0) {
                Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    try {
                        SelectionKey key = selectionKeyIterator.next();
                        if (key.isReadable()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                            SocketChannel clientChannel = ((SocketChannel) key.channel());
                            clientChannel.read(byteBuffer);
                            byteBuffer.flip();
                            logger.info("收到消息:{}", decoder.decode(byteBuffer));
                            byteBuffer.flip();
                        }
                    } finally {
                        selectionKeyIterator.remove();
                    }
                }
            }
        }
    }
}
