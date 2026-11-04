package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StoreFormPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By nameField = AppiumBy.id("com.androidsample.generalstore:id/nameField");
    private final By shopButton = AppiumBy.id("com.androidsample.generalstore:id/btnLetsShop");

    public StoreFormPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isNameFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).isDisplayed();
    }

    public StoreFormPage enterName(String name) {
        var field = wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
        field.clear();
        field.sendKeys(name);
        return this;
    }

    public CataloguePage openCatalogue() {
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
        wait.until(ExpectedConditions.elementToBeClickable(shopButton)).click();
        return new CataloguePage(driver);
    }
}
