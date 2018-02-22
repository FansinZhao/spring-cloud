package com.fansin.spring.cloud.bytetcc.consumer.controller;

import com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.consumer.service.IAccountService;
import com.fansin.spring.cloud.bytetcc.consumer.service.ITransferService;
import lombok.extern.slf4j.Slf4j;
import org.bytesoft.compensable.Compensable;
import org.bytesoft.compensable.CompensableCancel;
import org.bytesoft.compensable.CompensableConfirm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * Created with IntelliJ IDEA.
 *
 * ByteTCC倾向于认为: 使用SpringCloud时, 直接对外提供服务的Controller应该明确规划好它是普通服务还是TCC服务.<br />
 * 因此, 0.4.x版本强制对外提供TCC服务的Controller必须加@Compensable注解(若没有实质业务, 也可以不必指定confirmableKey和cancellableKey).<br />
 * 若不加@Compensable注解, 则ByteTCC将其当成普通服务对待, 不接收Consumer端传播的事务上下文. 若它后续调用TCC服务, 则将开启新的TCC全局事务.
 *
 * @author fansin
 * @version 1.0
 * @date 18-2-21 下午23:51
 */
@SuppressWarnings("ALL")
@Compensable(interfaceClass = ITransferService.class, simplified = true)
@RestController
@Slf4j
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
	@Transactional(rollbackFor = Exception.class)
	public void transfer(@RequestParam String sourceAcctId, @RequestParam String targetAcctId, @RequestParam double amount) {
		this.acctService.decreaseAmount(sourceAcctId, amount);

		int value = this.transferMapper.increaseAmount(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		log.info("[try] exec increase: acct= {}, amount= {}\n", targetAcctId, amount);
	}

	/**
	 * Confirm transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@CompensableConfirm
	@Transactional(rollbackFor = Exception.class)
	public void confirmTransfer(String sourceAcctId, String targetAcctId, double amount) {
		int value = this.transferMapper.confirmIncrease(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		log.info("[confirm] done increase: sourceAcct={}, acct= {}, amount= {}\n",sourceAcctId, targetAcctId, amount);
	}

	/**
	 * Cancel transfer.
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@CompensableCancel
	@Transactional(rollbackFor = Exception.class)
	public void cancelTransfer(String sourceAcctId, String targetAcctId, double amount) {
		int value = this.transferMapper.cancelIncrease(targetAcctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		log.info("[cancel] undo increase: sourceAcct={}, acct= {}, amount= {}\n",sourceAcctId, targetAcctId, amount);
	}

}
