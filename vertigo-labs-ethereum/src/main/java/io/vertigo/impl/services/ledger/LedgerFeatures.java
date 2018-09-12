/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.impl.services.ledger;

import io.vertigo.app.config.Features;
import io.vertigo.commons.analytics.AnalyticsManager;
import io.vertigo.commons.daemon.DaemonManager;
import io.vertigo.commons.impl.analytics.AnalyticsManagerImpl;
import io.vertigo.commons.impl.daemon.DaemonManagerImpl;
import io.vertigo.core.param.Param;
import io.vertigo.ledger.services.LedgerManager;
import io.vertigo.plugins.ledger.ethereum.EthereumLedgerPlugin;

/**
 * Defines Ledger features.
 *
 * @author xdurand
 */
public final class LedgerFeatures extends Features {

	private int queueSizeThreshold = 10;
	private int autoFlushPeriod = 60;
	
	/**
	 * Constructor.
	 */
	public LedgerFeatures() {
		super("x-ledger");
	}
	
	/**
	 * 
	 * @return  the feature
	 */
	public LedgerFeatures withQueueSizeThreshold(int queueSizeThreshold) {
		this.queueSizeThreshold = queueSizeThreshold;
		return this;
	}
	
	/**
	 * 
	 * @return  the feature
	 */
	public LedgerFeatures withAutoFlushPeriod(int autoFlushPeriod) {
		this.autoFlushPeriod = autoFlushPeriod;
		return this;
	}
	
	/**
	 * Add Ethereum BlockChain Ledger
	 * @return  the feature
	 */
	public LedgerFeatures withEthereumBlockChain(Param... params) {
		getModuleConfigBuilder()
			.addPlugin(EthereumLedgerPlugin.class, params);
		return this;
	}
	
	/** {@inheritDoc} */
	@Override
	protected void buildFeatures() {
		getModuleConfigBuilder()
			.addComponent(DaemonManager.class, DaemonManagerImpl.class)
			.addComponent(AnalyticsManager.class, AnalyticsManagerImpl.class)
			.addComponent(LedgerManager.class, LedgerManagerImpl.class, 
					Param.of("queueSizeThreshold", String.valueOf(queueSizeThreshold)),
					Param.of("autoFlushPeriod", String.valueOf(autoFlushPeriod)));
	}
	
}
