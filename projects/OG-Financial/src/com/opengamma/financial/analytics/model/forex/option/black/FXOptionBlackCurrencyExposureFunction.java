/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.forex.option.black;

import java.util.Collections;
import java.util.Set;

import com.opengamma.analytics.financial.forex.calculator.CurrencyExposureBlackSmileForexCalculator;
import com.opengamma.analytics.financial.forex.calculator.CurrencyExposureBlackTermStructureForexCalculator;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.model.option.definition.ForexOptionDataBundle;
import com.opengamma.analytics.financial.model.option.definition.YieldCurveWithBlackForexTermStructureBundle;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.money.MultipleCurrencyAmount;

/**
 * 
 */
public class FXOptionBlackCurrencyExposureFunction extends FXOptionBlackMultiValuedFunction {
  private static final CurrencyExposureBlackSmileForexCalculator SMILE_CALCULATOR = CurrencyExposureBlackSmileForexCalculator.getInstance();
  private static final CurrencyExposureBlackTermStructureForexCalculator FLAT_CALCULATOR = CurrencyExposureBlackTermStructureForexCalculator.getInstance();

  public FXOptionBlackCurrencyExposureFunction() {
    super(ValueRequirementNames.FX_CURRENCY_EXPOSURE);
  }

  @Override
  protected Set<ComputedValue> getResult(final InstrumentDerivative forex, final ForexOptionDataBundle<?> data, final ComputationTarget target,
      final Set<ValueRequirement> desiredValues, final FunctionInputs inputs, final ValueSpecification spec, final FunctionExecutionContext executionContext) {
    if (data instanceof YieldCurveWithBlackForexTermStructureBundle) {
      final MultipleCurrencyAmount result = FLAT_CALCULATOR.visit(forex, data);
      return Collections.singleton(new ComputedValue(spec, result));
    }
    final MultipleCurrencyAmount result = SMILE_CALCULATOR.visit(forex, data);
    return Collections.singleton(new ComputedValue(spec, result));
  }
}
