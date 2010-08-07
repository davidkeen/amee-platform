package com.amee.platform.resource.search;

import com.amee.base.domain.ResultsWrapper;
import com.amee.base.resource.MediaTypeNotSupportedException;
import com.amee.base.resource.RendererBeanFinder;
import com.amee.base.resource.RequestWrapper;
import com.amee.base.resource.ResourceBuilder;
import com.amee.base.validation.ValidationException;
import com.amee.domain.AMEEEntity;
import com.amee.domain.data.DataCategory;
import com.amee.domain.data.DataItem;
import com.amee.platform.resource.datacategory.DataCategoryBuilder;
import com.amee.platform.resource.dataitem.DataItemBuilder;
import com.amee.platform.search.SearchFilter;
import com.amee.platform.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public class SearchBuilder implements ResourceBuilder {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchFilterValidationHelper validationHelper;

    @Autowired
    private DataCategoryBuilder dataCategoryBuilder;

    @Autowired
    private DataItemBuilder dataItemBuilder;

    @Autowired
    private RendererBeanFinder rendererBeanFinder;

    private SearchRenderer renderer;

    @Transactional(readOnly = true)
    public Object handle(RequestWrapper requestWrapper) {
        renderer = (SearchRenderer) rendererBeanFinder.getRenderer(SearchRenderer.class, requestWrapper);
        if (renderer != null) {
            SearchFilter filter = new SearchFilter();
            filter.setLoadDataItemValues(
                    requestWrapper.getMatrixParameters().containsKey("full") ||
                            requestWrapper.getMatrixParameters().containsKey("values"));
            filter.setLoadMetadatas(
                    requestWrapper.getMatrixParameters().containsKey("full") ||
                            requestWrapper.getMatrixParameters().containsKey("authority") ||
                            requestWrapper.getMatrixParameters().containsKey("wikiDoc") ||
                            requestWrapper.getMatrixParameters().containsKey("provenance"));
            validationHelper.setSearchFilter(filter);
            if (validationHelper.isValid(requestWrapper.getQueryParameters())) {
                handle(requestWrapper, filter, renderer);
                renderer.ok();
            } else {
                throw new ValidationException(validationHelper.getValidationResult());
            }
            return renderer.getObject();
        } else {
            throw new MediaTypeNotSupportedException();
        }
    }

    protected void handle(
            RequestWrapper requestWrapper,
            SearchFilter filter,
            SearchRenderer renderer) {
        ResultsWrapper<AMEEEntity> resultsWrapper = searchService.getEntities(filter);
        renderer.setTruncated(resultsWrapper.isTruncated());
        for (AMEEEntity entity : resultsWrapper.getResults()) {
            switch (entity.getObjectType()) {
                case DC:
                    dataCategoryBuilder.handle(requestWrapper, (DataCategory) entity, renderer.getDataCategoryRenderer());
                    renderer.newDataCategory();
                    break;
                case DI:
                    dataItemBuilder.handle(requestWrapper, (DataItem) entity, renderer.getDataItemRenderer());
                    renderer.newDataItem();
                    break;
            }
        }
    }
}