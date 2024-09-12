package mantis.utils;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

public class InputUtils {
    public static void enterText(WebElement element, String text, Logger logger, String logMessage) {
        element.sendKeys(text);
        logger.info(logMessage, text);
    }
}