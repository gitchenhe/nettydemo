package com.chenhe.server.niodemo1.worker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenhe
 * @date 2019-05-07 16:17
 * @desc
 */
public class ClientAcceptListener implements Runnable {
    Logger logger = LoggerFactory.getLogger(ClientAcceptListener.class);

    Selector serverSelector, clientSelector;


    public ClientAcceptListener(Selector serverSelector, Selector clientSelector) {
        this.serverSelector = serverSelector;
        this.clientSelector = clientSelector;
    }

    @Override
    public void run() {
        logger.info("客户端连接监听程序启动成功...");
        while (true) {
            try {
                if (serverSelector.select(500) > 0) {
                    Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                    logger.info("新客户端连接:{}", selectionKeys.size());
                    Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();

                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(clientSelector, SelectionKey.OP_READ);
                            selectionKeyIterator.remove();
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("等待客户端连接异常", e);
            }
        }
    }
}
