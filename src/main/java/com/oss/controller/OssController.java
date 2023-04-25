package com.oss.controller;


import com.oss.block.Block;
import com.oss.block.BlockManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@Slf4j
@RestController
public class OssController {

    @Autowired
    private BlockManager blockManager;

    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(name = "uuid", required = true) String uuid) throws IOException {
        long start = System.currentTimeMillis();
        Block block = blockManager.read(uuid);
        long end = System.currentTimeMillis();
        log.info("获取文件耗时：{} 秒", ((end-start)/1000));
        String fileName = block.getIndex().getName();
        byte[] data = block.getData();
        //get the mimetype
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            //unknown mimetype so set the mimetype to application/octet-stream
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);

        /**
         * In a regular HTTP response, the Content-Disposition response header is a
         * header indicating if the content is expected to be displayed inline in the
         * browser, that is, as a Web page or as part of a Web page, or as an
         * attachment, that is downloaded and saved locally.
         *
         */

        /**
         * Here we have mentioned it to show inline
         */
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));

        //Here we have mentioned it to show as attachment
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

        response.setContentLength((int) data.length);

        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));

        FileCopyUtils.copy(inputStream, response.getOutputStream());

    }
}

