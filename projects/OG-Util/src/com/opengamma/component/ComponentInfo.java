/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component;

import java.util.HashMap;
import java.util.Map;

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

import com.opengamma.util.ArgumentChecker;

/**
 * Information about a principal component of the OpenGamma system.
 */
@BeanDefinition
public class ComponentInfo extends DirectBean {

  /**
   * The component type representing the available functionality.
   */
  @PropertyDefinition(validate = "notNull")
  private Class<?> _type;
  /**
   * The classifier of the type.
   * This acts as a key to disambiguate multiple options for the same component type.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /**
   * The extensible set of attributes that help describe the component.
   */
  @PropertyDefinition(validate = "notNull")
  private final Map<String, String> _attributes = new HashMap<String, String>();

  /**
   * Creates an instance.
   */
  protected ComponentInfo() {
  }

  /**
   * Creates an instance.
   * 
   * @param type  the component type, not null
   * @param classifier  the classifier, not null
   */
  public ComponentInfo(Class<?> type, String classifier) {
    setType(type);
    setClassifier(classifier);
  }

  //-------------------------------------------------------------------------
  /**
   * Converts this info to a key.
   * 
   * @return the key for the component, not null
   */
  ComponentKey toComponentKey() {
    return ComponentKey.of(_type, _classifier);
  }

  //-------------------------------------------------------------------------
  /**
   * Adds a key value pair to attributes
   *
   * @param key  the key to add, not null
   * @param value  the value to add, not null
   */
  public void addAttribute(String key, String value) {
    ArgumentChecker.notNull(key, "key");
    ArgumentChecker.notNull(value, "value");
    _attributes.put(key, value);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ComponentInfo}.
   * @return the meta-bean, not null
   */
  public static ComponentInfo.Meta meta() {
    return ComponentInfo.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(ComponentInfo.Meta.INSTANCE);
  }

  @Override
  public ComponentInfo.Meta metaBean() {
    return ComponentInfo.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 3575610:  // type
        return getType();
      case -281470431:  // classifier
        return getClassifier();
      case 405645655:  // attributes
        return getAttributes();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 3575610:  // type
        setType((Class<?>) newValue);
        return;
      case -281470431:  // classifier
        setClassifier((String) newValue);
        return;
      case 405645655:  // attributes
        setAttributes((Map<String, String>) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_type, "type");
    JodaBeanUtils.notNull(_classifier, "classifier");
    JodaBeanUtils.notNull(_attributes, "attributes");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ComponentInfo other = (ComponentInfo) obj;
      return JodaBeanUtils.equal(getType(), other.getType()) &&
          JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(getAttributes(), other.getAttributes());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(getAttributes());
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the component type representing the available functionality.
   * @return the value of the property, not null
   */
  public Class<?> getType() {
    return _type;
  }

  /**
   * Sets the component type representing the available functionality.
   * @param type  the new value of the property, not null
   */
  public void setType(Class<?> type) {
    JodaBeanUtils.notNull(type, "type");
    this._type = type;
  }

  /**
   * Gets the the {@code type} property.
   * @return the property, not null
   */
  public final Property<Class<?>> type() {
    return metaBean().type().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier of the type.
   * This acts as a key to disambiguate multiple options for the same component type.
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier of the type.
   * This acts as a key to disambiguate multiple options for the same component type.
   * @param classifier  the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * This acts as a key to disambiguate multiple options for the same component type.
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the extensible set of attributes that help describe the component.
   * @return the value of the property, not null
   */
  public Map<String, String> getAttributes() {
    return _attributes;
  }

  /**
   * Sets the extensible set of attributes that help describe the component.
   * @param attributes  the new value of the property
   */
  public void setAttributes(Map<String, String> attributes) {
    this._attributes.clear();
    this._attributes.putAll(attributes);
  }

  /**
   * Gets the the {@code attributes} property.
   * @return the property, not null
   */
  public final Property<Map<String, String>> attributes() {
    return metaBean().attributes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ComponentInfo}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code type} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Class<?>> _type = DirectMetaProperty.ofReadWrite(
        this, "type", ComponentInfo.class, (Class) Class.class);
    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", ComponentInfo.class, String.class);
    /**
     * The meta-property for the {@code attributes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<String, String>> _attributes = DirectMetaProperty.ofReadWrite(
        this, "attributes", ComponentInfo.class, (Class) Map.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
        this, null,
        "type",
        "classifier",
        "attributes");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3575610:  // type
          return _type;
        case -281470431:  // classifier
          return _classifier;
        case 405645655:  // attributes
          return _attributes;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ComponentInfo> builder() {
      return new DirectBeanBuilder<ComponentInfo>(new ComponentInfo());
    }

    @Override
    public Class<? extends ComponentInfo> beanType() {
      return ComponentInfo.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code type} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Class<?>> type() {
      return _type;
    }

    /**
     * The meta-property for the {@code classifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code attributes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Map<String, String>> attributes() {
      return _attributes;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
