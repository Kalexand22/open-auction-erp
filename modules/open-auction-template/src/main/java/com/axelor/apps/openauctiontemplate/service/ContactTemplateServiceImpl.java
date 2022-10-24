package com.axelor.apps.openauctiontemplate.service;

import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.repo.CompanyRepository;
import com.axelor.apps.base.db.repo.PartnerRepository;
import com.axelor.apps.openauction.db.ContactTemplate;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactTemplateServiceImpl implements ContactTemplateService {
  private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  PartnerRepository partnerRepository;
  CompanyRepository companyRepository;

  @Inject
  public ContactTemplateServiceImpl(
      PartnerRepository partnerRepository, CompanyRepository companyRepository) {
    this.partnerRepository = partnerRepository;
    this.companyRepository = companyRepository;
  }

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public Partner createContactFromTemplate(ContactTemplate contactTemplate, Partner tmpPartner) {
    log.debug("Creation d'un contact depuis un modèle");
    tmpPartner.setIsContact(true);
    tmpPartner.setIsCustomer(true);
    Set<Company> set = new HashSet<Company>(companyRepository.all().fetch());
    tmpPartner.setCompanySet(set);
    tmpPartner = initPartnerFromTemplate(contactTemplate, tmpPartner);
    tmpPartner.setContactTemplateCode(contactTemplate);
    partnerRepository.save(tmpPartner);
    log.debug("Creation d'un contact depuis un modèle OK");
    return tmpPartner;
  }

  private Partner initPartnerFromTemplate(ContactTemplate contactTemplate, Partner tmpPartner) {
    tmpPartner = (Partner) TransferFields.transferFields(contactTemplate, tmpPartner);
    return tmpPartner;
  }
}
