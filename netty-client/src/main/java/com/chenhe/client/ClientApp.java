package com.chenhe.client;

import com.chenhe.client.client.MessageReadListener;
import com.chenhe.client.client.NettyClient;
import com.chenhe.entity.MessageData;
import com.chenhe.entity.MessageTypeEnum;
import com.google.gson.Gson;

import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Hello world!
 */
public class ClientApp {
    public static void main(String[] args) {
        SocketChannel socketChannel = NettyClient.init("localhost", 1234);

        new Thread(new MessageReadListener(socketChannel)).start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你的id");
        String id = scanner.nextLine();
        MessageData messageData = new MessageData();
        messageData.setMessageType(MessageTypeEnum.REGISTER);
        messageData.setFrom(id);

        Gson gson = new Gson();
        NettyClient.send(gson.toJson(messageData));

        while (true) {
            System.out.println("请输入接受消息的人");
            String to = scanner.nextLine();
            System.out.println("请输入消息");
            String msg = scanner.nextLine();
            messageData = new MessageData();
            messageData.setFrom(id);
            messageData.setTo(to);
            messageData.setMessageType(MessageTypeEnum.GENERAL);
            messageData.setMessage(msg);
            NettyClient.send(gson.toJson(messageData));
        }
    }
}
