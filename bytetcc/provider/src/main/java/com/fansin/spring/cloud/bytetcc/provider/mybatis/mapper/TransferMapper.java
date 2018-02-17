package com.fansin.spring.cloud.bytetcc.provider.mybatis.mapper;

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
	int decreaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);


	/**
	 * Increase amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int increaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);

}
