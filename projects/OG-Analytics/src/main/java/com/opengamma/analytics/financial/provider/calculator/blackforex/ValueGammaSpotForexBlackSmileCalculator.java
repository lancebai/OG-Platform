/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.calculator.blackforex;

import com.opengamma.analytics.financial.forex.derivative.ForexOptionVanilla;
import com.opengamma.analytics.financial.forex.provider.ForexOptionVanillaBlackSmileMethod;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorAdapter;
import com.opengamma.analytics.financial.provider.description.forex.BlackForexSmileProviderInterface;
import com.opengamma.util.money.CurrencyAmount;

/**
 * Calculates the value gamma (second order derivative with respect to the spot rate)
 * multiplied by the spot rate for Forex derivatives in the Black (Garman-Kohlhagen) world.
 */
public class ValueGammaSpotForexBlackSmileCalculator extends InstrumentDerivativeVisitorAdapter<BlackForexSmileProviderInterface, CurrencyAmount> {

  /**
   * The unique instance of the calculator.
   */
  private static final ValueGammaSpotForexBlackSmileCalculator INSTANCE = new ValueGammaSpotForexBlackSmileCalculator();

  /**
   * Gets the calculator instance.
   * @return The calculator.
   */
  public static ValueGammaSpotForexBlackSmileCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Constructor.
   */
  ValueGammaSpotForexBlackSmileCalculator() {
  }

  /**
   * The methods used by the different instruments.
   */
  private static final ForexOptionVanillaBlackSmileMethod METHOD_FXOPTIONVANILLA = ForexOptionVanillaBlackSmileMethod.getInstance();

  /**
   * The value gamma is provided with "direct quote", i.e. (1 foreign = x domestic) and not the reverse quote (1 domestic = x foreign).
   * @param optionForex The Forex option.
   * @param smileMulticurves The curve and smile data.
   * @return The value gamma multiplied by the spot.
   */
  @Override
  public CurrencyAmount visitForexOptionVanilla(final ForexOptionVanilla optionForex, final BlackForexSmileProviderInterface smileMulticurves) {
    return METHOD_FXOPTIONVANILLA.gammaSpot(optionForex, smileMulticurves, true);
  }
}
