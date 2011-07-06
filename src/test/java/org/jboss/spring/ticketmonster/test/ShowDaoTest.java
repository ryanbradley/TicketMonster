package org.jboss.spring.ticketmonster.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

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
}
