package com.fansin.spring.cloud.bytetcc.provider.service;

/**
 * The interface Account service.
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
