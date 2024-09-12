package mantis.utils;

public class UrlUtils {

    private static final String BASE_URL = "https://mantis-prova.base2.com.br/view.php?id=";

    public static String buildIssueUrl(String issueId) {
        return BASE_URL + issueId;
    }
}