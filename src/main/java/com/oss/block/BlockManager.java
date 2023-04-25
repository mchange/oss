package com.oss.block;

import com.oss.common.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 管理器
 */
@Slf4j
@Component
public class BlockManager {
    @Autowired
    private BlockWriter blockWriter;
    @Autowired
    private BlockReader blockReader;
    @Autowired
    private BlockIndexManager blockIndexManager;
    @Autowired
    private BlockTools blockTools;


    /**
     * 写入小文件
     * @param small 小文件
     * @return
     */
    public boolean write(File small){
        boolean result = false;
        try {
            result = blockIndexManager.write(blockWriter.appendBlock(blockTools.getBlockFile(small), small));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 读取小文件
     * @param uuid 小文件uuid
     * @return
     */
    public Block read(String uuid) throws IOException {

        // 1. 根据uuid找到对应的索引
        BlockIndex index = blockIndexManager.getBlockIndex(uuid);

        // 2. 根据索引读取文件内容
        byte[] data = blockReader.readBlock(blockTools.getBlockFile(uuid), index);

        return new Block(data, index);
    }




}
