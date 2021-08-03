# ENCURTADOR DE URL (shortner-url)
API de cadastro e "encurtador" de URL

## PRÉ-REQUISITOS

* Ter instalado o plugin **lombok** na IDE onde será executada a aplicação.
* Ter acesso a internet.

## TÉCNOLOGIAS UTILIZADAS
* Java 11 (SpringBoot 2.5.3)
* MongoDB 2.5.3

## ACESSO AO MONGODB
Base da Aplicação: mongodb+srv://tdsAdmin:Tds2021@cluster0.gxekc.mongodb.net/desafioTDS

Base de Testes: mongodb+srv://tdsAdmin:Tds2021@cluster0.gxekc.mongodb.net/desafioTDStest

## ENDPOINTS (POSTMAN)

DEFAULT_PATH - http://localhost:8080/shortnerurl

### Create URL Data

POST - {{DEFAULT_PATH}}/api/url-data/create-url-data

Payload - <code>{
   "param": "string"
}</code>

### Get URL Data

POST - {{DEFAULT_PATH}}/api/url-data/get-url-data

Payload - <code>{
   "param": "string"
}</code>

### Execute Redirect

POST - {{DEFAULT_PATH}}/api/url-data/execute-redirect

Payload - <code>{
   "param": "string"
}</code>

### Get URL Stats

POST - {{DEFAULT_PATH}}/api/url-data/get-url-stats

Payload - <code>{
   "param": "string"
}</code>

## ACESSO AO SWAGGER

Para utilização da aplicação via *swagger* basta acessar o endereço http://localhost:8080/shortnerurl/swagger-ui/index.html e utilizar os serviços disponíveis

