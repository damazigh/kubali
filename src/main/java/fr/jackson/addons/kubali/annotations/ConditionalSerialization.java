/*
 * Copyright 2018 Amazigh DJEBARRI

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package fr.jackson.addons.kubali.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.core.processors.IContext;
import fr.jackson.addons.kubali.core.processors.impl.Context;
import fr.jackson.addons.kubali.model.Filter;

/**
 * 
 * @author adjebarri
 * @since 0.0.1
 *        <p>
 *        This annotation allows to mark a class as a conditional serializable
 *        entity. Indeed, at startup the classpath resources will be scanned in
 *        order to determine which entities are annotated with
 *        {@link ConditionalSerialization}. A corresponding default
 *        {@link Filter} will be associated to each entity and stored in the
 *        {@link Context}.
 *        </p>
 * 
 * @see IContext
 * @see IIntrospector
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalSerialization {

}
