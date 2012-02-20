package com.amee.domain.data;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.amee.domain.AMEEEntity;
import com.amee.domain.AMEEStatus;
import com.amee.domain.IAMEEEntityReference;
import com.amee.domain.ObjectType;
import com.amee.domain.ValueDefinition;
import com.amee.domain.path.Pathable;
import com.amee.platform.science.AmountCompoundUnit;
import com.amee.platform.science.AmountPerUnit;
import com.amee.platform.science.AmountUnit;

@Entity
@Table(name = "RETURN_VALUE_DEFINITION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReturnValueDefinition extends AMEEEntity implements Pathable {

	public static final int NAME_MAX_SIZE = 255;
    public static final int TYPE_MIN_SIZE = 1;
    public static final int TYPE_MAX_SIZE = 255;
    public static final int UNIT_MAX_SIZE = 255;
    public static final int PER_UNIT_MAX_SIZE = 255;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ITEM_DEFINITION_ID")
    private ItemDefinition itemDefinition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "VALUE_DEFINITION_ID")
    private ValueDefinition valueDefinition;

    @Column(name = "TYPE", length = TYPE_MAX_SIZE, nullable = false)
    private String type = "";

    @Column(name = "UNIT", length = UNIT_MAX_SIZE, nullable = false)
    private String unit = "";

    @Column(name = "PER_UNIT", length = PER_UNIT_MAX_SIZE, nullable = false)
    private String perUnit = "";
    
    @Column(name = "NAME", length = NAME_MAX_SIZE, nullable = false)
    private String name = "";

    @Column(name = "DEFAULT_TYPE")
    private boolean defaultType = false;

    @Transient
    private Boolean previousDefaultType;

    public ReturnValueDefinition() {
        super();
    }

    public ReturnValueDefinition(ItemDefinition itemDefinition) {
        super();
        setItemDefinition(itemDefinition);
        itemDefinition.add(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AmountUnit getUnit() {
        return StringUtils.isNotBlank(unit) ? AmountUnit.valueOf(unit) : AmountUnit.ONE;
    }

    public void setUnit(AmountUnit unit) {
        this.unit = unit.toString();
    }

    public AmountPerUnit getPerUnit() {
        return StringUtils.isNotBlank(perUnit) ? AmountPerUnit.valueOf(perUnit) : AmountPerUnit.ONE;
    }

    public void setPerUnit(AmountPerUnit perUnit) {
        this.perUnit = perUnit.toString();
    }

    public boolean isDefaultType() {
        return defaultType;
    }

    public void setDefaultType(boolean defaultType) {
        this.previousDefaultType = this.defaultType;
        this.defaultType = defaultType;
    }

    public boolean hasDefaultTypeChanged() {
        return (previousDefaultType != null) && (previousDefaultType != defaultType);
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }

    public void setItemDefinition(ItemDefinition itemDefinition) {
        this.itemDefinition = itemDefinition;
    }

    public ValueDefinition getValueDefinition() {
        return valueDefinition;
    }

    public void setValueDefinition(ValueDefinition valueDefinition) {
        this.valueDefinition = valueDefinition;
    }

    @Override
    public String getPath() {
        return getUid();
    }

    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name){
    	if(name == null){
    		this.name = "";
    	}else{
			this.name = name;
    	}
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public String getDisplayPath() {
        return getPath();
    }

    @Override
    public String getFullPath() {
        return getItemDefinition().getFullPath() + "/" + getPath();
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.RVD;
    }

    @Override
    public List<IAMEEEntityReference> getHierarchy() {
        List<IAMEEEntityReference> entities = getItemDefinition().getHierarchy();
        entities.add(this);
        return entities;
    }

    @Override
    public boolean isTrash() {
        return status.equals(AMEEStatus.TRASH) || getItemDefinition().isTrash();
    }

    public AmountCompoundUnit getCompoundUnit() {
        return getUnit().with(getPerUnit());
    }
}