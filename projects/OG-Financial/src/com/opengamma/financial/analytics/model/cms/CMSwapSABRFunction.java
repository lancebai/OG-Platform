/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.cms;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.holiday.HolidaySource;
import com.opengamma.core.region.RegionSource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.analytics.conversion.FixedIncomeConverterDataProvider;
import com.opengamma.financial.analytics.conversion.SwapSecurityConverter;
import com.opengamma.financial.analytics.conversion.SwapSecurityUtils;
import com.opengamma.financial.analytics.fixedincome.InterestRateInstrumentType;
import com.opengamma.financial.analytics.ircurve.YieldCurveFunction;
import com.opengamma.financial.analytics.volatility.cube.VolatilityCubeFunctionHelper;
import com.opengamma.financial.analytics.volatility.fittedresults.SABRFittedSurfaces;
import com.opengamma.financial.convention.ConventionBundleSource;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.interestrate.YieldCurveBundle;
import com.opengamma.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.financial.model.option.definition.SABRInterestRateExtrapolationParameters;
import com.opengamma.financial.model.option.definition.SABRInterestRateParameters;
import com.opengamma.financial.model.volatility.smile.function.SABRFormulaData;
import com.opengamma.financial.model.volatility.smile.function.VolatilityFunctionFactory;
import com.opengamma.financial.model.volatility.smile.function.VolatilityFunctionProvider;
import com.opengamma.financial.model.volatility.surface.VolatilitySurface;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityUtils;
import com.opengamma.financial.security.swap.SwapSecurity;
import com.opengamma.util.money.Currency;

/**
 * 
 */
public abstract class CMSwapSABRFunction extends AbstractFunction.NonCompiledInvoker {
  @SuppressWarnings("unchecked")
  private static final VolatilityFunctionProvider<SABRFormulaData> SABR_FUNCTION = (VolatilityFunctionProvider<SABRFormulaData>) VolatilityFunctionFactory
      .getCalculator(VolatilityFunctionFactory.HAGAN);
  private static final double CUT_OFF = 0.1;
  private static final double MU = 5;  

  private final boolean _useSABRExtrapolation;
  private final String _forwardCurveName;
  private final String _fundingCurveName;
  private final VolatilityCubeFunctionHelper _helper;
  private SwapSecurityConverter _swapVisitor;
  private FixedIncomeConverterDataProvider _converter;

  public CMSwapSABRFunction(final String currency, final String definitionName, final String useSABRExtrapolation, String forwardCurveName, String fundingCurveName) {
    this(Currency.of(currency), definitionName, Boolean.parseBoolean(useSABRExtrapolation), forwardCurveName, fundingCurveName);
  }

  public CMSwapSABRFunction(final Currency currency, final String definitionName, final boolean useSABRExtrapolation, String forwardCurveName, String fundingCurveName) {
    Validate.notNull(currency, "currency");
    Validate.notNull(definitionName, "cube definition name");
    Validate.notNull(forwardCurveName, "forward curve name");
    Validate.notNull(fundingCurveName, "funding curve name");
    _helper = new VolatilityCubeFunctionHelper(currency, definitionName);
    _useSABRExtrapolation = useSABRExtrapolation;
    _forwardCurveName = forwardCurveName;
    _fundingCurveName = fundingCurveName;
  }

  @Override
  public void init(final FunctionCompilationContext context) {
    final HolidaySource holidaySource = OpenGammaCompilationContext.getHolidaySource(context);
    final RegionSource regionSource = OpenGammaCompilationContext.getRegionSource(context);
    final ConventionBundleSource conventionSource = OpenGammaCompilationContext.getConventionBundleSource(context);
    _swapVisitor = new SwapSecurityConverter(holidaySource, conventionSource, regionSource);
    _converter = new FixedIncomeConverterDataProvider(conventionSource);
  }

  @Override
  public ComputationTargetType getTargetType() {
    return ComputationTargetType.SECURITY;
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final Set<ValueRequirement> requirements = new HashSet<ValueRequirement>();
    requirements.add(getCubeRequirement(target));
    if (_forwardCurveName.equals(_fundingCurveName)) {
      requirements.add(getCurveRequirement(target, _forwardCurveName, null, null));
      return requirements;
    }
    requirements.add(getCurveRequirement(target, _forwardCurveName, _forwardCurveName, _fundingCurveName));
    requirements.add(getCurveRequirement(target, _fundingCurveName, _forwardCurveName, _fundingCurveName));
    return requirements;
  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    if (target.getType() != ComputationTargetType.SECURITY) {
      return false;
    }
    final FinancialSecurity security = (FinancialSecurity) target.getSecurity();
    return security instanceof SwapSecurity && 
       (SwapSecurityUtils.getSwapType(((SwapSecurity) security)) == InterestRateInstrumentType.SWAP_FIXED_CMS ||
        SwapSecurityUtils.getSwapType(((SwapSecurity) security)) == InterestRateInstrumentType.SWAP_CMS_CMS ||
        SwapSecurityUtils.getSwapType(((SwapSecurity) security)) == InterestRateInstrumentType.SWAP_IBOR_CMS);
  }

  protected ValueRequirement getCurveRequirement(final ComputationTarget target, final String curveName,
      final String advisoryForward, final String advisoryFunding) {
    return YieldCurveFunction.getCurveRequirement(FinancialSecurityUtils.getCurrency(target.getSecurity()), curveName,
        advisoryForward, advisoryFunding);
  }

  protected ValueRequirement getCubeRequirement(final ComputationTarget target) {
    final ValueProperties properties = ValueProperties.with(ValuePropertyNames.CUBE, _helper.getDefinitionName()).get();
    return new ValueRequirement(ValueRequirementNames.SABR_SURFACES, FinancialSecurityUtils.getCurrency(target.getSecurity()), properties);
  }

  protected SwapSecurityConverter getVisitor() {
    return _swapVisitor;
  }

  protected FixedIncomeConverterDataProvider getConverter() {
    return _converter;
  }
  
  protected VolatilityCubeFunctionHelper getHelper() {
    return _helper;
  }

  protected String getForwardCurveName() {
    return _forwardCurveName;
  }

  protected String getFundingCurveName() {
    return _fundingCurveName;    
  }
  
  protected YieldCurveBundle getYieldCurves(final ComputationTarget target, final FunctionInputs inputs) {
    final ValueRequirement forwardCurveRequirement = getCurveRequirement(target, _forwardCurveName, _forwardCurveName, _fundingCurveName);
    final Object forwardCurveObject = inputs.getValue(forwardCurveRequirement);
    if (forwardCurveObject == null) {
      throw new OpenGammaRuntimeException("Could not get " + forwardCurveRequirement);
    }
    Object fundingCurveObject = null;
    if (!_forwardCurveName.equals(_fundingCurveName)) {
      final ValueRequirement fundingCurveRequirement = getCurveRequirement(target, _fundingCurveName,  _forwardCurveName, _fundingCurveName);
      fundingCurveObject = inputs.getValue(fundingCurveRequirement);
      if (fundingCurveObject == null) {
        throw new OpenGammaRuntimeException("Could not get " + fundingCurveRequirement);
      }
    }
    final YieldAndDiscountCurve forwardCurve = (YieldAndDiscountCurve) forwardCurveObject;
    final YieldAndDiscountCurve fundingCurve = fundingCurveObject == null ? forwardCurve
        : (YieldAndDiscountCurve) fundingCurveObject;
    return new YieldCurveBundle(new String[] {_fundingCurveName, _forwardCurveName},
        new YieldAndDiscountCurve[] {fundingCurve, forwardCurve});
  }

  protected SABRInterestRateParameters getModelParameters(final ComputationTarget target, final FunctionInputs inputs) {
    final Currency currency = FinancialSecurityUtils.getCurrency(target.getSecurity());
    final ValueRequirement surfacesRequirement = getCubeRequirement(target);
    final Object surfacesObject = inputs.getValue(surfacesRequirement);
    if (surfacesObject == null) {
      throw new OpenGammaRuntimeException("Could not get " + surfacesRequirement);
    }
    final SABRFittedSurfaces surfaces = (SABRFittedSurfaces) surfacesObject;
    if (!surfaces.getCurrency().equals(currency)) {
      throw new OpenGammaRuntimeException("Don't know how this happened");
    }
    final VolatilitySurface alphaSurface = surfaces.getAlphaSurface();
    final VolatilitySurface betaSurface = surfaces.getBetaSurface();
    final VolatilitySurface nuSurface = surfaces.getNuSurface();
    final VolatilitySurface rhoSurface = surfaces.getRhoSurface();
    final DayCount dayCount = surfaces.getDayCount();
    return _useSABRExtrapolation ? new SABRInterestRateExtrapolationParameters(alphaSurface, betaSurface, rhoSurface, nuSurface, dayCount, CUT_OFF, MU) :
        new SABRInterestRateParameters(alphaSurface, betaSurface, rhoSurface, nuSurface, dayCount, SABR_FUNCTION);
  }
}
