package com.chenhe.netty2demo.entity;

import com.chenhe.util.HessianSerializerUtil;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author chenhe
 * @date 2019-05-10 09:55
 * @desc 自定义消息结构
 */
public class Message implements Serializable {
    private int tag;
    private int commandCode;
    private int version;
    private int length;
    private byte[] content;

    public Message(int tag, int commandCode, int version, int length, byte[] content) {
        this.tag = tag;
        this.commandCode = commandCode;
        this.version = version;
        this.length = length;
        this.content = content;
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getCommandCode() {
        return commandCode;
    }

    public void setCommandCode(int commandCode) {
        this.commandCode = commandCode;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Message{");
        sb.append("tag=").append(tag);
        sb.append(", commandCode=").append(commandCode);
        sb.append(", version=").append(version);
        sb.append(", length=").append(length);
        sb.append(", content=").append((String)HessianSerializerUtil.deserialize(content));
        return sb.toString();
    }
}
