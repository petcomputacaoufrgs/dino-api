# DinoAPI
### O DinoAPI é uma RESTful API de serviços para o projeto `DinoApp`.
### O DinoApp é um aplicativo feito em parceria com o Hospítal de Clínicas de Porto Alegre que tem como objetivo incentivar as crianças do hospital a seguirem a rotina de tratamento médico.

## Spring
#### O Spring é um framework Java que torna o processo de desenvolvimento mais simples, rápido e seguro.
- Spring Data
- Spring Security
- Spring Web Service

## WebSocket
#### WebSocket é um protocolo de comunicação que cria canais de comunicação full-duplex sobre uma única conexão TCP.

## Google OAuth 2.0
Para autenticação é utilizada a API OAuth 2.0 do Google. Para mais detalhes acesse:
- https://developers.google.com/identity/protocols/oauth2
- https://console.developers.google.com/

## Configuração
### Para executar crie o arquivo "application.properties" em "resources" com o seguinte conteúdo:
```java
#Configurações do banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/{Nome do banco de dados}
spring.datasource.username={Seu usuário}
spring.datasource.password={Sua senha}

#Configurações para desenvolvimento
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update

#App 
app.origin= {Endereço onde está o DinoApp}

#Google
googleoauth2.clientid={Google Cloud Client Id}
googleoauth2.clientsecret={Google Cloud Client Secret}

#Port
server.port=${PORT:5000}
```
