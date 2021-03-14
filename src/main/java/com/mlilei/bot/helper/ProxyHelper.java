package com.mlilei.bot.helper;

import com.alibaba.fastjson.JSON;
import com.deep007.goniub.request.HttpsProxy;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Stack;

import static com.mlilei.bot.helper.ThreadHelper.EXECUTOR_SERVICE;

/**
 * @Author lilei
 * @Description
 * @Date 2021/3/9 10:40
 */
public class ProxyHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyHelper.class);

    private static final Stack<HttpsProxy> IP = new Stack<>();


    static {
        for (int i = 1; i <= 10; i++) {
            String proxy = ConfigHelper.getProperty("proxy" + i);
            if (Strings.isEmpty(proxy)) {
                continue;
            }
            for (String proxyUrl : proxy.split("\\|")) {
                double finalI = 1.0 / i;
                EXECUTOR_SERVICE.submit(() -> {
                    RateLimiter rateLimiter = RateLimiter.create(finalI);
                    while (true) {
                        rateLimiter.acquire();
                        HttpsProxy httpsProxy = getProxy(proxyUrl);
                        if (null != httpsProxy) {
                            addProxy(httpsProxy);
                        }
                    }
                });
            }
        }
    }


    private static synchronized void addProxy(HttpsProxy httpsProxy) {
        LOGGER.info("addProxy {}", JSON.toJSONString(httpsProxy));
        IP.push(httpsProxy);
    }

    public synchronized static HttpsProxy getProxy() {
        if (IP.isEmpty()) {
            LOGGER.info("getProxy null");
            return null;
        }
        HttpsProxy proxy = IP.pop();
        LOGGER.info("getProxy {}", JSON.toJSONString(proxy));
        return proxy;
    }

    private static HttpsProxy getProxy(String proxy) {
        final Document document;
        try {
            document = Jsoup.connect(proxy).get();
            final String text = document.text();
            if (text.contains(":")) {
                final String[] split = text.split(":");
                return new HttpsProxy(split[0], Integer.parseInt(split[1]));
            } else {
                LOGGER.error("ProxyHelper.getProxy  document={}", text);
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("ProxyHelper.getProxy Exception {}", proxy, e);
            return null;
        }

    }

}
