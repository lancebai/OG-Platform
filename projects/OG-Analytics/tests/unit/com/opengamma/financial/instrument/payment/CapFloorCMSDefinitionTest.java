/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.instrument.payment;

import static org.testng.AssertJUnit.assertEquals;

import javax.time.calendar.LocalDate;
import javax.time.calendar.Period;
import javax.time.calendar.ZonedDateTime;

import org.testng.annotations.Test;

import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.financial.instrument.annuity.AnnuityCouponFixedDefinition;
import com.opengamma.financial.instrument.annuity.AnnuityCouponIborDefinition;
import com.opengamma.financial.instrument.index.CMSIndex;
import com.opengamma.financial.instrument.index.IborIndex;
import com.opengamma.financial.instrument.swap.ZZZSwapFixedIborDefinition;
import com.opengamma.financial.interestrate.payments.CapFloorCMS;
import com.opengamma.financial.interestrate.payments.CouponCMS;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtil;

/**
 * Test related to CouponCMSDefinition construction.
 */
public class CapFloorCMSDefinitionTest {

  //Swap 5Y
  private static final Currency CUR = Currency.USD;
  private static final Calendar CALENDAR = new MondayToFridayCalendar("A");
  private static final BusinessDayConvention BUSINESS_DAY = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Modified Following");
  private static final boolean IS_EOM = true;
  private static final Period ANNUITY_TENOR = Period.ofYears(5);
  private static final ZonedDateTime SETTLEMENT_DATE = DateUtil.getUTCDate(2011, 3, 17);
  //Fixed leg: Semi-annual bond
  private static final Period FIXED_PAYMENT_PERIOD = Period.ofMonths(6);
  private static final DayCount FIXED_DAY_COUNT = DayCountFactory.INSTANCE.getDayCount("30/360");
  private static final double RATE = 0.0325;
  private static final boolean FIXED_IS_PAYER = true;
  private static final AnnuityCouponFixedDefinition FIXED_ANNUITY = AnnuityCouponFixedDefinition.from(CUR, SETTLEMENT_DATE, ANNUITY_TENOR, FIXED_PAYMENT_PERIOD, CALENDAR, FIXED_DAY_COUNT,
      BUSINESS_DAY, IS_EOM, 1.0, RATE, FIXED_IS_PAYER);
  //Ibor leg: quarterly money
  private static final Period INDEX_TENOR = Period.ofMonths(3);
  private static final int SETTLEMENT_DAYS = 2;
  private static final DayCount DAY_COUNT = DayCountFactory.INSTANCE.getDayCount("Actual/360");
  private static final IborIndex IBOR_INDEX = new IborIndex(CUR, INDEX_TENOR, SETTLEMENT_DAYS, CALENDAR, DAY_COUNT, BUSINESS_DAY, IS_EOM);
  private static final AnnuityCouponIborDefinition IBOR_ANNUITY = AnnuityCouponIborDefinition.from(SETTLEMENT_DATE, ANNUITY_TENOR, 1.0, IBOR_INDEX, !FIXED_IS_PAYER);
  // CMS coupon construction
  private static final CMSIndex CMS_INDEX = new CMSIndex(FIXED_PAYMENT_PERIOD, FIXED_DAY_COUNT, IBOR_INDEX, ANNUITY_TENOR);
  private static final ZZZSwapFixedIborDefinition SWAP_DEFINITION = new ZZZSwapFixedIborDefinition(FIXED_ANNUITY, IBOR_ANNUITY);
  private static final ZonedDateTime PAYMENT_DATE = DateUtil.getUTCDate(2011, 4, 6);
  private static final ZonedDateTime FIXING_DATE = DateUtil.getUTCDate(2010, 12, 30);
  private static final ZonedDateTime ACCRUAL_START_DATE = DateUtil.getUTCDate(2011, 1, 5);
  private static final ZonedDateTime ACCRUAL_END_DATE = DateUtil.getUTCDate(2011, 4, 5);
  private static final DayCount PAYMENT_DAY_COUNT = DayCountFactory.INSTANCE.getDayCount("Actual/360");
  private static final double ACCRUAL_FACTOR = PAYMENT_DAY_COUNT.getDayCountFraction(ACCRUAL_START_DATE, ACCRUAL_END_DATE);
  private static final double NOTIONAL = 1000000; //1m
  private static final ZonedDateTime FAKE_DATE = DateUtil.getUTCDate(0, 1, 1);
  private static final CouponFloatingDefinition COUPON = new CouponFloatingDefinition(CUR, PAYMENT_DATE, ACCRUAL_START_DATE, ACCRUAL_END_DATE, ACCRUAL_FACTOR, NOTIONAL, FAKE_DATE);
  private static final CouponFloatingDefinition FLOAT_COUPON = CouponFloatingDefinition.from(COUPON, FIXING_DATE);
  private static final CouponCMSDefinition CMS_COUPON_DEFINITION = CouponCMSDefinition.from(FLOAT_COUPON, SWAP_DEFINITION, CMS_INDEX);
  // CMS cap
  private static final double STRIKE = 0.04;
  private static final boolean IS_CAP = true;
  private static final CapFloorCMSDefinition CMS_CAP_DEFINITION = CapFloorCMSDefinition.from(CMS_COUPON_DEFINITION, STRIKE, IS_CAP);
  // to derivatives
  private static final LocalDate REFERENCE_DATE = LocalDate.of(2010, 8, 18);
  private static final String FUNDING_CURVE_NAME = " Funding";
  private static final String FORWARD_CURVE_NAME = " Forward";
  private static final String[] CURVES_NAME = {FUNDING_CURVE_NAME, FORWARD_CURVE_NAME};

  private static final CouponCMS CMS_COUPON = (CouponCMS) CMS_COUPON_DEFINITION.toDerivative(REFERENCE_DATE, CURVES_NAME);
  private static final CapFloorCMS CMS_CAP = (CapFloorCMS) CMS_CAP_DEFINITION.toDerivative(REFERENCE_DATE, CURVES_NAME);

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullCoupon() {
    CapFloorCMSDefinition.from(null, STRIKE, IS_CAP);
  }

  @Test
  public void testGetter() {
    assertEquals(STRIKE, CMS_CAP.geStrike(), 1E-10);
    assertEquals(IS_CAP, CMS_CAP.isCap());
  }

  @Test
  public void testEqual() {
    CapFloorCMSDefinition floor = CapFloorCMSDefinition.from(CMS_COUPON_DEFINITION, STRIKE, !IS_CAP);
    assertEquals(floor == CMS_CAP_DEFINITION, false);
    CapFloorCMSDefinition capPlus = CapFloorCMSDefinition.from(CMS_COUPON_DEFINITION, STRIKE + 0.01, IS_CAP);
    assertEquals(capPlus == CMS_CAP_DEFINITION, false);
  }

  @Test
  public void testToDerivative() {
    CapFloorCMS capDirect = CapFloorCMS.from(CMS_COUPON, STRIKE, IS_CAP);
    assertEquals(capDirect, CMS_CAP);
  }

}
