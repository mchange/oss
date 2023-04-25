package com.oss.block;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件块读取
 */
@Component
public class BlockReader {

    /**
     * 根据索引读取文件
     * @param block     大文件块
     * @param index     文件块索引
     * @return          文件内容
     */
    public byte[] readBlock(File block, BlockIndex index){
        FileInputStream fis = null;
        FileChannel channel = null;
        try {
            fis = new FileInputStream(block);
            channel = fis.getChannel();
            long position = index.getPosition();
            long size = index.getSize();
            // 将文件的一部分映射到内存中，返回一个MappedByteBuffer对象
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, size);
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return bytes;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(null != channel && channel.isOpen()){
                try {
                    channel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if(null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
