package com.chenhe.entity;

import java.io.Serializable;

/**
 * @author chenhe
 * @date 2019-05-09 13:19
 * @desc
 */
public class MessageBean implements Serializable {
    private String type;
    private String version;
    private int length;
    private Object content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
