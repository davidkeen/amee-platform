package com.amee.platform.resource.dataitem;

import com.amee.base.domain.ResultsWrapper;
import com.amee.base.resource.*;
import com.amee.base.validation.ValidationException;
import com.amee.domain.data.DataCategory;
import com.amee.domain.data.DataItem;
import com.amee.domain.environment.Environment;
import com.amee.platform.search.DataItemFilter;
import com.amee.platform.search.DataItemFilterValidationHelper;
import com.amee.platform.search.SearchService;
import com.amee.service.data.DataService;
import com.amee.service.environment.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public class DataItemsBuilder implements ResourceBuilder {

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private DataService dataService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private DataItemBuilder dataItemBuilder;

    @Autowired
    private DataItemFilterValidationHelper validationHelper;

    @Autowired
    private RendererBeanFinder rendererBeanFinder;

    private DataItemsRenderer renderer;

    @Transactional(readOnly = true)
    public Object handle(RequestWrapper requestWrapper) {
        // Get Renderer.
        renderer = (DataItemsRenderer) rendererBeanFinder.getRenderer(DataItemsRenderer.class, requestWrapper);
        if (renderer != null) {
            // Get Environment.
            Environment environment = environmentService.getEnvironmentByName("AMEE");
            // Get DataCategory identifier.
            String dataCategoryIdentifier = requestWrapper.getAttributes().get("categoryIdentifier");
            if (dataCategoryIdentifier != null) {
                // Get DataCategory.
                DataCategory dataCategory = dataService.getDataCategoryByIdentifier(environment, dataCategoryIdentifier);
                if ((dataCategory != null) && (dataCategory.getItemDefinition() != null)) {
                    // Create filter and do search.
                    DataItemFilter filter = new DataItemFilter(dataCategory.getItemDefinition());
                    filter.setLoadDataItemValues(
                            requestWrapper.getMatrixParameters().containsKey("full") ||
                                    requestWrapper.getMatrixParameters().containsKey("values"));
                    filter.setLoadMetadatas(
                            requestWrapper.getMatrixParameters().containsKey("full") ||
                                    requestWrapper.getMatrixParameters().containsKey("wikiDoc") ||
                                    requestWrapper.getMatrixParameters().containsKey("provenance"));
                    validationHelper.setDataItemFilter(filter);
                    if (validationHelper.isValid(requestWrapper.getQueryParameters())) {
                        handle(requestWrapper, dataCategory, filter, renderer);
                        renderer.ok();
                    } else {
                        throw new ValidationException(validationHelper.getValidationResult());
                    }
                } else {
                    throw new NotFoundException();
                }
            } else {
                throw new MissingAttributeException("categoryIdentifier");
            }
        } else {
            throw new MediaTypeNotSupportedException();
        }
        return renderer.getObject();
    }

    protected void handle(
            RequestWrapper requestWrapper,
            DataCategory dataCategory,
            DataItemFilter filter,
            DataItemsRenderer renderer) {
        ResultsWrapper<DataItem> resultsWrapper = searchService.getDataItems(dataCategory, filter);
        renderer.setTruncated(resultsWrapper.isTruncated());
        for (DataItem dataItem : resultsWrapper.getResults()) {
            dataItemBuilder.handle(requestWrapper, dataItem, renderer.getDataItemRenderer());
            renderer.newDataItem();
        }
    }

}