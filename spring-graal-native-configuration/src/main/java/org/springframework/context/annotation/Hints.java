/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.context.annotation;

import org.springframework.graal.extension.ConfigurationHint;
import org.springframework.graal.extension.NativeImageConfiguration;
import org.springframework.graal.extension.TypeInfo;
import org.springframework.graal.type.AccessBits;

/*
// Not quite right... this is a superclass of a selector we've already added...
proposedHints.put(AdviceModeImportSelector,
		new CompilationHint(true, true, new String[0]
		));
		*/
@ConfigurationHint(value = AdviceModeImportSelector.class, abortIfTypesMissing = true, follow = true)
// TODO can be {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
// @Imports has @CompilationHint(skipIfTypesMissing=false?, follow=true)
@ConfigurationHint(value = Import.class, abortIfTypesMissing = false, follow = true) // TODO verify these flags...
@ConfigurationHint(value = Conditional.class, extractTypesFromAttributes = { "value" }) // TODO need extract?
// These don't specify a triggering value target so are always exposed
@ConfigurationHint(typeInfos = { @TypeInfo(types = { ComponentScan.class,
		Configuration.class }, access = AccessBits.CLASS | AccessBits.PUBLIC_METHODS) })
@ConfigurationHint(typeInfos = {
		@TypeInfo(types = { AnnotationConfigApplicationContext.class,CommonAnnotationBeanPostProcessor.class }, 
				  access = AccessBits.CLASS | AccessBits.PUBLIC_CONSTRUCTORS),
		@TypeInfo(types = { ConfigurationClassPostProcessor.class }) // This needs more access than those
})
public class Hints implements NativeImageConfiguration {
}
