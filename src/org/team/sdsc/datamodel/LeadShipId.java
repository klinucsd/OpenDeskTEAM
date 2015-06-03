package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.io.*;
import org.apache.commons.lang.builder.*;


/**
 * Represents a TEAM LeadShipId.
 *
 * @author Kai Lin
 */
@Embeddable
public class LeadShipId implements Serializable {

	private Integer personId;
	private Integer siteId;
	private Integer protocolId;
				
	public LeadShipId() {}	
		
	public LeadShipId(Integer siteId, Integer protocolId, Integer personId) {
		this.siteId = siteId;
		this.protocolId = protocolId;
		this.personId = personId;
	}
		
		
	public Integer getPersonId() {
		return personId;
	}
		
	public Integer getSiteId() {
		return siteId;
	}
		
	public Integer getProtocolId() {
		return protocolId;
	}


    public boolean equals(Object obj) { 
    	if (obj instanceof LeadShipId) {
    		LeadShipId aId = (LeadShipId)obj;
    		return this.personId == aId.personId &&
    			   this.siteId == aId.siteId &&
    			   this.protocolId == aId.protocolId;
    	}     	
    	return false;
    }

    
    
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(personId);
        builder.append(siteId);
        builder.append(protocolId);
        return builder.toHashCode();
    }


	public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(personId+"  "+siteId+"   "+protocolId);
		return builder.toString();
	}


}