package com.axelor.apps.openauctionbase.service;

import com.axelor.apps.openauction.db.Lot;
import com.axelor.apps.openauction.db.PictureAttachement;
import com.axelor.apps.openauction.db.repo.PictureAttachementRepository;
import com.axelor.meta.MetaFiles;
import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;

public class LotService {
  PictureAttachementRepository pictureAttachementRepo;

  @Inject private MetaFiles metaFiles;

  @Inject
  public LotService(PictureAttachementRepository pictureAttachementRepo) {
    this.pictureAttachementRepo = pictureAttachementRepo;
  }

  public PictureAttachement addPicture(Lot lot) throws IOException {
    PictureAttachement pictureAttachement = new PictureAttachement();

    pictureAttachement.setSourceLotNo(lot);

    File imgTmpFile = MetaFiles.createTempFile("IMG", ".png").toFile();

    pictureAttachement.setPicture(metaFiles.upload(imgTmpFile));
    pictureAttachementRepo.save(pictureAttachement);

    return pictureAttachement;
  }
}
