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

/**
 * A response contains
 * - a status (in fact en errorMsg if there is only one error
 * - a body response in a string  ( a json formatted string is perfect)
 *
* @author pchretien
*/
public final class VResponse {
	private final String response;
	private final String errorMsg;

	public static VResponse createResponse(final String response) {
		Assertion.checkArgNotEmpty(response);
		//-----
		return new VResponse(response, null);
	}

	public static VResponse createResponseWithError(final String ErrorMsg) {
		Assertion.checkArgNotEmpty(ErrorMsg, "errorMsg is required");
		//-----
		return new VResponse(null, ErrorMsg);
	}

	private VResponse(final String response, final String errorMsg) {
		this.response = response;
		this.errorMsg = errorMsg;
	}

	public boolean hasError() {
		return errorMsg != null;
	}

	public String getResponse() {
		Assertion.checkNotNull(response, "there is no valid response");
		//-----
		return response;
	}

	public String getErrorMsg() {
		Assertion.checkNotNull(errorMsg, "there is no error");
		//-----
		return errorMsg;
	}
}
