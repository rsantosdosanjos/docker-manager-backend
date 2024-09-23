
# Docker Manager Backend

Esta aplicação gerencia containers e imagens Docker através de uma API RESTful, permitindo operações como listar, criar, iniciar, parar e deletar containers e imagens Docker. A aplicação foi desenvolvida utilizando Java, Spring Boot e a Docker Java API.

## Endpoints Disponíveis

### Containers

- **GET /api/containers?showAll={true|false}**: Lista todos os containers (padrão é mostrar apenas os ativos).
- **POST /api/containers/{id}/start**: Inicia um container especificado pelo ID.
- **POST /api/containers/{id}/stop**: Para um container especificado pelo ID.
- **DELETE /api/containers/{id}**: Deleta um container especificado pelo ID.
- **POST /api/containers?imageName={nome_da_imagem}**: Cria um novo container a partir de uma imagem especificada.

### Imagens

- **GET /api/images**: Lista todas as imagens disponíveis.
- **GET /api/images/filter?filterName={nome_do_filtro}**: Filtra as imagens pelo nome especificado.

## Dependências

- **Java 17** ou superior
- **Spring Boot 3.3.3**
- **Docker Java API**: Para interação com a API Docker.
- **Apache HttpClient 5**: Para gerenciar as conexões HTTP ao Docker.
- **Docker**: Certifique-se de que o Docker está ativo no seu ambiente.

## Como Executar a Aplicação

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/resantosdosanjos/docker-manager-backend.git
   cd docker-manager-backend
   ```

2. **Configuração do Docker**:
   No arquivo de configuração, defina o caminho do socket Docker, ou se preferir, utilize a comunicação via TCP. Adicione o seguinte ao `application.properties`:
   ```properties
   docker.socket.path=unix:///var/run/docker.sock
   ```

   Se você estiver utilizando um caminho diferente ou acessando o Docker remotamente, ajuste o valor conforme necessário.

3. **Compilar e executar**:
   Certifique-se de que o Maven está instalado e execute os seguintes comandos:
   ```bash
   ./mvnw clean install
   java -jar target/docker-manager-backend-1.0.jar
   ```

4. **Acessar a API**:
   A aplicação estará disponível na porta padrão `8080`. Para verificar os containers, você pode fazer um request HTTP:
   ```bash
   curl http://localhost:8080/api/containers
   ```

5. **Testar a Aplicação**:
   Utilize ferramentas como **Postman**, **Insominia**, **Apidog** ou **cURL** para interagir com os endpoints mencionados acima.

## Exemplos de Uso

- **Listar Containers**:
   ```bash
   curl -X GET "http://localhost:8080/api/containers?showAll=true"
   ```

- **Criar Container**:
   ```bash
   curl -X POST "http://localhost:8080/api/containers?imageName=nginx"
   ```

- **Iniciar Container**:
   ```bash
   curl -X POST "http://localhost:8080/api/containers/{id}/start"
   ```

- **Parar Container**:
   ```bash
   curl -X POST "http://localhost:8080/api/containers/{id}/stop"
   ```

- **Deletar Container**:
   ```bash
   curl -X DELETE "http://localhost:8080/api/containers/{id}"
   ```

- **Listar Imagens**:
   ```bash
   curl -X GET "http://localhost:8080/api/images"
   ```

- **Filtrar Imagens**:
   ```bash
   curl -X GET "http://localhost:8080/api/images/filter?filterName=nginx"
   ```

## Problemas Comuns

1. **Erro de socket**: Certifique-se de que o Docker está rodando e o arquivo `/var/run/docker.sock` está acessível para o usuário que está executando a aplicação.
2. **Permissões**: Caso esteja enfrentando problemas de permissão, adicione seu usuário ao grupo `docker`:
   ```bash
   sudo usermod -aG docker $USER
   ```

3. **Configuração Remota**: Se precisar acessar o Docker remotamente, verifique se o Docker está configurado para aceitar conexões TCP.

## Arquitetura da Aplicação

A aplicação segue a arquitetura de camadas:
- **Configuração (DockerClientConfig)**: Configura o cliente Docker para se comunicar via Unix socket ou TCP.
- **Serviços (DockerService)**: Contém a lógica de negócios para interagir com containers e imagens Docker.
- **Controladores (DockerContainersController, DockerImagesController)**: Exponha endpoints REST para gerenciamento de containers e imagens.

## Testes Unitários

Os testes unitários para a aplicação foram implementados utilizando o **JUnit 5** e o **Mockito** para mockar as dependências do `DockerClient`. Eles garantem que os serviços e controladores estão funcionando corretamente, sem a necessidade de uma instância real do Docker. Abaixo estão detalhes sobre como os testes foram organizados:

### Teste Unitário para o `DockerService`

Os testes para o `DockerService` verificam as funcionalidades principais relacionadas à interação com o Docker. Para mockar o comportamento do `DockerClient` e seus comandos (`listContainersCmd`, `listImagesCmd`), utilizamos o Mockito para simular o comportamento da API.

- **Testes incluídos**:
    - `testListContainers()`: Verifica se a listagem de containers está funcionando corretamente, configurando mocks para simular a resposta do Docker.
    - `testListImages()`: Verifica se a listagem de imagens está retornando corretamente as informações mockadas.

#### Exemplo do Teste para Listagem de Containers

```java
@Test
void testListContainers() {
    ListContainersCmd listContainersCmd = Mockito.mock(ListContainersCmd.class);
    Container container = new Container();
    List<Container> mockContainers = Arrays.asList(container);

    // Configura o mock para o comando
    when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
    when(listContainersCmd.withShowAll(true)).thenReturn(listContainersCmd);
    when(listContainersCmd.exec()).thenReturn(mockContainers);

    // Chama o método e verifica o resultado
    List<Container> containers = dockerService.listContainers(true);
    assertEquals(1, containers.size());
}
```

#### Explicação

- O método `listContainersCmd()` do `DockerClient` é mockado para retornar um comando simulado (`ListContainersCmd`).
- Em seguida, configuramos o comando para retornar uma lista simulada de containers quando o método `exec()` for chamado.
- Finalmente, verificamos se o serviço `dockerService.listContainers(true)` está retornando a lista mockada corretamente.

### Teste Unitário para o `DockerContainersController`

Os testes para o `DockerContainersController` verificam o comportamento dos endpoints relacionados ao gerenciamento de containers. Novamente, o comportamento do `DockerService` é mockado para simular interações sem o uso real de containers Docker.

- **Testes incluídos**:
    - `testListContainers()`: Verifica a listagem de containers a partir do endpoint `/api/containers`.
    - `testStartContainer()`: Verifica se o endpoint para iniciar containers está chamando o serviço corretamente.
    - `testStopContainer()`: Verifica se o endpoint para parar containers está funcionando.

#### Exemplo do Teste para Iniciar Containers

```java
@Test
void testStartContainer() {
    dockerContainersController.startContainer("12345");
    Mockito.verify(dockerService).startContainer("12345");
}
```

#### Explicação

- O método `startContainer()` do controlador chama o serviço `dockerService.startContainer("12345")`.
- Usamos `Mockito.verify()` para garantir que o serviço foi chamado com o ID correto do container.

### Teste Unitário para o `DockerImagesController`

Os testes para o `DockerImagesController` focam nos endpoints que listam e filtram imagens Docker. Assim como nos testes anteriores, o comportamento do `DockerService` é mockado.

- **Testes incluídos**:
    - `testListImages()`: Verifica se a listagem de imagens está funcionando corretamente.
    - `testFilterImages()`: Verifica se o filtro de imagens retorna os resultados corretos com base no filtro fornecido.

#### Exemplo do Teste para Filtrar Imagens

```java
@Test
void testFilterImages() {
    Image image = new Image();
    List<Image> mockImages = Collections.singletonList(image);

    Mockito.when(dockerService.filterImages("nginx")).thenReturn(mockImages);

    List<Image> images = dockerImagesController.listImages("nginx");
    assertEquals(1, images.size());
}
```

#### Explicação

- O método `filterImages("nginx")` no serviço é mockado para retornar uma lista de imagens filtradas.
- O controlador chama o serviço com o filtro especificado, e verificamos se o retorno está correto.

### Executando os Testes

Para executar os testes unitários, use o Maven:

```bash
./mvnw test
```

Isso executará todos os testes definidos para os serviços e controladores, garantindo que a lógica da aplicação está funcionando como esperado.