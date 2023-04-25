package com.oss.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileUtils {


    /**
     * 追加文件内容，已加锁
     * @param file
     * @param content
     * @throws IOException
     */
    public static void doAppend(File file, byte[] content) throws IOException {
        FileChannel channel = FileChannel.open(file.toPath(),StandardOpenOption.APPEND);
        FileLock lock = channel.lock();
        channel.write(ByteBuffer.wrap(content));
        lock.release();
        channel.close();
//        Files.write(file.toPath(), content, StandardOpenOption.APPEND);
    }

    /**
     * 获取文件扩展名
     * @param fileName  文件名称
     * @return
     */
    public static String getExtension(String fileName){
        int index = fileName.lastIndexOf(".");
        String extension = "";
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return extension;
    }
}
