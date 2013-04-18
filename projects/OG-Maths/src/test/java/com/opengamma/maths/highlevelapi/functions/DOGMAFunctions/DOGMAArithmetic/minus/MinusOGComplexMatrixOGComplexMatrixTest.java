/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.minus;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.opengamma.analytics.math.statistics.distribution.fnlib.D1MACH;
import com.opengamma.maths.commonapi.exceptions.MathsExceptionNonConformance;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexMatrix;
import com.opengamma.util.test.TestGroup;

/**
 * Test plus on OGComplexMatrix/OGComplexMatrix combo
 */
@Test(groups = TestGroup.UNIT)
public class MinusOGComplexMatrixOGComplexMatrixTest {
  static double[] _data4x3Scale1 = new double[] {1, 2, 4, 5, 7, 8, 10, 11, 2, 3, 5, 6, 8, 9, 11, 12, 3, 4, 6, 7, 9, 10, 12, 13 };
  static double[] _data4x3Scale2 = new double[] {10.00, 20.00, 40.00, 50.00, 70.00, 80.00, 100.00, 110.00, 20.00, 30.00, 50.00, 60.00, 80.00, 90.00, 110.00, 120.00, 30.00, 40.00, 60.00, 70.00, 90.00,
      100.00, 120.00, 130.00 };
  static OGComplexMatrix F4x3Scale1 = new OGComplexMatrix(_data4x3Scale1, 4, 3);
  static OGComplexMatrix F4x3Scale2 = new OGComplexMatrix(_data4x3Scale2, 4, 3);
  static OGComplexMatrix F1x1Scale1 = new OGComplexMatrix(new double[] {1, 2 }, 1, 1);
  static OGComplexMatrix F1x1Scale2 = new OGComplexMatrix(new double[] {10, 20 }, 1, 1);

  // null ptr etc is caught by the function pointer code

  private static MinusOGComplexMatrixOGComplexMatrix minus = new MinusOGComplexMatrixOGComplexMatrix();

  @Test
  public static void scalarFullMinusFull() {
    OGComplexMatrix answer = new OGComplexMatrix(new double[][] { {9., 8., 7. }, {6., 5., 4. }, {3., 2., 1. }, {0., -1., -2. } }, new double[][] { {18., 17., 16. }, {15., 14., 13. },
        {12., 11., 10. }, {9., 8., 7. } });
    assertTrue(answer.equals(minus.eval(F1x1Scale2, F4x3Scale1)));
  }

  @Test
  public static void FullMinusScalarFull() {
    OGComplexMatrix answer = new OGComplexMatrix(new double[][] { {-9., -8., -7. }, {-6., -5., -4. }, {-3., -2., -1. }, {0., 1., 2. } }, new double[][] { {-18., -17., -16. }, {-15., -14., -13. },
        {-12., -11., -10. }, {-9., -8., -7. } });
    assertTrue(answer.equals(minus.eval(F4x3Scale1, F1x1Scale2)));
  }

  @Test(expectedExceptions = MathsExceptionNonConformance.class)
  public static void BadCommuteRows() {
    minus.eval(new OGComplexMatrix(new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9 }, 3, 3), F4x3Scale1);
  }

  @Test(expectedExceptions = MathsExceptionNonConformance.class)
  public static void BadCommuteCols() {
    minus.eval(new OGComplexMatrix(new double[] {1, 2, 3, 4, 5, 6, 7, 8 }, 4, 2), F4x3Scale1);
  }

  @Test
  public static void FullMinusFull() {
    OGComplexMatrix answer = new OGComplexMatrix(new double[][] { {-9., -18., -27. }, {-36., -45., -54. }, {-63., -72., -81. }, {-90., -99., -108. } }, new double[][] { {-18., -27., -36. },
        {-45., -54., -63. }, {-72., -81., -90. }, {-99., -108., -117. } });
    assertTrue(answer.fuzzyequals(minus.eval(F4x3Scale1, F4x3Scale2), 10 * D1MACH.four()));
  }

}