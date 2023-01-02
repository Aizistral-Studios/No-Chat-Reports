# No Chat Reports

<a href="https://www.curseforge.com/minecraft/mc-mods/no-chat-reports"><img alt="curseforge" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/available/curseforge_vector.svg"></a> <a href="https://modrinth.com/mod/no-chat-reports"><img alt="modrinth" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/available/modrinth_vector.svg"></a> <a href="https://github.com/Aizistral-Studios/No-Chat-Reports"><img alt="github" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/available/github_vector.svg"></a> <a href="https://gitlab.com/Aizistral-Studios/No-Chat-Reports"><img alt="gitlab" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/available/gitlab_vector.svg"></a>

This mod strips cryptographic signatures that since 1.19 are attached to every message sent in the chat. Removing them makes it impossible to track and associate your chat messages with your Minecraft and, by extension, Microsoft account.

**As of 1.19.1, it also disables Player Chat Reporting.**

Please notice that, while I am aware of [Guardian](https://github.com/nodusclient/guardian) and certain other exploits that allow you to counteract reporting system on client against the will of the server, I am consciously choosing to not integrate them as part of this mod. No Chat Reports will remain effective on client side only if given server allows it to be. If you do not like servers that enforce chat signing and fully support chat reporting - I advise to simply not play on them.

Also, while there are many plugins out there that "borrow" the name and even icon of my mod, please be aware that **I have not authored any of them** and cannot say how well they do their job.
 
## Installation and Use:

This mod supports both Forge and Fabric, just download the version for respective modloader. It can be installed on either client, server, or both sides, and will function differently depending on which sides it is present at:

**1. Only Client:** Client will refuse to send account's public key to the server, and signatures will be stripped from the messages that you send. This way it won't be useful to try and report your messages, as there will be no proof they were actually sent from your account. Server will relay them unless `enforce-secure-profile` option is set to true in server options (which it is by default since 1.19.1), in which case you will not be able to join, unless you agree to send signed messages (the mod will supply warning screen);

**2. Only Server:** Clients will still attach signatures when sending messages to the server, but server will strip them before relaying them to other players. This way chat reporting will not work for any players that join. You can enable player to system message conversion in config, to prevent players without the mod from seeing them as "Not Secure"

**3. Both Client and Server:** Signature will be stripped on client side before sending messages to server, server will make no attempt to verify message signatures. Chat reporting and "Only Show Secure Chat" will not function, and players will be notified that those features are disabled by the mod when trying to use them.

Although the mod can function when present on only one of the sides, it can be configured to demand itself to be installed on another side in order to play. This way you can install it on server and require all clients that join to have this mod in order to have full functionality, but can also leave it server-only if you prefer. Configuration files are located in <u>NoChatReports</u> subfolder of default config folder.

Additionally, if installed on client, this mod will disable telemetry (similarly to how [No Telemetry](https://modrinth.com/mod/no-telemetry) does).

To make Velocity compatible with this mod, set `force-key-authentication = false` in `velocity.toml`. Thanks to [MrMelon54](https://github.com/MrMelon54) for pointing this out.

## Chat Encryption
I have put together a video dedicated to chat encryption, you can watch it to learn how to use it (and whether you should): https://www.youtube.com/watch?v=e7RzNP32k-s

## Documentation:
You can find documentation and other information relevant to this mod on the [wiki](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/). It currently features following articles:
- [Configuration Files](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/)
- [Protecting Server Players](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Protecting-Server-Players/)
- [How to Get Safe Server Status](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/How-to-Get-Safe-Server-Status)
- [I Got Banned!](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/I-Got-Banned)
- [To Encrypt or Not to Encrypt](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/To-Encrypt-or-Not-To-Encrypt)
- [The Realms Question](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/The-Realms-Question)


## For Developers:
If you develop your own mod, plugin or other server software that in some way prevents chat reports, you can make No Chat Reports on client recognize servers with your software as safe. I wrote [a small article about this](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/How-to-Get-Safe-Server-Status). For clarification or further inquiries - contact me via Discord, link in "External resources".

## Reasoning:
See my videos for an explanation of how message signatures and chat reporting work in the game, their failures and the reasoning behind the creation of this mod:
1. https://www.youtube.com/watch?v=hYAUEMlugyw
2. https://www.youtube.com/watch?v=DobmW1ZUcbQ
3. https://www.youtube.com/watch?v=gH_q7ZuCJs0

<a href="https://bisecthosting.com/AIZISTRAL" target="_blank">![image](https://www.bisecthosting.com/partners/custom-banners/af63cb17-c373-4c82-aa1d-29beb7b045a9.png)</a>
