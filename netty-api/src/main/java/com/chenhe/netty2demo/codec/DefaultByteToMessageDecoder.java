package com.chenhe.netty2demo.codec;

import com.chenhe.netty2demo.entity.ConstantValue;
import com.chenhe.netty2demo.entity.Message;
import com.chenhe.util.HessianSerializerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author chenhe
 * @date 2019-05-10 10:01
 * @desc 将二进制消息转换成对象
 * +--------------------------------------------------+
 * | tag | commandCode |version |  length |  content  |
 * |  4  |       4     |   4    |    4    |    n      |
 * +--------------------------------------------------+
 */
public class DefaultByteToMessageDecoder extends ByteToMessageDecoder {

    private final int MIN_LENGTH = 4 + 4 + 4 + 4;
    private final int TAG = 1;
    private int maxLength;

    private static Logger logger = LoggerFactory.getLogger(DefaultByteToMessageDecoder.class);

    public DefaultByteToMessageDecoder(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (byteBuf.readableBytes() <= MIN_LENGTH) {
            //消息不完整
            logger.info("消息不完整,实际长度({}) < 最小长度 ({})",byteBuf.readableBytes(),MIN_LENGTH);
            return;
        }

        if (byteBuf.readableBytes() > maxLength) {
            logger.info("消息长度过长,实际长度({}) > 长度限制({})", byteBuf.readableBytes(), maxLength);
            byteBuf.skipBytes(byteBuf.readableBytes());
        }

        int tag;
        while (true) {
            //标记当前读取位置
            byteBuf.markReaderIndex();

            if ((tag = byteBuf.readInt()) == ConstantValue.HEAD_TAG) {
                //找到了消息的开头
                break;
            }
            //未找到消息头,需要重置标志位
            byteBuf.resetReaderIndex();
            byteBuf.readByte();

            if (byteBuf.readableBytes() <= MIN_LENGTH) {
                return;
            }
        }

        int commandCode = byteBuf.readInt();
        int version = byteBuf.readInt();
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        if (length > 0) {
            if (byteBuf.readableBytes() < length) {
                //包还没齐,
                byteBuf.resetReaderIndex();
                return;
            }
            byteBuf.readBytes(bytes);
        }


        Message message = new Message(tag,commandCode,version,length,bytes);

        logger.info("[收到消息] 二进制转义: commandCode({}),version:({})", message.getCommandCode(),message.getVersion());
        list.add(message);
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().freeMemory());
        ByteBuf byteBuf = Unpooled.buffer(381786527);
        System.out.println(Runtime.getRuntime().freeMemory());
        Unpooled.directBuffer();
        byteBuf.writeInt(2);
        byteBuf.writeInt(3);
        byteBuf.writeInt(4);

        byteBuf.markReaderIndex();

        logger.info("读int前,消息可读字节:{}", byteBuf.readableBytes());
        byteBuf.readInt();
        logger.info("读int后,消息可读字节:{}", byteBuf.readableBytes());
        byteBuf.resetReaderIndex();
        logger.info("重置读取位后,可读字节:{}", byteBuf.readableBytes());
    }
}
