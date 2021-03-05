package com.mlilei.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BotApplication {

    public static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(50, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<>());

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(BotApplication.class, args);
    }

}
