package com.garanti.TeknikServis.service;


import com.garanti.TeknikServis.excepton.EntityNoContentException;
import com.garanti.TeknikServis.excepton.MultipleProposalCreationException;
import com.garanti.TeknikServis.model.Proposal;
import com.garanti.TeknikServis.model.ProposalAdminDto;
import com.garanti.TeknikServis.model.ProposalDto;
import com.garanti.TeknikServis.repo.ProposalRepo;
import com.garanti.TeknikServis.repo.UserRepo;
import com.garanti.TeknikServis.security.TokenParser;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;


@Service
@AllArgsConstructor
public class ProposalService {
    private ProposalRepo proposalRepo;
    private UserRepo userRepo;
    private MessageSource messageSource;

    @Transactional
    public Proposal save(Proposal proposal, HttpHeaders headers, Locale locale) {
        String username = TokenParser.jwt(headers.get("Authorization").get(0).substring(7));
        proposal.setUSER_ID(userRepo.getUserId(username));
        if (proposalRepo.save(proposal)) {
            return proposalRepo.getProposalById(proposalRepo.getByMaxPRPID());
        }
        throw new MultipleProposalCreationException(messageSource.getMessage("proposal.creation.fail", null, locale));
    }

    @Transactional
    public String deleteByProposalId(Integer proposalID, HttpHeaders headers, Locale locale) {
        if (proposalID != null) {
            String username = TokenParser.jwt(headers.get("Authorization").get(0).substring(7));
            if (proposalRepo.deleteByProposalId(proposalID, userRepo.getUserId(username)))
                return messageSource.getMessage("proposal.delete.success", null, locale);
            throw new EntityNoContentException(messageSource.getMessage("proposal.delete.no.content", null, locale));
        }
        throw new IllegalArgumentException(messageSource.getMessage("proposal.wrong.arg", null, locale));
    }

    public List<ProposalDto> getByUserOffers(HttpHeaders headers,Locale locale) {
        String username = TokenParser.jwt(headers.get("Authorization").get(0).substring(7));
        List<ProposalDto> proposals = proposalRepo.getByUserOffers(userRepo.getUserId(username));
        if (!proposals.isEmpty())
            return proposals;
        throw new EntityNoContentException(messageSource.getMessage("no.waiting.offer", null, locale));
    }

    public List<ProposalDto> getApprovedOffers(HttpHeaders headers, Locale locale) {
        String username = TokenParser.jwt(headers.get("Authorization").get(0).substring(7));
        List<ProposalDto> proposals = proposalRepo.getApprovedOffers(userRepo.getUserId(username));
        if (!proposals.isEmpty())
            return proposals;
        throw new EntityNoContentException(messageSource.getMessage("no.approved.offer", null, locale));
    }
    public List<Proposal> getAll(Locale locale) {

        List<Proposal> bookings = proposalRepo.getAll();
        if (!bookings.isEmpty())
            return bookings;
        throw new EntityNoContentException(messageSource.getMessage("proposal.no.content", null, locale));

    }

    public ProposalAdminDto getById(long id) {
        return proposalRepo.getById(id);

    }

    public Proposal updateById(long id, boolean approval) {
        int sayi = proposalRepo.infoStatusApproval(id);

        if (approval) {
            return proposalRepo.updateById(id, 1);
        }
        else{
            return proposalRepo.updateById(id, 0);
        }

        // throw new EntityNoContentException("Daha önce işlem gerçekleştirildiğinden işlem gerçekleştirilemiyor.");
    }
}
