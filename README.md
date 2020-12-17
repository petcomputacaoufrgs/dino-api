# DinoAPI
### API de serviços para o projeto `DinoApp`

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
