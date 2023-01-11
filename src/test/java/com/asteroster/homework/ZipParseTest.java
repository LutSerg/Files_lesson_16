package com.asteroster.homework;

import com.asteroster.FilesParsingTest;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipParseTest {

    ClassLoader cl = FilesParsingTest.class.getClassLoader();


    @Test
    void zipParsingTest() throws Exception {
        try (
                InputStream zipResource = cl.getResourceAsStream("zip/homework.zip");
                ZipInputStream zis = new ZipInputStream(zipResource)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("junit-user-guide-5.9.1.pdf")) {
                    PDF pdfContent = new PDF(zis);
                    assertThat(pdfContent.author).contains("Sam Brannen");
                } else if (entry.getName().contains("csvexample")) {
                    CSVReader csvContent = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = csvContent.readAll();
                    assertThat(content.get(1)[1]).contains("Files");
                } else if (entry.getName().contains("sortovojprokat")) {
                    XLS xlsContent = new XLS(zis);
                    assertThat(xlsContent.excel.getSheetAt(0).getRow(4).getCell(0).getStringCellValue())
                            .contains("АРМАТУРА");
                }
            }
        }
    }
}
