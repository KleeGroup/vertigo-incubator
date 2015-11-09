/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.ccc.json;

import static spark.Spark.get;
import io.vertigo.ccc.console.VConsoleHandler;
import io.vertigo.shell.command.VCommand;
import io.vertigo.shell.command.VResponse;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;

public final class Hub {
	private Hub() {
		//private
	}

	public static void main(final String[] args) {
		final VConsoleHandler consoleHandler = new VConsoleHandler();

		get(new CommandRoute("/:cmd", consoleHandler));
	}

	private static final class CommandRoute extends Route {
		private final VConsoleHandler consoleHandler;

		CommandRoute(final String path, final VConsoleHandler consoleHandler) {
			super(path);
			this.consoleHandler = consoleHandler;
		}

		/** {@inheritDoc} */
		@Override
		public Object handle(final Request request, final Response response) {
			final String commandName = request.params(":cmd");
			final Map<String, String> params = new HashMap<>();
			for (final String queryParam : request.queryParams()) {
				params.put(queryParam, request.queryParams(queryParam));
			}
			final VResponse vresponse = consoleHandler.execCommand(new VCommand(commandName, params));
			return vresponse.getResponse();
		}
	}
}
