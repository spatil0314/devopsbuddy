package com.devopsbuddy.web.domain.frontend;

/**
 * Includes credit card information. 
 */
public class ProAccountPayload extends BasicAccountPayload {

	private String cardNumber;
	private String cardCode;
	private String cardMonth;
	private String cardYear;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(final String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(final String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCardMonth() {
		return cardMonth;
	}

	public void setCardMonth(final String cardMonth) {
		this.cardMonth = cardMonth;
	}

	public String getCardYear() {
		return cardYear;
	}

	public void setCardYear(final String cardYear) {
		this.cardYear = cardYear;
	}
}