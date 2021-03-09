package com.mlilei.bot;

import com.deep007.goniub.request.HttpsProxy;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author lilei
 * @Description
 * @Date 2021/3/9 10:40
 */
@Component
public class ProxyHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyHelper.class);

    public static HttpsProxy getProxy() {
        final String proxy = ConfigHelper.PROPERTIES.getProperty("proxy");
        if (Strings.isEmpty(proxy)) {
            return null;
        }

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
            LOGGER.error("ProxyHelper.getProxy Exception", e);
            return null;
        }

    }
}
