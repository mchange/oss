package com.oss.block;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class BlockIndex {
    private String uuid;
    private long position;
    private long size;
    private String name;

    public BlockIndex() {
    }

    public BlockIndex(String uuid, long position, long size, String name) {
        this.uuid = uuid;
        this.position = position;
        this.size = size;
        this.name = name;
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

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this).trim() + "\n";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
