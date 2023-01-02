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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to declare that the annotated element is present only in the specified environment.
 *
 * <p>Use with caution, as Fabric-loader will remove the annotated element in a mismatched environment!</p>
 *
 * <p>When the annotated element is removed, bytecode associated with the element will not be removed.
 * For example, if a field is removed, its initializer code will not, and will cause an error on execution.</p>
 *
 * <p>If an overriding method has this annotation and its overridden method doesn't,
 * unexpected behavior may happen. If an overridden method has this annotation
 * while the overriding method doesn't, it is safe, but the method can be used from
 * the overridden class only in the specified environment.</p>
 *
 * @see EnvironmentInterface
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PACKAGE})
@Documented
public @interface Environment {
	/**
	 * Returns the environment type that the annotated element is only present in.
	 */
	EnvType value();
}
