package cn.com.siss.spring.boot.scheduled;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2016/12/30.
 */
@Configuration
@EnableScheduling
public class ScheduledConfig implements SchedulingConfigurer {

    @Value("${spring.thread.scheduledThreadPoolSize:4}")
    private Integer scheduledThreadPoolSize;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize);
    }
}
