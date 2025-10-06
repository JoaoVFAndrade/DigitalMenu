# DigitalMenu

DigitalMenu é um sistema de gerenciamento de pedidos para restaurantes, construído com **Spring Boot**, **MySQL** e **Docker**.  
O projeto permite cadastrar, atualizar e listar pedidos, clientes e produtos de forma prática e moderna.

---

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Docker](https://www.docker.com/get-started)

---

## ▶️ Rodando o projeto com Docker

### - Subir os containers juntos (MySQL + aplicação)  
```bash
  docker-compose up --build 
  ```

### - Caso nenhuma modificação tenha sido feita no Dockerfile, você pode usar apenas:
```bash
  docker-compose up
  ```

### - Subir containers de forma separada
```bash
  docker-compose up -d digitalmenu-db
  ```

```bash
  docker-compose up -d digitalmenu-app
  ```

### - Listar containers em execução
```bash
  docker ps
 ```

### - Parar e remover containers, redes e volumes criados pelo compose
```bash
  docker-compose down
 ```