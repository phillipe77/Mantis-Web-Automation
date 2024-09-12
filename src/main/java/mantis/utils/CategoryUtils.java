package mantis.utils;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;

import java.util.List;

public class CategoryUtils {
    public static void selectCategory(Select categoryDropdown, String category, Logger logger) {
        try {
            categoryDropdown.selectByVisibleText(category);
            logger.info("Categoria selecionada: {}", category);
        } catch (NoSuchElementException e) {
            logger.warn("Categoria '{}' não encontrada. Selecionando a primeira opção válida.", category);
            List<WebElement> options = categoryDropdown.getOptions();
            for (int i = 1; i < options.size(); i++) {
                if (!options.get(i).getText().trim().isEmpty()) {
                    categoryDropdown.selectByIndex(i);
                    logger.info("Selecionada a categoria: {}", options.get(i).getText());
                    return;
                }
            }
            throw new RuntimeException("Nenhuma opção de categoria válida disponível");
        }
    }
}