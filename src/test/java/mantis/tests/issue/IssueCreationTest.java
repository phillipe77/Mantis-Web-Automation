package mantis.tests.issue;

import mantis.pages.DashboardPage;
import mantis.pages.IssuePage;
import mantis.tests.BaseTest;
import mantis.utils.ConfigReader;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueCreationTest extends BaseTest {

    @Test
    @Description("Verifica se é possível criar uma nova tarefa e visualizá-la")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateNewIssue() {
        logger.info("Iniciando teste de criação de nova tarefa");

        try {
            performLogin();
            performAction(this::navigateToCreateIssuePage);
            performAction(this::createAndVerifyIssue);
        } finally {
            // Aqui o WebDriver será fechado após todas as ações serem realizadas
            if (driver != null) {
                logger.info("Encerrando o WebDriver");
                driver.quit();
            }
        }
    }

    private void navigateToCreateIssuePage() {
        DashboardPage dashboardPage = new DashboardPage(driver);
        assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard não foi exibido após o login");
        logger.info("Dashboard exibido com sucesso");
        dashboardPage.navigateToCreateIssuePage();
        logger.info("Navegado para a página de criação de tarefas");
    }

    private void createAndVerifyIssue() {
        IssuePage issuePage = new IssuePage(driver);
        String category = ConfigReader.getProperty("issue.category");
        String summary = generateSummary();
        String description = "Esta é uma descrição de teste para uma nova tarefa.";
        logger.info("Criando nova tarefa - Categoria: {}, Resumo: {}", category, summary);
        issuePage.createNewIssue(category, summary, description);

        // Adiciona a verificação do erro de limite logo após criar a tarefa
        handleLimitError();  // Verifica se o erro de limite ocorreu

        verifyIssueCreation(issuePage, summary, description);
        logger.info("Nova tarefa criada e verificada com sucesso: {}", summary);
    }

    private String generateSummary() {
        return "Nova tarefa de teste " + System.currentTimeMillis();
    }

    private void verifyIssueCreation(IssuePage issuePage, String expectedSummary, String expectedDescription) {
        handleLimitError();  // Verifica se o erro de limite ocorreu ao visualizar a tarefa

        assertTrue(issuePage.isIssueVisible(expectedSummary), "A nova tarefa não está visível na página de visualização");
        logger.info("Nova tarefa está visível na página de visualização");
        String actualSummary = issuePage.getIssueSummary();
        assertEquals(expectedSummary, actualSummary, "O resumo da tarefa não corresponde ao esperado");
        logger.info("Resumo da tarefa verificado com sucesso. Esperado: '{}', Atual: '{}'", expectedSummary, actualSummary);
        assertEquals(expectedDescription, issuePage.getIssueDescription(), "A descrição da tarefa não corresponde ao esperado");
        logger.info("Descrição da tarefa verificada com sucesso");
    }
}
