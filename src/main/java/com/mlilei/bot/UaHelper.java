package com.mlilei.bot;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

/**
 * @Author lilei
 * @Description
 * @Date 2021/3/4 15:31
 */
public class UaHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UaHelper.class);

    private static List<String> uaList = Lists.newArrayList();

    static {
        try {
            InputStream inputStream = ConfigHelper.class.getClassLoader().getResourceAsStream("ua_string.csv");
            if (null == inputStream) {
                LOGGER.error("配置加载失败，文件不存在 ua_string.csv");
            } else {
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    uaList.add(line);
                }
                LOGGER.info("配置加载完成");
            }
        } catch (Exception e) {
            LOGGER.error("配置加载失败", e);
        }
    }

    public static String getUa() {
        if (CollectionUtils.isEmpty(uaList)) {
            return null;
        }
        final int i = RandomUtils.nextInt(0, uaList.size());
        return uaList.get(i);
    }
}
