package com.chenhe.netty.codec;

import com.chenhe.util.HessianSerializerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

/**
 * @author chenhe
 * @date 2019-05-09 10:23
 * @desc
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    Charset charset = Charset.forName("UTF-8");
    //CharsetDecoder decoder = charset.newDecoder();

    Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try{
            logger.info("准备反序列化");
            char protocol = msg.readChar();
            char version = msg.readChar();
            int length = msg.readInt();
            byte[] bytes = new byte[length];
           // msg.readBytes(bytes, 0);
            msg.readBytes(bytes,0,msg.readableBytes());

            String content = //new String(bytes);
                     HessianSerializerUtil.deserialize(bytes);
            logger.info("消息协议:[{}],版本:[{}],长度:[{}],内容:[{}]", (int)protocol, (int)version,length, content);

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes("TCP".getBytes());
        byteBuf.writeBytes("1.0".getBytes());
        String msg = "hello world 1231231231231231231231231231";
        byteBuf.writeInt(msg.getBytes().length);
        byteBuf.writeBytes(msg.getBytes());


        String str = byteBuf.readBytes(3).toString(Charset.defaultCharset());
        System.out.println(str);
        str = byteBuf.readBytes(3).toString(Charset.defaultCharset());
        System.out.println(str);
        int length = byteBuf.readInt();
        System.out.println(length);
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);

        // msg = byteBuf.readBytes(length).toString(Charset.defaultCharset());
        // System.out.println(msg);
        System.out.println(new String(bytes));

        new Scanner(System.in).nextLine();
    }
}
