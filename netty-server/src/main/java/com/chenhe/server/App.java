package com.chenhe.server;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        NettyServer.start(1234);

        new Scanner(System.in).nextLine();
    }
}
