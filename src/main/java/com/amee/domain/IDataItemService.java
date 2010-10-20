package com.amee.domain;

import com.amee.domain.item.data.NuDataItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public interface IDataItemService extends IItemService {

    // The UNIX time epoch, which is 1970-01-01 00:00:00. See: http://en.wikipedia.org/wiki/Unix_epoch
    public final static Date EPOCH = new Date(0);

    public NuDataItem getItemByUid(String uid);

    public String getLabel(NuDataItem dataItem);

    /*
     * TODO: The following methods should live in a Renderer but are being
     * TODO: added here for convenience.
     */
    public JSONObject getJSONObject(NuDataItem dataItem, boolean detailed, boolean showHistory) throws JSONException;
}
