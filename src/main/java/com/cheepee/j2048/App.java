package com.cheepee.j2048;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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

        GameBoard board = readBoard(driver);

        try {
//            AIDriver ai = new RandomAIDriver();
            AIDriver ai = new HttpAIDriver(new URI("http://localhost:2048"));
            ai.start(board);

            int moveCount = 0;
            try {
                while (!gameLost(driver) && !gameWon(driver)) {
                    CharSequence aiMove = ai.nextMove();

                    Actions actions = new Actions(driver);
                    actions.sendKeys(aiMove);
                    actions.perform();
                    moveCount++;

                    try {
                        // TODO: fix condition
                        WebElement score = driver.findElement(By.cssSelector(".best-container"));
                        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                                .withTimeout(1, TimeUnit.SECONDS)
                                .pollingEvery(200, TimeUnit.MILLISECONDS);
                        wait.until(ExpectedConditions.stalenessOf(score));
                    } catch (TimeoutException ex) {
                    }

                    board = readBoard(driver);
                    board.print(System.out);
                    ai.setBoardState(aiMove, board);
                }
            } finally {
                ai.end();
            }

            System.out.printf("\nDone in %d moves, and the outcome is... ", moveCount);

            if (gameWon(driver)) {
                System.out.println("Victory!!!");
            } else if (gameLost(driver)) {
                System.out.println("defeat :(");
            } else {
                System.out.println("I'm bored. Till next time...");
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AIException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        driver.quit();
    }

    private static GameBoard readBoard(WebDriver driver) throws NumberFormatException {
        WebElement boardContainer = driver.findElement(By.cssSelector(".tile-container"));
        GameBoard board = new GameBoard();
        List<WebElement> tiles = boardContainer.findElements(By.cssSelector(".tile"));
        for (WebElement tile : tiles) {
            int x = -1;
            int y = -1;
            int value = 0;
            String tileClass = tile.getAttribute("class");
            StringTokenizer tokenizer = new StringTokenizer(tileClass);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.startsWith("tile-position-")) {
                    StringTokenizer posTokenizer = new StringTokenizer(token, "-");
                    posTokenizer.nextToken();
                    posTokenizer.nextToken();
                    x = Integer.parseInt(posTokenizer.nextToken()) - 1;
                    y = Integer.parseInt(posTokenizer.nextToken()) - 1;
                } else if (token.startsWith("tile-")) {
                    StringTokenizer valueTokenizer = new StringTokenizer(token, "-");
                    valueTokenizer.nextToken();
                    value = Integer.parseInt(valueTokenizer.nextToken());
                }

                if (value > 0 && x >= 0 && y >= 0) {
                    board.setTile(x, y, value);
                    break;
                }
            }
        }
        return board;
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

    private static boolean gameLost(WebDriver driver) {
        try {
            driver.findElement(By.cssSelector(".game-over"));
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    private static boolean gameWon(WebDriver driver) {
        try {
            driver.findElement(By.cssSelector(".game-won"));
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

}
