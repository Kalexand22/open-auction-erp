package com.axelor.apps.openauctionbase.service;

public interface LotValueJournalPostLine {

  // PROCEDURE Run@1000000001(pLotValueJnl@1000000000 : Record 8011460);
  public void run(com.axelor.apps.openauction.db.LotValueJournal pLotValueJnl);
  // PROCEDURE FindDuplicate@1100281000(pLotValueEntry@1100281000 : Record 8011461) : Boolean;
  public Boolean findDuplicate(com.axelor.apps.openauction.db.LotValueEntry pLotValueEntry);
  // PROCEDURE CancelLotValue@1100481000(pLotValueEntry@1100481000 : Record 8011461);
  public void cancelLotValue(com.axelor.apps.openauction.db.LotValueEntry pLotValueEntry);
}
