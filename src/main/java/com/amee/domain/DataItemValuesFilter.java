package com.amee.domain;

import com.amee.domain.data.ItemValueDefinition;
import com.amee.domain.item.data.DataItem;

import java.util.Date;

public class DataItemValuesFilter extends LimitFilter {

    private Date startDate = DataItemService.EPOCH;
    private Date endDate = DataItemService.Y2038;
    private DataItem dataItem;
    private ItemValueDefinition itemValueDefinition;

    public DataItemValuesFilter() {
        super();
    }

    @Override
    public int getResultLimitDefault() {
        return 50;
    }

    @Override
    public int getResultLimitMax() {
        return 100;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            startDate = DataItemService.EPOCH;
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            endDate = DataItemService.Y2038;
        }
        this.endDate = endDate;
    }

    public DataItem getDataItem() {
        return dataItem;
    }

    public void setDataItem(DataItem dataItem) {
        this.dataItem = dataItem;
    }

    public ItemValueDefinition getItemValueDefinition() {
        return itemValueDefinition;
    }

    public void setItemValueDefinition(ItemValueDefinition itemValueDefinition) {
        this.itemValueDefinition = itemValueDefinition;
    }
}