# EcoImpact API — Cidades ESG Inteligentes

Projeto desenvolvido como parte da atividade da fase **Navegando pelo Mundo DevOps**, com o objetivo de aplicar práticas de Integração Contínua, Entrega Contínua, containerização, automação de testes e deploy em múltiplos ambientes.

A aplicação consiste em uma API REST para simulação de impacto ambiental, permitindo registrar atividades e calcular uma estimativa de emissão de CO₂.

---

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- Spring Boot Actuator
- Maven
- JUnit
- Mockito
- H2 Database
- MySQL 8
- Docker
- Docker Compose
- Git
- GitHub
- GitHub Actions
- Railway

---

## Funcionalidades

A API permite:

- Criar simulações de impacto ambiental
- Listar simulações cadastradas
- Buscar simulação por ID
- Excluir simulações
- Calcular impacto estimado em kg de CO₂
- Persistir informações em banco de dados
- Consultar a saúde da aplicação por meio do Spring Boot Actuator

Exemplo de simulação:

```json
{
  "categoria": "TRANSPORTE",
  "atividade": "CARRO",
  "quantidade": 50
}
```

Exemplo de resposta:

```json
{
  "id": 1,
  "categoria": "TRANSPORTE",
  "atividade": "CARRO",
  "quantidade": 50.0,
  "impactoKgCo2": 15.5,
  "dataCriacao": "2026-10-06T04:25:56"
}
```

Os fatores utilizados nos cálculos são simplificados e foram definidos para fins acadêmicos e demonstrativos.

---

## Endpoints da API

### Listar todas as simulações

```http
GET /api/simulacoes
```

### Buscar simulação por ID

```http
GET /api/simulacoes/{id}
```

### Criar uma simulação

```http
POST /api/simulacoes
```

Exemplo de body:

```json
{
  "categoria": "TRANSPORTE",
  "atividade": "CARRO",
  "quantidade": 50
}
```

### Excluir uma simulação

```http
DELETE /api/simulacoes/{id}
```

### Health Check

```http
GET /actuator/health
```

Resposta esperada:

```json
{
  "status": "UP"
}
```

---

# Execução local

## Pré-requisitos

Para executar o projeto localmente é necessário possuir:

- Java 21
- Maven
- Docker
- Docker Compose
- Git

---

## Executar com Maven

Na raiz do projeto:

```bash
mvn clean test
```

Depois:

```bash
mvn spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

Health Check:

```text
http://localhost:8080/actuator/health
```

---

# Testes automatizados

O projeto possui testes automatizados utilizando JUnit e Mockito.

Atualmente são validados:

- Inicialização do contexto Spring
- Cálculo de impacto para carro
- Cálculo de impacto para ônibus
- Cálculo de impacto para energia elétrica

Para executar os testes:

```bash
mvn clean test
```

Resultado esperado:

```text
Tests run: 4
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Os testes também são executados automaticamente no pipeline de Integração Contínua.

---

# Docker

A aplicação possui um `Dockerfile` responsável pela criação da imagem Docker do projeto.

Foi utilizada uma estratégia de **multi-stage build**, separando a etapa de compilação da etapa de execução.

Fluxo simplificado:

```text
Código Java
    |
    v
Java 21 JDK
    |
    v
Maven
    |
    v
Build do projeto
    |
    v
Arquivo JAR
    |
    v
Java 21 JRE
    |
    v
Container da API
```

Para construir a imagem:

```bash
docker build -t ecoimpact-api:local .
```

Para executar o container:

```bash
docker run -d --name ecoimpact-api -p 8080:8080 ecoimpact-api:local
```

Para verificar os containers em execução:

```bash
docker ps
```

A aplicação poderá ser acessada em:

```text
http://localhost:8080
```

---

# Docker Compose

O projeto possui um arquivo:

```text
docker-compose.yml
```

Ele é responsável por orquestrar os containers necessários para a aplicação local.

A estrutura contém:

- EcoImpact API
- MySQL 8
- Volume persistente
- Rede Docker dedicada
- Variáveis de ambiente
- Health Check do banco de dados

Arquitetura local:

```text
Docker Compose
      |
      +-----------------------+
      |                       |
      v                       v
EcoImpact API              MySQL 8
Spring Boot                  |
      |                      v
      +--------------> Volume persistente
      |
      v
ecoimpact-network
```

Para subir os serviços:

```bash
docker compose up --build -d
```

Para consultar os containers:

```bash
docker compose ps
```

Para visualizar os logs:

```bash
docker compose logs
```

Para encerrar os containers:

```bash
docker compose down
```

O volume do MySQL permite que os dados sejam preservados mesmo após a remoção e recriação dos containers.

O teste de persistência foi realizado removendo os containers com:

```bash
docker compose down
```

e recriando posteriormente com:

```bash
docker compose up -d
```

Após a recriação, os registros armazenados continuaram disponíveis no banco.

---

# Variáveis de ambiente

O projeto utiliza variáveis de ambiente para evitar que informações sensíveis sejam armazenadas diretamente no código-fonte.

O arquivo:

```text
.env.example
```

serve como exemplo das variáveis necessárias.

O arquivo real:

```text
.env
```

não é versionado pelo Git.

As principais variáveis utilizadas pela aplicação são:

```text
DATABASE_URL
DATABASE_USER
DATABASE_PASSWORD
SPRING_PROFILES_ACTIVE
```

No Railway, as credenciais do banco MySQL são referenciadas pelas variáveis disponibilizadas pelo próprio serviço de banco.

---

# Banco de dados

## Ambiente local

Durante desenvolvimento e testes simples, a aplicação pode utilizar H2.

No ambiente Docker Compose é utilizado:

```text
MySQL 8
```

com armazenamento em volume persistente.

## Ambientes na nuvem

Os ambientes de staging e produção utilizam bancos MySQL separados no Railway.

Isso permite isolar os dados de homologação dos dados de produção.

---

# Pipeline CI/CD

O pipeline de Integração Contínua foi implementado utilizando **GitHub Actions**.

O arquivo responsável pelo workflow está localizado em:

```text
.github/workflows/ci.yml
```

O pipeline executa as seguintes etapas:

```text
Push / Pull Request
        |
        v
Checkout do código
        |
        v
Configuração do Java 21
        |
        v
Build Maven
        |
        v
Testes automatizados
        |
        v
Validação da imagem Docker
        |
        v
CI aprovado
```

O workflow é executado em:

```text
feature/**
develop
main
```

Também é executado em Pull Requests direcionados para:

```text
develop
main
```

---

# Estratégia de branches

O projeto utiliza uma estratégia de branches baseada no seguinte fluxo:

```text
feature/*
    |
    v
develop
    |
    v
STAGING
    |
    v
main
    |
    v
PRODUCTION
```

As novas alterações são desenvolvidas inicialmente em branches:

```text
feature/*
```

Depois são enviadas por Pull Request para:

```text
develop
```

Após os testes automatizados e validações, a branch `develop` é utilizada como origem para o ambiente de staging.

Depois da validação em staging, é criado um Pull Request:

```text
develop -> main
```

A branch `main` representa a versão de produção.

---

# Pull Requests

Durante o desenvolvimento foram utilizados Pull Requests para controlar a integração das alterações.

Foram utilizados fluxos como:

```text
feature/ci-pipeline
        |
        v
develop
```

e posteriormente:

```text
develop
   |
   v
main
```

Os Pull Requests foram integrados somente após a execução bem-sucedida dos checks do GitHub Actions.

---

# Integração Contínua

O GitHub Actions realiza automaticamente:

- Build do projeto
- Configuração do Java 21
- Execução dos testes automatizados
- Validação do Dockerfile
- Verificação das alterações antes dos merges

Isso reduz a possibilidade de uma alteração com erro chegar aos ambientes de staging ou produção.

---

# Entrega Contínua

O deploy foi integrado entre GitHub e Railway.

O Railway utiliza a funcionalidade:

```text
Wait for CI
```

Com isso, o deploy aguarda a conclusão bem-sucedida do GitHub Actions.

Fluxo de staging:

```text
Push / Merge em develop
        |
        v
GitHub Actions
        |
        v
Build + Testes
        |
        v
CI aprovado
        |
        v
Railway
        |
        v
STAGING
```

Fluxo de produção:

```text
Merge em main
        |
        v
GitHub Actions
        |
        v
Build + Testes
        |
        v
CI aprovado
        |
        v
Railway
        |
        v
PRODUCTION
```

---

# Ambiente de Staging

O ambiente de staging está conectado à branch:

```text
develop
```

Hospedagem:

```text
Railway
```

URL pública:

```text
https://ecoimpact-devops-staging.up.railway.app
```

Health Check:

```text
https://ecoimpact-devops-staging.up.railway.app/actuator/health
```

Endpoint da API:

```text
https://ecoimpact-devops-staging.up.railway.app/api/simulacoes
```

O ambiente foi validado com:

- Health Check retornando `UP`
- Requisição GET
- Requisição POST
- Persistência no MySQL
- Cálculo de impacto ambiental

---

# Ambiente de Produção

O ambiente de produção está conectado à branch:

```text
main
```

Hospedagem:

```text
Railway
```

URL pública:

```text
https://ecoimpact-devops-production.up.railway.app
```

Health Check:

```text
https://ecoimpact-devops-production.up.railway.app/actuator/health
```

Endpoint da API:

```text
https://ecoimpact-devops-production.up.railway.app/api/simulacoes
```

O ambiente de produção foi validado com:

- Health Check retornando `UP`
- Requisição GET
- Requisição POST
- Persistência no MySQL
- Cálculo de impacto ambiental

---

# Arquitetura da solução

A arquitetura geral do projeto pode ser representada da seguinte forma:

```text
                    GitHub
                       |
           +-----------+-----------+
           |                       |
           v                       v
        develop                   main
           |                       |
           v                       v
    GitHub Actions          GitHub Actions
           |                       |
           v                       v
      CI aprovado              CI aprovado
           |                       |
           v                       v
 Railway Staging         Railway Production
           |                       |
     +-----+-----+             +---+-----+
     |           |             |         |
     v           v             v         v
Spring Boot    MySQL       Spring Boot  MySQL
   API         Staging        API      Production
```

---

# Health Check

O Spring Boot Actuator foi utilizado para verificar a disponibilidade da aplicação.

Endpoint:

```http
GET /actuator/health
```

Resultado esperado:

```json
{
  "status": "UP"
}
```

Esse endpoint foi utilizado tanto localmente quanto nos ambientes de staging e produção.

---

# Evidências realizadas durante o projeto

Durante o desenvolvimento foram realizadas e registradas evidências de:

- Build Maven com sucesso
- Testes automatizados
- Spring Boot executando localmente
- Health Check local
- Criação da imagem Docker
- Execução da aplicação em container
- Docker Compose
- MySQL healthy
- Volume persistente
- Persistência de dados após recriação dos containers
- Git e controle de versão
- Branches `feature`, `develop` e `main`
- Pull Requests
- GitHub Actions
- CI executado com sucesso
- Deploy automático de staging
- Deploy automático de produção
- Railway
- MySQL em staging
- MySQL em produção
- Health Check em staging
- Health Check em produção
- POST em staging
- GET em staging
- POST em produção
- GET em produção

---

# Estrutura principal do projeto

```text
ecoimpact/
|
+-- .github/
|   +-- workflows/
|       +-- ci.yml
|
+-- .mvn/
|
+-- src/
|   +-- main/
|   |   +-- java/
|   |   |   +-- br/com/fiap/ecoimpact/
|   |   |       +-- controller/
|   |   |       +-- dto/
|   |   |       +-- model/
|   |   |       +-- repository/
|   |   |       +-- service/
|   |   |       +-- EcoimpactApplication.java
|   |   |
|   |   +-- resources/
|   |       +-- application.properties
|   |       +-- application-docker.properties
|   |
|   +-- test/
|       +-- java/
|           +-- br/com/fiap/ecoimpact/
|               +-- service/
|               |   +-- SimulacaoServiceTest.java
|               |
|               +-- EcoimpactApplicationTests.java
|
+-- .dockerignore
+-- .env.example
+-- .gitignore
+-- Dockerfile
+-- docker-compose.yml
+-- pom.xml
+-- mvnw
+-- mvnw.cmd
+-- README.md
```

---

# Repositório

Repositório GitHub:

```text
https://github.com/llucaslluz/ecoimpact-devops
```

---

# Autor

**Lucas da Luz Morais**

FIAP — Análise e Desenvolvimento de Sistemas

Projeto acadêmico desenvolvido para aplicação prática de conceitos de DevOps.

---

# Checklist da entrega

- [x] Projeto Java com Spring Boot
- [x] Código-fonte organizado
- [x] Maven
- [x] API REST
- [x] Spring Data JPA
- [x] Banco H2 para desenvolvimento/testes
- [x] MySQL
- [x] Testes automatizados
- [x] JUnit
- [x] Mockito
- [x] Spring Boot Actuator
- [x] Health Check
- [x] Dockerfile funcional
- [x] Multi-stage build
- [x] Docker Compose
- [x] Volume persistente
- [x] Variáveis de ambiente
- [x] Network Docker
- [x] `.env.example`
- [x] Git
- [x] GitHub
- [x] Branch `develop`
- [x] Branch `main`
- [x] Branches `feature/*`
- [x] Pull Requests
- [x] GitHub Actions
- [x] Integração Contínua
- [x] Build automático
- [x] Testes automáticos
- [x] Validação automática do Dockerfile
- [x] Deploy automático
- [x] Ambiente de staging
- [x] Ambiente de produção
- [x] MySQL separado por ambiente
- [x] URL pública de staging
- [x] URL pública de produção
- [x] Health Check em staging
- [x] Health Check em produção
- [x] API testada em staging
- [x] API testada em produção
- [x] README documentado