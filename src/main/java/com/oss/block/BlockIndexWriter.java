package com.oss.block;

import com.oss.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 追加块索引文件
 */
@Component
public class BlockIndexWriter {
    @Autowired
    BlockTools blockTools;

    /**
     * 追加写入块索引文件
     * @param index
     * @return
     */
    public boolean appendIndex(BlockIndex index) {
        try {
            File file = blockTools.getIndexFile(index.getUuid());
            FileUtils.doAppend(file, index.toString().getBytes());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
