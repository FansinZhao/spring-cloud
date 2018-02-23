package com.fansin.spring.cloud.bytetcc.consumer.controller;

import com.fansin.spring.cloud.bytetcc.consumer.mybatis.mapper.TransferMapper;
import com.fansin.spring.cloud.bytetcc.consumer.service.IAccountService;
import com.fansin.spring.cloud.bytetcc.consumer.service.ITransferService;
import lombok.extern.slf4j.Slf4j;
import org.bytesoft.compensable.Compensable;
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
@Compensable(interfaceClass = ITransferService.class, confirmableKey = "transferServiceConfirm", cancellableKey = "transferServiceCancel")
@RestController
@Slf4j
public class TransferController implements ITransferService {

	@Autowired
	private TransferMapper transferMapper;

	@Autowired
	private IAccountService acctService;

	/**
	 * Transfer.
	 *
	 * [standalone]
	 * try-confirm:
	 *
	 * curl -d 'sourceAcctId=1001&targetAcctId=2001&amount=100' http://127.0.0.1:8763/transfer
	 *
	 * try-cancel:(故意将targetAcctId修改为不存在值,更新结果为-1,抛出异常,这时会调用[!provider]服务的cancel)
	 *
	 * curl -d 'sourceAcctId=1001&targetAcctId=3001&amount=100' http://127.0.0.1:8763/transfer
	 *
	 * [docker]
	 * try-confirm:
	 *
	 * curl -d 'sourceAcctId=1001&targetAcctId=2001&amount=100' http://172.16.0.8:8763/transfer
	 *
	 * try-cancel:(故意将targetAcctId修改为不存在值,更新结果为-1,抛出异常,这时会调用[!provider]服务的cancel)
	 *
	 * curl -d 'sourceAcctId=1001&targetAcctId=3001&amount=100' http://172.16.0.8:8763/transfer
	 *
	 *
	 * @param sourceAcctId the source acct id
	 * @param targetAcctId the target acct id
	 * @param amount       the amount
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public void transfer(@RequestParam String sourceAcctId, @RequestParam String targetAcctId, @RequestParam double amount) {
		this.acctService.decreaseAmount(sourceAcctId, amount);
		this.increaseAmount(targetAcctId, amount);
	}

	/**
	 *
	 * @param acctId
	 * @param amount
	 */
	private void increaseAmount(String acctId, double amount) {
		int value = this.transferMapper.increaseAmount(acctId, amount);
		if (value != 1) {
			throw new IllegalStateException("ERROR!");
		}
		log.info("[try] exec increase: acct= {}, amount= {}\n", acctId, amount);
	}

}
