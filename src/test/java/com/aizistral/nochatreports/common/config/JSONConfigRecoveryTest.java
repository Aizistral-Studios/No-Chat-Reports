package com.aizistral.nochatreports.common.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONConfigRecoveryTest {
	@Test
	public void corruptedConfigIsPreservedAndDefaultsAreLoaded() throws IOException {
		Path testRoot = Path.of("build", "test-config");
		Files.createDirectories(testRoot);
		Path configDir = Files.createTempDirectory(testRoot, "ncr-config-");
		Path configFile = configDir.resolve("NoChatReports/NCR-Common.json");
		Files.createDirectories(configFile.getParent());

		String corruptedContents = "{ invalid json";
		Files.writeString(configFile, corruptedContents, StandardCharsets.UTF_8);

		NCRCore.awaken(new TestPlatformProvider(configDir));

		assertTrue(NCRConfig.getCommon().convertToGameMessage());
		JsonObject recoveredConfig = JsonParser.parseString(
				Files.readString(configFile, StandardCharsets.UTF_8)).getAsJsonObject();
		assertTrue(recoveredConfig.get("convertToGameMessage").getAsBoolean());

		try (var files = Files.list(configFile.getParent())) {
			Path backup = files
					.filter(path -> path.getFileName().toString().startsWith("NCR-Common.json.corrupt-"))
					.findFirst()
					.orElseThrow();
			assertEquals(corruptedContents, Files.readString(backup, StandardCharsets.UTF_8));
		}
	}

	private static final class TestPlatformProvider implements PlatformProvider {
		private final Path configDir;

		private TestPlatformProvider(Path configDir) {
			this.configDir = configDir;
		}

		@Override
		public boolean isOnClient() {
			return false;
		}

		@Override
		public boolean isOnDedicatedServer() {
			return true;
		}

		@Override
		public Path getMinecraftDir() {
			return this.configDir;
		}

		@Override
		public Path getConfigDir() {
			return this.configDir;
		}
	}
}
