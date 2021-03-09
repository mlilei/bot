package com.mlilei.bot;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BotApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(JSON.toJSONString(ProxyHelper.getProxy()));


    }

}
