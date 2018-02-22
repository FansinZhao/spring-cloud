package com.fansin.spring.cloud.bytetcc.consumer.service.impl;

import com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.consumer.service.ITransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-21 下午23:51
 */
@Service("transferServiceCancel")
@Slf4j
public class TransferServiceCancelImpl implements ITransferService {

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
	@Transactional(rollbackFor = Exception.class)
	public void transfer(String sourceAcctId, String targetAcctId, double amount) {
		int value = this.transferMapper.cancelIncrease(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		log.info("[cancel] exec decrease: sourceAcct={}, acct= {}, amount= {}\n",sourceAcctId, targetAcctId, amount);
	}

}
