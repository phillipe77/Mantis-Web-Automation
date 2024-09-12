package mantis.tests.issue;

import mantis.pages.IssuePage;
import mantis.pages.DashboardPage;
import mantis.tests.BaseTest;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IssueFilterTest extends BaseTest {

    @Test
    @Description("Verifica se é possível filtrar as tarefas pelo estado 'novo' e verificar os resultados.")
    @Severity(SeverityLevel.NORMAL)
    public void testFilterByNewStatus() {
        logger.info("Iniciando o teste de filtro de tarefas pelo estado 'novo'.");

        performLogin();
        performAction(this::navigateToTaskPage);
        performAction(this::applyNewStatusFilter);
        performAction(this::verifyFilterApplied);
    }

    private void navigateToTaskPage() {
        DashboardPage dashboardPage = new DashboardPage(driver);
        assertTrue(dashboardPage.isDashboardDisplayed(), "O dashboard não foi exibido após o login");
        logger.info("Dashboard exibido com sucesso");
        dashboardPage.navigateToTaskList();
        logger.info("Navegado para a lista de tarefas");
    }

    private void applyNewStatusFilter() {
        IssuePage issuePage = new IssuePage(driver);

        // Clicar no botão "Redefinir" antes de aplicar o filtro
        issuePage.clickResetFilters();

        // Selecionar o estado "novo" e aplicar o filtro
        issuePage.selectStatusNew();
        issuePage.applyFilter();
        logger.info("Filtro pelo estado 'novo' aplicado após redefinir os filtros.");
    }

    private void verifyFilterApplied() {
        IssuePage issuePage = new IssuePage(driver);
        assertTrue(issuePage.isFilterApplied(), "O filtro não foi aplicado corretamente.");
        logger.info("Filtro verificado com sucesso.");
    }
}