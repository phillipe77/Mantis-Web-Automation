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

        try {
            performLogin();  // Executa o login
            navigateToIssue(ConfigReader.getProperty("issue.id"));  // Navega até a tarefa

            // Executa a ação de monitorar a tarefa
            performAction(this::monitorIssue);

            // Verifica se a tarefa está monitorada corretamente
            performAction(this::verifyIssueMonitored);

        } finally {
            // Encerrar o WebDriver após todas as ações e validações
            if (driver != null) {
                logger.info("Encerrando o WebDriver");
                driver.quit();
            }
        }
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

        // Verifica se a tarefa está sendo monitorada corretamente
        assertTrue(issuePage.isIssueBeingMonitored(), "A tarefa monitorada não está na lista de monitoradas.");
        logger.info("Tarefa monitorada verificada com sucesso.");
    }
}
