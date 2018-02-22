package com.fansin.spring.cloud.bytetcc.provider.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-21 下午23:51
 */
public interface IAccountService {

	/**
	 * Increase amount.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 */
	void increaseAmount(String accountId, double amount);

	/**
	 * Decrease amount.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 */
	void decreaseAmount(String accountId, double amount);

}
