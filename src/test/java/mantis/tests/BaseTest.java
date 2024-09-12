package mantis.tests;

import mantis.core.DriverFactory;
import mantis.pages.DashboardPage;
import mantis.pages.LoginPage;
import mantis.utils.ConfigReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assumptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTest {
    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        String browser = ConfigReader.getProperty("browser");
        driver = DriverFactory.createDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicit.wait"))));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicit.wait"))));
        driver.manage().window().maximize();
        logger.info("WebDriver initialized with browser: " + browser);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed");
        }
    }

    protected void handleLimitError() {
        if (isLimitErrorVisible()) {
            logger.warn("Limite de atividades atingido. O teste será considerado como ignorado.");
            Assumptions.assumeTrue(false, "Limite de atividades atingido, impossível continuar o teste.");
        }
    }

    private boolean isLimitErrorVisible() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.alert.alert-danger")));
            return errorElement.getText().contains("APPLICATION ERROR #27");
        } catch (Exception e) {
            return false;
        }
    }

    protected void performAction(Runnable action) {
        try {
            action.run();
            handleLimitError();
        } catch (Exception e) {
            logger.error("Erro durante a execução da ação", e);
            throw e;
        }
    }

    protected void performLogin() {
        performAction(() -> {
            driver.get(ConfigReader.getProperty("base.url"));
            logger.info("Navegando para a URL base: {}", ConfigReader.getProperty("base.url"));
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login(ConfigReader.getProperty("valid.username"), ConfigReader.getProperty("valid.password"));
            logger.info("Login realizado com sucesso");
        });
    }

    protected void navigateToIssue(String issueId) {
        performAction(() -> {
            DashboardPage dashboardPage = new DashboardPage(driver);
            assertTrue(dashboardPage.isDashboardDisplayed(), "O dashboard não foi exibido após o login");
            logger.info("Dashboard exibido com sucesso");
            dashboardPage.navigateToIssueById(issueId);
            logger.info("Navegado para a tarefa com ID: {}", issueId);
        });
    }
}