# 📊 Monitor de Câmbio Inteligente - Java API & UX

Este projeto de desenvolvimento pessoal é uma ferramenta de monitoramento de câmbio em tempo real desenvolvida para demonstrar conceitos de **Integração de APIs**, **Persistência de Dados** e **Interface Gráfica (GUI)** em Java.

Originalmente concebido como um bot de RPA (Selenium), o projeto evoluiu para uma arquitetura baseada em API para garantir maior estabilidade, performance e escalabilidade, seguindo padrões de mercado financeiro.

## 🚀 Funcionalidades
* **Consulta em Tempo Real:** Integração com a AwesomeAPI para obter cotações atualizadas do par USD/BRL.
* **Indicador de Tendência (UX):** Algoritmo que compara o valor atual com o anterior, destacando em **verde (alta)** ou **vermelho (baixa)**.
* **Persistência de Estado (Memória):** O sistema lê o histórico ao iniciar para manter o contexto das últimas 24h, mesmo após o fechamento do app.
* **Log de Auditoria:** Gravação automática de todas as consultas em um arquivo `.txt` local.

## 🛠️ Tecnologias Utilizadas
* **Java 25:** Linguagem base com recursos modernos.
* **Maven:** Gestão de dependências e automação de build.
* **JSON (org.json):** Para parsing e manipulação dos dados recebidos da API.
* **Java Swing:** Para criação da interface gráfica interativa.
* **HTTPURLConnection:** Para comunicação assíncrona com servidores web.

## 🏗️ Arquitetura e Decisões Técnicas
### De RPA para API
Durante o desenvolvimento, optou-se por migrar de uma automação baseada em navegador (Selenium) para uma integração via API. Essa decisão foi tomada para:
1. **Eliminar fragilidades:** Sites mudam seletores frequentemente; APIs são contratos estáveis.
2. **Performance:** Chamadas via API consomem 90% menos memória e processamento que um navegador "Headless".
3. **Segurança:** Evita bloqueios por CAPTCHA ou autenticação de segurança do Google.

### Persistência de Dados
O sistema utiliza I/O (Input/Output) para ler e escrever em arquivos. Ao iniciar, o método `carregarUltimoPrecoDoArquivo()` garante que o software tenha "memória de longo prazo", uma característica essencial para sistemas de monitoramento financeiro.

## 📸 Como rodar o projeto
1. Certifique-se de ter o **Java 17+** e o **Maven** instalados.
2. Clone o repositório.
3. No IntelliJ, aguarde o Maven baixar as dependências (`pom.xml`).
4. Execute a classe `Main.java`.