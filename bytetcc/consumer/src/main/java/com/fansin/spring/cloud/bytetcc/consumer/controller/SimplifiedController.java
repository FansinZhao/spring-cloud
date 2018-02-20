package com.fansin.spring.cloud.bytetcc.consumer.controller;

import com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.consumer.service.IAccountService;
import com.fansin.spring.cloud.bytetcc.consumer.service.ITransferService;
import org.bytesoft.compensable.Compensable;
import org.bytesoft.compensable.CompensableCancel;
import org.bytesoft.compensable.CompensableConfirm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Simplified controller.
 */
@Compensable(interfaceClass = ITransferService.class, simplified = true)
@RestController
public class SimplifiedController implements ITransferService {
	@Autowired
	private TransferMapper transferMapper;

	@Autowired
	private IAccountService acctService;

	/**
	 *
	 * curl -d 'sourceAcctId=1001&targetAcctId=2001&amount=100' http://127.0.0.1:8763/simplified/transfer
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/simplified/transfer", method = RequestMethod.POST)
	@Transactional
	public void transfer(@RequestParam String sourceAcctId, @RequestParam String targetAcctId, @RequestParam double amount) {
		this.acctService.decreaseAmount(sourceAcctId, amount);
		this.increaseAmount(targetAcctId, amount);

		// throw new IllegalStateException("rollback!");
	}

	private void increaseAmount(String acctId, double amount) {
		int value = this.transferMapper.increaseAmount(acctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("exec increase: acct= %s, amount= %7.2f%n", acctId, amount);
	}

	/**
	 * Confirm transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@CompensableConfirm
	@Transactional
	public void confirmTransfer(String sourceAcctId, String targetAcctId, double amount) {
		int value = this.transferMapper.confirmIncrease(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("done increase: acct= %s, amount= %7.2f%n", targetAcctId, amount);
	}

	/**
	 * Cancel transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@CompensableCancel
	@Transactional
	public void cancelTransfer(String sourceAcctId, String targetAcctId, double amount) {
		int value = this.transferMapper.cancelIncrease(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", targetAcctId, amount);
	}

}
