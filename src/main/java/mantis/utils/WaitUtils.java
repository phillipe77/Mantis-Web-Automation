package mantis.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WaitUtils {
    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);
    private static volatile WaitUtils instance;
    private final WebDriverWait wait;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);


    private WaitUtils(WebDriver driver, Duration timeout) {
        this.wait = new WebDriverWait(driver, timeout);
    }


    public static WaitUtils getInstance(WebDriver driver) {
        if (instance == null) {
            synchronized (WaitUtils.class) {
                if (instance == null) {
                    instance = new WaitUtils(driver, DEFAULT_TIMEOUT);
                    logger.info("WaitUtils instance created with default timeout");
                }
            }
        }
        return instance;
    }

    public static WaitUtils getInstance(WebDriver driver, Duration timeout) {
        if (instance == null) {
            synchronized (WaitUtils.class) {
                if (instance == null) {
                    instance = new WaitUtils(driver, timeout);
                    logger.info("WaitUtils instance created with custom timeout: {}", timeout);
                }
            }
        }
        return instance;
    }

    public WebElement waitForVisibility(WebElement element) {
        logger.debug("Waiting for element visibility");
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForVisibility(By locator) {
        logger.debug("Waiting for element visibility with locator: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickability(WebElement element) {
        logger.debug("Waiting for element to be clickable");
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForPresence(By locator) {
        logger.debug("Waiting for element presence with locator: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean isElementVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            logger.debug("Element is visible");
            return true;
        } catch (Exception e) {
            logger.warn("Element is not visible", e);
            return false;
        }
    }

    }

