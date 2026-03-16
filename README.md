# Itau Challenge - Pet Location API

## Descrição do projeto
Esta é uma API robusta projetada para gerenciar e rastrear a localização de animais de estimação. O sistema recebe coordenadas geográficas (latitude e longitude), realiza a geocodificação reversa para obter um endereço legível e armazena o histórico de localização do pet. Foi desenvolvida com foco extremo em **Qualidade de Código**, **Arquitetura Limpa** e **Extensibilidade**.

## Tecnologias
- **Java 17** & **Spring Boot 4.0.3**
- **Spring Data JPA** (Persistência)
- **H2 Database** (Banco em memória para testes/demo)
- **SpringDoc OpenAPI (Swagger)** (Documentação de API)
- **Micrometer & Actuator** (Observabilidade e Métricas de Negócio)
- **JaCoCo** (Code Coverage)
- **AspectJ (AOP)** (Logging Estruturado e Auditoria)
- **Maven** (Gestão de dependências)
- **spring-dotenv** (Gestão segura de variáveis de ambiente)

## Instalação e Configuração
Para clonar este projeto usando Git:
```bash
git clone https://github.com/petherson/pet-location-api.git
```
Ou baixe o arquivo ZIP do repositório e extraia-o em sua máquina.

## Pré Requisitos
Para rodar este projeto, você precisará ter instalado em sua máquina:
- **Java JDK 17** ou superior
- **Apache Maven 3.8+**
- Um cliente para testes de API (Postman, Insomnia ou cURL)
- Uma chave de API do **PositionStack** (opcional, para teste real)

## Compilação
Para compilar o projeto e baixar todas as dependências:
```bash
cd pet-location-api
mvn clean compile
```

## Configuração das variáveis de ambiente
Este projeto utiliza um arquivo `.env` para proteger segredos e configurar o ambiente.
1. Na raiz do projeto, crie um arquivo chamado `.env` (o arquivo já foi criado para você neste ambiente).
2. Configure as seguintes variáveis:
```env
# Define o provedor de geocodificação: 'mock' (padrão) ou 'positionstack'
GEOCODING_PROVIDER=mock
# Sua chave de API do PositionStack (necessária se o provider for 'positionstack')
GEOCODING_API_KEY=21e714075c4ea05682406a43a851075d
```

## Execução da aplicação
Para executar a aplicação, rode os comandos:
```bash
mvn spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

## Documentação da API (Swagger)
Este projeto utiliza a especificação OpenAPI para documentação e catálogo de APIs. Depois de executar a aplicação, é possível consultar todas as operações fornecidas pelo serviço e testá-las respectivamente acessando o endereço:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Principais Endpoints:
- `POST /api/pets/{id}/location`: Atualiza a localização de um pet.
- `GET /actuator/health`: Verifica a saúde do sistema.
- `GET /actuator/prometheus`: Métricas brutas para monitoramento.

## Solução
Para atender da melhor forma os requisitos de escalabilidade e manutenibilidade, apliquei os seguintes padrões e princípios:

### Clean Architecture & SOLID
A aplicação é dividida em camadas que isolam a regra de negócio da infraestrutura:
- **Domain**: Núcleo do sistema com Entidades e Value Objects (Coordinates) validados.
- **Application**: Casos de uso desacoplados via Ports.
- **Infrastructure**: Implementações detalhadas (JPA, HTTP Clients).

### Design Patterns Utilizados
- **Strategy Pattern (Geocoding)**: Implementei múltiplas estratégias de geocodificação (`Mock` e `PositionStack`). Isso permite trocar o provedor de mapas sem alterar uma única linha do serviço de Pet.
- **Factory (Spring Managed)**: Utilizei o mecanismo de `@ConditionalOnProperty` do Spring para atuar como uma Factory estática em tempo de carregamento, instanciando o adaptador correto baseado no arquivo `.env`.
- **Observer/Domain Events**: O domínio dispara eventos (`PetLocationUpdatedEvent`) quando uma localização muda, permitindo que outros módulos (como um futuro sistema de notificação) reajam de forma assíncrona.
- **Adapter Pattern**: Fundamental para integrar o sistema com o Banco de Dados JPA e APIs externas de forma plugável.

### Observabilidade e Qualidade
- **AOP (Aspect Oriented Programming)**: Usei aspectos para interceptar chamadas e gerar logs de performance sem poluir a lógica de negócio.
- **RFC 7807 (Problem Details)**: Erros da API seguem um padrão rico e semântico.
- **Testes**: Cobertura completa com testes de unidade (Mocks) e integração (Application Context).
