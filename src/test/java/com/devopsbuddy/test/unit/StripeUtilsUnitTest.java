package com.devopsbuddy.test.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;

import org.junit.Test;

import com.devopsbuddy.test.integration.StripeIntegrationTest;
import com.devopsbuddy.utils.StripeUtils;
import com.devopsbuddy.web.domain.frontend.ProAccountPayload;

public class StripeUtilsUnitTest {

	@Test
	public void createStripeTokenParamsFromUserPayload() {

		final ProAccountPayload payload = new ProAccountPayload();
		final String cardNumber = StripeIntegrationTest.TEST_CC_NUMBER;
		payload.setCardNumber(cardNumber);
		final String cardCode = StripeIntegrationTest.TEST_CC_CVC_NBR;
		payload.setCardCode(cardCode);
		final String cardMonth = String.valueOf(StripeIntegrationTest.TEST_CC_EXP_MONTH);
		payload.setCardMonth(cardMonth);
		final String cardYear = String.valueOf(LocalDate.now(Clock.systemUTC()).getYear() + 1);
		payload.setCardYear(cardYear);

		final Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
		final Map<String, Object> cardParams = (Map<String, Object>) tokenParams.get(StripeUtils.STRIPE_CARD_KEY);
		assertThat(cardNumber, is(cardParams.get(StripeUtils.STRIPE_CARD_NUMBER_KEY)));
		assertThat(cardMonth, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_MONTH_KEY))));
		assertThat(cardYear, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_YEAR_KEY))));
		assertThat(cardCode, is(cardParams.get(StripeUtils.STRIPE_CVC_KEY)));
	}
}