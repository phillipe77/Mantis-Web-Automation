package mantis.pages;

import mantis.utils.UrlUtils;
import mantis.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DashboardPage {
    private final WebDriver driver;
    private final WaitUtils waitUtils;
    private static final Logger logger = LoggerFactory.getLogger(DashboardPage.class);

    @FindBy(css = "i[class='ace-icon fa fa-angle-down']")
    private WebElement dropdownToggle;

    @FindBy(css = "a[href='/bug_report_page.php']")
    private WebElement reportIssueLink;

    @FindBy(css = "div#monitored")
    private WebElement monitoredSection;

    @FindBy(xpath = "//a[contains(@href, 'view_all_bug_page.php')]")
    private WebElement viewTasksLink;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = WaitUtils.getInstance(driver); // Singleton para WaitUtils
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardDisplayed() {
        return waitForElementVisibility(dropdownToggle, "Erro ao verificar se o dashboard está visível");
    }

    public void navigateToCreateIssuePage() {
        clickElement(reportIssueLink, "Navegando para a página de criação de issue");
    }

    public void navigateToIssueById(String issueId) {
        try {
            String issueUrl = UrlUtils.buildIssueUrl(issueId); // Usando utilitário para construir a URL
            driver.get(issueUrl);
            logger.info("Navegando para a tarefa com ID: {}", issueId);
        } catch (Exception e) {
            logger.error("Erro ao navegar para a tarefa com ID: {}", issueId, e);
        }
    }

    public void navigateToTaskList() {
        clickElement(viewTasksLink, "Navegando para a página de tarefas.");
    }


    private boolean waitForElementVisibility(WebElement element, String errorMessage) {
        try {
            return waitUtils.waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            logger.error(errorMessage, e);
            return false;
        }
    }

    private void clickElement(WebElement element, String logMessage) {
        try {
            waitUtils.waitForClickability(element).click();
            logger.info(logMessage);
        } catch (Exception e) {
            logger.error("Erro ao clicar no elemento: {}", logMessage, e);
        }
    }

}