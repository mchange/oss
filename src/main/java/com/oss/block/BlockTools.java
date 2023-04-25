package com.oss.block;

import com.oss.common.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class BlockTools {

    @Autowired
    private OssConfig ossConfig;

    /**
     * 根据索引查找对于的块文件
     * @param index 索引
     * @return
     */
    public File getBlockFile(BlockIndex index) throws IOException {
        return getBlockFile(index.getUuid());
    }

    public File getBlockFile(File small) throws IOException {
        return getBlockFile(small.getAbsolutePath().replace(ossConfig.getIgnoreBlockStore(), ""));
    }

    public File getBlockFile(String uuid) throws IOException {
        int num = getTargetFileNum(uuid);
        File blockFile = new File(String.format("%s/%d.oss", ossConfig.getBlockStore(), num));
        log.debug("block file path:{}", blockFile.getAbsolutePath());
        if(!blockFile.exists()){
            blockFile.createNewFile();
        }
        return blockFile;
    }


    /**
     * 根据文件全路径查找对应的块文件
     * @param uuid 文件唯一标识符，例如：文件全路径
     * @return
     */
    public File getIndexFile(String uuid) throws IOException {
        int num = getTargetFileNum(uuid);
        File indexFile = new File(String.format("%s/%d.index", ossConfig.getIndexStore(), num));
        log.debug("index file path:{}", indexFile.getAbsolutePath());
        if(!indexFile.exists()){
            indexFile.createNewFile();
        }
        return indexFile;
    }

    /**
     * 根据要读取小文件的uuid计算对应的索引和块文件的文件编号
     * @param uuid
     * @return
     */
    public int getTargetFileNum(String uuid){
        return Math.abs(uuid.hashCode()) % ossConfig.getTotalNumber();
    }

    /**
     * 根据文件编号获取索引文件全路径
     * @param num
     * @return
     */
    public String getAbsoluteIndexFilePath(int num){
        return String.format("%s/%d.index", ossConfig.getIndexStore(), num);
    }
}
