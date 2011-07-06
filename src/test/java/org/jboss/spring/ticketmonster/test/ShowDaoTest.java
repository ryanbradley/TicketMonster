package org.jboss.spring.ticketmonster.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.ShowTime;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml",
"classpath:META-INF/spring/ticketmonster-business-context.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=true)
public class ShowDaoTest {

	@Autowired
	private ShowDao showDao;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	
	@Test
	public void testGetEvent() {
		Show s1 = showDao.getShow((long)1);
		Show s2 = showDao.getShow((long)3);
		
		Assert.assertEquals("City Central Concert Hall", s1.getVenue().getName());
		Assert.assertEquals("Rock concert of the decade", s1.getEvent().getName());
		
		Assert.assertEquals("Sydney Opera House", s2.getVenue().getName());
		Assert.assertEquals("Rock concert of the decade", s2.getEvent().getName());
	}
	
	@Test
	public void testGetShowTimes() throws ParseException {
		List<ShowTime> showTimes = new ArrayList<ShowTime>();
		
		showTimes = showDao.getShowTimes((long)1, (long)1);
		
		Date firstShowExpected = formatter.parse("Sat Jan 01 19:00:00 EST 2011");
		Date secondShowExpected = formatter.parse("Sun Jan 02 19:00:00 EST 2011");
		
		Date thirdShowExpected = formatter.parse("Mon Jan 03 19:30:00 EST 2011");
		Date fourthShowExpected = formatter.parse("Tue Jan 04 19:30:00 EST 2011");
		
		Date firstShow = showTimes.get(0).getDate();
		Assert.assertEquals(firstShowExpected, firstShow);
		Date secondShow = showTimes.get(1).getDate();
		Assert.assertEquals(secondShowExpected, secondShow);
		
		showTimes = showDao.getShowTimes((long)1, (long)2);
		Date thirdShow = showTimes.get(0).getDate();
		Assert.assertEquals(thirdShowExpected, thirdShow);
		Date fourthShow = showTimes.get(1).getDate();
		Assert.assertEquals(fourthShowExpected, fourthShow);
	}
	
	@Test
	public void testGetCategories() {
		List<PriceCategory> categories = showDao.getCategories((long)1, (long)1);
		
		PriceCategory category = categories.get(0);
		Assert.assertEquals(219.50, category.getPrice(), 0);
		category = categories.get(1);
		Assert.assertEquals(199.50, category.getPrice(), 0);
		category = categories.get(2);
		Assert.assertEquals(179.50, category.getPrice(), 0);
		category = categories.get(3);
		Assert.assertEquals(149.50, category.getPrice(), 0);
		
		categories = showDao.getCategories((long)1, (long)2);
		
		category = categories.get(0);
		Assert.assertEquals(157.50, category.getPrice(),0);
		category = categories.get(1);
		Assert.assertEquals(167.75, category.getPrice(),0);
		category = categories.get(2);
		Assert.assertEquals(187.50, category.getPrice(),0);
		category = categories.get(3);
		Assert.assertEquals(197.75, category.getPrice(),0);
		category = categories.get(4);
		Assert.assertEquals(157.50, category.getPrice(),0);
		category = categories.get(5);
		Assert.assertEquals(167.75, category.getPrice(),0);
		category = categories.get(6);
		Assert.assertEquals(145.0, category.getPrice(),0);
		category = categories.get(7);
		Assert.assertEquals(155.0, category.getPrice(),0);
		category = categories.get(8);
		Assert.assertEquals(145.0, category.getPrice(),0);
		category = categories.get(9);
		Assert.assertEquals(155.0, category.getPrice(),0);
		category = categories.get(10);
		Assert.assertEquals(145.0, category.getPrice(),0);
		category = categories.get(11);
		Assert.assertEquals(155.0, category.getPrice(),0);
		category = categories.get(12);
		Assert.assertEquals(112.5, category.getPrice(),0);
		category = categories.get(13);
		Assert.assertEquals(122.5, category.getPrice(),0);
	}
	
}
