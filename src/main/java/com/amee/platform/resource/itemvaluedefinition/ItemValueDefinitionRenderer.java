package com.amee.platform.resource.itemvaluedefinition;

import com.amee.base.resource.Renderer;
import com.amee.domain.data.ItemDefinition;
import com.amee.domain.data.ItemValueDefinition;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public interface ItemValueDefinitionRenderer extends Renderer {

    public final static DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.dateTimeNoMillis();

    public void newItemValueDefinition(ItemValueDefinition itemValueDefinition);

    public void addBasic();

    public void addName();

    public void addPath();

    public void addValue();

    public void addAudit();

    public void addWikiDoc();

    public void addItemDefinition(ItemDefinition id);

    public void addUsages();

    public void addChoices();

    public void addFlags();

    public void addUnits();

    public Object getObject();
}
