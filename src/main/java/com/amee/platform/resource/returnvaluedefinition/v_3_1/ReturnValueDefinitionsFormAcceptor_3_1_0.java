package com.amee.platform.resource.returnvaluedefinition.v_3_1;

import com.amee.base.domain.Since;
import com.amee.base.resource.RequestWrapper;
import com.amee.base.resource.ResponseHelper;
import com.amee.base.validation.ValidationException;
import com.amee.domain.data.ReturnValueDefinition;
import com.amee.platform.resource.returnvaluedefinition.ReturnValueDefinitionValidationHelper;
import com.amee.platform.resource.returnvaluedefinition.ReturnValueDefinitionsAcceptor;
import com.amee.platform.resource.returnvaluedefinition.ReturnValueDefinitionsResource;
import com.amee.service.definition.DefinitionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Since("3.1.0")
public class ReturnValueDefinitionsFormAcceptor_3_1_0 extends ReturnValueDefinitionsAcceptor implements ReturnValueDefinitionsResource.FormAcceptor {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DefinitionService definitionService;

    @Autowired
    private ReturnValueDefinitionValidationHelper validationHelper;

    protected Object handle(RequestWrapper requestWrapper, ReturnValueDefinition returnValueDefinition) {
        validationHelper.setReturnValueDefinition(returnValueDefinition);
        if (validationHelper.isValid(requestWrapper.getFormParameters())) {
            log.debug("handle() Persist ReturnValueDefinition.");

            // Add to ItemDefinition.
            returnValueDefinition.getItemDefinition().add(returnValueDefinition);

            // If default is true, update the others.
            if (returnValueDefinition.isDefaultType()) {
                definitionService.unsetDefaultTypes(returnValueDefinition);
            }

            // Invalidate based on the ItemDefinition.
            definitionService.invalidate(returnValueDefinition.getItemDefinition());
            return ResponseHelper.getOK(
                    requestWrapper,
                    "/" + requestWrapper.getVersion() +
                            "/definitions/" + requestWrapper.getAttributes().get("itemDefinitionIdentifier") +
                            "/returnvalues/" + returnValueDefinition.getUid());
        } else {
            throw new ValidationException(validationHelper.getValidationResult());
        }
    }
}