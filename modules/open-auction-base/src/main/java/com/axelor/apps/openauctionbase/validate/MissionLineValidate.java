package com.axelor.apps.openauctionbase.validate;

import com.axelor.apps.openauction.db.AuctionHeader;
import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.MissionLine;
import com.axelor.apps.openauction.db.repo.LotRepository;
import com.axelor.apps.openauction.db.repo.MissionLineRepository;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.inject.Beans;

public class MissionLineValidate {
  /*
   * OnValidate=BEGIN
      //EVALUATE("Lot Mission No.","Lot No./Mission"); //AP10.ST

  //<<AP39 isat.sc
  // Bloquer modif si lot affecté à une vente
  IF Type = Type::Lot THEN BEGIN
      CALCFIELDS("Current Auction No.");
      //TESTFIELD("Current Auction No.",'')
  IF "Current Auction No." <> '' THEN
      ERROR(Text8011405);
      //"Lot No./Mission" := xRec."Lot No./Mission";
  END;
  //>>AP39 isat.sc
  END;
   */
  public MissionLine validateLotMissionNo(MissionLine missionLine, Integer lotNoMission)
      throws AxelorException {
    // EVALUATE("Lot Mission No.","Lot No./Mission"); //AP10.ST
    missionLine.setLotNoMission(lotNoMission);

    if (this.getCurrentAuctionNo(missionLine) != null) {
      throw new AxelorException(
          missionLine,
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR,
          "Vous ne pouvez pas modifier le numéro de Lot/Mission car ce lot est déjà affecté à une vente");
    }

    return missionLine;
  }

  public AuctionHeader getCurrentAuctionNo(MissionLine missionLine) throws AxelorException {
    if (missionLine.getNoLot() != null) {
      return missionLine.getNoLot().getCurrentAuctionNo();
    }
    return null;
  }
  /*
   * OnValidate=VAR
      lStandardText@1000000000 : Record 7;
      lMissionHeader@1000000002 : Record 8011402;
      lLot@1000000001 : Record 8011404;
      BEGIN
      CASE Type OF
          Type::" " : BEGIN
          lStandardText.GET("No.");
          Description := lStandardText.Description;
          "Lot Type" := "Lot Type"::"Origin Lot Component"; // AP06.ISAT.ST
          END;
          Type::Lot : BEGIN
          IF lLot.GET("No.") THEN BEGIN   // debug isat.sf 16/07/09
              lMissionHeader.GET("Mission No.");
              Description := COPYSTR(lLot.Description,1,MAXSTRLEN(Description));
              "Lot Template Code" := lLot."Lot Template Code";
              "Lot Type" := lLot."Lot Type"; // AP06.ISAT.ST
              "Lot Reference No." := lLot."Reference No."; //ap33 isat.zw

              lLot.VALIDATE("Current Mission No.", "Mission No."); //ap17 isat.zw
              lLot."Responsibility Center" := lMissionHeader."Responsibility Center";
              lLot.Judicial := lMissionHeader.Judicial;
              lLot."Current Mission Line No." := "Line No.";
              //lLot."Lot General Status" := lLot."Lot General Status"::"On Mission"; //ap22 isat.zw
              lLot.VALIDATE("Lot General Status", lLot."Lot General Status"::"On Mission");//ap22 isat.zw
              //<<ap30 isat.zw
              CALCFIELDS("Lawyer Bus. No.");
              IF lLot."Lawyer Bus. No." <> "Lawyer Bus. No." THEN
              lLot.VALIDATE("Lawyer Bus. No.", "Lawyer Bus. No.");
              //>>ap30 isat.zw
              lLot.MODIFY;
          END;
          END;
      END;
      END;
  */
  public MissionLine validateNoLot(MissionLine missionLine, Lot lot) {
    LotValidate lotValidate = Beans.get(LotValidate.class);
    missionLine.setNoLot(lot);
    if (missionLine.getType() == null
        || missionLine.getType().equals(MissionLineRepository.TYPE_SELECT_EMPTY)) {
      missionLine.setDescription(lot.getDescription());
      missionLine.setLotType(LotRepository.LOTTYPE_SELECT_ORIGINLOTCOMPONENT);
    } else {
      if (lot != null) {
        missionLine.setDescription(lot.getDescription());
        missionLine.setLotTemplateCode(lot.getLotTemplateCode());
        missionLine.setLotType(lot.getLotType());
        missionLine.setLotReferenceNo(lot.getReferenceNo());

        // validating lot
        lot = lotValidate.validateCurrentMissionNo(lot, missionLine.getMissionNo());
        lot.setResponsibilityCenter(missionLine.getMissionNo().getResponsibilityCenter());
        lot.setJudicial(missionLine.getMissionNo().getJudicial());
        lot.setCurrentMissionLineNo(missionLine);
        lot =
            lotValidate.validateLotGeneralStatus(
                lot, LotRepository.LOTGENERALSTATUS_SELECT_ONMISSION);

        // TODO Laywer Bus. No.
      }
    }
    return missionLine;
  }
}