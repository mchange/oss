package com.oss.block;

public class Block {
    private byte[] data;
    private BlockIndex index;

    public Block(byte[] data, BlockIndex index) {
        this.data = data;
        this.index = index;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public BlockIndex getIndex() {
        return index;
    }

    public void setIndex(BlockIndex index) {
        this.index = index;
    }
}
