package com.cheepee.j2048;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class App {

    public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();

        driver.get("http://gabrielecirulli.github.io/2048/");
        driver.manage().window().maximize();

        waitAndCloseNotice(driver);
        scrollToGame(driver);

        WebElement score = driver.findElement(By.cssSelector(".best-container"));

        long end = System.currentTimeMillis() + 100000;
        while (System.currentTimeMillis() < end) {
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ARROW_LEFT);
            actions.perform();

            try {
                Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                        .withTimeout(2, TimeUnit.SECONDS)
                        .pollingEvery(500, TimeUnit.MILLISECONDS);
                wait.until(ExpectedConditions.stalenessOf(score));
            } catch (TimeoutException ex) {
            }
        }

        driver.quit();
    }

    private static void waitAndCloseNotice(WebDriver driver) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(3, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        try {
            By noticeCloseButtonLocator = By.cssSelector(".notice-close-button");
            wait.until(ExpectedConditions.presenceOfElementLocated(noticeCloseButtonLocator));

            WebElement noticeCloseButton = driver.findElement(noticeCloseButtonLocator);
            wait.until(ExpectedConditions.elementToBeClickable(noticeCloseButton));

            noticeCloseButton.click();
        } catch (TimeoutException ex) {
        }
    }

    private static void scrollToGame(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("javascript:window.scrollBy(0,210)");
    }
}
