/*
 * @author Constantin Chelban (constantink@saltedge.com)
 * Copyright (c) 2020 Salt Edge.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.saltedge.connector.sdk.api.services;

import com.saltedge.connector.sdk.api.models.Account;
import com.saltedge.connector.sdk.api.models.AccountBalance;
import com.saltedge.connector.sdk.api.models.Amount;
import com.saltedge.connector.sdk.api.models.ExchangeRate;
import com.saltedge.connector.sdk.api.models.requests.FundsConfirmationRequest;
import com.saltedge.connector.sdk.models.Token;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FundsServiceTests extends BaseServicesTests {
	@Autowired
	protected FundsService testService;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
        given(providerService.getExchangeRates()).willReturn(Lists.list(
                new ExchangeRate("EUR", 1.0f),
                new ExchangeRate("USD", 0.90f),
                new ExchangeRate("GBP", 1.502f)
        ));
        Account account = new Account();
        account.setIban("iban");
        account.setBalances(Lists.list(new AccountBalance("100.0", "GBP", "openingBooked")));
        account.setCurrencyCode("EUR");
        given(providerService.getAccountsOfUser("1")).willReturn(Lists.list(account));
	}

	@Test
	public void givenValidRequest_whenConfirmFunds_thenReturnTrue() {
		// given
        Token token = new Token("1");
        Account account = new Account();
        account.setIban("iban");
        account.setCurrencyCode("EUR");
        FundsConfirmationRequest request = new FundsConfirmationRequest(
                account,
                new Amount("1.0", "EUR")
        );

		// when
        boolean result = testService.confirmFunds(token, request);

		// then
        assertThat(result).isTrue();
	}

    @Test
    public void givenValidRequestWithBigAmount_whenConfirmFunds_thenReturnTrue() {
        // given
        Token token = new Token("1");
        Account account = new Account();
        account.setIban("iban");
        account.setCurrencyCode("EUR");
        FundsConfirmationRequest request = new FundsConfirmationRequest(
                account,
                new Amount("123456789.0", "USD")
        );

        // when
        boolean result = testService.confirmFunds(token, request);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void givenInvalidIban_whenConfirmFunds_thenReturnTrue() {
        // given
        Token token = new Token("1");
        Account account = new Account();
        account.setIban("iban1");
        account.setCurrencyCode("EUR");
        FundsConfirmationRequest request = new FundsConfirmationRequest(
                account,
                new Amount("1.0", "USD")
        );

        // when
        boolean result = testService.confirmFunds(token, request);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void givenInvalidCurrency_whenConfirmFunds_thenReturnTrue() {
        // given
        Token token = new Token("1");
        Account account = new Account();
        account.setIban("iban1");
        account.setCurrencyCode("EUR");
        FundsConfirmationRequest request = new FundsConfirmationRequest(
                account,
                new Amount("1.0", "RUB")
        );

        // when
        boolean result = testService.confirmFunds(token, request);

        // then
        assertThat(result).isFalse();
    }

}
