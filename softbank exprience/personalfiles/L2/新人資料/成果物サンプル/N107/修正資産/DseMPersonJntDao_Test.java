package test.jp.co.softbankmobile.dse.integration.dao.master;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringWithJNDIRunner;

import jp.co.softbankmobile.dse.integration.dao.master.IdseMPersonJntDao;
import jp.co.softbankmobile.dse.integration.entity.DseMPersonJnt;
import jp.co.softbankmobile.dse.integration.entity.DseMPersonModelImpl;

@RunWith(SpringWithJNDIRunner.class)
@ContextConfiguration(locations = { "classpath*:Context.xml" })
public class DseMPersonJntDao_Test {
	@Resource
	private IdseMPersonJntDao dao;

	@Test
	public void testMethod1() throws ParseException {
		// toDomain確認点
		DseMPersonJnt rtnFirst = dao.findChargeName("cyuki", "9000002L3e");
		// toDomainPerson確認点
		List<DseMPersonModelImpl> rtnSec = dao.findbyProviderDelFlg("SBTM", "workgroup01");
		// findbyChargeId確認点
		List <DseMPersonJnt> rtnThird = dao.findbyChargeId("cyuki");

		Assert.assertNotNull(rtnFirst);
		Assert.assertNotNull(rtnSec);
		Assert.assertNotNull(rtnThird);
	}
	
}
