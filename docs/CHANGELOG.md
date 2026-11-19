#### **\[Build 1.19.2-v1.5.1\]:**

- Restored parity with Fabric branch ([target version 1.19.2-v1.13.11, commit e400b6](https://github.com/Aizistral-Studios/No-Chat-Reports/commit/e400b60ca8b35e9b9ee47f44dd11162c41172a75));
- Fixed key validation in Encryption Settings screen not working correctly in some cases;
- Fixed AES-based encryption algorithms not working correctly ([#290](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/290));
- Added Brazilian Portuguese translation ([thanks FITFC, #252](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/252)).


#### **\[Build 1.19.2-v1.5.0\]:**

- Restored parity with Fabric branch ([target version 1.19.2-v1.13.10, commit 205917c](https://github.com/Aizistral-Studios/No-Chat-Reports/commit/205917c003270449593ff3f1b7ad39b33381db4b)).


#### **\[Build 1.19.2-v1.4.0\]:**

- First release for 1.19.2;
- Restored parity with Fabric branch ([target version 1.19.2-v1.9.0, commit e7ed3e0](https://github.com/Aizistral-Studios/No-Chat-Reports/commit/e7ed3e014f0df81353c32429caf95686ed0dad00));
- Fixed conflict with No Telemetry mod ([#151](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/151)).


#### **\[Build 1.19.1-v1.3.3\]:**

- Fixed "versionEasterEgg" not disabling version replacement in title screen ([#138](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/138));
- Fixed mod not working correctly when logging into servers with "enforce-secure-profile" enabled ([#143](https://github.com/Aizistral-Studios/No-Chat-Reports/issues/143)).


#### **\[Build 1.19.1-v1.3.2\]:**

- Ensured change from previous release works properly.


#### **\[Build 1.19.1-v1.3.1\]:**

- Re-allowed client to disconnect when chat chain is broken. Yes, this was the "major exploit" kennytv was talking about, or more specifically - unchecked speculation that even if true, would only apply in very limited number of circumstances.


#### **\[Build 1.19.1-v1.3.0\]:**

- First release for 1.19.1;
- Restored parity with Fabric branch ([target version 1.19.1-v1.8.2, commit 984000f](https://github.com/Aizistral-Studios/No-Chat-Reports/commit/984000f16aa8bd86b624f38878cad4d503cb0694)).


#### **\[Build 1.19-v1.2.3\]:**

- Added Polish translation ([thanks GerbilPL, #24](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/24));
- Added Russian translation ([thanks sst4nk0, #25](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/25));
- Added German translation ([thanks Doenerstyle, #34](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/34));
- Added French translation ([thanks Mrredstone5230, #29](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/29)).


#### **\[Build 1.19-v1.2.2\]:**

- Added Simplified Chinese translation ([thanks CJYKK, #21](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/21)).


#### **\[Build 1.19-v1.2.1\]:**

- Replaced all overwrites with injects, which should hopefully increase compatibility with other mods ([thanks ToxicAven, #8](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/8));
- Remade implementation of player to system message convertion, in a way that should prevent some possible conflicts.


#### **\[Build 1.19-v1.2.0\]:**

- Implemented optional server-sided bypass for signature verification, by converting player messages to system messages ([thanks JFronny, #1](https://github.com/Aizistral-Studios/No-Chat-Reports/pull/1));
- Minor optimizations (once again thanks JFronny).


#### **\[Build 1.19-v1.1.0\]:**

- Public keys are now prevented from being sent to server;
- Client no longer requires mod to be present on server by default;
- Implemented suppression of built-in telemetry.


#### **\[Build 1.19-v1.0.0\]:**

- Initial release.
