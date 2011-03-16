/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.math.function.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 */
public class KroneckerDeltaFunctionTest {
  private static final KroneckerDeltaFunction F = new KroneckerDeltaFunction();

  @Test(expected = IllegalArgumentException.class)
  public void testNull1() {
    F.evaluate(null, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNull2() {
    F.evaluate(1, null);
  }

  @Test
  public void test() {
    assertEquals(F.evaluate(1, 1).intValue(), 1);
    assertEquals(F.evaluate(1, 2).intValue(), 0);
  }
}
