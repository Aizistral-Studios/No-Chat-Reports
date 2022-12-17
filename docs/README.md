# No Chat Reports

A mod that strips cryptographic signatures from chat messages and disables Player Chat Reporting in Minecraft.   
GitLab Mirror: https://gitlab.com/Aizistral-Studios/No-Chat-Reports

## Installation and Use

This mod supports both Forge and Fabric and can be installed on the client, server, or both. It will function differently depending on where it is installed:

1. **Only on the client**: The client will refuse to send the account's public key to the server, and signatures will be stripped from the messages that are sent. This way it won't be possible to report your messages, as there will be no proof they were actually sent from your account. The server will relay them unless the `enforce-secure-profile` option is set to true (which it is by default since 1.19.1), in which case you will not be able to send messages unless you agree to sign them (the mod will display a warning screen).
2. **Only on the server**: Clients will still attach signatures when sending messages to the server, but the server will strip them before relaying them to other players. This way Chat Reporting will not work for any players that join. You can enable player to system message conversion in the config to prevent players without the mod from seeing them as "Not Secure".
3. **On both the client and server**: The signature will be stripped on the client side before sending messages to the server, and the server will make no attempt to verify message signatures. Chat Reporting and `Only Show Secure Chat` will not function, and players will be notified that these features are disabled by the mod when trying to use them.

Even though the mod can function when present on only one side, it can be configured to demand itself to be installed on the other side in order to play. This way you can install it on the server and require all clients that join to have this mod in order to have full functionality, or leave it server-only if you prefer. Configuration files are located in the NoChatReports subfolder of the default config folder.

Additionally, if installed on the client, this mod will disable telemetry (similar to how No Telemetry does).

To make Velocity compatible with this mod, set force-key-authentication = false in velocity.toml. Thanks to MrMelon54 for pointing this out.

## Chat Encryption

I have created a video about chat encryption, you can watch it to learn how to use it and whether you should: 
[![Encryption tutorial](https://img.youtube.com/vi/e7RzNP32k-s/mqdefault.jpg)](https://www.youtube.com/watch?v=e7RzNP32k-s)

## Documentation

You can find documentation and other information relevant to this mod on the [Wiki](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki). It currently features the following articles:
- Configuration Files
- Protecting Server Players
- How to Get Safe Server Status
- I Got Banned!
- To Encrypt or Not to Encrypt
- The Realms Question

## For Developers

If you develop your own mod, plugin, or other server software that in some way prevents chat reports, you can make No Chat Reports on the client recognize servers with your software as safe. I wrote a [small article about this](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/How-to-Get-Safe-Server-Status). You can also provide feedback and receive support. For clarification or further inquiries, please contact me via [Discord](https://discord.com/invite/fuWK8ns).
