package com.gzu.lionsoj.judge.sandbox;

import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeRequest;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeResponse;
import com.gzu.lionsoj.model.enums.SystemLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: CodeSandboxTest
 * @Description: TODO
 * @Author: Lions
 * @Datetime: 1/4/2024 3:19 AM
 */
@SpringBootTest
class CodeSandboxTest {

    @Value("${sandbox.type:example}")
    private String type;

    @Test
    void execute() {
        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance(type);
        String code = "";
        String language = SystemLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.execute(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }

    @Test
    void executeByProxy() {
        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance(type);
        String code = "";
        String language = SystemLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandBox);
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.execute(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
}
