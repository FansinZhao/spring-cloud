package com.fansin.spring.cloud.bytetcc.consumer.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The interface Account service.
 */
@FeignClient(value = "BYTETCC-PROVIDER")
public interface IAccountService {

	/**
	 * Increase amount.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/increase")
	void increaseAmount(@RequestParam("acctId") String accountId, @RequestParam("amount") double amount);

	/**
	 * Decrease amount.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/decrease")
	void decreaseAmount(@RequestParam("acctId") String accountId, @RequestParam("amount") double amount);

}
