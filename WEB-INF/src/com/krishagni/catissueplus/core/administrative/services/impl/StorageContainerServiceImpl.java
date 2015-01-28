
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.events.AllocateSpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqSpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionAllocatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

import edu.wustl.catissuecore.domain.AbstractPosition;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;

public class StorageContainerServiceImpl implements StorageContainerService {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public StorageContainersEvent getStorageContainers(ReqStorageContainersEvent req) {		
		List<StorageContainerSummary> containers = daoFactory.getStorageContainerDao().getStorageContainers(
				req.getName(),
				req.getMaxResults(),
				req.getSpecimenId(),
				req.isOnlyFreeContainers());
		
		return StorageContainersEvent.ok(containers);
	}

	@Override
	@PlusTransactional
	public StorageContainerEvent getStorageContainer(ReqStorageContainerEvent req) {
		try {
			Long containerId = req.getId();
			StorageContainer container = daoFactory.getStorageContainerDao().getStorageContainer(containerId);
			if (container == null) {
				StorageContainerEvent.notFound(containerId);
			}
			
			return StorageContainerEvent.ok(StorageContainerDetail.from(container, req.isIncludeOccupiedPositions()));
		} catch (Exception e) {
			return StorageContainerEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public SpecimenPositionEvent getSpecimenPosition(ReqSpecimenPositionEvent req) {
		Long posId = req.getPosId(), specimenId = req.getSpecimenId();
		
		SpecimenPosition result = null;
		if (posId != null) {
			result = daoFactory.getSpecimenPositionDao().getPosition(posId);
		} else if (specimenId != null) {
			result = daoFactory.getSpecimenPositionDao().getPositionBySpecimenId(specimenId);
		}
	
		return SpecimenPositionEvent.ok(SpecimenPositionDetail.from(result));
	}

	@Override
	@PlusTransactional
	public SpecimenPositionAllocatedEvent assignSpecimenPosition(AllocateSpecimenPositionEvent req) {
		try {
			SpecimenPosition position = createSpecimenPosition(req.getPosition());
			daoFactory.getSpecimenPositionDao().saveOrUpdate(position);
			return SpecimenPositionAllocatedEvent.ok(SpecimenPositionDetail.from(position));
		} catch (ObjectCreationException oce) {
			return SpecimenPositionAllocatedEvent.badRequest(oce);			
		} catch (IllegalArgumentException iae) {
			return SpecimenPositionAllocatedEvent.badRequest(iae);
		} catch (Exception e) {
			return SpecimenPositionAllocatedEvent.serverError(e);
		}
	}	
	
	@Override
	@PlusTransactional
	public SpecimenPositionUpdatedEvent updateSpecimenPosition(UpdateSpecimenPositionEvent req) {
		try {
			Long posId = req.getPosition().getId();
			SpecimenPosition existing = daoFactory.getSpecimenPositionDao().getPosition(posId);
			if (existing == null) {
				ObjectCreationException oce = new ObjectCreationException();
				oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "positionId");
				throw oce;
			}
			
			SpecimenPosition position = createSpecimenPosition(req.getPosition());
			updateSpecimenPosition(existing, position);						
			daoFactory.getSpecimenPositionDao().saveOrUpdate(existing);
			return SpecimenPositionUpdatedEvent.ok(SpecimenPositionDetail.from(position));
		} catch (ObjectCreationException oce) {
			return SpecimenPositionUpdatedEvent.badRequest(oce);			
		} catch (IllegalArgumentException iae) {
			return SpecimenPositionUpdatedEvent.badRequest(iae);
		} catch (Exception e) {
			return SpecimenPositionUpdatedEvent.serverError(e);
		}
	}
		
	private SpecimenPosition createSpecimenPosition(SpecimenPositionDetail requestedPosition) {
		ObjectCreationException oce = new ObjectCreationException();
		
		SpecimenPosition position = new SpecimenPosition();
		position.setId(requestedPosition.getId());
		setContainer(position, requestedPosition, oce);
		setSpecimen(position, requestedPosition, oce);
		setCoordinates(position, requestedPosition, oce);

		oce.checkErrorAndThrow();
		return position;
	}
	
	private void setContainer(SpecimenPosition position, SpecimenPositionDetail detail, ObjectCreationException oce) {
		Long containerId = detail.getStorageContainerId();
		
		if (containerId == null) {
			oce.addError(StorageContainerErrorCode.MISSING_ATTR_VALUE, "storageContainerId");
			return;
		}
		
		StorageContainer container = daoFactory.getStorageContainerDao().getStorageContainer(containerId);
		if (container == null) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "storageContainerId");
			return;
		}
		
		position.setStorageContainer(container);
	}
	
	private void setSpecimen(SpecimenPosition position, SpecimenPositionDetail detail, ObjectCreationException oce) {
		Long specimenId = detail.getSpecimenId();
		
		if (specimenId == null) {
			oce.addError(StorageContainerErrorCode.MISSING_ATTR_VALUE, "specimenId");
			return;
		}
		
		Specimen specimen = daoFactory.getStorageContainerDao().getSpecimen(specimenId);
		if (specimen == null) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "specimenId");
			return;
		}
		
		position.setSpecimen(specimen);
	}
	
	private void setCoordinates(SpecimenPosition position, SpecimenPositionDetail detail, ObjectCreationException oce) {
		String posX = detail.getPosX();
		String posY = detail.getPosY();
		
		if (StringUtils.isBlank(posX) || StringUtils.isBlank(posY)) {
			allocateNextAvailablePosition(position, oce);
		} else {
			allocatePosition(position, posX, posY, oce);						
		}
		
	}
	
	private void allocateNextAvailablePosition(SpecimenPosition position, ObjectCreationException oce) {
		StorageContainer container = position.getStorageContainer();
		Map<Integer, AbstractPosition> occupiedPositions = getOccupiedPositions(container);
		
		int dimensionOneCapacity = container.getCapacity().getOneDimensionCapacity();
		for (int y = 1; y <= container.getCapacity().getTwoDimensionCapacity(); ++y) {
			for (int x = 1; x <= container.getCapacity().getOneDimensionCapacity(); ++x) {
				Integer pos = (y - 1) * dimensionOneCapacity + x;
				if (!occupiedPositions.containsKey(pos)) {
					position.setPositionDimensionOne(x);
					position.setPositionDimensionTwo(y);
					toLabelingSchemePositions(position);
					return;
				}				
			}
		}		
		
		oce.addError(StorageContainerErrorCode.NO_FREE_POSITION, "storageContainer");
	}
	
	private void allocatePosition(SpecimenPosition position, String posX, String posY, ObjectCreationException oce) {
		StorageContainer container = position.getStorageContainer();
		Specimen specimen = position.getSpecimen();
		
		Integer posXInt = fromLabelingScheme(container.getOneDimensionLabellingScheme(), posX);
		Integer posYInt = fromLabelingScheme(container.getOneDimensionLabellingScheme(), posY);
		if (!isAvailable(container, specimen, posXInt, posYInt)) {
			oce.addError(StorageContainerErrorCode.NO_FREE_POSITION, "posX/posY");
			return;
		}
		
		position.setPositionDimensionOne(posXInt);
		position.setPositionDimensionOneString(posX);
		position.setPositionDimensionTwo(posYInt);
		position.setPositionDimensionTwoString(posY);		
	}
	
	private boolean isAvailable(StorageContainer container, Specimen specimen, Integer posX, Integer posY) {
		int dimensionOneCapacity = container.getCapacity().getOneDimensionCapacity();
		
		Map<Integer, AbstractPosition> occupiedPositions = getOccupiedPositions(container);
		Integer pos = (posY - 1) * dimensionOneCapacity + posX;
		
		AbstractPosition occupiedPos = occupiedPositions.get(pos);
		if (occupiedPos == null) {
			return true;
		}
		
		if (occupiedPos instanceof SpecimenPosition) {
			SpecimenPosition occupiedSpecimenPos = (SpecimenPosition)occupiedPos;
			if (occupiedSpecimenPos.getSpecimen().getId().equals(specimen.getId())) {
				return true;
			}
		}
		
		return false;
	}
	
	private Map<Integer, AbstractPosition> getOccupiedPositions(StorageContainer container) {
		Map<Integer, AbstractPosition> occupiedPositions = new HashMap<Integer, AbstractPosition>();
		
		int dimensionOneCapacity = container.getCapacity().getOneDimensionCapacity();
		for (SpecimenPosition occupiedPos : container.getSpecimenPositionCollection()) {
			Integer pos = (occupiedPos.getPositionDimensionTwo() - 1) * dimensionOneCapacity + occupiedPos.getPositionDimensionOne();
			occupiedPositions.put(pos, occupiedPos);
		}
		
		for (ContainerPosition occupiedPos : container.getOccupiedPositions()) {
			Integer pos = (occupiedPos.getPositionDimensionTwo() - 1) * dimensionOneCapacity + occupiedPos.getPositionDimensionOne();
			occupiedPositions.put(pos, occupiedPos);
		}
		
		return occupiedPositions;
	}
	
	private void updateSpecimenPosition(SpecimenPosition existing, SpecimenPosition newPos) {
		existing.setSpecimen(newPos.getSpecimen());
		existing.setStorageContainer(newPos.getStorageContainer());
		existing.setPositionDimensionOne(newPos.getPositionDimensionOne());
		existing.setPositionDimensionTwo(newPos.getPositionDimensionTwo());
		existing.setPositionDimensionOneString(newPos.getPositionDimensionOneString());
		existing.setPositionDimensionTwoString(newPos.getPositionDimensionTwoString());
		existing.setId(newPos.getId());
	}
	
	private void toLabelingSchemePositions(SpecimenPosition position) {
		String dimensionOneScheme = position.getStorageContainer().getOneDimensionLabellingScheme();
		String posX = converters.get(dimensionOneScheme).fromInteger(position.getPositionDimensionOne());
		position.setPositionDimensionOneString(posX);
		
		String dimensionTwoScheme = position.getStorageContainer().getTwoDimensionLabellingScheme();
		String posY = converters.get(dimensionTwoScheme).fromInteger(position.getPositionDimensionTwo());
		position.setPositionDimensionTwoString(posY);
	}
	
	private Integer fromLabelingScheme(String labelingScheme, String pos) {
		try {
			return converters.get(labelingScheme).toInteger(pos);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid position: " + pos + " for " + labelingScheme);
		}
	}
	
	private Map<String, Converter> converters = new HashMap<String, Converter>() {
		private static final long serialVersionUID = -1198152629671796530L;

		{
			put("Numbers", new NumberConverter());
			put("Alphabets Upper Case", new AlphabetConverter(true));
			put("Alphabets Lower Case", new AlphabetConverter(false));
			put("Roman Upper Case", new RomanConverter(true));
			put("Roman Lower Case", new RomanConverter(false));
		}
	};
	
	private interface Converter {
		public Integer toInteger(String pos);
		
		public String fromInteger(Integer pos);
	}
	
	private class NumberConverter implements Converter {

		@Override
		public Integer toInteger(String pos) {
			if (pos == null) {
				return null;
			}
			
			return Integer.parseInt(pos);
		}

		@Override
		public String fromInteger(Integer pos) {
			if (pos == null || pos <= 0) {
				throw new IllegalArgumentException("Number can't be lesser than 1");
			}
			
			return pos.toString();
		}		
	}
	
	private class RomanConverter implements Converter {
		private final Map<String, Integer> romanLiterals = new HashMap<String, Integer>() {
			{
				put("m", 1000);
				put("cm", 900);
				put("d",  500);
				put("cd", 400);
				put("c",  100);
				put("xc",  90);
				put("l",   50);
				put("xl",  40);
				put("x",   10);
				put("ix",   9);
				put("v",    5);
				put("iv",   4);
				put("i",    1);				
			}			
		};
		
		private boolean upper;
		
		public RomanConverter(boolean upper) {
			this.upper = upper;
		}

		@Override
		public Integer toInteger(String pos) {
			pos = pos.toLowerCase();
			
			int result = 0;
			int len = pos.length(), idx = len;
			while (idx > 0) {
				--idx;
				if (idx == len - 1) {
					Integer val = romanLiterals.get(pos.substring(idx, idx + 1));
					if (val == null) {
						throw new IllegalArgumentException("Invalid roman number: " + pos);
					}
					result += val;
				} else {
					Integer current = romanLiterals.get(pos.substring(idx, idx + 1));
					Integer ahead = romanLiterals.get(pos.substring(idx + 1, idx + 2));
					if (current == null || ahead == null) {
						throw new IllegalArgumentException("Invalid roman number: " + pos);
					}
					
					if (current < ahead) {
						result -= current;
					} else {
						result += current;
					}
				}
			}
			
			return result;
		}

		@Override
		public String fromInteger(Integer pos) {
			if (pos == null || pos <= 0) {
				throw new IllegalArgumentException("Number can't be lesser than 1");
			}
			
			StringBuilder result = new StringBuilder();			
			int num = pos;
			for (Map.Entry<String, Integer> literal : romanLiterals.entrySet()) {
				while (num >= literal.getValue()) {
					result.append(literal.getKey());
					num -= literal.getValue();
				}
			}

			return upper ? result.toString().toUpperCase() : result.toString();
		}
		
	}
	
	private class AlphabetConverter implements Converter {
		private final char[] alphabets = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
		};
				
		private boolean upper;
		
		public AlphabetConverter(boolean upper) {
			this.upper = upper;			
		}

		@Override
		public Integer toInteger(String pos) {
			pos = pos.toLowerCase();
			
			if (!StringUtils.isAlpha(pos)) {
				throw new IllegalArgumentException("Invalid alpha string: " + pos);
			}
			
			int len = pos.length();
			int base = 1, result = 0;
			while (len > 0) {
				len--;
				
				int charAt = pos.charAt(len);
				result = result + (charAt - 'a' + 1) * base;
				base *= 26;
			}

			return result;
		}

		@Override
		public String fromInteger(Integer pos) {
			if (pos == null || pos <= 0) {
				throw new IllegalArgumentException("Number can't be lesser than 1");
			}
			
			StringBuilder result = new StringBuilder();			
			int num = pos;
		    while (num > 0) {
		    	result.insert(0, alphabets[(num - 1) % 26]);
		    	num = (num - 1) / 26;		      
		    }

		    return upper ? result.toString().toUpperCase() : result.toString();
		}		
	}
}
