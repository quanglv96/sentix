package sansan.sentix.Config;

import io.sentry.spring.SentryTaskDecorator;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Log4j2
public class AsyncConfig implements AsyncConfigurer {

    @Override
    @Bean("defaultAsyncExecutor")
    public Executor getAsyncExecutor() {
        return createExecutor(5, 10, 25, "async-");
    }

    @Bean("crawlNews")
    public Executor crawlNewsExecutor() {
        return createExecutor(10, 20, 50, "crawl-news-");
    }

    private ThreadPoolTaskExecutor createExecutor(
            int corePoolSize,
            int maxPoolSize,
            int queueCapacity,
            String threadNamePrefix) {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setTaskDecorator(new SentryTaskDecorator());

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("Async method [{}] failed. Params: {}",
                        method.getName(),
                        java.util.Arrays.toString(params),
                        ex
                );
    }
}