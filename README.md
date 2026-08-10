
# TEAZ

Teaz is the system running the Teaz Discord bot, the [teaz.fun](https://teaz.fun) website and more.

-----

### NEXUS
![](./nexus/logo.png)

##### Features:
- Discord bot
  - AI chatbot
  - Online radio streaming
  - Minigames
  - Twitch integration
- Website Backend
- __More features coming soon...__

##### Technologies:
- Java
- Spring Boot
- SQLite
- JDA
- Lavalink
- Twitch4J

-----

### PRISM
![](./prism/logo.png)

##### Features:
- Web access to the most important features of the Discord bot
- Exclusive minigames
- __More features coming soon...__

##### Technologies:
- JavaScript
- Vue
- GSAP
- ESLint

-----

## Working on Teaz
##### If you want to help out developing Teaz, check out this setup guide:
> Note: I will not be accepting pull requests from people who have not been in contact with me before.

<br>

### 1. Get in contact with me
`DISCORD:` [Discord Server](https://discord.gg/HrqGzbd)

`EMAIL:` eddie@sinistra.de

<br>

### 2. Clone the repository
```bash
git clone https://github.com/teeetime/cup 
```

<br>

### 3. Create Springboot Config
> Important: The Nexus application will not run without `application-dev.properties`.  
> This configuration file contains sensitive keys and passphrases.  
> **Do not share your config with others!!!**  

##### Create file
```bash
cd nexus/src/main/resources
```
Create file called `application-dev.properties`  

##### Paste template into file
```properties
server.port=8080

app.base-url=https://localhost
app.jwt.secret=

spring.security.oauth2.client.registration.discord.client-id=
spring.security.oauth2.client.registration.discord.client-secret=

discord.bot.token=
discord.bot.id=
discord.bot.prefix=.
discord.bot.admin-id=
discord.bot.chatgpt-token=

twitch.clientId=
twitch.clientSecret=
twitch.channelName=
twitch.discord.notificationChannelId=
```

From here on, you need to ask teeetime for help. Better tutorial coming soon.

> Note: Make sure your IDE starts the Springboot application with the `dev` profile.

<br>

### 4. Install the Node.js project
```bash
cd prism
npm install
```

<br>

### 5. Get Lavalink
Follow the instructions in the [Lavalink installation guide](lavalink/README.md).

<br>

### 6. Try to start all applications
##### Nexus
Start the Springboot application with the `dev` profile.  
Make sure to use Java 25. If you don't have a Java 25 JDK, you can obtain it here: [Temurin JDK](https://adoptium.net/de/temurin/releases).

##### Prism
```bash
cd prism
npm run dev
```

##### Lavalink
```bash
cd lavalink
./start.bat
```
<br><br>

> This guide is incomplete, contact teeetime for help.