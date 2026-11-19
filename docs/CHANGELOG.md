#### **\[Build 1.19.2-v1.13.12\]:**

- Fixed key validation in Encryption Settings screen not working correctly in some cases;
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
