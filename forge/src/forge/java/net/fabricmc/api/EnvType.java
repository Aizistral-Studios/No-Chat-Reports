/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.api;

/**
 * Represents a type of environment.
 *
 * <p>A type of environment is a jar file in a <i>Minecraft</i> version's json file's {@code download}
 * subsection, including the {@code client.jar} and the {@code server.jar}.</p>
 *
 * @see Environment
 * @see EnvironmentInterface
 */
public enum EnvType {
	/**
	 * Represents the client environment type, in which the {@code client.jar} for a
	 * <i>Minecraft</i> version is the main game jar.
	 *
	 * <p>A client environment type has all client logic (client rendering and integrated
	 * server logic), the data generator logic, and dedicated server logic. It encompasses
	 * everything that is available on the {@linkplain #SERVER server environment type}.</p>
	 */
	CLIENT,
	/**
	 * Represents the server environment type, in which the {@code server.jar} for a
	 * <i>Minecraft</i> version is the main game jar.
	 *
	 * <p>A server environment type has the dedicated server logic and data generator
	 * logic, which are all included in the {@linkplain #CLIENT client environment type}.
	 * However, the server environment type has its libraries embedded compared to the
	 * client.</p>
	 */
	SERVER
}
