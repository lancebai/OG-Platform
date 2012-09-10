/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.masterdb;

import java.util.Map;

import javax.time.Instant;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.PublicSPI;

/**
 * A request message used to override a database master's time source remotely.
 */
@PublicSPI
@BeanDefinition
public class TimeOverrideRequest extends DirectBean {

  /**
   * The time override
   */
  @PropertyDefinition
  private Instant _timeOverride;
  
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code TimeOverrideRequest}.
   * @return the meta-bean, not null
   */
  public static TimeOverrideRequest.Meta meta() {
    return TimeOverrideRequest.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(TimeOverrideRequest.Meta.INSTANCE);
  }

  @Override
  public TimeOverrideRequest.Meta metaBean() {
    return TimeOverrideRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 872592825:  // timeOverride
        return getTimeOverride();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 872592825:  // timeOverride
        setTimeOverride((Instant) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      TimeOverrideRequest other = (TimeOverrideRequest) obj;
      return JodaBeanUtils.equal(getTimeOverride(), other.getTimeOverride());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getTimeOverride());
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time override
   * @return the value of the property
   */
  public Instant getTimeOverride() {
    return _timeOverride;
  }

  /**
   * Sets the time override
   * @param timeOverride  the new value of the property
   */
  public void setTimeOverride(Instant timeOverride) {
    this._timeOverride = timeOverride;
  }

  /**
   * Gets the the {@code timeOverride} property.
   * @return the property, not null
   */
  public final Property<Instant> timeOverride() {
    return metaBean().timeOverride().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code TimeOverrideRequest}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code timeOverride} property.
     */
    private final MetaProperty<Instant> _timeOverride = DirectMetaProperty.ofReadWrite(
        this, "timeOverride", TimeOverrideRequest.class, Instant.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "timeOverride");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 872592825:  // timeOverride
          return _timeOverride;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends TimeOverrideRequest> builder() {
      return new DirectBeanBuilder<TimeOverrideRequest>(new TimeOverrideRequest());
    }

    @Override
    public Class<? extends TimeOverrideRequest> beanType() {
      return TimeOverrideRequest.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code timeOverride} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Instant> timeOverride() {
      return _timeOverride;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
