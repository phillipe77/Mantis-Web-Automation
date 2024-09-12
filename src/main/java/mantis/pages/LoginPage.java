package mantis.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mantis.utils.WaitUtils;
import mantis.utils.InputUtils;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);


    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(css = "input[type='submit']")
    private WebElement submitUsernameButton;


    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "input[type='submit']")
    private WebElement submitPasswordButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        // Use o método getInstance para obter a instância de WaitUtils
        this.waitUtils = WaitUtils.getInstance(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    private void enterText(WebElement element, String text, String logMessage) {
        InputUtils.enterText(waitUtils.waitForVisibility(element), text, logger, logMessage);
    }

    private void clickButton(WebElement button, String logMessage) {
        waitUtils.waitForClickability(button).click();
        logger.info(logMessage);
    }

    public void enterUsername(String username) {
        enterText(usernameInput, username, "Nome de usuário inserido: {}");
    }

    public void clickSubmitUsername() {
        clickButton(submitUsernameButton, "Botão de submissão do nome de usuário clicado");
    }

    public void enterPassword(String password) {
        enterText(passwordInput, password, "Senha inserida");
    }

    public void clickSubmitPassword() {
        clickButton(submitPasswordButton, "Botão de submissão da senha clicado");
    }

    public void login(String username, String password) {
        enterUsername(username);
        clickSubmitUsername();
        enterPassword(password);
        clickSubmitPassword();
    }

    public boolean isLoginUsernamePageDisplayed() {
        return isElementDisplayed(usernameInput, "Página de nome de usuário não foi exibida");
    }

    public boolean isLoginPasswordPageDisplayed() {
        return isElementDisplayed(passwordInput, "Página de senha não foi exibida");
    }

    private boolean isElementDisplayed(WebElement element, String errorMessage) {
        try {
            return waitUtils.waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            logger.error(errorMessage, e);
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        try {
            WebElement errorMessage = driver.findElement(By.cssSelector(".alert-danger"));
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            logger.error("Erro ao verificar a mensagem de erro", e);
            return false;
        }
    }
}