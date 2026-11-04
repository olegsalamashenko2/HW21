import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CataloguePage;
import pages.StoreFormPage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
        StoreFormPage formPage = new StoreFormPage(driver);
        assertTrue(formPage.isNameFieldDisplayed(), "Поле імені має відображатися");
    }

    @Test
    public void shouldOpenCatalogueAfterEnteringName() {
        StoreFormPage formPage = new StoreFormPage(driver);
        CataloguePage cataloguePage = formPage.enterName("Test User").openCatalogue();
        assertTrue(cataloguePage.isProductDisplayed(), "У каталозі має відображатися товар");
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
