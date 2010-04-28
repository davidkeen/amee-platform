package com.amee.platform.science;

import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class MockExternalValue implements ExternalValue {

    private String value;
    private Date startDate;
    private String unit;
    private String perUnit;
    private String canonicalUnit;
    private String canonicalPerUnit;

    public MockExternalValue() {
        super();
    }

    public MockExternalValue(String value) {
        this();
        this.value = value;
    }

    public MockExternalValue(String value, Date startDate) {
        this(value);
        this.startDate = startDate;
    }

    public String getUsableValue() {
        return value;
    }

    public AmountUnit getUnit() {
        return (unit != null) ? AmountUnit.valueOf(unit) : getCanonicalUnit();
    }

    public AmountPerUnit getPerUnit() {
        return (perUnit != null) ? AmountPerUnit.valueOf(perUnit) : getCanonicalPerUnit();
    }

    public AmountUnit getCanonicalUnit() {
        return (canonicalUnit != null) ? AmountUnit.valueOf(canonicalUnit) : AmountUnit.ONE;
    }

    public AmountPerUnit getCanonicalPerUnit() {
        return (canonicalPerUnit != null) ? AmountPerUnit.valueOf(canonicalPerUnit) : AmountPerUnit.ONE;
    }

    public AmountCompoundUnit getCompoundUnit() {
        return getUnit().with(getPerUnit());
    }

    public AmountCompoundUnit getCanonicalCompoundUnit() {
        return getUnit().with(getPerUnit());
    }

    public StartEndDate getStartDate() {
        return new StartEndDate(startDate);
    }

    public boolean isDecimal() {
        return true;
    }

    public boolean isConvertible() {
        return true;
    }

    public boolean hasUnit() {
        return !StringUtils.isBlank(unit);
    }

    public boolean hasPerUnit() {
        return !StringUtils.isBlank(perUnit);
    }

    public String getLabel() {
        return "MockExternalValue: " + value;
    }
}
