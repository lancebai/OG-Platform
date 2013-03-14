/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.credit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import com.google.common.collect.Iterables;
import com.opengamma.analytics.math.curve.NodalObjectsCurve;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.OpenGammaExecutionContext;
import com.opengamma.financial.analytics.curve.ConfigDBCurveSpecificationBuilder;
import com.opengamma.financial.analytics.curve.CurveDefinition;
import com.opengamma.financial.analytics.curve.CurveSpecification;
import com.opengamma.financial.analytics.curve.credit.ConfigDBCurveDefinitionSource;
import com.opengamma.financial.analytics.curve.credit.CurveDefinitionSource;
import com.opengamma.financial.analytics.curve.credit.CurveSpecificationBuilder;
import com.opengamma.financial.analytics.ircurve.strips.CurveStripWithIdentifier;
import com.opengamma.util.async.AsynchronousExecution;
import com.opengamma.util.time.Tenor;

/**
 *
 */
public class ISDACreditSpreadCurveFunction extends AbstractFunction {
  private static final Logger s_logger = LoggerFactory.getLogger(ISDACreditSpreadCurveFunction.class);

  @Override
  public CompiledFunctionDefinition compile(final FunctionCompilationContext compilationContext, final Instant atInstant) {
    final ZonedDateTime atZDT = ZonedDateTime.ofInstant(atInstant, ZoneOffset.UTC);
    return new AbstractInvokingCompiledFunction(atZDT.with(LocalTime.MIDNIGHT), atZDT.plusDays(1).with(LocalTime.MIDNIGHT).minusNanos(1000000)) {

      @SuppressWarnings("synthetic-access")
      @Override
      public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target,
          final Set<ValueRequirement> desiredValues) throws AsynchronousExecution {
        final ZonedDateTime now = ZonedDateTime.now(executionContext.getValuationClock());
        final ValueRequirement desiredValue = Iterables.getOnlyElement(desiredValues);
        final String curveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
        final ConfigSource configSource = OpenGammaExecutionContext.getConfigSource(executionContext);
        final CurveSpecification curveSpecification = getCurveSpecification(configSource, now.toLocalDate(), curveName);
        final int nStrips = curveSpecification.getStrips().size();
        final Tenor[] tenors = new Tenor[nStrips];
        final Double[] marketSpreads = new Double[nStrips];
        int i = 0;
        for (final CurveStripWithIdentifier strip : curveSpecification.getStrips()) {
          //final Object marketSpreadObject = inputs.getValue(new ValueRequirement(MarketDataRequirementNames.MARKET_VALUE, ComputationTargetType.PRIMITIVE, strip.getIdentifier()));
          //if (marketSpreadObject != null) {
          tenors[i] = strip.getCurveStrip().getResolvedMaturity();
          marketSpreads[i++] = 0.05; //(Double) marketSpreadObject;
          //}
        }
        final NodalObjectsCurve<Tenor, Double> curve = NodalObjectsCurve.from(tenors, marketSpreads);
        final ValueProperties properties = createValueProperties()
            .with(ValuePropertyNames.CURVE, curveName)
            .get();
        final ValueSpecification spec = new ValueSpecification(ValueRequirementNames.CREDIT_SPREAD_CURVE, target.toSpecification(), properties);
        return Collections.singleton(new ComputedValue(spec, curve));
      }

      @Override
      public ComputationTargetType getTargetType() {
        return ComputationTargetType.NULL;
      }

      @Override
      public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
        @SuppressWarnings("synthetic-access")
        final ValueProperties properties = createValueProperties()
        .withAny(ValuePropertyNames.CURVE)
        .get();
        return Collections.singleton(new ValueSpecification(ValueRequirementNames.CREDIT_SPREAD_CURVE, target.toSpecification(), properties));
      }

      @Override
      public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
        final ValueProperties constraints = desiredValue.getConstraints();
        final Set<String> curveNames = constraints.getValues(ValuePropertyNames.CURVE);
        if (curveNames == null || curveNames.size() != 1) {
          return null;
        }
        final String curveName = Iterables.getOnlyElement(curveNames);
        final Set<ValueRequirement> requirements = new HashSet<>();
        final ConfigSource configSource = OpenGammaCompilationContext.getConfigSource(context);
        try {
          final CurveSpecification specification = getCurveSpecification(configSource, atZDT.toLocalDate(), curveName);
          //        for (final CurveStripWithIdentifier strip : specification.getStrips()) {
          //          if (strip.getCurveStrip() instanceof CreditSpreadStrip) {
          //            requirements.add(new ValueRequirement(MarketDataRequirementNames.MARKET_VALUE, ComputationTargetType.PRIMITIVE, strip.getIdentifier()));
          //          }
          //        }
          return requirements;
        } catch (final Exception e) {
          s_logger.error(e.getMessage());
          return null;
        }
      }

      @Override
      public boolean canHandleMissingRequirements() {
        return true;
      }

      @Override
      public boolean canHandleMissingInputs() {
        return true;
      }

      private CurveSpecification getCurveSpecification(final ConfigSource configSource, final LocalDate curveDate, final String curveName) {
        final CurveDefinitionSource curveDefinitionSource = new ConfigDBCurveDefinitionSource(configSource);
        final CurveDefinition curveDefinition = curveDefinitionSource.getCurveDefinition(curveName);
        final CurveSpecificationBuilder curveSpecificationBuilder = new ConfigDBCurveSpecificationBuilder(configSource);
        return curveSpecificationBuilder.buildCurve(curveDate, curveDefinition);
      }
    };
  }

}