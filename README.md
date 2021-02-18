# DinoAPI
#### O DinoAPI é uma RESTful API de serviços para o projeto `DinoApp`.
#### O DinoApp é um aplicativo feito em parceria com o Hospítal de Clínicas de Porto Alegre que tem como objetivo incentivar as crianças do hospital a seguirem a rotina de tratamento médico.

### Spring
#### O Spring é um framework Java que torna o processo de desenvolvimento mais simples, rápido e seguro.
- Spring Data
- Spring Security
- Spring Web Service

### WebSocket
#### WebSocket é um protocolo de comunicação que cria canais de comunicação full-duplex sobre uma única conexão TCP.

### Google OAuth 2.0
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

#Configurações opcionais de desenvolvimento
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update

#Configuração do cliente 
app.origin={Endereço onde está o DinoApp}

#Configurações do Google
googleoauth2.clientid={Google Cloud Client Id}
googleoauth2.clientsecret={Google Cloud Client Secret}

#Configuração de porta
server.port=${PORT:5000}
```
## Executar Projeto (Windows 10)
```java
1) Instale o Java JDK 11:
	1.1) Vá para https://www.oracle.com/java/technologies/javase-jdk11-downloads.html, selecione "Windows x64 Installer" e faça o donwload.
	1.2) Complete a instalação.
	1.3) Vá para o diretório da instalação. Exemplo: C:\Program Files\Java\jdk-11.0.10.
	1.4) Abra a pasta bin e copie o seu diretório. Exemplo: C:\Program Files\Java\jdk-11.0.10\bin.
	1.5) Vá para as variáveis de sistema, selecione "Path" e clique em "Editar".
	1.6) Adicione o diretório copiado (1.4).
	1.7) Verifique se há outro path relacionado ao Java, se sim remova-o.
	1.8) Salve as modificações.
	1.9) Para testar abra o terminal e execute "java -version" onde o retorno deverá ser 11.
2) Instale o Maven:
	2.1) Baixe o arquivo .zip com o Maven em https://maven.apache.org/download.cgi.
	2.2) Extraia os arquivos no diretório do sistema. Exemplo: OS(C:).
	2.3) Abra a pasta bin folder e copie o seu diretório. Exemplo: C:\maven\bin.
	2.4) Vá para as variáveis de sistema, selecione "Path" e clique em "Editar".
	2.5) Adicione o diretório copiado (2.3).
	2.6) Salve as modificações.
	2.7) Abra o terminal e execute o comando "mvn -v", a versão do Maven será retornada se a instalação foi bem sucedida.
3) Compile a aplicação
	3.1) Abra o diretório da aplicação no terminal.
	3.2) Execute "mvn install". Ao final do processo deverá ser exibida a mensagem "BUILD SUCCESS".
4) Execute
	4.1) Abra o diretório target e copie o nome do arquivo com a extensão ".jar". 
	4.2) Volte ao terminal e execute "java -jar target/{nome do arquivo}". Exemplo: "java -jar target/dinoapi-0.0.1-SNAPSHOT.jar".
```

