package com.axelor.apps.openauctionbase.csv;

import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.repo.AuctionHeaderRepository;
import com.axelor.apps.openauction.db.repo.LotRepository;
import com.google.inject.Inject;
import java.util.Map;

public class ImportLot {
  AuctionHeaderRepository auctionHeaderRepository;
  LotRepository lotRepository;

  @Inject
  public ImportLot(AuctionHeaderRepository auctionHeaderRepository, LotRepository lotRepository) {
    this.auctionHeaderRepository = auctionHeaderRepository;
    this.lotRepository = lotRepository;
  }

  public Object importLot(Object bean, Map<String, Object> values) {

    Lot lot = (Lot) bean;

    if (lot.getCurrentAuctionNo() != null
        && lot.getCurrentAuctionNo().getDescription().equals("")) {
      lot.setCurrentAuctionNo(null);
      lotRepository.save(lot);
      auctionHeaderRepository.remove(lot.getCurrentAuctionNo());
    }

    return lot;
  }
}
