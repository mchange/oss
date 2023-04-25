package com.oss;

import com.oss.block.*;
import com.oss.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
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
		Arrays.stream(dirs.listFiles()).forEach(f -> {
			if(f.isFile() && !f.isHidden()){
//				try {
//					indexWriter.appendIndex(blockWriter.appendBlock(blockManager.getBlockFile(f), f));
					blockManager.write(f);
//				} catch (IOException e) {
//					throw new RuntimeException(e);
//				}
			}
		});

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

}
