# Stackoverflow Clone

Este é um projeto simples usando Spring Boot, criado para fins educacionais.

[![Build Status](https://travis-ci.org/ValeryKorzhavin/stackoverflow.svg?branch=master)](https://travis-ci.org/ValeryKorzhavin/stackoverflow)
[![Maintainability](https://api.codeclimate.com/v1/badges/585b28b85a4fd5d79713/maintainability)](https://codeclimate.com/github/ValeryKorzhavin/stackoverflow/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/585b28b85a4fd5d79713/test_coverage)](https://codeclimate.com/github/ValeryKorzhavin/stackoverflow/test_coverage)

## Sobre
Este projeto foi criado com o objetivo de ser educacional.

Veja a demonstração: [Stackoverflow Clone](https://springdemo-valerykorzh.herokuapp.com/)

## Tecnologias Utilizadas
- Java 12
- Spring Boot 2
- Spring Data JPA
- Spring Security
- Hibernate
- PostgreSQL
- Liquibase (para migrações)
- JUnit para testes
- Thymeleaf como motor de templates
- Project Lombok
- Mapstruct
- Bootstrap 4
- JQuery
- Construído com Maven

## Requisitos
- Java 12

## Compilar e Executar
```shell script
$ make # build & run
$ make test # compilar & executar testes
```

## Migrações de Banco de Dados
Liquibase é utilizado para criar e atualizar o esquema do banco de dados. Os esquemas estão localizados em /src/main/resources/db/changelog.

Execute este comando para gerar uma nova migração:
```shell script
$ make generate-migration
```

## Funcionalidades
- Criar/Atualizar Contas/Perguntas/Respostas
- Fazer upload de avatar
- Votar em Perguntas/Respostas
- Ordenar Perguntas/Contas/Tags por votos, nome, data de criação