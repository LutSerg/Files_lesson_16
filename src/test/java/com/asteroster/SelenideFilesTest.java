package com.asteroster;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {

    /*static {
        Configuration.fileDownload = FileDownloadMode.PROXY;  - для скачиваняи при отсутствии атрибута href
    }*/

    @Test
    void selenideDownloadTest () throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedJunitFile = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloadedJunitFile)) {
            byte[] bytes = is.readAllBytes();
            String textContent = new String(bytes, StandardCharsets.UTF_8);
            assertThat(textContent).contains("Contributions to JUnit 5 are both welcomed and appreciated. For specific guidelines");
        }
    }

    @Test
    void selenideUploadFileTest() {
        open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("rat.jpg");
        $(".qq-file-info").shouldHave(Condition.text("rat.jpg"));
    }
}
