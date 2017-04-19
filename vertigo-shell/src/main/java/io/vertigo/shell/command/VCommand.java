/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.shell.command;

import io.vertigo.lang.Assertion;

import java.util.Collections;
import java.util.Map;

/**
 * Generic request with args to interact with vertigo, components.
 * A command contains
 *  - a name like put, push...
 *  - a list of args identified by their name and value.
 *
 * @author pchretien
 */
public final class VCommand {
	private final String name;

	private final Map<String, String> args;

	public VCommand(final String name) {
		this(name, Collections.<String, String> emptyMap());
	}

	public VCommand(final String name, final Map<String, String> args) {
		Assertion.checkNotNull(name, "name is required");
		Assertion.checkNotNull(args, "args is required, may be empty");
		//-----
		//		System.out.println("cmd : name => " + name);
		//		System.out.println("cmd : args => " + args);
		this.name = name;
		this.args = args;
	}

	public String getName() {
		return name;
	}

	public String arg(final String argName, final String defaultValue) {
		//		System.out.println("arg : name => " + argName + " contains " + args.containsKey(argName));
		return args.containsKey(argName) ? args.get(argName) : defaultValue;
	}

	public int arg(final String argName, final int defaultValue) {
		return args.containsKey(argName) ? Integer.valueOf(args.get(argName)).intValue() : defaultValue;
	}
}
