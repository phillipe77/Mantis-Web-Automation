package mantis.tests.issue;

import mantis.pages.IssuePage;
import mantis.pages.DashboardPage;
import mantis.tests.BaseTest;
import mantis.utils.ConfigReader;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IssueSearchTest extends BaseTest {

    @Test
    @Description("Busca uma tarefa específica pelo número e verifica os detalhes.")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchTaskById() {
        logger.info("Iniciando o teste de busca por tarefa específica.");

        String issueId = ConfigReader.getProperty("issue.id");
        logger.info("ID da tarefa a ser buscada: {}", issueId);

        performLogin();
        performAction(this::navigateToTaskPage);
        performAction(() -> searchForIssue(issueId));
        performAction(() -> verifyIssueFound(issueId));
    }

    private void navigateToTaskPage() {
        DashboardPage dashboardPage = new DashboardPage(driver);
        assertTrue(dashboardPage.isDashboardDisplayed(), "O dashboard não foi exibido após o login");
        logger.info("Dashboard exibido com sucesso");
        dashboardPage.navigateToTaskList();
        logger.info("Navegado para a lista de tarefas");
    }

    private void searchForIssue(String issueId) {
        IssuePage issuePage = new IssuePage(driver);
        issuePage.searchIssueById(issueId);
        logger.info("Busca pela tarefa '{}' realizada.", issueId);
    }

    private void verifyIssueFound(String issueId) {
        IssuePage issuePage = new IssuePage(driver);


        boolean isTaskDisplayed = issuePage.isTaskDetailsPageDisplayed();

        if (!isTaskDisplayed) {
            logger.error("A página de detalhes da tarefa '{}' não foi carregada corretamente.", issueId);
        }


        assertTrue(isTaskDisplayed, "A página de detalhes da tarefa '" + issueId + "' não foi carregada corretamente.");
        logger.info("Tarefa '{}' verificada com sucesso.", issueId);
    }

}
