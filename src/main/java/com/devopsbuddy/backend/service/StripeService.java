package com.devopsbuddy.backend.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Token;

@Service
public class StripeService {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(StripeService.class);

	@Autowired
	private String stripeKey;

	/**
	 * Creates a Stripe customer and returns the Stripe customer id
	 * @param tokenParams The credit card details to obtain a token. These will never be stored in the DB
	 * @param customerParams The parameters which identify the customer
	 * @return The stripe customer id which can then be used to perform billing operations at a later stage
	 * @throws com.devopsbuddy.exceptions.StripeException If an error occurred while interacting with Stripe
	 */
	public String createCustomer(final Map<String, Object> tokenParams, final Map<String, Object> customerParams) {

		Stripe.apiKey = stripeKey;

		String stripeCustomerId = null;
		try {
			final Token token = Token.create(tokenParams);
			customerParams.put("source", token.getId());
			final Customer customer = Customer.create(customerParams);
			stripeCustomerId = customer.getId();
		} catch (final StripeException e) {
			LOG.error("An exception occurred while creating the Stripe customer", e);
			throw new com.devopsbuddy.exceptions.StripeException(e);
		} 
		return stripeCustomerId;
	}
}
