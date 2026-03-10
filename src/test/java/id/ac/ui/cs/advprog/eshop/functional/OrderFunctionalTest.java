package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class OrderFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void testCreateAndFindOrder(ChromeDriver driver) {
        // 1. Create order
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("author")).sendKeys("Haikal");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 2. Redirected to history form, search for the order
        assertEquals(baseUrl + "/order/history", driver.getCurrentUrl());
        driver.findElement(By.id("author")).sendKeys("Haikal");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 3. See the result in the table
        String pageSource = driver.getPageSource();
        assert(pageSource.contains("Haikal"));
        assert(pageSource.contains("WAITING_PAYMENT"));
    }
}