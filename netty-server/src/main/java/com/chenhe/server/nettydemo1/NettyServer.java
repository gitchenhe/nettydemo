package com.chenhe.server.nettydemo1;

import com.chenhe.netty.codec.MessageDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author chenhe
 * @date 2019-05-08 14:08
 * @desc 协议结构
 * <p>
 * +----------+---------+--------+----------------+
 * | protocol | version | length | Actual Content |
 * |    2     |    2    |   4    | "HELLO, WORLD" |
 * +----------+---------+--------+----------------+
 */
public class NettyServer {

    public static void start(int port) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
                                .addLast(new MessageDecoder());
                    }
                }).bind(port).channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        start(1234);
/*        int x = 10000;
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeChar(1024);
        System.out.println(byteBuf.readChar());*/
    }
}
