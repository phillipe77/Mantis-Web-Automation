package mantis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Desculpe, não foi possível encontrar config.properties");
            } else {
                // Carrega o arquivo de configuração
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Falha ao carregar o arquivo de configuração: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        // Verifica primeiro se a variável de ambiente existe
        String envValue = System.getenv(key);
        if (envValue != null) {
            return envValue; // Retorna a variável de ambiente se encontrada
        }

        // Caso contrário, retorna o valor do arquivo .properties
        return properties.getProperty(key);
    }
}
