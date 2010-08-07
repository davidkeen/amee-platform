package com.amee.platform.resource.dataitem.v_3_0;

import com.amee.base.domain.Since;
import com.amee.domain.data.ItemValue;
import org.jdom.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Version 3.0 does not include the 'history' flag of an ItemValue. Otherwise the representation is the same as 3.1.
 */
@Service("dataItemDOMRenderer_3_0")
@Scope("prototype")
@Since("3.0.0")
public class DataItemDOMRenderer extends com.amee.platform.resource.dataitem.v_3_1.DataItemDOMRenderer {

    public void newValue(ItemValue itemValue) {
        Element valueElem = new Element("Value");
        valueElem.addContent(new Element("Path").setText(itemValue.getPath()));
        valueElem.addContent(new Element("Value").setText(itemValue.getValue()));
        if (itemValue.hasUnit()) {
            valueElem.addContent(new Element("Unit").setText(itemValue.getUnit().toString()));
        }
        if (itemValue.hasPerUnit()) {
            valueElem.addContent(new Element("PerUnit").setText(itemValue.getPerUnit().toString()));
            valueElem.addContent(new Element("CompoundUnit").setText(itemValue.getCompoundUnit().toString()));
        }
        valuesElem.addContent(valueElem);
    }
}
