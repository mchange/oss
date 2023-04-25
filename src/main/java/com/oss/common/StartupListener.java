package com.oss.common;

import com.oss.block.BlockIndexManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private BlockIndexManager blockIndexManager;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("开始初始化任务--------------->");
        blockIndexManager.reloadAll();
        log.info("完成初始化任务<---------------");
    }
}
