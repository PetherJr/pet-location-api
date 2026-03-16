# Itau Challenge - Pet Location API

## Descrição do projeto
Esta é uma API robusta projetada para gerenciar e rastrear a localização de animais de estimação. O sistema recebe coordenadas geográficas (latitude e longitude) vinculadas a um ID de sensor, realiza a geocodificação reversa para obter um endereço legível e armazena o histórico de localização. Foi desenvolvida seguindo os princípios de **Arquitetura Limpa (Clean Architecture)** e **SOLID**.

## Tecnologias
- **Java 17** & **Spring Boot 3.4.3**
- **Spring Data JPA** (Persistência)
- **H2 Database** (Banco em memória)
- **SpringDoc OpenAPI (Swagger)** (Documentação de API)
- **Micrometer & Actuator** (Observabilidade e Métricas)
- **JaCoCo** (Code Coverage)
- **AspectJ (AOP)** (Logging Estruturado)
- **Maven** (Gestão de dependências)

## Instalação e Configuração
Para clonar este projeto usando Git:
```bash
git clone https://github.com/PetherJr/pet-location-api.git
```

## Pré-requisitos
- **Java JDK 17** ou superior
- **Apache Maven 3.8+**

## Compilação e Testes
Para compilar o projeto e rodar os testes:
```bash
mvn clean compile test
```

## Configuração (Variáveis de Ambiente)
Este projeto utiliza `.env` (ou variáveis de ambiente) para configuração. Crie um arquivo `.env` na raiz do projeto seguindo o formato:

```env
GEOCODING_API_KEY=Chave da API caso use positionstack
GEOCODING_PROVIDER=mock (padrão) ou positionstack
```

## Execução
```bash
mvn spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

## Documentação da API (Swagger)
Acesse a documentação interativa em:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Principais Endpoints:
- `POST /api/v1/pets/locations`: Recebe uma localização (latitude, longitude, sensorId).
- `GET /actuator/health`: Saúde do sistema.
- `GET /actuator/prometheus`: Métricas para monitoramento.

## Arquitetura
A aplicação segue os princípios da **Arquitetura Limpa**:
- **Domain**: Contém a lógica de negócio e modelos (Entities).
- **Application**: Orquestra os casos de uso (`ResolvePetLocationUseCase`) e define as portas de entrada/saída.
- **Infrastructure**: Contém os detalhes técnicos (Adaptadores de persistência, Clientes HTTP, Configurações).
- **Presentation**: Camada de entrada (REST Controllers e DTOs).

### Observabilidade e Qualidade
- **AOP**: Logs estruturados para cada execução de caso de uso com Correlation ID.
- **Metric**: Contagem de requisições e monitoramento de performance com Micrometer.
- **Problem Details (RFC 7807)**: Tratamento de erros padronizado.
- **Testes**: Suite de testes automatizados garantindo a integridade do fluxo principal.
