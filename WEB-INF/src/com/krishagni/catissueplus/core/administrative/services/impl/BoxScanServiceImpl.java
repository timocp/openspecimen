package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import BoxMapAPI.IBoxMapAPI;
import bmclientapp.BoxMapSocketClient;

import com.krishagni.catissueplus.core.administrative.events.BoxScannerDetail;
import com.krishagni.catissueplus.core.administrative.events.GetAllBoxScannerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllBoxScannersEvent;
import com.krishagni.catissueplus.core.administrative.events.ScanContainerSpecimenDetails;
import com.krishagni.catissueplus.core.administrative.events.ScanStorageContainerDetails;
import com.krishagni.catissueplus.core.administrative.events.ScanStorageContainerDetailsEvents;
import com.krishagni.catissueplus.core.administrative.services.BoxScanService;
import com.krishagni.catissueplus.core.biospecimen.domain.BoxScanner;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;

import edu.wustl.catissuecore.domain.StorageContainer;


public class BoxScanServiceImpl implements BoxScanService{

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	@Override
	@PlusTransactional
	public GetAllBoxScannerEvent getAllBoxScanners(ReqAllBoxScannersEvent reqEvent) {
		List<BoxScanner> list= daoFactory.getContainerDao().getBoxScannerList();
		List<BoxScannerDetail> result = new ArrayList<BoxScannerDetail>(); 	
		for (BoxScanner boxScanner : list) {
			result.add(BoxScannerDetail.fromDomain(boxScanner));
		}
		GetAllBoxScannerEvent response = new GetAllBoxScannerEvent();
		response.ok(result);
		return GetAllBoxScannerEvent.ok(result);
	}
	
	@Override
  @PlusTransactional
  public ScanStorageContainerDetailsEvents validateAndPopulateScanContainerData(ScanStorageContainerDetails details)
  {
    try
    {
      List<StorageContainer> containers = daoFactory.getStorageContainerDao().getAllStorageContainers(
          details.getContainerName(), 1);
      details.setOneDimensionCapacity(containers.get(0).getCapacity().getOneDimensionCapacity());
      details.setTwoDimensionCapacity(containers.get(0).getCapacity().getTwoDimensionCapacity());
      details.setOneDimensionLabellingScheme(containers.get(0).getOneDimensionLabellingScheme());
      details.setTwoDimensionLabellingScheme(containers.get(0).getTwoDimensionLabellingScheme());
      for (ScanContainerSpecimenDetails specDetails : details.getSpecimenList())
      {
        Specimen spec = daoFactory.getSpecimenDao().getSpecimenByBarcode(specDetails.getBarCode());
        if(spec == null){
          specDetails.setNotPresent(true);
          continue;
        }System.out.println(spec.getSpecimenPosition());
        
        if (spec.getSpecimenPosition() != null &&(
            spec.getSpecimenPosition().getPositionDimensionOne()!=Integer.parseInt(specDetails.getPosX())
            || spec.getSpecimenPosition().getPositionDimensionTwo()!= Integer.parseInt(specDetails.getPosY())
            || !spec.getSpecimenPosition().getStorageContainer().getName().equals(details.getContainerName())))
        {
          specDetails.setConflict(true);
          specDetails.setActualContainerName(spec.getSpecimenPosition().getStorageContainer().getName());
          specDetails.setActualPosX(spec.getSpecimenPosition().getPositionDimensionOne().toString());
          specDetails.setActualPosY(spec.getSpecimenPosition().getPositionDimensionTwo().toString());
        }else{
          specDetails.setConflict(false);
        }
        specDetails.setSpecimenId(spec.getId());
        specDetails.setSepcimenLable(spec.getLabel());
      //  specDetails.setPosX(spec.getSpecimenPosition().getPositionDimensionOne().toString());
        //specDetails.setPosY(spec.getSpecimenPosition().getPositionDimensionTwo().toString());
//        specDetails.setContainerName(spec.getSpecimenPosition().getStorageContainer().getName());
        specDetails.setTissueSite(spec.getTissueSite());
        specDetails.setType(spec.getSpecimenType());
      }
      return ScanStorageContainerDetailsEvents.ok(details);
    }
    catch (Exception e)
    {
      return ScanStorageContainerDetailsEvents.serverError(e);
    }
  }
  @Override
  @PlusTransactional
  public ScanStorageContainerDetailsEvents getScanContainerData(String ipAddress,String selContName){
    ScanStorageContainerDetails details = null;
    
    int port;
    String host;
    int ret;
    port = IBoxMapAPI.DEFAULT_PORT;
    host = ipAddress;
    
    BoxMapSocketClient client = new BoxMapSocketClient();

    ret = client.Connect(host, port);
    if (0 != ret) {
        return null;
    }
    details = client.getStorageContainerDetails();
    if(!StringUtils.isEmpty(selContName)){
    	details.setContainerName(selContName);
    }
    client.Close();
    return ScanStorageContainerDetailsEvents.ok(details);
  }
	@Override
	public void resolveConflicts(List<ScanContainerSpecimenDetails> conflictedSpecimenList) {
		// TODO Auto-generated method stub
		
	}

}
