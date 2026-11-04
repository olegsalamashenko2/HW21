import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class GeneralStoreTest {
    private AndroidDriver driver;

    @BeforeMethod
    public void connectToAppium() throws Exception {
        // Те же настройки устройства и приложения, что использовали в Inspector.
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("Android Emulator")
                .setUdid("emulator-5554")
                .setAppPackage("com.androidsample.generalstore")
                .setAppActivity("com.androidsample.generalstore.SplashActivity")
                .setAppWaitActivity("com.androidsample.generalstore.*")
                .setNoReset(true);

        // Открываем стартовый экран даже если приложение уже запущено.
        options.setCapability("appium:forceAppLaunch", true);
        driver = new AndroidDriver(URI.create("http://127.0.0.1:4723").toURL(), options);
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
