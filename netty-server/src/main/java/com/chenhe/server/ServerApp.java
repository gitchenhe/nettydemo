package com.chenhe.server;

import com.chenhe.server.worker2.NettyServer2;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class ServerApp
{
    public static void main( String[] args )
    {
        /**
         * 启动服务,消息传递使用json,使用java装的消息对象
         */
        NettyServer.start(1234);
        /**
         * 消息传递使用字符串,实现注册,广播,私聊
         */
        NettyServer2.start(1234);

        new Scanner(System.in).nextLine();
    }
}
