package se.vgregion.verticalprio.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.vgregion.verticalprio.entity.SektorRaad;

public class SektorRaadBeanTest {

	private SektorRaad mkSektorRaad() {
		SektorRaad sr = new SektorRaad();
		sr.setId(12l);
		sr.setParentId(10l);
		sr.setKod("kod");
		sr.setBeskrivning("beskrivning");
		sr.setKortBeskrivning("kortBeskrivning");
		sr.setAble(false);
		sr.setChildren(new ArrayList<SektorRaad>());
		return sr;
	}

	private SektorRaadBean mkSektorRaadBean() {
		SektorRaadBean sr = new SektorRaadBean();
		sr.setId(12l);
		sr.setParentId(10l);
		sr.setKod("kod");
		sr.setBeskrivning("beskrivning");
		sr.setKortBeskrivning("kortBeskrivning");
		sr.setAble(false);
		sr.setBeanChildren(new ArrayList<SektorRaadBean>());
		return sr;
	}

	private void checkTypicalTestValues(SektorRaad sr) {
		assertEquals("kod", sr.getKod());
		assertEquals("beskrivning", sr.getBeskrivning());
		assertEquals("kortBeskrivning", sr.getKortBeskrivning());
		assertEquals((Long) 12l, sr.getId());
		assertEquals((Long) 10l, sr.getParentId());
		assertFalse(sr.isAble());
	}

	@Test
	public void toSektorRaadBean() {
		SektorRaad sr = mkSektorRaad();
		sr.getChildren().add(mkSektorRaad());
		SektorRaadBean srb = SektorRaadBean.toSektorRaadBean(sr);
		checkTypicalTestValues(srb);
		checkTypicalTestValues(srb.getBeanChildren().get(0));
	}

	@Test
	public void toSektorRaadBeans() {
		SektorRaad sr = mkSektorRaad();
		sr.getChildren().add(mkSektorRaad());
		List<SektorRaad> sektorRaads = new ArrayList<SektorRaad>();
		sektorRaads.add(sr);

		List<SektorRaadBean> result = SektorRaadBean.toSektorRaadBeans(sektorRaads);
		SektorRaadBean srb = result.get(0);

		checkTypicalTestValues(srb);
		checkTypicalTestValues(srb.getBeanChildren().get(0));
	}

	@Test
	public void toSektorRaad() {
		SektorRaadBean bean = mkSektorRaadBean();
		bean.getBeanChildren().add(mkSektorRaadBean());
		SektorRaad result = SektorRaadBean.toSektorRaad(bean);
		checkTypicalTestValues(result);
		checkTypicalTestValues(result.getChildren().get(0));
	}

	@Test
	public void toSektorRaads() {
		SektorRaadBean bean = mkSektorRaadBean();
		bean.getBeanChildren().add(mkSektorRaadBean());
		List<SektorRaadBean> sektorRaads = new ArrayList<SektorRaadBean>();
		sektorRaads.add(bean);

		List<SektorRaad> result = SektorRaadBean.toSektorRaads(sektorRaads);
		SektorRaad itemResult = result.get(0);

		checkTypicalTestValues(itemResult);
		checkTypicalTestValues(itemResult.getChildren().get(0));
	}

}
