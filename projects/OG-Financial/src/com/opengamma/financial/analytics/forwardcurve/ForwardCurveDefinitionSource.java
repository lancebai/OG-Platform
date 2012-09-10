/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.forwardcurve;

import javax.time.Instant;
/**
 * 
 */
public interface ForwardCurveDefinitionSource {

  ForwardCurveDefinition getDefinition(final String name, final String uniqueIdName);

  ForwardCurveDefinition getDefinition(final String name, final String uniqueIdName, final Instant version);
}
