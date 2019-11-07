/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.studio.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.vertigo.core.definition.Definition;
import io.vertigo.lang.Assertion;

public final class DefinitionTypeModel<D extends Definition> {
	private final Class<D> definitionClass;
	private final List<D> definitions;

	public DefinitionTypeModel(final Class<D> definitionClass, final Collection<D> definitions) {
		Assertion.checkNotNull(definitionClass);
		Assertion.checkNotNull(definitions);
		//---
		this.definitionClass = definitionClass;
		this.definitions = new ArrayList<>(definitions);
	}

	public String getName() {
		return definitionClass.getSimpleName();
	}

	public int getCount() {
		return definitions.size();
	}

	public List<? extends Definition> getDefinitions() {
		return definitions;
	}
}
