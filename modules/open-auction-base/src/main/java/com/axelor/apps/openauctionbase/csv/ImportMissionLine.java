package com.axelor.apps.openauctionbase.csv;

import java.util.Map;

import com.axelor.apps.openauction.db.MissionLine;
import com.axelor.apps.openauction.db.repo.LotRepository;
import com.axelor.apps.openauction.db.repo.MissionLineRepository;
import com.google.inject.Inject;

public class ImportMissionLine {
    MissionLineRepository missionLineRepository;
    LotRepository lotRepository;
    @Inject
    public ImportMissionLine(MissionLineRepository missionLineRepository, LotRepository lotRepository) {
        this.missionLineRepository = missionLineRepository;
        this.lotRepository = lotRepository;
    }

    public Object importMissionLine(Object bean, Map<String, Object> values) {

        MissionLine missionLine = (MissionLine) bean;    
        
        if (missionLine.getNoLot() != null && missionLine.getNoLot().getDescription().equals(""))
        {
            missionLine.setNoLot(null);
            missionLineRepository.save(missionLine);
            lotRepository.remove(missionLine.getNoLot());
        }    
    
        return missionLine;
      }
}

