package com.chenhe.server;

import com.chenhe.server.worker2.NettyServer2;

import java.util.Scanner;

/**
 * @author chenhe
 * @date 2019-05-08 11:13
 * @desc
 */
public class ChatRoomServer {

    public static void main(String[] args) {
        NettyServer2.start(1234);

        new Scanner(System.in).nextLine();
    }
}
