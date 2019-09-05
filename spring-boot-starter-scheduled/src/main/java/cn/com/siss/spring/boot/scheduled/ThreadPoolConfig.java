package cn.com.siss.spring.boot.scheduled;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池的属性配置类
 *
 * Created by HJ on 2017/10/19.
 */
@Component
@ConfigurationProperties(prefix = "spring.thread.pool")
public class ThreadPoolConfig {
    /**
     * 线程池维护线程的最少数量
     */
    private int corePoolSize = 5;

    /**
     * 线程池维护线程的最大数量
     */
    private int maxPoolSize = 100;

    /**
     * 线程池所使用的缓冲队列容量
     */
    private int queueCapacity = 25;

    /**
     * 线程池维护线程所允许的空闲时间(秒)
     */
    private int keepAliveSeconds = 300;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
}
