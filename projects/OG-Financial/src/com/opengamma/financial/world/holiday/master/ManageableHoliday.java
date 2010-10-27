/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.world.holiday.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.time.calendar.LocalDate;

import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.BasicMetaBean;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaProperty;

import com.opengamma.financial.Currency;
import com.opengamma.financial.world.holiday.Holiday;
import com.opengamma.financial.world.holiday.HolidayType;
import com.opengamma.id.Identifier;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.ArgumentChecker;

/**
 * The manageable implementation of a set of holiday dates.
 * <p>
 * This implementation is used by the holiday master to store and manipulate the data.
 */
@BeanDefinition
public class ManageableHoliday extends DirectBean implements Holiday, Serializable {

  /**
   * The unique identifier of the holiday.
   */
  @PropertyDefinition
  private UniqueIdentifier _uniqueIdentifier;
  /**
   * The type of the holiday.
   */
  @PropertyDefinition
  private HolidayType _type;
  /**
   * The region identifier, used when this is a holiday of type BANK.
   * This must be null if the type is not BANK.
   */
  @PropertyDefinition
  private Identifier _regionId;
  /**
   * The exchange identifier, used when this is a holiday of type SETTLEMENT or TRADING.
   * This must be null if the type is not SETTLEMENT or TRADING.
   */
  @PropertyDefinition
  private Identifier _exchangeId;
  /**
   * The ISO currency, used when this is a holiday of type CURRENCY.
   * This must be null if the type is not CURRENCY.
   */
  @PropertyDefinition
  private String _currencyISO;
  /**
   * The list of dates that the target (currency/region/exchange) is on holiday.
   */
  @PropertyDefinition
  private final List<LocalDate> _holidayDates = new ArrayList<LocalDate>();

  /**
   * Create an instance for deserialization.
   */
  public ManageableHoliday() {
  }

  /**
   * Create an instance from another holiday instance.
   * <p>
   * This copies the specified holiday creating an independent copy.
   * 
   * @param holiday  the holiday to copy, not null
   * @return a new holiday copying the old one, not null
   */
  public static ManageableHoliday copyOf(final Holiday holiday) {
    ArgumentChecker.notNull(holiday, "holiday");
    ManageableHoliday copy = new ManageableHoliday();
    copy.setUniqueIdentifier(holiday.getUniqueIdentifier());
    copy.setType(holiday.getType());
    copy.setRegionId(holiday.getRegionId());
    copy.setExchangeId(holiday.getExchangeId());
    copy.setCurrencyISO(holiday.getCurrencyISO());
    copy.setHolidayDates(holiday.getHolidayDates());
    return copy;
  }

  /**
   * Create a CURRENCY holiday from a collection of holiday dates.
   * <p>
   * The unique identifier is managed separately using {@link #setUniqueIdentifier}.
   * 
   * @param currency the currency of this CURRENCY holiday schedule, not null
   * @param holidaySeries the dates on which holidays fall, not null
   */
  public ManageableHoliday(Currency currency, Collection<LocalDate> holidaySeries) {
    ArgumentChecker.notNull(currency, "currency");
    ArgumentChecker.notNull(holidaySeries, "holidaySeries");
    setCurrencyISO(currency.getISOCode());
    setType(HolidayType.CURRENCY);
    getHolidayDates().addAll(holidaySeries);
  }

  /**
   * Create a BANK, SETTLEMENT or TRADING holiday from a collection of holiday dates.
   * <p>
   * The unique identifier is managed separately using {@link #setUniqueIdentifier}.
   * 
   * @param holidayType  the type of the holiday, not null
   * @param regionOrExchangeId  the Identifier for either a region (for a BANK holiday) or an exchange (for a SETTLEMENT or TRADING holiday), not null
   * @param holidaySeries  a collection of dates on which holidays fall, not null
   */
  public ManageableHoliday(HolidayType holidayType, Identifier regionOrExchangeId, Collection<LocalDate> holidaySeries) {
    ArgumentChecker.notNull(holidayType, "holidayType");
    ArgumentChecker.notNull(regionOrExchangeId, "regionOrExchangeId");
    ArgumentChecker.notNull(holidaySeries, "holidaySeries");
    switch (holidayType) {
      case BANK:
        setRegionId(regionOrExchangeId);
        break;
      case SETTLEMENT:
      case TRADING:
        setExchangeId(regionOrExchangeId);
        break;
      case CURRENCY:
      default:
        throw new IllegalArgumentException("Use the Currency constructor for a currency related Holiday");
    }
    setType(holidayType);
    getHolidayDates().addAll(holidaySeries);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ManageableHoliday}.
   * @return the meta-bean, not null
   */
  public static ManageableHoliday.Meta meta() {
    return ManageableHoliday.Meta.INSTANCE;
  }

  @Override
  public ManageableHoliday.Meta metaBean() {
    return ManageableHoliday.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -125484198:  // uniqueIdentifier
        return getUniqueIdentifier();
      case 3575610:  // type
        return getType();
      case -690339025:  // regionId
        return getRegionId();
      case 913218206:  // exchangeId
        return getExchangeId();
      case 586606260:  // currencyISO
        return getCurrencyISO();
      case -367347:  // holidayDates
        return getHolidayDates();
    }
    return super.propertyGet(propertyName);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -125484198:  // uniqueIdentifier
        setUniqueIdentifier((UniqueIdentifier) newValue);
        return;
      case 3575610:  // type
        setType((HolidayType) newValue);
        return;
      case -690339025:  // regionId
        setRegionId((Identifier) newValue);
        return;
      case 913218206:  // exchangeId
        setExchangeId((Identifier) newValue);
        return;
      case 586606260:  // currencyISO
        setCurrencyISO((String) newValue);
        return;
      case -367347:  // holidayDates
        setHolidayDates((List<LocalDate>) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique identifier of the holiday.
   * @return the value of the property
   */
  public UniqueIdentifier getUniqueIdentifier() {
    return _uniqueIdentifier;
  }

  /**
   * Sets the unique identifier of the holiday.
   * @param uniqueIdentifier  the new value of the property
   */
  public void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier) {
    this._uniqueIdentifier = uniqueIdentifier;
  }

  /**
   * Gets the the {@code uniqueIdentifier} property.
   * @return the property, not null
   */
  public final Property<UniqueIdentifier> uniqueIdentifier() {
    return metaBean().uniqueIdentifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the type of the holiday.
   * @return the value of the property
   */
  public HolidayType getType() {
    return _type;
  }

  /**
   * Sets the type of the holiday.
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
   * Gets the region identifier, used when this is a holiday of type BANK.
   * This must be null if the type is not BANK.
   * @return the value of the property
   */
  public Identifier getRegionId() {
    return _regionId;
  }

  /**
   * Sets the region identifier, used when this is a holiday of type BANK.
   * This must be null if the type is not BANK.
   * @param regionId  the new value of the property
   */
  public void setRegionId(Identifier regionId) {
    this._regionId = regionId;
  }

  /**
   * Gets the the {@code regionId} property.
   * This must be null if the type is not BANK.
   * @return the property, not null
   */
  public final Property<Identifier> regionId() {
    return metaBean().regionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange identifier, used when this is a holiday of type SETTLEMENT or TRADING.
   * This must be null if the type is not SETTLEMENT or TRADING.
   * @return the value of the property
   */
  public Identifier getExchangeId() {
    return _exchangeId;
  }

  /**
   * Sets the exchange identifier, used when this is a holiday of type SETTLEMENT or TRADING.
   * This must be null if the type is not SETTLEMENT or TRADING.
   * @param exchangeId  the new value of the property
   */
  public void setExchangeId(Identifier exchangeId) {
    this._exchangeId = exchangeId;
  }

  /**
   * Gets the the {@code exchangeId} property.
   * This must be null if the type is not SETTLEMENT or TRADING.
   * @return the property, not null
   */
  public final Property<Identifier> exchangeId() {
    return metaBean().exchangeId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the ISO currency, used when this is a holiday of type CURRENCY.
   * This must be null if the type is not CURRENCY.
   * @return the value of the property
   */
  public String getCurrencyISO() {
    return _currencyISO;
  }

  /**
   * Sets the ISO currency, used when this is a holiday of type CURRENCY.
   * This must be null if the type is not CURRENCY.
   * @param currencyISO  the new value of the property
   */
  public void setCurrencyISO(String currencyISO) {
    this._currencyISO = currencyISO;
  }

  /**
   * Gets the the {@code currencyISO} property.
   * This must be null if the type is not CURRENCY.
   * @return the property, not null
   */
  public final Property<String> currencyISO() {
    return metaBean().currencyISO().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the list of dates that the target (currency/region/exchange) is on holiday.
   * @return the value of the property
   */
  public List<LocalDate> getHolidayDates() {
    return _holidayDates;
  }

  /**
   * Sets the list of dates that the target (currency/region/exchange) is on holiday.
   * @param holidayDates  the new value of the property
   */
  public void setHolidayDates(List<LocalDate> holidayDates) {
    this._holidayDates.clear();
    this._holidayDates.addAll(holidayDates);
  }

  /**
   * Gets the the {@code holidayDates} property.
   * @return the property, not null
   */
  public final Property<List<LocalDate>> holidayDates() {
    return metaBean().holidayDates().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ManageableHoliday}.
   */
  public static class Meta extends BasicMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueIdentifier} property.
     */
    private final MetaProperty<UniqueIdentifier> _uniqueIdentifier = DirectMetaProperty.ofReadWrite(this, "uniqueIdentifier", UniqueIdentifier.class);
    /**
     * The meta-property for the {@code type} property.
     */
    private final MetaProperty<HolidayType> _type = DirectMetaProperty.ofReadWrite(this, "type", HolidayType.class);
    /**
     * The meta-property for the {@code regionId} property.
     */
    private final MetaProperty<Identifier> _regionId = DirectMetaProperty.ofReadWrite(this, "regionId", Identifier.class);
    /**
     * The meta-property for the {@code exchangeId} property.
     */
    private final MetaProperty<Identifier> _exchangeId = DirectMetaProperty.ofReadWrite(this, "exchangeId", Identifier.class);
    /**
     * The meta-property for the {@code currencyISO} property.
     */
    private final MetaProperty<String> _currencyISO = DirectMetaProperty.ofReadWrite(this, "currencyISO", String.class);
    /**
     * The meta-property for the {@code holidayDates} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<LocalDate>> _holidayDates = DirectMetaProperty.ofReadWrite(this, "holidayDates", (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings({"unchecked", "rawtypes" })
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap();
      temp.put("uniqueIdentifier", _uniqueIdentifier);
      temp.put("type", _type);
      temp.put("regionId", _regionId);
      temp.put("exchangeId", _exchangeId);
      temp.put("currencyISO", _currencyISO);
      temp.put("holidayDates", _holidayDates);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public ManageableHoliday createBean() {
      return new ManageableHoliday();
    }

    @Override
    public Class<? extends ManageableHoliday> beanType() {
      return ManageableHoliday.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uniqueIdentifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueIdentifier> uniqueIdentifier() {
      return _uniqueIdentifier;
    }

    /**
     * The meta-property for the {@code type} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HolidayType> type() {
      return _type;
    }

    /**
     * The meta-property for the {@code regionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Identifier> regionId() {
      return _regionId;
    }

    /**
     * The meta-property for the {@code exchangeId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Identifier> exchangeId() {
      return _exchangeId;
    }

    /**
     * The meta-property for the {@code currencyISO} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> currencyISO() {
      return _currencyISO;
    }

    /**
     * The meta-property for the {@code holidayDates} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<LocalDate>> holidayDates() {
      return _holidayDates;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
