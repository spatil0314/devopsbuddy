package com.devopsbuddy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.web.i18n.I18nService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevopsbuddyApplicationTests {

	@Autowired
	private I18nService i18nService;

	@Test
	public void testMessageByLocaleService() {
		final String expected = "Bootstrap starter template";
		final String messageId="index.main.callout";
		final String actual = i18nService.getMessage(messageId);
		Assert.assertEquals("The actual and expected Strings don't match", expected, actual);
	}

}
