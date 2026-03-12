# Pet Location API

Este projeto consiste em uma API desenvolvida para auxiliar na localização de animais de estimação perdidos, especificamente voltada para sistemas de sinistro de seguro pet. A aplicação recebe dados de geolocalização enviados por dispositivos acoplados à coleira dos animais e resolve esses dados em endereços compreensíveis por humanos.

## Objetivo do Projeto

A finalidade principal é receber informações brutas de sensores (identificação do sensor, latitude, longitude e timestamp) e transformar esses dados em uma localização completa, incluindo país, estado, cidade, bairro e logradouro. Para isso, a API integra-se ao serviço PositionStack, realizando o processo de geocodificação reversa.

## Tecnologias Utilizadas

A solução foi construída utilizando ferramentas modernas e amplamente adotadas no ecossistema Java:

- Java 17: Versão de suporte de longo prazo utilizada para aproveitar as melhorias de linguagem e performance.
- Spring Boot 3.5.11: Framework base para a construção da API, facilitando a configuração e o desenvolvimento.
- Spring Data JPA: Abstração para persistência de dados.
- H2 Database: Banco de dados em memória utilizado para facilitar a execução imediata do desafio sem dependências externas de infraestrutura.
- OpenAPI e Swagger UI: Para documentação interativa e testes manuais dos endpoints.
- JUnit 5, Mockito e WireMock: Conjunto de ferramentas para garantir a qualidade do código através de testes unitários e de integração.
- Micrometer e Spring Boot Actuator: Implementação de observabilidade para métricas e monitoramento de saúde da aplicação.
- JaCoCo: Plugin para análise e relatório de cobertura de testes.

## Arquitetura e Decisões de Design

A aplicação segue os princípios da Clean Architecture (Arquitetura Limpa), organizada nas seguintes camadas:

- Domain: Contém as entidades de negócio e modelos fundamentais, livre de dependências de frameworks externos.
- Application: Define as regras de negócio através de Use Cases e as portas de entrada e saída (Ports).
- Infrastructure: Implementa as adaptações para tecnologias específicas, como o cliente HTTP para integração externa e os repositórios de banco de dados.
- Presentation: Camada de interface com o usuário (REST Controllers) e gestão de DTOs.

Estas escolhas garantem que o sistema seja fácil de testar, manter e evoluir, permitindo, por exemplo, a troca do provedor de geolocalização com impacto mínimo no núcleo do negócio.

## Como Executar a Aplicação

### Pré-requisitos

Para rodar este projeto, você precisará ter instalado em sua máquina:
- Java Development Kit (JDK) versão 17 ou superior.
- Maven (opcional, pois o projeto inclui o Maven Wrapper).

### Configuração do Ambiente

O projeto exige uma chave de API do PositionStack para funcionar corretamente. Você deve configurar essa chave como uma variável de ambiente:

No Linux ou macOS:
export POSITIONSTACK_API_KEY=sua_chave_aqui

No Windows (PowerShell):
$env:POSITIONSTACK_API_KEY="sua_chave_aqui"

### Execução

Com o ambiente configurado, navegue até a raiz do projeto e execute o seguinte comando:
./mvnw spring-boot:run

A aplicação iniciará por padrão na porta 8080.

## Como Testar

### Documentação Interativa (Swagger)

Uma vez que a aplicação esteja rodando, você pode acessar a interface do Swagger para explorar e testar os endpoints:
http://localhost:8080/swagger-ui.html

### Teste Manual via Terminal

Você também pode enviar uma requisição POST utilizando o curl:

curl -X POST http://localhost:8080/api/v1/pets/locations \
-H "Content-Type: application/json" \
-d '{
  "sensorId": "SEN-123",
  "latitude": -23.561684,
  "longitude": -46.656139,
  "timestamp": "2026-03-11T10:15:30Z"
}'

### Testes Automatizados

Para executar a suíte completa de testes (unitários e de integração) e gerar o relatório de cobertura, utilize:
./mvnw clean verify

O relatório detalhado de cobertura gerado pelo JaCoCo poderá ser visualizado abrindo o arquivo:
target/site/jacoco/index.html

## Observabilidade e Diagnóstico

A aplicação foi projetada com foco em monitoramento:

- Saúde do Sistema: Disponível em /actuator/health.
- Métricas: O endpoint /actuator/metrics expõe dados sobre latência de chamadas ao provedor externo, contagem de erros e total de requisições processadas.
- Rastreabilidade: Todas as requisições recebem um X-Correlation-Id. Este ID é propagado nos logs estruturados, permitindo rastrear o fluxo completo de uma transação, desde a chegada na API até a interação com serviços externos.

## Resiliência e Tratamento de Erros

A API implementa estratégias para lidar com falhas de forma elegante:

- Timeouts: O cliente HTTP está configurado com limites de tempo (5 segundos) para evitar que lentidões no provedor de geocode travem a aplicação.
- Padronização de Erros: Falhas são reportadas seguindo o padrão RFC 7807 (Problem Details), fornecendo respostas claras e estruturadas em caso de erros de validação (400), falhas no provedor (502) ou timeouts de rede (504).
