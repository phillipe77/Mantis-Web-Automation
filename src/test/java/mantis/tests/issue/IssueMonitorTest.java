package mantis.tests.issue;

import mantis.pages.IssuePage;
import mantis.tests.BaseTest;
import mantis.utils.ConfigReader;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IssueMonitorTest extends BaseTest {

    @Test
    @Description("Verifica se é possível monitorar uma tarefa e verificar a tarefa na lista de monitoradas")
    @Severity(SeverityLevel.NORMAL)
    public void testMonitorIssue() {
        logger.info("Iniciando teste de monitoramento de tarefa");

        performLogin();
        navigateToIssue(ConfigReader.getProperty("issue.id"));
        performAction(this::monitorIssue);
        performAction(this::verifyIssueMonitored);
    }

    private void monitorIssue() {
        IssuePage issuePage = new IssuePage(driver);

        if (issuePage.isIssueBeingMonitored()) {
            issuePage.unmonitorIssue();
            logger.info("Tarefa já estava sendo monitorada. Monitoramento interrompido.");
        }

        issuePage.monitorIssue();
        logger.info("Tarefa monitorada com sucesso.");
    }

    private void verifyIssueMonitored() {
        IssuePage issuePage = new IssuePage(driver);
        assertTrue(issuePage.isIssueBeingMonitored(), "A tarefa monitorada não está na lista de monitoradas.");
        logger.info("Tarefa monitorada verificada com sucesso.");
    }
}