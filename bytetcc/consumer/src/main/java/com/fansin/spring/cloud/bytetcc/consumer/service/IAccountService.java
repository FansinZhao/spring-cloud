package com.fansin.spring.cloud.bytetcc.consumer.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-21 下午23:51
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
