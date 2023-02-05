package com.garanti.TeknikServis.validation;

import com.garanti.TeknikServis.enumeration.Approval;
import com.garanti.TeknikServis.model.Proposal;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProposalValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Proposal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Proposal proposal = (Proposal) target;
        proposal.setAPPROVAL('4');
        if (!Approval.isValid(proposal.getAPPROVAL())) {
            errors.rejectValue("APPROVAL", "Status must be one of the approved values.");
        }

    }
}
