package com.fansin.spring.cloud.bytetcc.consumer.service.impl;

import com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.consumer.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Transfer service cancel.
 */
@Service("transferServiceCancel")
public class TransferServiceCancel implements ITransferService {

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
		int value = this.transferMapper.cancelIncrease(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("exec decrease: acct= %s, amount= %7.2f%n", targetAcctId, amount);
	}

}
