package com.gzu.lionsoj.manager;

import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname: CosManagerTest
 * @Description: Cos 操作测试
 * @Author: lions
 * @Datetime: 12/29/2023 12:37 AM
 */
@SpringBootTest
class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    void putObject() {
        cosManager.putObject("test", "test.json");
    }
}