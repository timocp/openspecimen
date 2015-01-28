
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import BoxMapAPI.IBoxMapAPI;
import bmclientapp.BoxMapSocketClient;

import com.krishagni.catissueplus.core.administrative.events.BoxScannerDetail;
import com.krishagni.catissueplus.core.administrative.events.GetAllBoxScannerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllBoxScannersEvent;
import com.krishagni.catissueplus.core.administrative.events.ResolveScanConflictEvent;
import com.krishagni.catissueplus.core.administrative.events.ScanContainerSpecimenDetails;
import com.krishagni.catissueplus.core.administrative.events.ScanStorageContainerDetails;
import com.krishagni.catissueplus.core.administrative.events.ScanStorageContainerDetailsEvents;
import com.krishagni.catissueplus.core.administrative.services.BoxScanService;
import com.krishagni.catissueplus.core.biospecimen.domain.BoxScanner;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;

import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.exception.BizLogicException;

public class BoxScanServiceImpl implements BoxScanService {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public GetAllBoxScannerEvent getAllBoxScanners(ReqAllBoxScannersEvent reqEvent) {
		List<BoxScanner> list = daoFactory.getContainerDao().getBoxScannerList();
		List<BoxScannerDetail> result = new ArrayList<BoxScannerDetail>();
		for (BoxScanner boxScanner : list) {
			result.add(BoxScannerDetail.fromDomain(boxScanner));
		}
		return GetAllBoxScannerEvent.ok(result);
	}

	@Override
	@PlusTransactional
	public ScanStorageContainerDetailsEvents validateAndPopulateScanContainerData(ScanStorageContainerDetails details) {
		try {
			List<StorageContainer> containers = daoFactory.getStorageContainerDao().getAllStorageContainers(
					details.getContainerName(), 1);
			details.setOneDimensionCapacity(containers.get(0).getCapacity().getOneDimensionCapacity());
			details.setTwoDimensionCapacity(containers.get(0).getCapacity().getTwoDimensionCapacity());
			details.setOneDimensionLabellingScheme(containers.get(0).getOneDimensionLabellingScheme());
			details.setTwoDimensionLabellingScheme(containers.get(0).getTwoDimensionLabellingScheme());
			for (ScanContainerSpecimenDetails specDetails : details.getSpecimenList()) {
				Specimen spec = daoFactory.getSpecimenDao().getSpecimenByBarcode(specDetails.getBarCode());
				if (spec == null) {
					spec = daoFactory.getSpecimenDao().getSpecimenByRfId(specDetails.getBarCode());
					if(spec == null){
						specDetails.setNotPresent(true);
						continue;
					}
				}

				if (spec.getSpecimenPosition() != null
						&& (spec.getSpecimenPosition().getPositionDimensionOne() != Integer.parseInt(specDetails.getPosX())
								|| spec.getSpecimenPosition().getPositionDimensionTwo() != Integer.parseInt(specDetails.getPosY()) || !spec
								.getSpecimenPosition().getStorageContainer().getName().equals(details.getContainerName()))) {
					specDetails.setConflict(true);
					specDetails.setActualContainerName(spec.getSpecimenPosition().getStorageContainer().getName());
					specDetails.setActualPosX(spec.getSpecimenPosition().getPositionDimensionOne().toString());
					specDetails.setActualPosY(spec.getSpecimenPosition().getPositionDimensionTwo().toString());
				}
				else if (spec.getSpecimenPosition() == null) {
					specDetails.setConflict(true);
				}
				else {
					specDetails.setConflict(false);
				}
				specDetails.setSpecimenId(spec.getId());
				specDetails.setSepcimenLable(spec.getLabel());
				specDetails.setTissueSite(spec.getTissueSite());
				specDetails.setType(spec.getSpecimenType());
			}
			return ScanStorageContainerDetailsEvents.ok(details);
		}
		catch (Exception e) {
			return ScanStorageContainerDetailsEvents.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ScanStorageContainerDetailsEvents getScanContainerData(String ipAddress, String selContName) {
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
		details = client.getStorageContainerDetails(selContName);
		if (!StringUtils.isEmpty(selContName)) {
			details.setContainerName(selContName);
		}
		client.Close();
		return ScanStorageContainerDetailsEvents.ok(details);
	}

	@Override
	@Transactional
	public ScanStorageContainerDetailsEvents resolveConflicts(ResolveScanConflictEvent req) {
		ScanStorageContainerDetails detail = req.getDetails();
		List<ScanContainerSpecimenDetails> list = detail.getSpecimenList();
		for (ScanContainerSpecimenDetails scanSpecimen : list) {
			if (!scanSpecimen.isNotPresent() && scanSpecimen.isConflict()) {
				TransferEventParameters trfrEvent = new TransferEventParameters();
				trfrEvent.setFromPositionDimensionOne(null);
				trfrEvent.setFromPositionDimensionTwo(null);
				trfrEvent.setFromStorageContainer(null);
				User user = new User();
				user.setId(req.getSessionDataBean().getUserId());
				trfrEvent.setUser(user);
				edu.wustl.catissuecore.domain.Specimen sp = new edu.wustl.catissuecore.domain.Specimen();
				sp.setId(scanSpecimen.getSpecimenId());
				trfrEvent.setSpecimen(sp);
				trfrEvent.setTimestamp(new Date());
				trfrEvent.setToPositionDimensionOne(Integer.valueOf(scanSpecimen.getPosX()));
				trfrEvent.setToPositionDimensionTwo(Integer.valueOf(scanSpecimen.getPosY()));
				StorageContainer cont = new StorageContainer();
				cont.setName(scanSpecimen.getContainerName());
				//				cont.setId(scanSpecimen.get);
				trfrEvent.setToStorageContainer(cont);

				SpecimenEventParametersBizLogic eventBizLogic = new SpecimenEventParametersBizLogic();
				try {
					eventBizLogic.insert(trfrEvent, req.getSessionDataBean());
					scanSpecimen.setConflict(false);
					scanSpecimen.setActualContainerName(null);
					scanSpecimen.setActualPosX(null);
					scanSpecimen.setActualPosY(null);
				}
				catch (BizLogicException e) {
					return ScanStorageContainerDetailsEvents.serverError(e);
				}
			}
		}
		return ScanStorageContainerDetailsEvents.ok(detail);
	}

}
