#### **\[Build 1.20.1-v2.2.2\]:**

- Improved config I/O error handling and logging;
- Changelog format was changed to Markdown, [file available on Github](https://github.com/Aizistral-Studios/No-Chat-Reports/blob/1.20-Unified/docs/CHANGELOG.md).


#### **\[Build 1.20.1-v2.2.1\]:**

- Initial 1.20.1 port (pretend it required changes);
- Updated Traditional Chinese translation ([thanks notlin4, #407](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/407)).


#### **\[Build 1.20-v2.2.0\]:**

- Initial 1.20 port.


#### **\[Build 1.19.4-v2.1.6\]:**

- Removed unnecessary debug log accidentally left in previous update.


#### **\[Build 1.19.4-v2.1.5\]:**

- Fixed decryption issue with custom chat formats using full-width colon ([#376](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/376));
- Improved English localization ([thanks Doenerstyle, #403](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/403));
- Updated Vietnamese translation ([thanks I&#95;am&#95;Vietnam, #405](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/405));
- Updated German translation ([thanks Doenerstyle, #403](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/403)).


#### **\[Build 1.19.4-v2.1.4\]:**

- Updated Korean translation ([thanks xlzv, #398](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/398)).


#### **\[Build 1.19.4-v2.1.3\]:**

- Updated Thai translation ([thanks NaiNonTH, #395](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/395));
- Updated Polish translation ([thanks GerbilPL, #396](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/396));
- Updated Korean translation ([thanks xlzv, #397](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/397)).


#### **\[Build 1.19.4-v2.1.2\]:**

- Changed mod's logger name to "NoChatReports" (previously displayed as "NCRCore");
- Fixed incorrect key displayed for encryption bypass shortcut on MacOS ([#361](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/361));
- Fixed errors when Mod Menu is installed without Cloth Config API ([#392](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/392));
- Fixed incorrect alignment of custom buttons in multiplayer menu ([#386](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/386));
- Changed singleplayer safety status wording to reflect that self-hosted LAN servers are also safe ([#372](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/372));
- Fabric's annotation classes are no longer shipped with the mod, should fix related mod conflicts on Forge ([#389](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/389));
- Corrected issue tracker link in mod metadata ([thanks ChildishGiant, #387](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/387));
- Added /message and /reply to default list of encryptable commands ([thanks Podgorica, #382](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/382));
- Updated Polish translation ([thanks GerbilPL, #388](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/388));
- Updated Simplified Chinese translation ([thanks GodGun968, #391](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/391));
- Updated Traditional Chinese translation ([thanks notlin4, #384](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/384)).


#### **\[Build 1.19.4-v2.1.1\]:**

- Fixed mod's buttons causing issues with chat navigation ([#375](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/375));
- Updated Italian translation ([thanks WVam, #377](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/377));
- Added Japanese translation ([thanks IlyaIvanovsky, #374](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/374));
- Updated Simplified and Traditional Chinese translation ([thanks IlyaIvanovsky, #374](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/374)).


#### **\[Build 1.19.4-v2.1.0\]:**

- Initial 1.19.4 port;
- Updated Italian translation ([thanks WVam, #363](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/363));
- Updated Simplified and Traditional Chinese translation ([thanks IlyaIvanovsky, #359](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/359)).


#### **\[Build 1.19.3-v2.0.0\]:**

- Unified Forge and Fabric branches. Versioning and update releases for both modloaders will be synchronized from now on;
- Command signatures are now stripped on server side ([#351](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/351));
- Updated Simplified Chinese translation (thanks GodGun968 and IlyaIvanovsky, [#338](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/338)/[#342](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/342)/[#344](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/344)/[#354](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/354));
- Updated Finnish translation ([thanks Joquliina, #336](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/336)).


#### **\[Build 1.19.3-v1.19.0\]:**

- Improved safety status tooltip for Realms servers;
- Added separate safety status for singleplayer ([#330](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/330));
- Added dropdown selection for default signing mode in Mod Menu config screen ([#324](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/324));
- Telemetry button is now removed by default if No Chat Reports is configured to disable telemetry. This can be adjusted via "removeTelemetryButton" option in client config ([#322](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/322));
- Fixed system message indicators displaying in singleplayer even when they are disabled in client config ([#325](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/325));
- Fixed IPv6 addresses not being incorrectly written to server preferences config ([thanks zischknall, #329](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/329));
- Updated Estonian translation.


#### **\[Build 1.19.3-v1.18.0\]:**

- Initial 1.19.3 port;
- Minor changes to legacy networking protocol recognition;
- Mod will now automatically remove "Secure" label from server if it detects any signed messages;
- Base64R no longer uses "." as part of encoding characters to not interfere with common format of client-sided commands;
- Implemented chat signing modes. Global signing mode peference can be set in config, while safety status button allows to set different mode on per-server basis ([#314](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/314));
- Removed server whitelist options as they are replaced by signing mode preferences;
- Offline servers will now be automatically recognized as secure ([#301](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/301));
- Offline accounts are now properly acknowledged by the mod;
- Updated Estonian translation.


#### **\[Build 1.19.3-pre3-v1.17.0\]:**

- Initial 1.19.3 Pre-Release 3 port;
- Reinstated Mod Menu/Cloth Config API integration;
- Reorganized config options in Mod Menu, listed recently added ones ([#284](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/284));
- Fixed two overlaying tooltips being rendered for "Only Show Secure Chat" option in chat settings ([#302](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/302));
- Updated Simplified Chinese translation (thanks IlyaIvanovsky, [#305](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/305)/[#310](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/310)).


#### **\[Build 1.19.3-pre2-v1.16.0\]:**

- Initial 1.19.3 Pre-Release 2 port;
- Replaced networking channel with more generic solutions to improve security and cross-compatibility ([#255](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/255), [#297](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/297));
- Servers on version prior to 1.19.1 are now recognized as secure ([#123](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/123)).


#### **\[Build 22w46a-v1.15.0\]:**

- Initial 22w46a port;
- Fixed chat signing being prevented even with mod toggle off;
- Fixed tooltip of config reload button being rendered regardless of whether button itself is hovered or not;
- Fixed crash when opening Social Interactions screen ([#286](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/286));
- Fixed crash when opening Encryption Settings screen ([#288](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/288));
- Fixed mod toggle button not reflecting current mod status ([#287](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/287));
- Fixed key validation in Encryption Settings not working correctly in some cases;
- Improved log readability when mod configs fail to read ([#291](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/291));
- Option "demandOnClient" is now disabled by default, while "convertToGameMessage" is enabled;
- Temporarily disabled Mod Menu/Cloth Config API integration until they update;
- Last chat message is now automatically resent when No Chat Report detects signing requirement with "skipSigningWarning" option enabled;
- Added more specific safety status button tooltip for when server is manually whitelisted ([#280](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/280));
- Updated Polish translation ([thanks GerbilPL, #295](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/295));
- Updated Estonian translation.


#### **\[Build 22w44a-v1.14.4\]:**

- Added "skipSigningWarning" option, which automatically enables chat signing when server demands it ([thanks Madis0, #274](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/274));
- Updated Polish translation ([thanks GerbilPL, #275](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/275));
- Minor technical changes.


#### **\[Build 22w44a-v1.14.3\]:**

- Fixed vanilla clients being kicked with "Received chat packet with missing or invalid signature" when sending messages on server with No Chat Reports installed ([#271](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/271)).


#### **\[Build 22w44a-v1.14.2\]:**

- Fixed and updated some tooltips for option labes in Mod Menu/Cloth Config API integration ([thanks Madis0, #270](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/270));
- Updated Estonian translation.


#### **\[Build 22w44a-v1.14.1\]:**

- Removed outdated safety status tooltip for when "whitelistAllServers" option is enabled;
- Re-enabled optional Mod Menu/Cloth Config API integration ([thanks Madis0, #264](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/264));
- Mod can no longer be toggled mid-session. Doing so via ModMenu will schedule the change to be applied after leaving the server;
- Server can no longer be added to/removed from whitelist by clicking safety status button while "whitelistAllServers" option is enabled ([#269](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/269));
- Vanilla's missing profile public key message is now replaced with more informative one on client. Added config option to hide it when not important ([#261](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/261));
- Mod will no longer set safety status to "unintrusive" after sucessful execution of commands without signed arguments ([#260](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/260));
- Improved wording on signing status tooltips ([thanks Madis0, #263](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/263));
- Updated Estonian translation.


#### **\[Build 22w44a-v1.14.0\]:**

- Initial 22w44a port;
- Adapted server safety status functionality and "Unsafe Server" screen to new chat session system;
- Config options "reconnectAwaitSeconds", "postDisconnectAwaitSeconds", "signingCheckDelaySeconds" and "serverSigningChecks" were removed;
- Config options "hideRedChatIndicators", "hideYellowChatIndicators" and "hideGrayChatIndicators" were renamed to "hideInsecureMessageIndicators", "hideModifiedMessageIndicators" and "hideSystemMessageIndicators" respectively;
- Server safety status icon can now be clicked to add current server to whitelist ([#221](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/221));
- Added Brazilian Portuguese translation ([thanks FITFC, #252](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/252)).


#### **\[Build 1.19.2-v1.13.11\]:**

- Improved encryption compatibility with custom chat formats ([#248](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/248));
- Fixed mod config reload button not using correct translation key ([thanks MODKILLER1001, #242](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/242)).


#### **\[Build 1.19.2-v1.13.10\]:**

- Bundled Fabric API version raised to 0.64.0+1.19.2 ([#239](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/239));
- Fixed signing check interval not applying correctly to servers when "whitelistAllServers" is enabled ([#228](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/228));
- Fixed unknown server safety status displaying after re-enabling NCR mid-session ([#229](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/229));
- Increased contrast of server safety status icons, reworked Realms icon ([#233](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/233));
- Meessages can now be sent unencrypted while encryption is enabled by holding Ctrl when sending ([#231](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/231));
- Added config option to disable CTS indicators for decrypted messages ([#238](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/238));
- AES-based algorithms now use Base64R encoding, which features alternative table of encoding symbols. This alleviates small possibility of slurs appearing in encoded text ([#237](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/237));
- Slightly improved wording of Realms warning ([thanks Madis0, #227](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/227));
- Added more options to Mod Menu config GUI ([thanks MODKILLER1001, #230](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/230));
- Updated Estonian translation.


#### **\[Build 1.19.2-v1.13.9\]:**

- Made "About Encryption" and "Encryption Settings" GUIs more adaptive to uncommon resolutions ([#220](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/220));
- Fixed Base64-encoded messages sometimes starting with "/" and being recognized as commands ([#223](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/223));
- Ensured UTF-8 encoding is always used during encryption and decryption ([#224](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/224));
- Fixed Ceasar cipher breaking some languages ([#224](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/224));
- Added signing enforcement check interval for whitelisted servers. 12 hours by default, configurable ([#222](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/222));
- Added Thai translation ([thanks NaiNonTH, #225](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/225)).


#### **\[Build 1.19.2-v1.13.8\]:**

- Fixed a bug where disabling client-sided functionality of the mod made it impossible to join singleplayer worlds if "demandOnClient" was enabled in config (this time for sure) ([#219](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/219)).


#### **\[Build 1.19.2-v1.13.7\]:**

- Fixed a bug where disabling client-sided functionality of the mod made it impossible to join singleplayer worlds if "demandOnClient" was enabled in config ([#219](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/219));
- Fixed "Illegal characters in chat" disconnects when using Caesar encryption with shift ~90 ([#216](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/216));
- Added warning screen on entering Realms menu ([#189](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/189));
- Minor corrections to English translation;
- Updated Estonian translation;
- Updated Italian translation ([thanks WVam, #215](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/215)).


#### **\[Build 1.19.2-v1.13.6\]:**

- Added delay before automatic reconnects when joining servers with "enforce-secure-profile=true". Fixes frequent disconnects with DecoderException on Paper servers ([#190](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/190));
- Fixed narrator reading encrypted messages as-is even when decryption is available ([#208](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/208));
- Names of encryption algorithms are now localizable ([#207](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/207));
- Added special server safety status for Realms servers ([#189](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/189));
- Mod no longer tries to parse disconnects that occur after entering the server succesfully ([#199](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/199));
- Fixed occasional persistence of player's agreement to enter unsafe server;
- Clarified wording on "Unintrusive" server safety status;
- Updated Korean translation ([thanks xlzv, #210](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/210));
- Updated Italian translation ([thanks WVam, #211](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/211)).


#### **\[Build 1.19.2-v1.13.5\]:**

- Fixed "Encryption Settings" screen not adapting to huge GUI scaling correctly in some cases.


#### **\[Build 1.19.2-v1.13.4\]:**

- Decryption should now be more compatible with custom chat formats ([#204](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/204)).


#### **\[Build 1.19.2-v1.13.3\]:**

- Fixed "Encryption Settings" screen not functioning correctly.


#### **\[Build 1.19.2-v1.13.2\]:**

- Added an extra bit of debug logging to help analyze the structure of received chat messages.


#### **\[Build 1.19.2-v1.13.1\]:**

- Fixed "About Encryption" and "Encryption Settings" GUIs overflowing with huge GUI scaling ([#200](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/200));
- Encryption button now adjusts its position depending on whether or not server safety status is displayed ([#202](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/202)).


#### **\[Build 1.19.2-v1.13.0\]:**

- Added optional chat encryption functionality ([#95](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/95)). Read more about it [on the wiki](https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/To-Encrypt-or-Not-To-Encrypt);
- Updated Estonian translation;
- Updated Russian translation ([thanks Felix14, #195](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/195));
- Updated Polish translation (thanks GerbilPL, [#197](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/197)/[#198](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/198)).


#### **\[Build 1.19.2-v1.12.0\]:**

- Fixed mod not recognizing BungeeCord/Waterfall disconnects correctly ([#185](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/185));
- Added button that toggles most of mod's client-sided functionality to multiplayer menu, along with two related config options;
- Updated Mod Menu/Cloth Config API integration with new config options ([thanks MODKILLER1001, #193](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/193));
- Updated Estonian translation;
- Updated Italian translation ([thanks WVam, #188](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/188));
- Updated Spanish translation ([thanks Srockowo, #191](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/191));
- Added Pirate Speak translation ([thanks MODKILLER1001, #192](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/192)).


#### **\[Build 1.19.2-v1.11.2\]:**

- Fixed mod not loading correctly on 1.19.1 clients ([#182](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/182));
- Updated Estonian translation;
- Updated Polish translation ([thanks GerbilPL, #180](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/180)).


#### **\[Build 1.19.2-v1.11.1\]:**

- Whitelist of unsafe servers is now stored as a separate file in mod's config subfolder ([#40](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/40)).


#### **\[Build 1.19.2-v1.11.0\]:**

- Config files are now stored in "NoChatReports" subfolder of "config" folder. They were separated into common and client files;
- Added "README" file that is generated in mod's config folder and contains link to wiki article documenting config files;
- Added "Safe Server" icon, which is displayed in multiplayer menu next to servers that have No Chat Reports installed. Also added config options to disable it or change its position;
- The mod now sends and handles additional data during server pings. This is used to display abovementioned icon, and in theory allows other mods and plugins to mark server as "Safe" ([thanks fxmorin, #127](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/127));
- Updated Mod Menu/Cloth Config API integration with new config options ([thanks MODKILLER1001, #175](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/175));
- This and following mod releases for 1.19.2 should now be compatible with 1.19.1 ([#170](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/170));
- Disconnect message from "demandOnClient" is now configurable ([#48](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/48));
- Updated Estonian translation;
- Updated Polish translation (thanks GerbilPL, [#167](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/167)/[#168](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/168)/[#176](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/176));
- Updated Italian translation ([thanks WVam, #173](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/173)).


#### **\[Build 1.19.2-v1.10.2\]:**

- Added reset button and default field value to whitelisted servers option in Mod Menu config integration ([thanks MODKILLER1001, #162](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/162));
- Updated Polish translation ([thanks GerbilPL, #165](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/165));
- Updated Simplified Chinese translation (thanks IlyaIvanovsky and MSDNicrosoft, [#164](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/164)/[#166](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/166)).


#### **\[Build 1.19.2-v1.10.1\]:**

- Fixed optional Mod Menu/Cloth Config API integration from previous release not working properly;
- ServerData is now remembered in automatic reconnects. Should fix the bug with Xaero's Worldmap resets ([#156](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/156)).


#### **\[Build 1.19.2-v1.10.0\]:**

- Necessary Fabric API modules are now bundled with the mod itself ([thanks LoganDark, #151](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/151));
- The mod now features optional integration with Mod Menu and Cloth Config API, allowing for in-game configuration when both are installed on client ([thanks MODKILLER1001, #146](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/146));
- Updated Polish translation ([thanks GerbilPL, #153](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/153));
- Updated Spanish translation ([thanks M4rtinOF, #158](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/158));
- Added Danish translation ([thanks MagnusHJensen, #159](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/159));
- Added Slovakian translation ([thanks SmajloSlovakian, #152](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/152)).


#### **\[Build 1.19.2-v1.9.1\]:**

- Fixed conflict with No Telemetry mod ([#151](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/151));
- Updated Turkish translation ([thanks localfossa, #149](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/149));
- Updated Simplified Chinese translation ([thanks chronosacaria, #147](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/147)).


#### **\[Build 1.19.2-v1.9.0\]:**

- First release for 1.19.2 ([thanks CraftingDragon007, #144](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/144));
- Added config option to always skip "Unsafe Server" warning screen ([thanks Madis0, #124](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/124));
- Compressed some of the GUI textures ([thanks Madis0, #136](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/136));
- Removed version easter egg ([thanks Madis0, #145](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/145));
- Updated Italian translation ([thanks WVam, #126](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/126));
- Updated Polish translation ([thanks GerbilPL, #133](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/133));
- Added Finnish translation ([thanks Joquliina, #137](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/137)).


#### **\[Build 1.19.1-v1.8.4\]:**

- Ensured change from previous release works properly.


#### **\[Build 1.19.1-v1.8.3\]:**

- Re-allowed client to disconnect when chat chain is broken. Yes, this was the "major exploit" kennytv was talking about, or more specifically - unchecked speculation that even if true, would only apply in very limited number of circumstances.


#### **\[Build 1.19.1-v1.8.2\]:**

- Corrected English and updated German translation ([thanks WVam, #121](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/121));
- Added issue tracker link for ModMenu ([thanks MODKILLER1001, #122](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/122)).


#### **\[Build 1.19.1-v1.8.1\]:**

- Fixed crash when hovering over "Only Show Secure Chat" option in chat settings ([#120](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/120)).


#### **\[Build 1.19.1-v1.8.0\]:**

- Each color of Chat Trust Status indicator and "Chat messages can't be verified" toast now have individual config options tied to them ([thanks Madis0, #110](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/110));
- "Only Show Secure Chat" option in chat settings is now greyed out with the mod installed ([thanks kevinthegreat1, #117](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/117));
- Updated Italian translation ([thanks WVam, #115](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/115)).


#### **\[Build 1.19.1-v1.7.2\]:**

- Fixed "convertToGameMessage" option not working correctly ([#113](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/113));
- Fixed players being unable to join Realms with this mod. All Realms servers are now treated as unsafe ([#108](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/108));
- Expanded the context of warning when joining unsafe servers ([thanks Madis0, #112](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/112));
- Added Romanian translation ([thanks Secret-chest, #104](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/104));
- Added Korean translation ([thanks xlzv, #105](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/105));
- Added Traditional Chinese and updated Simplified Chinese translation ([thanks fire12324344 and Taskeren, #101](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/101));
- Updated Polish translation ([thanks GerbilPL, #103](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/103));
- Updated Turkish translation ([thanks localfossa, #106](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/106));
- Updated German translation ([thanks Doenerstyle, #107](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/107)).


#### **\[Build 1.19.1-v1.7.1\]:**

- Fixed client crash when classloading Social Interactions screen ([#100](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/100)).


#### **\[Build 1.19.1-v1.7.0\]:**

- First release for 1.19.1;
- Updated Estonian translation ([thanks Madis0, #98](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/98));
- Updated Simplified Chinese translation ([thanks Taskeren, #99](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/99));
- Added Indonesian translation ([thanks Natalius-dev, #94](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/94)).


#### **\[Build 1.19.1-rc2-v1.6.0\]:**

- The mod now removes "\[Not Secure\]" prefix for unsigned messages in server chat log ([#70](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/70));
- The "suppressMessageTrustIndicators" config option was renamed to "suppressVanillaSecurityNotices", and now is also responsible for removing "Chat messages can't be verified" toast client-side ([#87](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/87));
- Abovementioned toast will never be shown on servers that have "convertToGameMessage" option enabled;
- The mod now prevents client from disconnecting when chat chain is broken;
- Changed default configuration to demand the mod client-side when installed on server, instead of getting around chat signing with player to system message conversion. Should assist compatibility in the long run.


#### **\[Build 1.19.1-pre3-v1.5.0\]:**

- Added "versionEasterEgg", "disableTelemetry" and "showReloadButton" config options ([thanks Madis0, #83](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/83)).


#### **\[Build 1.19.1-pre3-v1.4.3\]:**

- Fixed potential safety state management problems after being disconnected from server.


#### **\[Build 1.19.1-pre3-v1.4.2\]:**

- Fixed server safety status icon not rendering;
- Removed timestamp scrambler (will be re-implemented later if remains effective);
- Gray line besides system messages will now be removed if "suppressMessageTrustIndicators" is enabled in config;
- Conversion to system messages now uses correct decoration when messages are sent using means such as /msg ([thanks e-im, #78](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/78));
- Added Catalan translation ([thanks Gorrion130 and localfossa, #79](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/79));
- Added Estonian translation ([thanks Madis0, #75](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/75));
- Updated Czech translation ([thanks Psojed, #80](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/80)).


#### **\[Build 1.19.1-pre3-v1.4.1\]:**

- Removed "forceAllowMultiplayer" config option (it will be later available as a separate mod);
- Added Czech translation ([thanks fym35, #69](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/69));
- Added Dutch translation ([thanks MaximevanderSmissen, #68](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/68));
- Added Italian translation ([thanks WVam, #65](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/65)).


#### **\[Build 1.19.1-pre3-v1.4.0\]:**

- Timestamp scrambler should now be less prone to violent explosions;
- Implemented config option to always hide report button on client ([#62](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/62));
- Added Ukrainian translation ([thanks celestora, #63](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/63));
- Added Spanish translation ([thanks Srockowo, #58](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/58));
- Updated German translation ([thanks Doenerstyle, #60](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/60)).


#### **\[Build 1.19.1-pre2-v1.3.2\]:**

- Implemented timestamp scrambler, which should help to make signed messages less usable as report evidence;
- Fixed crash on client startup ([#55](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/55));
- Updated Russian translation ([thanks A1Asriel, #56](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/56));
- Updated French translation ([thanks dahelip, #53](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/53));
- Updated Turkish translation ([thanks localfossa, #52](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/52)).


#### **\[Build 1.19.1-pre2-v1.3.1\]:**

- The mod will now always strip message signatures when playing on offline servers ([#44](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/44));
- Fixed endless warning screen loop when trying to log into the server with "enforce-secure-profile" enabled ([#42](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/42));
- Added config option to re-enable vanilla warnings about unsigned/modified messages ([#39](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/39));
- Fixed persisting assumptions about whitelisted servers ([#43](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/43));
- Added button that allows to reload No Chat Reports config to multiplayer menu;
- Added config option to enable logging of some debug informaion;
- Updated Polish translation ([thanks GerbilPL, #38/#46](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/46)).


#### **\[Build 1.19.1-pre2-v1.3.0\]:**

- The mod can now re-enable multiplayer button when banned, allowing to play on servers operated in offline mode;
- Player to system message conversion is now enabled by default;
- Report button in Social Interactions screen will be disabled if mod is present on both client and server, with tooltip indicating why;
- Implemented client-sided suppression of warnings about unsigned/modified messages;
- When connecting to a server you will now be able to know whether mod is present there or not, by the status icon that is rendered in the bottom left corner of screen chat and can be hovered for additional details;
- When connecting to servers without No Chat Reports and with "enforce-secure-profile" enabled you will now be warned that the mod will not be able to protect you. You can log in anyway by clicking "Proceed", and optionally checking the box that will disable future warnings when trying to enter that specific server. Whitelisted servers are stored in NoChatReports.json and can be added/removed manually.


#### **\[Build 1.19-v1.2.3\]:**

- Added Polish translation ([thanks GerbilPL, #24](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/24));
- Added Russian translation ([thanks sst4nk0, #25](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/25));
- Added German translation ([thanks Doenerstyle, #34](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/34));
- Added French translation ([thanks Mrredstone5230, #29](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/29)).


#### **\[Build 1.19-v1.2.2\]:**

- Added Simplified Chinese translation ([thanks CJYKK, #21](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/21));
- This and all further releases for Fabric will be marked Quilt-compatible.


#### **\[Build 1.19-v1.2.1\]:**

- Replaced all overwrites with injects, which should hopefully increase compatibility with other mods ([thanks ToxicAven, #7](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/7));
- Remade implementation of player to system message convertion, in a way that should resolve conflict with Styled Chat.


#### **\[Build 1.19-v1.2.0\]:**

- Implemented optional server-sided bypass for signature verification, by converting player messages to system messages ([thanks JFronny, #1](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/1));
- Minor optimizations (once again thanks JFronny).


#### **\[Build 1.19-v1.1.0\]:**

- Public keys are now prevented from being sent to server;
- Client no longer requires mod to be present on server by default;
- Implemented suppression of built-in telemetry.


#### **\[Build 1.19-v1.0.0\]:**

- Initial release.
