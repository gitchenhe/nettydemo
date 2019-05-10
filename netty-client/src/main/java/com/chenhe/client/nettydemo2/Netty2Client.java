package com.chenhe.client.nettydemo2;

import com.chenhe.entity.MessageData;
import com.chenhe.entity.MessageTypeEnum;
import com.chenhe.netty2demo.codec.DefaultMessageToMessageEncoder;
import com.chenhe.netty2demo.codec.ObjectToMessageEncoder;
import com.chenhe.netty2demo.entity.ConstantValue;
import com.chenhe.netty2demo.entity.Message;
import com.chenhe.util.HessianSerializerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author chenhe
 * @date 2019-05-10 11:40
 * @desc
 */
public class Netty2Client implements Serializable {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("localhost", 1234))
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("init......");
                        ch.pipeline()
                                .addLast(new DefaultMessageToMessageEncoder())
                                .addLast(new ObjectToMessageEncoder());
                                //.addLast(new DefaultHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect();
        channelFuture.sync(); // 异步连接服务器

        Channel channel = channelFuture.channel();

        Scanner scanner = new Scanner(System.in);
        while (true){
            String text = scanner.nextLine();
            if (text == null || "".equals(text)){
                continue;
            }
            byte[] bytes = HessianSerializerUtil.serialize(text);
            Message message = new Message(ConstantValue.HEAD_TAG,1,ConstantValue.VERSION,bytes.length,bytes);
            MessageData messageData = new MessageData();
            messageData.setFrom("陈贺");
            messageData.setTo("张三");
            messageData.setMessage("测试消息");
            messageData.setMessageType(MessageTypeEnum.GENERAL);

            channel.writeAndFlush(new Netty2Client());
        }
    }

   /* static class DefaultHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String msg = "你好";
            byte[] bytes = msg.getBytes(Charset.defaultCharset());
            Message message = new Message(ConstantValue.HEAD_TAG,1,ConstantValue.VERSION,bytes.length,bytes);

            ctx.writeAndFlush(message);

            System.out.println("发送消息");
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        }
    }*/
}
