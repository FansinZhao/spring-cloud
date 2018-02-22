package com.fansin.spring.cloud.bytetcc.consumer.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-21 下午23:51
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
