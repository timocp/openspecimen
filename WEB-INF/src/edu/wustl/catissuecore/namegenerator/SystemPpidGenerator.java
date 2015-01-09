package edu.wustl.catissuecore.namegenerator;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.tokenprocessor.DefaultUniqueIdGenerator;
import edu.wustl.common.util.KeySequenceGeneratorUtil;
import edu.wustl.common.util.logger.Logger;


public class SystemPpidGenerator implements LabelTokens{

	private static final Logger LOGGER = Logger.getCommonLogger(SystemPpidGenerator.class);
	
	@Override
	public String getTokenValue(Object object) throws ApplicationException {
		String nextAvailableId = "";
		try
		{
			final String uniqueId = KeySequenceGeneratorUtil.getNextUniqeId("SYS_PPID","Unique PPID");
			if (uniqueId != null)
			{
				nextAvailableId = uniqueId;
			}
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			final ErrorKey errorKey = ErrorKey.getErrorKey("error.generating.auto.PPI");
			throw new ApplicationException(errorKey, exception,
					exception.getMessage());
		}
		return nextAvailableId;
	}

}
