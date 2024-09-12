# 🚀 Mantis Web Automation

Projeto de testes automatizados para a aplicação web Mantis, desenvolvida em Java com Selenium e JUnit. O gerenciamento de dependências é feito com Maven, e os relatórios de testes são gerados utilizando o framework Allure.

## 🧪 Testes

Os testes automatizados incluem:

* **Login (Authentication)**: Verifica o processo de autenticação no sistema Mantis.
* **Criação de Tarefas (Issue Creation)**: Verifica a criação de novas tarefas no Mantis, garantindo que os campos obrigatórios (categoria, resumo e descrição) sejam fornecidos corretamente.
* **Adição de Comentários (Issue Comment)**: Valida a adição de comentários em uma tarefa existente e a verificação do comentário inserido.
* **Monitoramento de Tarefas (Issue Monitoring)**: Garante que uma tarefa pode ser monitorada e, posteriormente, desmonitorada.
* **Filtragem de Tarefas (Issue Filtering)**: Verifica a funcionalidade de filtragem de tarefas com base no estado e outros parâmetros configuráveis.
* **Pesquisa de Tarefas (Issue Search)**: Valida a pesquisa de tarefas por ID e outros atributos.
* **Adição de Marcadores (Add Tag)**: Verifica a funcionalidade de adição de marcadores a uma tarefa e valida a presença do marcador adicionado.

## 🏛️ Arquitetura de Testes

A arquitetura de testes segue o padrão de camadas de serviço, facilitando a escalabilidade e manutenção do projeto:

Camadas:
* **Core (DriverFactory)**: Contém a configuração e a criação do WebDriver, garantindo que o navegador seja inicializado corretamente.
* **Pages**: Classes que representam as páginas do sistema (Login, Dashboard, Issue). Cada página contém os métodos de interação com seus elementos.
* **Utils**: Utilitários de suporte, como:
  * **WaitUtils**: Gerencia as esperas explícitas durante a execução dos testes.
  * **ConfigReader**: Carrega as configurações do arquivo `config.properties` (como a URL base e as credenciais).
  * **InputUtils, CategoryUtils**: Funções auxiliares para manipulação de entradas e categorias.
* **Tests**: Conjunto de testes que utilizam as páginas e os utilitários para garantir modularidade e reuso de código.

### Tratamento de Limite de Atividades

O projeto tem implementado um mecanismo na Base Test para lidar com o limite de atividades na aplicação Mantis. Quando o limite é atingido, os testes são marcados como ignorados para evitar falhas falsas. Isso é feito através do método `handleLimitError()`:


## 🛠 Tecnologias Utilizadas

* **Java 17**: 
* **JUnit**: Framework de testes utilizado em conjunto com Selenium para a criação e execução de testes automatizados.
* **Selenium**: Biblioteca para automação de navegadores web.
* **Maven**: Gerenciamento de dependências e build do projeto.
* **Allure**: Framework utilizado para gerar relatórios de testes.

## 🧑‍💻 Execução do Projeto

### 🔧 Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:
* Java Development Kit (JDK) 17
* Apache Maven
* Allure Command Line 

### 🔧 Execução Local

1. Clone o repositório:

```bash
git clone https://github.com/phillipe77/Mantis-Web-Automation.git
cd Mantis-Web-Automation
```

2. Execute os testes com Maven:

```bash
mvn clean test
```

3. Gere o relatório Allure:

```bash
allure serve allure-results
```

## 🌟 Estrutura de Diretórios

* **src/main/java/mantis/core/DriverFactory**: Configura o WebDriver (navegador).
* **src/main/java/mantis/pages**: Contém as classes de páginas (LoginPage, IssuePage, DashboardPage).
* **src/main/java/mantis/utils**: Classes utilitárias como `WaitUtils`, `ConfigReader`, entre outras.
* **src/test/java/mantis/tests**: Contém os testes organizados em pacotes (`authentication` e `issue`).

## 📜 Licença

Este projeto está licenciado sob a **MIT License**. Confira o arquivo [LICENSE](LICENSE) para mais detalhes.
