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

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

public class VTransfer extends Transfer {

	private static final BigInteger GAS_UNIT_PER_BIT = new BigInteger("68");

	public VTransfer(Web3j web3j, TransactionManager transactionManager) {
		super(web3j, transactionManager);
	}

	/**
	 * 
	 * Given the duration required to execute a transaction, asyncronous execution is strongly
	 * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
	 * 
	 * @param toAddress
	 * @param value
	 * @param unit
	 * @param gasPrice
	 * @param gasLimit
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws TransactionException
	 */
	private TransactionReceipt send(
			String toAddress, BigDecimal value, Convert.Unit unit, String message) throws IOException, InterruptedException,
			TransactionException {

		BigInteger gasPrice = requestCurrentGasPrice();

		int messageLength = message.length();
		BigInteger bitSize = BigInteger.valueOf(messageLength % 2 == 0 ? messageLength / 2 : messageLength / 2 + 1);
		BigInteger storageCost = GAS_UNIT_PER_BIT.multiply(bitSize);
		BigInteger totalCost = GAS_LIMIT.add(storageCost);

		return send(toAddress, value, unit, gasPrice, totalCost, message);
	}

	/**
	 * 
	 * @param toAddress
	 * @param value
	 * @param unit
	 * @param gasPrice
	 * @param gasLimit
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws TransactionException
	 */
	private TransactionReceipt send(
			String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice,
			BigInteger gasLimit, String message) throws IOException, InterruptedException,
			TransactionException {

		BigDecimal weiValue = Convert.toWei(value, unit);
		if (!Numeric.isIntegerValue(weiValue)) {
			throw new UnsupportedOperationException(
					"Non decimal Wei value provided: " + value + " " + unit.toString()
							+ " = " + weiValue + " Wei");
		}

		String resolvedAddress = ensResolver.resolve(toAddress);
		return send(resolvedAddress, message, weiValue.toBigIntegerExact(), gasPrice, gasLimit);
	}

	/**
	 * 
	 * @param web3j
	 * @param credentials
	 * @param toAddress
	 * @param value
	 * @param unit
	 * @param gasPrice
	 * @param gasLimit
	 * @param message
	 * @return
	 */
	public static RemoteCall<TransactionReceipt> sendFunds(Web3j web3j, Credentials credentials,
			String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice,
			BigInteger gasLimit, String message) {

		TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

		return new RemoteCall<>(() -> new VTransfer(web3j, transactionManager).send(toAddress, value, unit, gasPrice, gasLimit, message));
	}

	/**
	 * 
	 * @param web3j
	 * @param credentials
	 * @param toAddress
	 * @param value
	 * @param unit
	 * @param gasPrice
	 * @param gasLimit
	 * @param message
	 * @return
	 */
	public static RemoteCall<TransactionReceipt> sendFunds(Web3j web3j, Credentials credentials,
			String toAddress, BigDecimal value, Convert.Unit unit, String message) {
		TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
		return new RemoteCall<>(() -> new VTransfer(web3j, transactionManager).send(toAddress, value, unit, message));
	}

}
