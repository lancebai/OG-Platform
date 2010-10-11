/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.view.compilation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.time.Instant;

import com.opengamma.engine.depgraph.DependencyGraphBuilder;
import com.opengamma.engine.function.resolver.CompiledFunctionResolver;
import com.opengamma.engine.view.ViewDefinition;

/**
 * Holds context relating to the partially-completed compilation of a view definition, for passing to different stages
 * of the compilation.
 */
public class ViewCompilationContext {

  private final ViewDefinition _viewDefinition;
  private final ViewCompilationServices _services;
  private final Map<String, DependencyGraphBuilder> _builders;

  /* package */ViewCompilationContext(ViewDefinition viewDefinition, ViewCompilationServices compilationServices, Instant atInstant) {
    _viewDefinition = viewDefinition;
    _services = compilationServices;
    _builders = generateBuilders(viewDefinition, compilationServices, atInstant);
  }

  // --------------------------------------------------------------------------
  public ViewDefinition getViewDefinition() {
    return _viewDefinition;
  }

  public ViewCompilationServices getServices() {
    return _services;
  }

  public Map<String, DependencyGraphBuilder> getBuilders() {
    return Collections.unmodifiableMap(_builders);
  }

  // --------------------------------------------------------------------------
  private Map<String, DependencyGraphBuilder> generateBuilders(ViewDefinition viewDefinition, ViewCompilationServices compilationServices, Instant atInstant) {
    Map<String, DependencyGraphBuilder> result = new HashMap<String, DependencyGraphBuilder>();
    final CompiledFunctionResolver functionResolver = compilationServices.getFunctionResolver().compile(compilationServices.getFunctionCompilationContext(), atInstant);
    for (String configName : viewDefinition.getAllCalculationConfigurationNames()) {
      DependencyGraphBuilder builder = new DependencyGraphBuilder();
      builder.setCalculationConfigurationName(configName);
      builder.setLiveDataAvailabilityProvider(compilationServices.getLiveDataAvailabilityProvider());
      builder.setTargetResolver(compilationServices.getComputationTargetResolver());
      builder.setFunctionResolver(functionResolver);
      builder.setCompilationContext(compilationServices.getFunctionCompilationContext());
      result.put(configName, builder);
    }
    return result;
  }

}
