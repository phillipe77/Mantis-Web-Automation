package mantis.tests.issue;

import mantis.pages.IssuePage;
import mantis.tests.BaseTest;
import mantis.utils.ConfigReader;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IssueAddTagTest extends BaseTest {

    @Test
    @Description("Verifica se é possível adicionar um marcador a uma tarefa e se ele é exibido corretamente, lidando com o limite de atividades")
    @Severity(SeverityLevel.NORMAL)
    public void testAddTagToIssue() {
        logger.info("Iniciando teste de adição de marcador à tarefa");

        performLogin();
        navigateToIssue(ConfigReader.getProperty("issue.id"));

        IssuePage issuePage = new IssuePage(driver);

        // Remove todas as tags existentes
        performAction(() -> {
            logger.info("Removendo todas as tags existentes");
            issuePage.removeAllTags();
            handleLimitError();  // Verifica se o erro de limite ocorreu
        });

        String tagName = "MarcadorTeste" + System.currentTimeMillis();
        logger.info("Tentando adicionar novo marcador: {}", tagName);

        performAction(() -> {
            boolean tagAdded = issuePage.addTag(tagName);
            handleLimitError();  // Verifica se o erro de limite ocorreu após adicionar o marcador

            if (tagAdded) {
                logger.info("Tentando verificar se o marcador foi adicionado: {}", tagName);
                boolean isTagVisible = issuePage.isTagVisible(tagName);
                assertTrue(isTagVisible, "O marcador não está visível na página de detalhes da tarefa");
                logger.info("Marcador verificado com sucesso: {}", tagName);
            } else {
                logger.warn("Falha ao adicionar o marcador: {}", tagName);
            }
        });
    }
}
