package com.mlilei.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Author lilei
 * @Description
 * @Date 2021/2/23 15:53
 */
public class BotStarterTest extends BotApplicationTests{

    @Autowired
    private BotStarter botStarter;

    @Test
    void starter() {
        botStarter.starter();
    }
}