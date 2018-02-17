package com.fansin.spring.cloud.bytetcc.consumer.service;

/**
 * The interface Transfer service.
 */
public interface ITransferService {

	/**
	 * Transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	void transfer(String sourceAcctId, String targetAcctId, double amount);

}
