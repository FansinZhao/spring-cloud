package com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * The interface Transfer dao.
 */
public interface TransferMapper {

	/**
	 * Increase amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int increaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);

	/**
	 * Confirm increase int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int confirmIncrease(@Param("acctId") String accountId, @Param("amount") double amount);

	/**
	 * Cancel increase int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int cancelIncrease(@Param("acctId") String accountId, @Param("amount") double amount);

}
