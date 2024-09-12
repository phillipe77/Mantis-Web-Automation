package mantis.tests.authetication;

import mantis.pages.DashboardPage;
import mantis.pages.LoginPage;
import mantis.tests.BaseTest;
import mantis.utils.ConfigReader;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @Test
    @Description("Verifica se o login com credenciais válidas exibe o dashboard")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidLogin() {
        try {
            performLogin(ConfigReader.getProperty("valid.username"), ConfigReader.getProperty("valid.password"));
            verifyDashboardDisplayed();
        } catch (Exception e) {
            logger.error("Erro durante a execução do teste de login válido", e);
            throw e; // Re-lança a exceção para que o teste falhe
        }
    }

    @Test
    @Description("Verifica se o login falha com senha inválida")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidPasswordLogin() {
        try {
            performLogin(ConfigReader.getProperty("valid.username"), "invalidPassword");
            verifyErrorDisplayed();
        } catch (Exception e) {
            logger.error("Erro durante a execução do teste de login com senha inválida", e);
            throw e; // Re-lança a exceção para que o teste falhe
        }
    }

    private void performLogin(String username, String password) {
        driver.get(ConfigReader.getProperty("base.url"));
        logger.info("Navegando para a URL base: {}", ConfigReader.getProperty("base.url"));
        LoginPage loginPage = new LoginPage(driver);

        // Login na primeira etapa (nome de usuário)
        assertTrue(loginPage.isLoginUsernamePageDisplayed(), "A página de nome de usuário não foi exibida");
        loginPage.enterUsername(username);
        loginPage.clickSubmitUsername();

        // Login na segunda etapa (senha)
        assertTrue(loginPage.isLoginPasswordPageDisplayed(), "A página de senha não foi exibida");
        loginPage.enterPassword(password);
        loginPage.clickSubmitPassword();
    }

    private void verifyDashboardDisplayed() {
        DashboardPage dashboardPage = new DashboardPage(driver);
        assertTrue(dashboardPage.isDashboardDisplayed(), "O dashboard não foi exibido");
        logger.info("Usuário logado com sucesso e dashboard exibido");
    }

    private void verifyErrorDisplayed() {
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isErrorDisplayed(), "A mensagem de erro não foi exibida para senha inválida");
    }
}