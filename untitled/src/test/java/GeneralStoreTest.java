import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;

import static org.testng.Assert.assertTrue;

public class GeneralStoreTest {
    private AndroidDriver driver;

    @BeforeMethod
    public void connectToAppium() throws Exception {
        Properties config = new Properties();
        try (InputStream stream = getClass().getResourceAsStream("/config.properties")) {
            if (stream == null) {
                throw new IOException("Не найден config.properties в ресурсах тестов");
            }
            config.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
        }

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(config.getProperty("appium.deviceName"))
                .setUdid(config.getProperty("appium.udid"))
                .setAppPackage(config.getProperty("appium.appPackage"))
                .setAppActivity(config.getProperty("appium.appActivity"))
                .setAppWaitActivity(config.getProperty("appium.appWaitActivity"))
                .setNoReset(Boolean.parseBoolean(config.getProperty("appium.noReset")));

        // Открываем стартовый экран даже если приложение уже запущено.
        options.setCapability("appium:forceAppLaunch",
                Boolean.parseBoolean(config.getProperty("appium.forceAppLaunch")));
        driver = new AndroidDriver(URI.create(config.getProperty("appium.server.url")).toURL(), options);
    }

    @Test
    public void shouldDisplayNameField() {
        // Ждём поле имени до 15 секунд, используя id из Inspector.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.id("com.androidsample.generalstore:id/nameField")))
                .isDisplayed());
    }

    @Test
    public void shouldOpenCatalogueAfterEnteringName() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Находим поле по id из Inspector и вводим имя.
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.id("com.androidsample.generalstore:id/nameField")))
                .sendKeys("Test User");

        // Прячем клавиатуру, чтобы она не перекрывала кнопку.
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }

        wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.id("com.androidsample.generalstore:id/btnLetsShop")))
                .click();

        // Проверяем результат перехода: видимое название товара в каталоге.
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.id("com.androidsample.generalstore:id/productName")))
                .isDisplayed());
    }

    @AfterMethod(alwaysRun = true)
    public void disconnectFromAppium() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }
}
