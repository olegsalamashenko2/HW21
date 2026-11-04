package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CataloguePage {
    private final WebDriverWait wait;
    private final By productName = AppiumBy.id("com.androidsample.generalstore:id/productName");

    public CataloguePage(AndroidDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isProductDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productName)).isDisplayed();
    }
}
