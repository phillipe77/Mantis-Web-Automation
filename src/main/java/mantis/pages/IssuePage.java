package mantis.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mantis.utils.WaitUtils;
import mantis.utils.InputUtils;
import mantis.utils.CategoryUtils;

import java.util.List;

public class IssuePage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;
    private static final Logger logger = LoggerFactory.getLogger(IssuePage.class);

    // Elementos para criação de issue
    @FindBy(id = "category_id")
    private WebElement categorySelect;
    @FindBy(id = "summary")
    private WebElement summaryInput;
    @FindBy(id = "description")
    private WebElement descriptionInput;
    @FindBy(css = "input[value='Criar Nova Tarefa']")
    private WebElement submitButton;

    // Elementos para visualização de issue
    @FindBy(css = "td.bug-summary")
    private WebElement issueSummary;
    @FindBy(css = "td.bug-description")
    private WebElement issueDescription;

    // Elementos para adicionar comentários
    @FindBy(id = "bugnote_text")
    private WebElement commentInput;
    @FindBy(css = "input[value='Adicionar Anotação']")
    private WebElement addCommentButton;

    // Elementos para monitorar/desmonitorar tarefa
    @FindBy(css = "input[value='Monitorar']")
    private WebElement monitorButton;
    @FindBy(css = "input[value='Parar de Monitorar']")
    private WebElement stopMonitoringButton;

    // Elementos para adicionar marcadores
    @FindBy(id = "tag_string")
    private WebElement tagInput;
    @FindBy(css = "input[value='Aplicar']")
    private WebElement applyTagButton;

    // Elementos da página de filtro
    @FindBy(css = "a#show_status_filter.dynamic-filter-expander")
    private WebElement statusFilterExpander;
    @FindBy(name = "status[]")
    private WebElement statusDropdown;

    @FindBy(name = "bug_id")
    private WebElement searchIssueInput;
    @FindBy(css = "a.btn.btn-sm.btn-primary.btn-white.btn-round")
    private WebElement resetFiltersButton;

    // Elemento do botão de aplicar filtro
    @FindBy(css = "input[type='submit'][value='Aplicar Filtro']")
    private WebElement filterButton;


    public IssuePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = WaitUtils.getInstance(driver); // Singleton para WaitUtils
        PageFactory.initElements(driver, this);
    }


    public void selectCategory(String category) {
        Select categoryDropdown = new Select(waitUtils.waitForVisibility(categorySelect));
        CategoryUtils.selectCategory(categoryDropdown, category, logger);
    }

    public void enterSummary(String summary) {
        InputUtils.enterText(waitUtils.waitForVisibility(summaryInput), summary, logger, "Resumo inserido: {}");
    }

    public void enterDescription(String description) {
        InputUtils.enterText(waitUtils.waitForVisibility(descriptionInput), description, logger, "Descrição inserida");
    }

    public IssuePage clickSubmit() {
        waitUtils.waitForClickability(submitButton).click();
        logger.info("Botão de submissão clicado");
        return new IssuePage(driver);
    }

    public void createNewIssue(String category, String summary, String description) {
        selectCategory(category);
        enterSummary(summary);
        enterDescription(description);
        clickSubmit();
    }

    public boolean isIssueVisible(String expectedSummary) {
        try {
            String actualSummary = waitUtils.waitForVisibility(issueSummary).getText();
            return actualSummary.endsWith(expectedSummary);
        } catch (Exception e) {
            logger.error("Erro ao verificar visibilidade da issue: {}", e.getMessage());
            return false;
        }
    }

    public String getIssueSummary() {
        String fullSummary = waitUtils.waitForVisibility(issueSummary).getText();
        return fullSummary.replaceFirst("^\\d+:\\s*", "");
    }

    public String getIssueDescription() {
        return waitUtils.waitForVisibility(issueDescription).getText();
    }

    public void addComment(String comment) {
        waitUtils.waitForVisibility(commentInput).sendKeys(comment);
        logger.info("Comentário inserido: {}", comment);
        waitUtils.waitForClickability(addCommentButton).click();
        logger.info("Botão de adicionar comentário clicado");
    }

    public boolean isCommentVisible(String comment) {
        List<WebElement> commentElements = driver.findElements(By.cssSelector("td.bugnote-note.bugnote-public"));
        return commentElements.stream().anyMatch(element -> element.getText().contains(comment));
    }


    public void monitorIssue() {
        waitUtils.waitForClickability(monitorButton).click();
        logger.info("Monitorando a tarefa.");
    }

    public void unmonitorIssue() {
        waitUtils.waitForClickability(stopMonitoringButton).click();
        logger.info("Parando de monitorar a tarefa.");
    }

    public boolean isIssueBeingMonitored() {
        return waitUtils.isElementVisible(stopMonitoringButton);
    }

    public boolean addTag(String tagName) {
        try {
            waitUtils.waitForVisibility(tagInput).sendKeys(tagName);
            logger.info("Marcador inserido: {}", tagName);
            waitUtils.waitForClickability(applyTagButton).click();
            logger.info("Botão de aplicar marcador clicado");

            // Aguarda um pouco para ver se o erro de limite aparece
            Thread.sleep(2000);

            if (isLimitErrorVisible()) {
                logger.error("Erro de limite de atividades detectado ao adicionar o marcador.");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Erro ao adicionar o marcador: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTagVisible(String tagName) {
        try {

            WebElement tagElement = waitUtils.waitForPresence(By.xpath("//td[contains(@class, 'bug-tags')]//span[contains(text(), '" + tagName + "')]"));
            logger.info("Marcador '{}' encontrado", tagName);
            return tagElement.isDisplayed();
        } catch (Exception e) {
            logger.error("Erro ao verificar se o marcador '{}' está visível: {}", tagName, e.getMessage());
            return false;
        }
    }


    public boolean isLimitErrorVisible() {
        try {
            WebElement errorElement = waitUtils.waitForPresence(By.cssSelector("div.alert.alert-danger"));
            return errorElement.getText().contains("APPLICATION ERROR #27");
        } catch (Exception e) {
            logger.info("Nenhum erro de limite de atividades foi detectado.");
            return false;
        }
    }

    public void removeAllTags() {
        try {
            List<WebElement> existingTags = driver.findElements(By.xpath("//a[contains(@href, 'tag_detach.php')]"));
            for (WebElement tagRemoveLink : existingTags) {
                waitUtils.waitForClickability(tagRemoveLink).click();
                Thread.sleep(300);
            }
            logger.info("Todas as tags existentes foram removidas");
        } catch (Exception e) {
            logger.error("Erro ao tentar remover todas as tags: ", e);
        }
    }


    public void openStatusFilter() {
        waitUtils.waitForClickability(statusFilterExpander).click();
        logger.info("Filtro de estado expandido.");
    }

    public void clickResetFilters() {
        waitUtils.waitForClickability(resetFiltersButton).click();
        logger.info("Botão 'Redefinir' clicado.");
    }

    public void selectStatusNew() {
        openStatusFilter();
        Select statusSelect = new Select(waitUtils.waitForVisibility(statusDropdown));
        statusSelect.selectByValue("10"); // Valor 10 corresponde ao estado "novo"
        logger.info("Estado 'novo' selecionado no dropdown.");
    }

    public void applyFilter() {
        try {
            waitUtils.waitForClickability(filterButton).click(); // Tente usar o clique normal
            logger.info("Botão Aplicar Filtro clicado com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao clicar no botão Aplicar Filtro: {}", e.getMessage());
        }
    }


    public boolean isFilterApplied() {
        try {
            WebElement filterResultsTable = waitUtils.waitForVisibility(driver.findElement(By.cssSelector("table#buglist")));
            List<WebElement> statusCells = filterResultsTable.findElements(By.cssSelector("td.column-status"));

            for (WebElement cell : statusCells) {
                if (!cell.getText().equalsIgnoreCase("novo")) {
                    logger.error("Tarefa com estado diferente de 'novo' encontrada: {}", cell.getText());
                    return false;
                }
            }
            logger.info("Todos os itens da tabela possuem o estado 'novo'.");
            return true;
        } catch (Exception e) {
            logger.error("Erro ao verificar a aplicação do filtro: {}", e.getMessage());
            return false;
        }
    }

    public void searchIssueById(String issueId) {
        WebElement searchInput = waitUtils.waitForVisibility(searchIssueInput);
        searchInput.clear();
        searchInput.sendKeys(issueId);
        searchInput.sendKeys(Keys.ENTER);
        logger.info("Número da tarefa inserido para busca: {}", issueId);
    }


    public boolean isTaskDetailsPageDisplayed() {
        try {

            waitUtils.waitForVisibility(issueSummary);
            waitUtils.waitForVisibility(issueDescription);

            return issueSummary.isDisplayed() && issueDescription.isDisplayed();
        } catch (Exception e) {
            logger.error("Erro ao verificar a página de detalhes da tarefa: {}", e.getMessage());
            return false;
        }
    }

}




