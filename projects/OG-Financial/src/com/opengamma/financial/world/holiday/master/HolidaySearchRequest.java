/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.world.holiday.master;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.BasicMetaBean;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaProperty;

import com.opengamma.financial.Currency;
import com.opengamma.financial.world.holiday.HolidayType;
import com.opengamma.id.Identifier;
import com.opengamma.id.IdentifierBundle;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.db.PagingRequest;

/**
 * Request for searching for holidays.
 */
@BeanDefinition
public class HolidaySearchRequest extends DirectBean {

  /**
   * The request for paging.
   * By default all matching items will be returned.
   */
  @PropertyDefinition
  private PagingRequest _pagingRequest = PagingRequest.ALL;
  /**
   * The holiday name, wildcards allowed, null to not match on name.
   */
  @PropertyDefinition
  private String _name;
  /**
   * The holiday type, null to not match on type.
   */
  @PropertyDefinition
  private HolidayType _type;
  /**
   * The identifier of the data provider.
   * This optional field can be used to capture the identifier used by the data provider.
   * This can be useful when receiving updates from the same provider.
   */
  @PropertyDefinition
  private Identifier _providerId;
  /**
   * A date to check to determine if it is a holiday, null to not match on type.
   */
  @PropertyDefinition
  private LocalDate _dateToCheck;
  /**
   * The currency to search for, null to not match on currency.
   */
  @PropertyDefinition
  private Currency _currency;
  /**
   * The region identifiers to match, null to not match on this field.
   * This will return holidays where the holiday region identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   */
  @PropertyDefinition
  private IdentifierBundle _regionIdentifiers;
  /**
   * The exchange identifiers to match, null to not match on this field.
   * This will return holidays where the holiday exchange identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   */
  @PropertyDefinition
  private IdentifierBundle _exchangeIdentifiers;
  /** 
   * The instant to search for a version at, null treated as the latest version.
   */
  @PropertyDefinition
  private Instant _versionAsOfInstant;
  /**
   * The instant to search for corrections for, null treated as the latest correction.
   */
  @PropertyDefinition
  private Instant _correctedToInstant;

  /**
   * Creates an instance.
   */
  public HolidaySearchRequest() {
  }

  /**
   * Creates an instance.
   * 
   * @param type  the type of the holiday, not null
   */
  public HolidaySearchRequest(final HolidayType type) {
    ArgumentChecker.notNull(type, "type");
    setType(type);
  }

  /**
   * Creates an instance to search for the specified currency.
   * <p>
   * The type will be set to be CURRENCY.
   * 
   * @param currency  the currency to search for, not null
   */
  public HolidaySearchRequest(Currency currency) {
    ArgumentChecker.notNull(currency, "currency");
    setCurrency(currency);
    setType(HolidayType.CURRENCY);
  }

  /**
   * Creates an instance to search for the specified identifier.
   * <p>
   * The type will be used to determine if the identifiers are regions or exchanges.
   * 
   * @param type  the type of the holiday, not null
   * @param exchangeOrRegionIds  the region or exchange identifiers to search for, not null
   */
  public HolidaySearchRequest(final HolidayType type, final IdentifierBundle exchangeOrRegionIds) {
    ArgumentChecker.notNull(type, "type");
    ArgumentChecker.notNull(exchangeOrRegionIds, "exchangeOrRegionIds");
    setType(type);
    switch (type) {
      case BANK:
        setRegionIdentifiers(exchangeOrRegionIds);
        break;
      case SETTLEMENT:
      case TRADING:
        setExchangeIdentifiers(exchangeOrRegionIds);
        break;
      case CURRENCY:
      default:
        throw new IllegalArgumentException("Use currency constructor to request a currency holiday");
    }
  }
  
  // -------------------------------------------------------------------------
  
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code HolidaySearchRequest}.
   * @return the meta-bean, not null
   */
  public static HolidaySearchRequest.Meta meta() {
    return HolidaySearchRequest.Meta.INSTANCE;
  }

  @Override
  public HolidaySearchRequest.Meta metaBean() {
    return HolidaySearchRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -2092032669:  // pagingRequest
        return getPagingRequest();
      case 3373707:  // name
        return getName();
      case 3575610:  // type
        return getType();
      case 205149932:  // providerId
        return getProviderId();
      case 14222271:  // dateToCheck
        return getDateToCheck();
      case 575402001:  // currency
        return getCurrency();
      case 596396374:  // regionIdentifiers
        return getRegionIdentifiers();
      case -339616057:  // exchangeIdentifiers
        return getExchangeIdentifiers();
      case 598802432:  // versionAsOfInstant
        return getVersionAsOfInstant();
      case -28367267:  // correctedToInstant
        return getCorrectedToInstant();
    }
    return super.propertyGet(propertyName);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -2092032669:  // pagingRequest
        setPagingRequest((PagingRequest) newValue);
        return;
      case 3373707:  // name
        setName((String) newValue);
        return;
      case 3575610:  // type
        setType((HolidayType) newValue);
        return;
      case 205149932:  // providerId
        setProviderId((Identifier) newValue);
        return;
      case 14222271:  // dateToCheck
        setDateToCheck((LocalDate) newValue);
        return;
      case 575402001:  // currency
        setCurrency((Currency) newValue);
        return;
      case 596396374:  // regionIdentifiers
        setRegionIdentifiers((IdentifierBundle) newValue);
        return;
      case -339616057:  // exchangeIdentifiers
        setExchangeIdentifiers((IdentifierBundle) newValue);
        return;
      case 598802432:  // versionAsOfInstant
        setVersionAsOfInstant((Instant) newValue);
        return;
      case -28367267:  // correctedToInstant
        setCorrectedToInstant((Instant) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the request for paging.
   * By default all matching items will be returned.
   * @return the value of the property
   */
  public PagingRequest getPagingRequest() {
    return _pagingRequest;
  }

  /**
   * Sets the request for paging.
   * By default all matching items will be returned.
   * @param pagingRequest  the new value of the property
   */
  public void setPagingRequest(PagingRequest pagingRequest) {
    this._pagingRequest = pagingRequest;
  }

  /**
   * Gets the the {@code pagingRequest} property.
   * By default all matching items will be returned.
   * @return the property, not null
   */
  public final Property<PagingRequest> pagingRequest() {
    return metaBean().pagingRequest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the holiday name, wildcards allowed, null to not match on name.
   * @return the value of the property
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the holiday name, wildcards allowed, null to not match on name.
   * @param name  the new value of the property
   */
  public void setName(String name) {
    this._name = name;
  }

  /**
   * Gets the the {@code name} property.
   * @return the property, not null
   */
  public final Property<String> name() {
    return metaBean().name().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the holiday type, null to not match on type.
   * @return the value of the property
   */
  public HolidayType getType() {
    return _type;
  }

  /**
   * Sets the holiday type, null to not match on type.
   * @param type  the new value of the property
   */
  public void setType(HolidayType type) {
    this._type = type;
  }

  /**
   * Gets the the {@code type} property.
   * @return the property, not null
   */
  public final Property<HolidayType> type() {
    return metaBean().type().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the identifier of the data provider.
   * This optional field can be used to capture the identifier used by the data provider.
   * This can be useful when receiving updates from the same provider.
   * @return the value of the property
   */
  public Identifier getProviderId() {
    return _providerId;
  }

  /**
   * Sets the identifier of the data provider.
   * This optional field can be used to capture the identifier used by the data provider.
   * This can be useful when receiving updates from the same provider.
   * @param providerId  the new value of the property
   */
  public void setProviderId(Identifier providerId) {
    this._providerId = providerId;
  }

  /**
   * Gets the the {@code providerId} property.
   * This optional field can be used to capture the identifier used by the data provider.
   * This can be useful when receiving updates from the same provider.
   * @return the property, not null
   */
  public final Property<Identifier> providerId() {
    return metaBean().providerId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets a date to check to determine if it is a holiday, null to not match on type.
   * @return the value of the property
   */
  public LocalDate getDateToCheck() {
    return _dateToCheck;
  }

  /**
   * Sets a date to check to determine if it is a holiday, null to not match on type.
   * @param dateToCheck  the new value of the property
   */
  public void setDateToCheck(LocalDate dateToCheck) {
    this._dateToCheck = dateToCheck;
  }

  /**
   * Gets the the {@code dateToCheck} property.
   * @return the property, not null
   */
  public final Property<LocalDate> dateToCheck() {
    return metaBean().dateToCheck().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency to search for, null to not match on currency.
   * @return the value of the property
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency to search for, null to not match on currency.
   * @param currency  the new value of the property
   */
  public void setCurrency(Currency currency) {
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<Currency> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the region identifiers to match, null to not match on this field.
   * This will return holidays where the holiday region identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   * @return the value of the property
   */
  public IdentifierBundle getRegionIdentifiers() {
    return _regionIdentifiers;
  }

  /**
   * Sets the region identifiers to match, null to not match on this field.
   * This will return holidays where the holiday region identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   * @param regionIdentifiers  the new value of the property
   */
  public void setRegionIdentifiers(IdentifierBundle regionIdentifiers) {
    this._regionIdentifiers = regionIdentifiers;
  }

  /**
   * Gets the the {@code regionIdentifiers} property.
   * This will return holidays where the holiday region identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   * @return the property, not null
   */
  public final Property<IdentifierBundle> regionIdentifiers() {
    return metaBean().regionIdentifiers().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange identifiers to match, null to not match on this field.
   * This will return holidays where the holiday exchange identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   * @return the value of the property
   */
  public IdentifierBundle getExchangeIdentifiers() {
    return _exchangeIdentifiers;
  }

  /**
   * Sets the exchange identifiers to match, null to not match on this field.
   * This will return holidays where the holiday exchange identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   * @param exchangeIdentifiers  the new value of the property
   */
  public void setExchangeIdentifiers(IdentifierBundle exchangeIdentifiers) {
    this._exchangeIdentifiers = exchangeIdentifiers;
  }

  /**
   * Gets the the {@code exchangeIdentifiers} property.
   * This will return holidays where the holiday exchange identifier matches on of the search identifiers.
   * Note that an empty bundle will not match any holidays, whereas a null bundle places
   * no restrictions on the result.
   * @return the property, not null
   */
  public final Property<IdentifierBundle> exchangeIdentifiers() {
    return metaBean().exchangeIdentifiers().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instant to search for a version at, null treated as the latest version.
   * @return the value of the property
   */
  public Instant getVersionAsOfInstant() {
    return _versionAsOfInstant;
  }

  /**
   * Sets the instant to search for a version at, null treated as the latest version.
   * @param versionAsOfInstant  the new value of the property
   */
  public void setVersionAsOfInstant(Instant versionAsOfInstant) {
    this._versionAsOfInstant = versionAsOfInstant;
  }

  /**
   * Gets the the {@code versionAsOfInstant} property.
   * @return the property, not null
   */
  public final Property<Instant> versionAsOfInstant() {
    return metaBean().versionAsOfInstant().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instant to search for corrections for, null treated as the latest correction.
   * @return the value of the property
   */
  public Instant getCorrectedToInstant() {
    return _correctedToInstant;
  }

  /**
   * Sets the instant to search for corrections for, null treated as the latest correction.
   * @param correctedToInstant  the new value of the property
   */
  public void setCorrectedToInstant(Instant correctedToInstant) {
    this._correctedToInstant = correctedToInstant;
  }

  /**
   * Gets the the {@code correctedToInstant} property.
   * @return the property, not null
   */
  public final Property<Instant> correctedToInstant() {
    return metaBean().correctedToInstant().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code HolidaySearchRequest}.
   */
  public static class Meta extends BasicMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code pagingRequest} property.
     */
    private final MetaProperty<PagingRequest> _pagingRequest = DirectMetaProperty.ofReadWrite(this, "pagingRequest", PagingRequest.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(this, "name", String.class);
    /**
     * The meta-property for the {@code type} property.
     */
    private final MetaProperty<HolidayType> _type = DirectMetaProperty.ofReadWrite(this, "type", HolidayType.class);
    /**
     * The meta-property for the {@code providerId} property.
     */
    private final MetaProperty<Identifier> _providerId = DirectMetaProperty.ofReadWrite(this, "providerId", Identifier.class);
    /**
     * The meta-property for the {@code dateToCheck} property.
     */
    private final MetaProperty<LocalDate> _dateToCheck = DirectMetaProperty.ofReadWrite(this, "dateToCheck", LocalDate.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(this, "currency", Currency.class);
    /**
     * The meta-property for the {@code regionIdentifiers} property.
     */
    private final MetaProperty<IdentifierBundle> _regionIdentifiers = DirectMetaProperty.ofReadWrite(this, "regionIdentifiers", IdentifierBundle.class);
    /**
     * The meta-property for the {@code exchangeIdentifiers} property.
     */
    private final MetaProperty<IdentifierBundle> _exchangeIdentifiers = DirectMetaProperty.ofReadWrite(this, "exchangeIdentifiers", IdentifierBundle.class);
    /**
     * The meta-property for the {@code versionAsOfInstant} property.
     */
    private final MetaProperty<Instant> _versionAsOfInstant = DirectMetaProperty.ofReadWrite(this, "versionAsOfInstant", Instant.class);
    /**
     * The meta-property for the {@code correctedToInstant} property.
     */
    private final MetaProperty<Instant> _correctedToInstant = DirectMetaProperty.ofReadWrite(this, "correctedToInstant", Instant.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings({"unchecked", "rawtypes" })
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap();
      temp.put("pagingRequest", _pagingRequest);
      temp.put("name", _name);
      temp.put("type", _type);
      temp.put("providerId", _providerId);
      temp.put("dateToCheck", _dateToCheck);
      temp.put("currency", _currency);
      temp.put("regionIdentifiers", _regionIdentifiers);
      temp.put("exchangeIdentifiers", _exchangeIdentifiers);
      temp.put("versionAsOfInstant", _versionAsOfInstant);
      temp.put("correctedToInstant", _correctedToInstant);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public HolidaySearchRequest createBean() {
      return new HolidaySearchRequest();
    }

    @Override
    public Class<? extends HolidaySearchRequest> beanType() {
      return HolidaySearchRequest.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code pagingRequest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PagingRequest> pagingRequest() {
      return _pagingRequest;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    /**
     * The meta-property for the {@code type} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HolidayType> type() {
      return _type;
    }

    /**
     * The meta-property for the {@code providerId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Identifier> providerId() {
      return _providerId;
    }

    /**
     * The meta-property for the {@code dateToCheck} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> dateToCheck() {
      return _dateToCheck;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code regionIdentifiers} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<IdentifierBundle> regionIdentifiers() {
      return _regionIdentifiers;
    }

    /**
     * The meta-property for the {@code exchangeIdentifiers} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<IdentifierBundle> exchangeIdentifiers() {
      return _exchangeIdentifiers;
    }

    /**
     * The meta-property for the {@code versionAsOfInstant} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Instant> versionAsOfInstant() {
      return _versionAsOfInstant;
    }

    /**
     * The meta-property for the {@code correctedToInstant} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Instant> correctedToInstant() {
      return _correctedToInstant;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
