/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMATrigonometry;

import java.util.HashMap;
import java.util.Map;

import com.opengamma.maths.commonapi.exceptions.MathsExceptionNotImplemented;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArraySuper;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGDoubleArray;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGSparseArray;
import com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMATrigonometry.tanh.TanhAbstract;
import com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMATrigonometry.tanh.TanhOGDoubleArray;
import com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMATrigonometry.tanh.TanhOGSparseArray;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;


/**
 * Overloaded Tanh
 */
public class Tanh {

  /**
   * hashmapped function pointers
   */
  private static Map<Class<?>, TanhAbstract<?>> s_functionPointers = new HashMap<Class<?>, TanhAbstract<?>>();
  static {
    s_functionPointers.put(OGDoubleArray.class, TanhOGDoubleArray.getInstance());
    s_functionPointers.put(OGSparseArray.class, TanhOGSparseArray.getInstance());    
  }

  @SuppressWarnings("unchecked")
  public <T extends OGArraySuper<Number>> OGArraySuper<Number> tanh(T array1) {
    Catchers.catchNullFromArgList(array1, 1);
    TanhAbstract<T> use = (TanhAbstract<T>) s_functionPointers.get(array1.getClass());
    if (use == null) {
      throw new MathsExceptionNotImplemented("Tanh() on array class " + array1.getClass().toString() + " is not yet implemented");
    }
    return use.tanh(array1);
  }
  
}
