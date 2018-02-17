package com.fansin.spring.cloud.bytetcc.consumer.controller;

import com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.consumer.service.IAccountService;
import com.fansin.spring.cloud.bytetcc.consumer.service.ITransferService;
import org.bytesoft.compensable.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * The type Transfer controller.
 */
@Compensable(interfaceClass = ITransferService.class, confirmableKey = "transferServiceConfirm", cancellableKey = "transferServiceCancel")
@RestController
public class TransferController implements ITransferService {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TransferMapper transferMapper;

	@Autowired
	private IAccountService acctService;

	/**
	 * Transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	@Transactional
	public void transfer(@RequestParam String sourceAcctId, @RequestParam String targetAcctId, @RequestParam double amount) {
		this.acctService.decreaseAmount(sourceAcctId, amount);
		this.increaseAmount(targetAcctId, amount);
	}

	private void increaseAmount(String acctId, double amount) {
		int value = this.transferMapper.increaseAmount(acctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("exec increase: acct= %s, amount= %7.2f%n", acctId, amount);
	}

	/**
	 * Transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
// @ResponseBody
	// @RequestMapping(value = "/transfer", method = RequestMethod.POST)
	@Transactional
	public void _transfer(@RequestParam String sourceAcctId, @RequestParam String targetAcctId, @RequestParam double amount) {
		ResponseEntity<Object> response = restTemplate.postForEntity(
				"http://SPRINGCLOUD-SAMPLE-PROVIDER/decrease?acctId={v1}&amount={v2}", null, Object.class, sourceAcctId,
				amount);
		int statusCode = response.getStatusCodeValue();
		if (statusCode < 200 || statusCode >= 300) {
			throw new IllegalStateException("ERROR!");
		}

		this.increaseAmount(targetAcctId, amount);
	}

}
