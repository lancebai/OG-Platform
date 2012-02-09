/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.examples.component;

import java.util.Collection;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableList;
import com.opengamma.component.factory.source.HistoricalTimeSeriesSourceComponentFactory;
import com.opengamma.examples.historical.normalization.MockHistoricalTimeSeriesFieldAdjustmentMapFactoryBean;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesResolver;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesSelector;
import com.opengamma.master.historicaltimeseries.impl.DefaultHistoricalTimeSeriesSelector;
import com.opengamma.master.historicaltimeseries.impl.FieldMappingHistoricalTimeSeriesResolver;
import com.opengamma.master.historicaltimeseries.impl.HistoricalTimeSeriesFieldAdjustmentMap;

/**
 * Component factory for the historical time-series source.
 */
@BeanDefinition
public class ExampleHistoricalTimeSeriesSourceComponentFactory extends HistoricalTimeSeriesSourceComponentFactory {

  //-------------------------------------------------------------------------
  @Override
  protected HistoricalTimeSeriesResolver initResolver() {
    MockHistoricalTimeSeriesFieldAdjustmentMapFactoryBean factory = new MockHistoricalTimeSeriesFieldAdjustmentMapFactoryBean();
    Collection<HistoricalTimeSeriesFieldAdjustmentMap> fieldAdjustmentMaps = ImmutableList.of(factory.getObjectCreating());
    
    HistoricalTimeSeriesSelector selector = new DefaultHistoricalTimeSeriesSelector(getConfigSource());
    return new FieldMappingHistoricalTimeSeriesResolver(fieldAdjustmentMaps, selector, getHistoricalTimeSeriesMaster());
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ExampleHistoricalTimeSeriesSourceComponentFactory}.
   * @return the meta-bean, not null
   */
  public static ExampleHistoricalTimeSeriesSourceComponentFactory.Meta meta() {
    return ExampleHistoricalTimeSeriesSourceComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(ExampleHistoricalTimeSeriesSourceComponentFactory.Meta.INSTANCE);
  }

  @Override
  public ExampleHistoricalTimeSeriesSourceComponentFactory.Meta metaBean() {
    return ExampleHistoricalTimeSeriesSourceComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ExampleHistoricalTimeSeriesSourceComponentFactory}.
   */
  public static class Meta extends HistoricalTimeSeriesSourceComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends ExampleHistoricalTimeSeriesSourceComponentFactory> builder() {
      return new DirectBeanBuilder<ExampleHistoricalTimeSeriesSourceComponentFactory>(new ExampleHistoricalTimeSeriesSourceComponentFactory());
    }

    @Override
    public Class<? extends ExampleHistoricalTimeSeriesSourceComponentFactory> beanType() {
      return ExampleHistoricalTimeSeriesSourceComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
