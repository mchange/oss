package com.oss.block;

import com.oss.common.OssConfig;
import com.oss.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 文件打包
 */
@Component
public class BlockWriter {

    @Autowired
    private OssConfig ossConfig;

    /**
     * 将小文件追加到大文件上
     * @param big   大文件
     * @param small 小文件
     * @return      文件索引
     */
    public BlockIndex appendBlock(File big, File small){
        return appendBlock(big, small, small.getAbsolutePath().replace(ossConfig.getIgnoreBlockStore(), ""));
    }

    /**
     * 将小文件追加到大文件上
     * @param big   大文件
     * @param small 小文件
     * @return      文件索引
     */
    public BlockIndex appendBlock(File big, File small, String uuid){
        BlockIndex index;
        try {
            byte[] smalls = Files.readAllBytes(small.toPath());
            long size = smalls.length;
            long start = big.length();
            FileUtils.doAppend(big, smalls);
            index = new BlockIndex(uuid, start, size, small.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return index;
    }


}
