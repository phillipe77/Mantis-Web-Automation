# Usando Maven com Java 17
FROM maven:3.8.1-openjdk-17-slim
LABEL authors="Phillipe Linhares"

# Instalando o Chrome e outras dependências necessárias
RUN apt-get update && apt-get install -y wget gnupg2 curl unzip --no-install-recommends \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update && apt-get -y install google-chrome-stable \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Instalando ChromeDriver
RUN wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/112.0.5615.49/chromedriver_linux64.zip \
    && unzip /tmp/chromedriver.zip -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip \
    && chmod +x /usr/local/bin/chromedriver

# Definindo o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos do projeto para o contêiner
COPY . .

# Permissões
RUN chmod -R 777 /app

# Definindo o PATH para o Chrome e o ChromeDriver
ENV PATH="/usr/local/bin:/usr/bin/google-chrome:${PATH}"

# Comando para executar o Maven (ajuste conforme necessário)
# Exemplo: RUN mvn clean test
