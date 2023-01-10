package com.asteroster;

import com.asteroster.model.Morpheus;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void pdfParsingTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloadedPdf = $("a[href='junit-user-guide-5.9.1.pdf']").download();
        PDF content = new PDF(downloadedPdf);
        assertThat(content.author).contains("Sam Brannen");
    }

    @Test
    void xlsParsingTest() throws Exception {
        try (InputStream resourceAsStream = cl.getResourceAsStream("xls/sortovojprokat.xls")) {
            XLS content = new XLS(resourceAsStream);
            assertThat(content.excel.getSheetAt(0).getRow(4).getCell(0).getStringCellValue())
                    .contains("АРМАТУРА");
        }
    }

    @Test
    void csvParsingTest() throws Exception {
        try (InputStream csvResource = cl.getResourceAsStream("csv/csvexample.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(csvResource))
        )
        {
            List<String[]> content = reader.readAll();
            assertThat(content.get(1)[1]).contains("Files");
        }
    }

    @Test
    void zipParsingTest() throws Exception {
        try (
                InputStream zipResource = cl.getResourceAsStream("zip/sortovojprokat.zip");
                ZipInputStream zis = new ZipInputStream(zipResource)
        )
        {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) !=null) {
                assertThat(entry.getName()).contains("sortovojprokat");
            }
        }
    }

    @Test
    void jsonParsingTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream jsonResource = cl.getResourceAsStream("json/morpheus.json");
                InputStreamReader reader = new InputStreamReader(jsonResource)
        ) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            assertThat(jsonObject.get("name").getAsString()).isEqualTo("morpheus");
        }
    }

    @Test
    void jsonParsingModelTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream jsonResource = cl.getResourceAsStream("json/morpheus.json");
                InputStreamReader reader = new InputStreamReader(jsonResource)
        ) {
            Morpheus jsonObject = gson.fromJson(reader, Morpheus.class);
            assertThat(jsonObject.name).isEqualTo("morpheus");
            assertThat(jsonObject.job).isEqualTo("leader");
        }
    }
}
