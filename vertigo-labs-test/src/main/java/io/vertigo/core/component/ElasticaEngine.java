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
package io.vertigo.core.component;

import io.vertigo.lang.Engine;

/** 
 * Gestion des appels distribués.
 * Ce module est utilisé 
 *  - soit en mode client 
 *  - soit en mode server
 *  
 * @author pchretien
 */
public interface ElasticaEngine extends Engine {
	/**
	 * Création d'un proxy client. 
	 * Le proxy permet de distribuer des services ; ces services sont déclarés dans une interface et déployés sur un serveur.  
	 * 
	 * @param <F> Type de l'interface à distribuer
	 * @return Interface cliente du service
	 */
	<F> F createProxy(final Class<F> facadeClass);

}
