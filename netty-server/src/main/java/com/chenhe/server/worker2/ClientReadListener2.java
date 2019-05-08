package com.chenhe.server.worker2;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author chenhe
 * @date 2019-05-07 16:19
 * @desc
 */
public class ClientReadListener2 implements Runnable {

    Logger logger = LoggerFactory.getLogger(ClientReadListener2.class);
    ConcurrentHashMap<String, SocketChannel> clientChannel;

    Selector selector;

    public ClientReadListener2(Selector selector, ConcurrentHashMap<String, SocketChannel> clientChannel) {
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
                            dealMsg(msg, socketChannel);

                        }
                        selectionKeyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dealMsg(String msg, SocketChannel socketChannel) throws IOException {
        String name = null;
        if (msg.startsWith("#")) {
            String[] id = msg.split("#");
            name = id[1].trim();
            if (clientChannel.containsKey(name)) {
                sendMsg("系统: id重复,请重新输入(格式:#id) \n", socketChannel);
                return;
            }

            clientChannel.put(name, socketChannel);
            sendMsg("系统: 欢迎加入聊天室(@id:私信.) \n", socketChannel);

            String newMsg = "系统: 欢迎:" + name + "进入聊天室 \n";
            clientChannel.forEach((key, value) -> {
                if (value != socketChannel) {
                    sendMsg(newMsg, value);
                }
            });
        } else if (msg.startsWith("@")) {
            try {
                String to = msg.split("@")[1];

                String[] messageAndTo = to.split(" ");
                to = messageAndTo[0];
                String newMsg = messageAndTo[1];
                to = to.trim();
                newMsg = newMsg.trim();
                AtomicReference<String> from = new AtomicReference<>();
                clientChannel.forEach((key, value) -> {
                    if (value == socketChannel) {
                        from.set(key);
                    }

                });


                if (from.get() == null || "".equals(from.get())) {
                    alertLogin(socketChannel);
                    return;
                }

                newMsg = String.format("[%s] 私信你: %s \n", from, newMsg);
                AtomicBoolean success = new AtomicBoolean(false);
                String finalNewMsg = newMsg;
                String finalTo = to;
                clientChannel.forEach((key, value) -> {
                    if (finalTo.equals(key)) {
                        success.set(true);
                        sendMsg(finalNewMsg, value);
                    }
                });
                if (!success.get()) {
                    newMsg = String.format("[%s] 不存在或已经离线 \n", from, msg);
                    sendMsg(newMsg, socketChannel);
                }
            } catch (Exception e) {
                logger.error("异常:", e);
            }
        } else {
            AtomicBoolean isRegister = new AtomicBoolean(false);
            AtomicReference<String> from = new AtomicReference<>();
            clientChannel.forEach((key, value) -> {
                if (value == socketChannel) {
                    from.set(key);
                    isRegister.set(true);
                }
            });
            msg = msg.trim();
            if (!isRegister.get()) {
                alertLogin(socketChannel);
            } else {
                String newMsg = "[" + from.get() + "] 发送消息: " + msg + "\n";
                clientChannel.forEach((key, value) -> {
                    if (value != socketChannel) {
                        sendMsg(newMsg, value);
                    }
                });
            }
        }
    }

    private void alertLogin(SocketChannel socketChannel) {
        String newMsg = "请先输入你的ID(格式: #id) \n";
        try {
            socketChannel.write(ByteBuffer.wrap(newMsg.getBytes(Charset.defaultCharset())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String msg, SocketChannel socketChannel) {
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(Charset.defaultCharset())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
