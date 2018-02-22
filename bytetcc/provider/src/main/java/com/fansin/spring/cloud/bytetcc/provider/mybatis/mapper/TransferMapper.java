package com.fansin.spring.cloud.bytetcc.provider.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-21 下午23:51
 */
public interface TransferMapper {

	/**
	 * decrease amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int decreaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);

	/**
	 * confirm amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int confirmDecreaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);

	/**
	 * cancel decrease amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int cancelDecreaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);

	/**
	 * Increase amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int increaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);


	/**
	 * Confirm Increase amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int confirmIncreaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);


	/**
	 * Increase amount int.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the int
	 */
	int cancelIncreaseAmount(@Param("acctId") String accountId, @Param("amount") double amount);


}
