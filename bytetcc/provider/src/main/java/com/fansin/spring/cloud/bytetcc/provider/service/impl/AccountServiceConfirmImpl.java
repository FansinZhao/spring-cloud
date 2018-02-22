package com.fansin.spring.cloud.bytetcc.provider.service.impl;

import com.fansin.spring.cloud.bytetcc.provider.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.provider.service.IAccountService;
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
@Service("accountServiceConfirm")
@Slf4j
public class AccountServiceConfirmImpl implements IAccountService {

	@Autowired
	private TransferMapper transferMapper;

	/**
	 *
	 * @param acctId
	 * @param amount    the amount
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void increaseAmount(String acctId, double amount) {
		int value = transferMapper.confirmIncreaseAmount(acctId,amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		log.info("[confirm] done increase: acct= {}, amount= {}\n", acctId, amount);
	}

	/**
	 *
	 * @param acctId
	 * @param amount    the amount
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void decreaseAmount(String acctId, double amount) {
		int value = transferMapper.confirmDecreaseAmount(acctId,amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		log.info("[confirm] done decrease: acct= {}, amount= {}\n", acctId, amount);
	}

}
