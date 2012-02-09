/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.master;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.component.factory.ComponentInfoAttributes;
import com.opengamma.financial.batch.AdHocBatchDbManager;
import com.opengamma.financial.batch.BatchMaster;
import com.opengamma.financial.batch.DataAdHocBatchDbManagerResource;
import com.opengamma.masterdb.batch.DbBatchMaster;
import com.opengamma.util.db.DbConnector;

/**
 * Component factory for the database batch master.
 */
@BeanDefinition
public class DbBatchMasterComponentFactory extends AbstractComponentFactory {

  /**
   * The classifier that the factory should publish under.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /**
   * The flag determining whether the component should be published by REST (default true).
   */
  @PropertyDefinition
  private boolean _publishRest = true;
  /**
   * The database connector.
   */
  @PropertyDefinition(validate = "notNull")
  private DbConnector _dbConnector;
  /**
   * The scheme used by the {@code UniqueId}.
   */
  @PropertyDefinition
  private String _idScheme;

  //-------------------------------------------------------------------------
  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) {
    ComponentInfo infoMaster = new ComponentInfo(BatchMaster.class, getClassifier());
    
    // master
    DbBatchMaster master = new DbBatchMaster(getDbConnector());
    if (getIdScheme() != null) {
      master.setUniqueIdScheme(getIdScheme());
    }
    infoMaster.addAttribute(ComponentInfoAttributes.UNIQUE_ID_SCHEME, master.getUniqueIdScheme());
    repo.registerComponent(infoMaster, master);
    
    // manager
    ComponentInfo infoAdHoc = new ComponentInfo(AdHocBatchDbManager.class, getClassifier());
    repo.registerComponent(infoAdHoc, master);
    
    // publish
    if (isPublishRest()) {
      repo.getRestComponents().publish(infoAdHoc, new DataAdHocBatchDbManagerResource(master));
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code DbBatchMasterComponentFactory}.
   * @return the meta-bean, not null
   */
  public static DbBatchMasterComponentFactory.Meta meta() {
    return DbBatchMasterComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(DbBatchMasterComponentFactory.Meta.INSTANCE);
  }

  @Override
  public DbBatchMasterComponentFactory.Meta metaBean() {
    return DbBatchMasterComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        return getClassifier();
      case -614707837:  // publishRest
        return isPublishRest();
      case 39794031:  // dbConnector
        return getDbConnector();
      case -661606752:  // idScheme
        return getIdScheme();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        setClassifier((String) newValue);
        return;
      case -614707837:  // publishRest
        setPublishRest((Boolean) newValue);
        return;
      case 39794031:  // dbConnector
        setDbConnector((DbConnector) newValue);
        return;
      case -661606752:  // idScheme
        setIdScheme((String) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_classifier, "classifier");
    JodaBeanUtils.notNull(_dbConnector, "dbConnector");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DbBatchMasterComponentFactory other = (DbBatchMasterComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(isPublishRest(), other.isPublishRest()) &&
          JodaBeanUtils.equal(getDbConnector(), other.getDbConnector()) &&
          JodaBeanUtils.equal(getIdScheme(), other.getIdScheme()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(isPublishRest());
    hash += hash * 31 + JodaBeanUtils.hashCode(getDbConnector());
    hash += hash * 31 + JodaBeanUtils.hashCode(getIdScheme());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier that the factory should publish under.
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier that the factory should publish under.
   * @param classifier  the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the flag determining whether the component should be published by REST (default true).
   * @return the value of the property
   */
  public boolean isPublishRest() {
    return _publishRest;
  }

  /**
   * Sets the flag determining whether the component should be published by REST (default true).
   * @param publishRest  the new value of the property
   */
  public void setPublishRest(boolean publishRest) {
    this._publishRest = publishRest;
  }

  /**
   * Gets the the {@code publishRest} property.
   * @return the property, not null
   */
  public final Property<Boolean> publishRest() {
    return metaBean().publishRest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the database connector.
   * @return the value of the property, not null
   */
  public DbConnector getDbConnector() {
    return _dbConnector;
  }

  /**
   * Sets the database connector.
   * @param dbConnector  the new value of the property, not null
   */
  public void setDbConnector(DbConnector dbConnector) {
    JodaBeanUtils.notNull(dbConnector, "dbConnector");
    this._dbConnector = dbConnector;
  }

  /**
   * Gets the the {@code dbConnector} property.
   * @return the property, not null
   */
  public final Property<DbConnector> dbConnector() {
    return metaBean().dbConnector().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the scheme used by the {@code UniqueId}.
   * @return the value of the property
   */
  public String getIdScheme() {
    return _idScheme;
  }

  /**
   * Sets the scheme used by the {@code UniqueId}.
   * @param idScheme  the new value of the property
   */
  public void setIdScheme(String idScheme) {
    this._idScheme = idScheme;
  }

  /**
   * Gets the the {@code idScheme} property.
   * @return the property, not null
   */
  public final Property<String> idScheme() {
    return metaBean().idScheme().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code DbBatchMasterComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", DbBatchMasterComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code publishRest} property.
     */
    private final MetaProperty<Boolean> _publishRest = DirectMetaProperty.ofReadWrite(
        this, "publishRest", DbBatchMasterComponentFactory.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code dbConnector} property.
     */
    private final MetaProperty<DbConnector> _dbConnector = DirectMetaProperty.ofReadWrite(
        this, "dbConnector", DbBatchMasterComponentFactory.class, DbConnector.class);
    /**
     * The meta-property for the {@code idScheme} property.
     */
    private final MetaProperty<String> _idScheme = DirectMetaProperty.ofReadWrite(
        this, "idScheme", DbBatchMasterComponentFactory.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "publishRest",
        "dbConnector",
        "idScheme");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return _classifier;
        case -614707837:  // publishRest
          return _publishRest;
        case 39794031:  // dbConnector
          return _dbConnector;
        case -661606752:  // idScheme
          return _idScheme;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends DbBatchMasterComponentFactory> builder() {
      return new DirectBeanBuilder<DbBatchMasterComponentFactory>(new DbBatchMasterComponentFactory());
    }

    @Override
    public Class<? extends DbBatchMasterComponentFactory> beanType() {
      return DbBatchMasterComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code classifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code publishRest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> publishRest() {
      return _publishRest;
    }

    /**
     * The meta-property for the {@code dbConnector} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DbConnector> dbConnector() {
      return _dbConnector;
    }

    /**
     * The meta-property for the {@code idScheme} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> idScheme() {
      return _idScheme;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
