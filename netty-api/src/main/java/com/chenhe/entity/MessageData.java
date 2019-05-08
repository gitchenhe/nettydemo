package com.chenhe.entity;

/**
 * @author chenhe
 * @date 2019-05-07 17:15
 * @desc
 */
public class MessageData {
    private MessageTypeEnum messageType;
    private String from;
    private String to;
    private String message;

    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType) {
        this.messageType = messageType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
