package com.gzu.lionsoj;

import com.gzu.lionsoj.config.WxOpenConfig;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname: MainApplicationTests
 * @Description: 主测试类
 * @Author: lions
 * @Datetime: 12/29/2023 12:38 AM
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

}
