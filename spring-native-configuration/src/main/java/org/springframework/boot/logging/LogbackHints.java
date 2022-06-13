/*
 * Copyright 2019-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.logging;

import ch.qos.logback.classic.Level;

import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeConfigurationRegistry;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeResourcesEntry;
import org.springframework.boot.logging.logback.ColorConverter;
import org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter;
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.nativex.AotOptions;
import org.springframework.nativex.hint.MethodHint;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.type.NativeConfiguration;
import org.springframework.util.ClassUtils;

// TODO Send a PR to Logback to remove reflection usage in ch.qos.logback.classic.PatternLayout
// TODO Initialize ch.qos.logback.classic.PatternLayout at build time?
@NativeHint(trigger = Level.class, types =
@TypeHint(types = {
		ColorConverter.class,
		WhitespaceThrowableProxyConverter.class,
		ExtendedWhitespaceThrowableProxyConverter.class
}, access = {}, methods = @MethodHint(name = "<init>")))
public class LogbackHints implements NativeConfiguration {

	@Override
	public void computeHints(NativeConfigurationRegistry registry, AotOptions aotOptions) {
		if (new ClassPathResource("logback.xml").exists()) {
			throw new LogbackXmlException("Embedded logback.xml file is not supported yet with Spring Native, read the support section of the documentation for more details");
		}
		if (!aotOptions.isRemoveXmlSupport() &&
				ClassUtils.isPresent("org.codehaus.janino.ScriptEvaluator", null) &&
				ClassUtils.isPresent("ch.qos.logback.classic.Level", null)) {

			registry.resources()
					.add(NativeResourcesEntry.of("org/springframework/boot/logging/logback/defaults.xml"))
					.add(NativeResourcesEntry.of("org/springframework/boot/logging/logback/console-appender.xml"))
					.add(NativeResourcesEntry.of("org/springframework/boot/logging/logback/file-appender.xml"));
		}
	}

	static class LogbackXmlException extends RuntimeException {

		public LogbackXmlException(String message) {
			super(message, null, true, false);
		}
	}
}
