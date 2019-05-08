package com.chenhe.server.worker;

import com.chenhe.entity.MessageData;
import com.chenhe.entity.MessageTypeEnum;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenhe
 * @date 2019-05-07 16:19
 * @desc
 */
public class ClientReadListener implements Runnable {

    Logger logger = LoggerFactory.getLogger(ClientReadListener.class);
    ConcurrentHashMap<String, SocketChannel> clientChannel;

    Selector selector;

    public ClientReadListener(Selector selector, ConcurrentHashMap<String, SocketChannel> clientChannel) {
        this.selector = selector;
        this.clientChannel = clientChannel;
    }

    @Override
    public void run() {
        logger.info("消息监听程序启动成功...");
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
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
                            int count = socketChannel.read(byteBuffer);
                            if (count == -1) {
                                socketChannel.close();
                                continue;
                            }

                            byteBuffer.flip();

                            String msg = decoder.decode(byteBuffer.asReadOnlyBuffer()).toString();
                            Gson gson = new Gson();

                            MessageData messageData = gson.fromJson(msg, MessageData.class);


                            if (MessageTypeEnum.REGISTER == messageData.getMessageType()) {
                                if (clientChannel.get(messageData.getFrom()) != null && clientChannel.get(messageData.getFrom()).isConnected()) {
                                    messageData = toMessageData("系统", null, "名称重复:" + messageData.getFrom(), MessageTypeEnum.GENERAL);
                                    socketChannel.write(ByteBuffer.wrap(gson.toJson(messageData).getBytes(Charset.defaultCharset())));
                                } else {
                                    MessageData newMessageData = toMessageData("系统", null, "欢迎:" + messageData.getFrom(), MessageTypeEnum.GENERAL);
                                    socketChannel.write(ByteBuffer.wrap(gson.toJson(newMessageData).getBytes(Charset.defaultCharset())));
                                    clientChannel.put(messageData.getFrom(), socketChannel);
                                }

                            }else if (MessageTypeEnum.GENERAL == messageData.getMessageType()) {
                                sendMsg(messageData, socketChannel);
                            }
                        }
                        selectionKeyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMsg(MessageData messageData, SocketChannel socketChannel) throws IOException {
        String to = messageData.getTo();


        SocketChannel toChannel = clientChannel.get(to);
        clientChannel.keySet().forEach(System.out::println);
        if (toChannel == null || !toChannel.isConnected()) {
            logger.warn("用户不存在,{}",to);
             messageData = toMessageData("系统",null,"用户不存在",MessageTypeEnum.GENERAL);
            socketChannel.write(ByteBuffer.wrap(new Gson().toJson(messageData).getBytes(Charset.defaultCharset())));
            return;
        }


        logger.info("消息转发给:{}",to);
        toChannel.write(ByteBuffer.wrap(new Gson().toJson(messageData).getBytes(Charset.defaultCharset())));
    }

    private MessageData toMessageData(String from, String to, String msg, MessageTypeEnum messageType) {
        MessageData messageData = new MessageData();
        messageData.setFrom(from);
        messageData.setTo(to);
        messageData.setMessage(msg);
        messageData.setMessageType(messageType);
        return messageData;
    }
}
