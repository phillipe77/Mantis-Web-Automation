package mantis.tests.issue;

import mantis.pages.IssuePage;
import mantis.tests.BaseTest;
import mantis.utils.ConfigReader;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IssueCommentTest extends BaseTest {

    @Test
    @Description("Verifica se é possível adicionar um comentário a uma tarefa e visualizar o comentário adicionado")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddCommentToIssue() {
        logger.info("Iniciando teste de adição de comentário à tarefa");

        performLogin();
        navigateToIssue(ConfigReader.getProperty("issue.id"));

        String comment = "Este é um comentário de teste " + System.currentTimeMillis();

        performAction(() -> addCommentToIssue(comment));
        performAction(() -> verifyCommentAdded(comment));
    }

    private void addCommentToIssue(String comment) {
        IssuePage issuePage = new IssuePage(driver);
        issuePage.addComment(comment);
        logger.info("Comentário adicionado à tarefa: {}", comment);
    }

    private void verifyCommentAdded(String comment) {
        IssuePage issuePage = new IssuePage(driver);
        assertTrue(issuePage.isCommentVisible(comment), "O comentário não está visível na página de detalhes da tarefa");
        logger.info("Comentário verificado com sucesso: {}", comment);
    }
}