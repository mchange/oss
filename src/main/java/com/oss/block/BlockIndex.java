package com.oss.block;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class BlockIndex {
    private String uuid;
    private long position;
    private long size;
    private String name;
    private String type;

    public BlockIndex() {
    }

    public BlockIndex(String uuid, long position, long size, String name, String type) {
        this.uuid = uuid;
        this.position = position;
        this.size = size;
        this.name = name;
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this).trim() + "\n";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
