# ws-project-plus: API robusta para gerenciamento do projeto Rasmoo Plus

Bem-vindo ao **ws-project-plus**, uma API REST desenvolvida para centralizar e facilitar o front do projeto Rasmoo Plus, projeto inspirado no layout da Netflix.

---

## Descrição

O **ws-project-plus** é uma aplicação backend que oferece endpoints para cadastro de tipos de assinaturas, gerenciamento de usuários, pagamentos da assinatura, controle de permissões e integrações com ferramentas populares do ecossistema de desenvolvimento.

### Tecnologias Utilizadas

#### ☕ Linguagem e Framework Principal
- Java 21: Linguagem principal utilizada no projeto.
- Spring Boot 3.4.3: Framework que facilita a criação de aplicações Java com configuração mínima.

#### 📦 Dependências Spring
- spring-boot-starter-web: Para criação de APIs RESTful.
- spring-boot-starter-data-jpa: Integração com JPA (Hibernate) para acesso a bancos de dados relacionais.
- spring-boot-starter-data-redis: Para integração com o banco de dados Redis (cache ou mensageria).
- spring-boot-starter-hateoas: Para implementar princípios HATEOAS em APIs REST.
- spring-boot-starter-validation: Validações automáticas com Bean Validation (JSR-380).
- spring-boot-starter-security: Para autenticação e autorização.
- spring-boot-starter-mail (v3.4.4): Envio de e-mails via SMTP.

#### 🛡️ Segurança e Autenticação
- Java JWT: Biblioteca para geração e verificação de tokens JWT.

#### 📚 Documentação
- OpenAPI: Geração automática da documentação da API via Swagger UI.

#### 🧪 Testes
- JUnit, Mockito: Conjunto de dependências para testes.
- H2 Database: Banco de dados em memória para testes.

#### 🛠️ Ferramentas de Desenvolvimento
- Devtools: Facilita o desenvolvimento com auto-reload.
- Lombok: Reduz boilerplate com anotações (@Getter, @Setter, etc.).
- **Docker** para gerenciamento do ambiente e deploy simplificado

#### 🐬 Banco de Dados
- MySQL: como banco de dados relacional principal
- Flyway: Ferramenta de versionamento e migração de banco de dados com suporte ao MySQL.
- MongoDB: como banco de dados NoSQL


---

## Como instalar e executar o projeto

Siga estes passos para configurar e colocar o ambiente de desenvolvimento em execução:

1. **Instale o docker e o docker-compose se você não os tiver**

2. **Clone o repositório**
   ```bash
   git clone https://github.com/silluana/ws-project-plus.git
   cd ws-project-plus
   ```

3. **Suba o banco de dados com Docker**
   ```bash
   docker-compose up -d
   ```

4. **Verifique se o MySQL e o Redis estão ativos**
   ```bash
   docker ps
   ```

5. **Inicie o ws-project-plus executando a classe principal WsProjectPlusApplication**

Consulte a rota disponível em ` http://localhost:8082/ws-project-plus/swagger-ui/index.html` após rodar o servidor para ver todos os endpoints disponíveis..

---

## Créditos

Projeto desenvolvido a partir das aulas do curso Spring Rest - Construindo API's Poderosas! [Rasmoo](https://github.com/Rasmoo).

---

## Licença

Este projeto está licenciado sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais informações.
