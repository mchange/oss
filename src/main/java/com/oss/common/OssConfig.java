package com.oss.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("oss.block")
public class OssConfig {

    private int totalNumber;
    private String blockStore;

    private String ignoreBlockStore;
    private String indexStore;

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getBlockStore() {
        return blockStore;
    }

    public void setBlockStore(String blockStore) {
        this.blockStore = blockStore;
    }

    public String getIndexStore() {
        return indexStore;
    }

    public void setIndexStore(String indexStore) {
        this.indexStore = indexStore;
    }

    public String getIgnoreBlockStore() {
        return ignoreBlockStore;
    }

    public void setIgnoreBlockStore(String ignoreBlockStore) {
        this.ignoreBlockStore = ignoreBlockStore;
    }
}
