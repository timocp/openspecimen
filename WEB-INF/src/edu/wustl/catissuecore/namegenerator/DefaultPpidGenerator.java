package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;


public class DefaultPpidGenerator implements LabelGenerator{

	private static final Logger LOGGER = Logger.getCommonLogger(DefaultPpidGenerator.class);
	
	@Override
	public void setLabel(Object object) {
		final CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) object;
		String token = "SYS_PPID";
		if (StringUtils.isEmpty(cpr.getProtocolParticipantIdentifier()))
		{
			try {
				StringBuilder builder = new StringBuilder(100);
				String ppIdPrefix = XMLPropertyHandler.getValue("PPID_prefix");
				String ppIdPostfix = XMLPropertyHandler.getValue("PPID_postfix");
				builder.append(ppIdPrefix).append(TokenFactory.getInstance(token).getTokenValue(object)).append(ppIdPostfix);
				cpr.setProtocolParticipantIdentifier(builder.toString());
			}
			catch(Exception exp)
			{
				LOGGER.error("Error while generating PPID");
				LOGGER.error(exp);
				cpr.setProtocolParticipantIdentifier(null);
			}
		}
	}

	@Override
	public void setLabel(List object) throws LabelGenException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabel(Collection<AbstractDomainObject> object) throws LabelGenException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabel(Object object, boolean ignoreCollectedStatus) throws LabelGenException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLabel(Object object) throws LabelGenException {
		// TODO Auto-generated method stub
		return null;
	}

}
