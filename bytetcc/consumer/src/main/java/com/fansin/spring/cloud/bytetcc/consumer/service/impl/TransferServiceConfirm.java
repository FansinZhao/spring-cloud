package com.fansin.spring.cloud.bytetcc.consumer.service.impl;

import com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.consumer.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Transfer service confirm.
 */
@Service("transferServiceConfirm")
public class TransferServiceConfirm implements ITransferService {
	@Autowired
	private TransferMapper transferMapper;

	/**
	 * Transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@Override
	@Transactional
	public void transfer(String sourceAcctId, String targetAcctId, double amount) {
		int value = this.transferMapper.confirmIncrease(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("done increase: acct= %s, amount= %7.2f%n", targetAcctId, amount);
	}

}
