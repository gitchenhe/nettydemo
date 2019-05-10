package com.chenhe.client.niodemo1;

import com.chenhe.entity.MessageData;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenhe
 * @date 2019-05-07 17:36
 * @desc 负责接收消息
 */
public class MessageReadListener implements Runnable {

    SocketChannel socketChannel;
    Selector selector;

    public MessageReadListener(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        try {
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        Gson gson = new Gson();
        while (true) {
            try {
                if (selector.select(500) > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();
                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                            socketChannel.read(byteBuffer);

                            byteBuffer.flip();

                            MessageData messageData = gson.fromJson(decoder.decode(byteBuffer.asReadOnlyBuffer()).toString(), MessageData.class);

                            System.out.println("[" + messageData.getFrom() + "] 发送的消息: " + messageData.getMessage());
                        }
                        selectionKeyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
