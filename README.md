# IT Boarding Bootcamp - Java - Gabriel Tavares

### Dependências 

Gradle 7.0.2 </br>
Java 11 </br>
MySQL

### Requisistos mínimos
- Java 11
- Gradle
- MySQL

### Swagger 
- Visualização de todas request e reponses http://localhost:8080/swagger-ui.html

# Requisito 6

- O arquivo do Requisto 06 pode ser encontrado seguindo o caminho src/main/resources/Requisito_Dia_6_PT-BR  

### Subindo a aplicação
- Para subirmos precisamos de alguns passos, primeiro devemos executar um comando Docker para instanciarmos um banco de dados mysql na nossa máquina

`docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=finalproject -d mysql:8.0.19`

- Após esse passo precisamos executar a classe Aplication do projeto com as seguintes configurações a variável de ambiente `APPLICATION=LOCAL` e o Active Profile `local`

### Spring Security
Para realizar request é necessário solicitar um token do tipo Bearer, para gerar o token é necessário ser autenticado no
endpoint `http://localhost:8080/api/v1/auth/login` com o método POST. Esse token deve ser adicionado no header com o paramêtro `Authorization: Bearer $token Os usuários cadastrados são o buyer@email.com, seller@email.com ou representative@email.com Todos esses usuários tem a senha padrão: 1234 e devem entrar no body no formato 
{ "email": email@email.com ,"password": password } 

### Web Server

Each Spring Boot web application includes an embedded web server. For servlet stack applications, Its supports three web Servers:
  * Tomcat (`spring-boot-starter-tomcat`)
  * Jetty (`spring-boot-starter-jetty`)
  * Undertow (`spring-boot-starter-undertow`)

This project is configured with Jetty, but to exchange WebServer, it is enough to configure the dependencies mentioned above in the build.gradle file.

