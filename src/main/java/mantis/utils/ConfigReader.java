package mantis.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            // Tenta carregar o arquivo de configuração localmente
            FileInputStream file = new FileInputStream("src/main/resources/config.properties");
            properties.load(file);
            file.close();
        } catch (IOException e) {
            System.err.println("Falha ao carregar o arquivo de configuração: " + e.getMessage());
            // Você pode optar por lançar a exceção aqui ou apenas logar o erro, dependendo da sua necessidade
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
