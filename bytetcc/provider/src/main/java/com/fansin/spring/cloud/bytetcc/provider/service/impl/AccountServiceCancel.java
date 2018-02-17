package com.fansin.spring.cloud.bytetcc.provider.service.impl;

import com.fansin.spring.cloud.bytetcc.provider.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Account service cancel.
 */
@Service("accountServiceCancel")
public class AccountServiceCancel implements IAccountService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	@Transactional
	public void increaseAmount(String acctId, double amount) {
		int value = this.jdbcTemplate.update("update tb_account_one set frozen = frozen - ? where acct_id = ?", amount, acctId);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("undo increase: acct= %s, amount= %7.2f%n", acctId, amount);
	}

	@Override
	@Transactional
	public void decreaseAmount(String acctId, double amount) {
		int value = this.jdbcTemplate.update(
				"update tb_account_one set amount = amount + ?, frozen = frozen - ? where acct_id = ?", amount, amount, acctId);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		System.out.printf("undo decrease: acct= %s, amount= %7.2f%n", acctId, amount);
	}

}
