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
package io.vertigo.plugins.ledger.ethereum;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import io.vertigo.impl.services.ledger.LedgerPlugin;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.VSystemException;
import io.vertigo.lang.WrappedException;
import io.vertigo.ledger.services.LedgerAddress;
import io.vertigo.ledger.services.LedgerTransaction;
import rx.Subscription;


/**
 * Client RPC Ethereum
 * @author xdurand
 *
 */
public final class EthereumLedgerPlugin implements LedgerPlugin {

	private static final Logger LOGGER = LogManager.getLogger(EthereumLedgerPlugin.class);
	private static final Map<String, Subscription> MAP_SUBSCRIPTIONS = new ConcurrentHashMap<>();

	private Web3j web3j; 
	private Credentials credentials;
	private LedgerAddress defaultDestAddr;
	private LedgerAddress myPublicAddr;


	@Inject
	public EthereumLedgerPlugin(
			@Named("urlRpcEthNode") String urlRpcEthNode,
			@Named("myAccountName") String myAccountName,
			@Named("myPublicAddr") String myPublicAddr,
			@Named("defaultDestAccountName") String defaultDestAccountName,
			@Named("defaultDestPublicAddr") String defaultDestPublicAddr,
			@Named("walletPassword") String walletPassword,
			@Named("walletPath") String walletPath) throws IOException, CipherException {
		this.myPublicAddr = new LedgerAddress(myAccountName, myPublicAddr);
		this.defaultDestAddr = new LedgerAddress(defaultDestAccountName, defaultDestPublicAddr);
		
		LOGGER.info("Connecting to RPC Ethereum Node: {}", urlRpcEthNode);
		web3j = Web3j.build(new HttpService(urlRpcEthNode));
		Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
		LOGGER.info("Connected to RPC Ethereum Node: {}. Client version: {}", urlRpcEthNode, web3ClientVersion.getWeb3ClientVersion());
		credentials = WalletUtils.loadCredentials(walletPassword, walletPath);
	}

	@Override
	public BigInteger getWalletBalance() {
		return getBalance(myPublicAddr);
	}
	
	@Override
	public BigInteger getBalance(LedgerAddress publicAddr) {
		EthGetBalance balance;
		try {
			balance = web3j.ethGetBalance(publicAddr.getPublicAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			throw WrappedException.wrap(e);
		}
		return balance.getBalance();
	}

	@Override
	public void sendData(String data) {
		sendData(data, defaultDestAddr);
	}
	
	public void sendData(String data, LedgerAddress destinationAdr) {

		try {
			TransactionReceipt transactionReceipt = VTransfer.sendFunds(web3j, credentials, destinationAdr.getPublicAddress(),
					BigDecimal.valueOf(0), Convert.Unit.WEI, data)
					.send();

			if (!transactionReceipt.isStatusOK()) {
				throw new VSystemException("Ethereum write failed", transactionReceipt.getStatus());
			}
			
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public void subscribeNewMessages(String name, Consumer<LedgerTransaction> consumer) {
		Assertion.checkArgNotEmpty(name);
		//-----
		Subscription subscription = web3j.catchUpToLatestTransactionObservable(DefaultBlockParameterName.EARLIEST)
				.filter(tx -> tx.getTo().equals(myPublicAddr.getPublicAddress()))
				.map(this::convertTransactionToLedgerTransaction)
				.subscribe(consumer::accept);
		LOGGER.info("Getting new messages sent to {}.", myPublicAddr);
		MAP_SUBSCRIPTIONS.put(name, subscription);
	}

	@Override
	public void subscribeExistingMessages(String name, Consumer<LedgerTransaction> consumer) {
		Assertion.checkArgNotEmpty(name);
		//-----
		Subscription subscription = web3j.catchUpToLatestTransactionObservable(DefaultBlockParameterName.EARLIEST)
				.filter(tx -> tx.getTo().equals(myPublicAddr.getPublicAddress()))
				.map(this::convertTransactionToLedgerTransaction)
				.subscribe(consumer::accept);
		LOGGER.info("Getting existing messages sent to {}.", myPublicAddr);
		MAP_SUBSCRIPTIONS.put(name, subscription);
	}

	@Override
	public void subscribeAllMessages(String name, Consumer<LedgerTransaction> consumer) {
		Assertion.checkArgNotEmpty(name);
		//-----
		Subscription subscription = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameterName.EARLIEST)
				.filter(tx -> tx.getTo().equals(myPublicAddr.getPublicAddress()))
				.map(this::convertTransactionToLedgerTransaction)
				.subscribe(consumer::accept);
		LOGGER.info("Getting all messages sent to {}.", myPublicAddr);
		MAP_SUBSCRIPTIONS.put(name, subscription);
	}
	
	@Override
	public void unsubscribe(String name) {
		Assertion.checkArgNotEmpty(name);
		//-----
		MAP_SUBSCRIPTIONS.get(name).unsubscribe();
	}

	private LedgerTransaction convertTransactionToLedgerTransaction(Transaction transaction) {
		LedgerTransaction ledgerTransaction = new LedgerTransaction();
		
		ledgerTransaction.setBlockHash(transaction.getBlockHash());
		ledgerTransaction.setBlockNumber(transaction.getBlockNumber());
		ledgerTransaction.setFrom(transaction.getFrom());
		ledgerTransaction.setTo(transaction.getTo());
		ledgerTransaction.setNonce(transaction.getNonce());
		ledgerTransaction.setTransactionIndex(transaction.getTransactionIndex());
		ledgerTransaction.setValue(transaction.getValue());
		ledgerTransaction.setMessage(transaction.getInput());
		
		return ledgerTransaction;
	}

}
