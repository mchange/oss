package com.oss.block;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.common.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Block;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 块索引管理
 */
@Component
@Slf4j
public class BlockIndexManager {

    @Autowired
    private BlockIndexWriter indexWriter;
    @Autowired
    private BlockTools blockTools;

    @Autowired
    private OssConfig ossConfig;

    /**
     * 级缓存，对应1级缓存的索引
     */
    private static final Map<Integer, Map<String, BlockIndex>> cache = new ConcurrentHashMap<>();

    /**
     * 重新加载索引
     */
    public void reloadAll(){
        long start = System.currentTimeMillis();
        for(int i=0; i<ossConfig.getTotalNumber(); i++){
            reload(i);
        }
        long end = System.currentTimeMillis();
        log.info("加载索引文件耗时 {} 毫秒", (end-start));
    }

    /**
     * 按块编号加载
     * @param blockNumber 块编号
     */
    public void reload(int blockNumber){
        Map<String, BlockIndex> map = new HashMap<>();
        File indexFile = new File(blockTools.getAbsoluteIndexFilePath(blockNumber));
        if(!indexFile.exists()){
            return;
        }
        try {
            Files.readAllLines(indexFile.toPath(), Charset.defaultCharset()).forEach(s -> {
                // 解析json
                try {
                    BlockIndex index =  new ObjectMapper().readValue(s, BlockIndex.class);
                    map.put(index.getUuid(), index);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            cache.put(blockNumber, map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加索引
     * @param index
     */
    private void addIndex(BlockIndex index){
        int num = blockTools.getTargetFileNum(index.getUuid());
        if(cache.containsKey(num)){
            cache.get(num).put(index.getUuid(), index);
        }else{
            cache.put(num, new HashMap<String, BlockIndex>());
        }
    }


    /**
     * 写入索引
     * @param index
     * @return
     */
    public boolean write(BlockIndex index){
        try {
            indexWriter.appendIndex(index);
            addIndex(index);
            return true;
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据请求文件的uuid查询对应的索引
     * @param uuid
     * @return
     */
    public BlockIndex getBlockIndex(String uuid){
        return cache.get(blockTools.getTargetFileNum(uuid)).get(uuid);
    }

}
