package com.oss;

import com.oss.block.*;
import com.oss.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

@SpringBootTest
@Slf4j
class OssApplicationTests {

	@Autowired
	BlockManager blockManager;
	@Autowired
	BlockIndexWriter indexWriter;

	@Autowired
	BlockWriter blockWriter;

	@Autowired
	BlockReader blockReader;

	@Autowired
	BlockTools blockTools;
	@Autowired
	BlockIndexManager blockIndexManager;

	@Test
	void contextLoads() {

	}

	@Test
	public void blockWriter() {
		File dirs = new File("/Users/admin/Documents/demo/oss/small/jpg");
		long start = System.currentTimeMillis();
		listDirAndFile(dirs.getAbsolutePath(), blockManager);
//		Arrays.stream(dirs.listFiles()).forEach(f -> {
//			if(f.isFile() && !f.isHidden()){
////				try {
////					indexWriter.appendIndex(blockWriter.appendBlock(blockManager.getBlockFile(f), f));
////					blockManager.write(f);
////				} catch (IOException e) {
////					throw new RuntimeException(e);
////				}
//			}
//		});

		long end = System.currentTimeMillis();
		System.out.println("耗时:" + ((end-start)/1000) + " s");
	}


	@Test
	public void readBlock() throws IOException {

		//{uuid='oss/small/jpg/IMG_1566.CR3', position=31979645, size=31363565, name='IMG_1566.CR3', type='CR3'}
//		BlockIndex blockIndex = new BlockIndex("oss/small/jpg/IMG_1566.CR3", 31979645, 31363565, "IMG_1566.CR3", "CR3");
//		byte[] contents = blockReader.readBlock(blockTools.getBlockFile(blockIndex), blockIndex);
		// 首先加载索引文件
		blockIndexManager.reloadAll();

		Block block = blockManager.read("oss/small/4.txt");
		BlockIndex blockIndex = block.getIndex();
		byte[] data = block.getData();

		File newFile = new File("/Users/admin/Documents/demo/oss/new/" + blockIndex.getName());
		if(!newFile.exists()){
			newFile.createNewFile();
		}
		FileUtils.doAppend(newFile, data);
	}


	public static void listDirAndFile(String basePath, BlockManager blockManager) {
		int fileNum = 0, folderNum = 0;
		File file = new File(basePath);
		LinkedList<File> list = new LinkedList<>();
		if (file.exists()) {
			if (null == file.listFiles()) {
				return;
			}
			list.addAll(Arrays.asList(file.listFiles()));
			while (!list.isEmpty()) {
				File current = list.removeFirst();
				File[] files = current.listFiles();
				if (null == files) {
					// 写入大文件
					if(!current.isHidden()) {
						fileNum++;
						blockManager.write(current);
					}
					continue;
				}
				log.info("开始合并文件夹：{}", current.getAbsolutePath());
				folderNum++;
				for (File f : files) {
					if (f.isDirectory()) {
						list.push(f); // 与广度优先遍历的唯一区别点：是往 List 末尾还是头部添加元素
					} else {
						fileNum++;
						// 写入大文件
						if(!f.isHidden()) {
							blockManager.write(f);
						}
					}
				}
			}
		} else {
			log.error("文件不存在!");
		}
		log.info("文件夹数量:" + folderNum + ",文件数量:" + fileNum);
	}
}
