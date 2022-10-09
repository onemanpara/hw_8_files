package me.mwi.test;


import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import me.mwi.model.Order;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class AllTest {

    ClassLoader cl = AllTest.class.getClassLoader();

    @Test
    void jsonTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files/order.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Order order = objectMapper.readValue(is, Order.class);
            assertThat(order.firstName).isEqualTo("Mikhail");
            assertThat(order.lastName).isEqualTo("Nesterov");
            assertThat(order.city).isEqualTo("Yekaterinburg");
            assertThat(order.orderNumber).isEqualTo(2342);
            assertThat(order.orderDetail.orderDate).isEqualTo("07.10.22");
            assertThat(order.orderDetail.productsDetail.get(1)).isEqualTo("t-shirt");
            assertThat(order.orderIsPaid).isTrue();
        }
    }

    @Test
    void pdfTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/files/zipTest.zip"));
        try (InputStream is = cl.getResourceAsStream("files/zipTest.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("Reglament.pdf")) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        PDF pdf = new PDF(inputStream);
                        assertThat(pdf.text).contains("В турнире примут участие 12 команд.");
                        System.out.println("");
                    }
                }
            }
        }
    }

    @Test
    void csvTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/files/zipTest.zip"));
        try (InputStream is = cl.getResourceAsStream("files/zipTest.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("grids.csv")) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                        List<String[]> text = reader.readAll();
                        String[] row = text.get(1);
                        assertThat(row[0]).contains("Converse");
                        System.out.println("");
                    }
                }
            }
        }
    }

    @Test
    void xlsxTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/files/zipTest.zip"));
        try (InputStream is = cl.getResourceAsStream("files/zipTest.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("trade.xlsx")) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        XLS xls = new XLS(inputStream);
                        assertThat(xls.excel.getSheetAt(0)
                                .getRow(0)
                                .getCell(2)
                                .getStringCellValue())
                                .isEqualTo("1092545. МОЮЩЕЕ СРЕДСТВО URNEX GRINDZ 430 Г");
                        System.out.println("");
                    }
                }
            }
        }
    }
}

