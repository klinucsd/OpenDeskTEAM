package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;


/**
 * Represents a TEAM LeadShip.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="PROTOCOL_LEAD_SCIENTIST") 
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllLeadShips", 
              query="from LeadShip as leadShip"),
  @NamedQuery(name="org.team.sdsc.datamodel.LeadShipsForProtocolAtSite", 
              query="from LeadShip as leadShip where leadShip.id.siteId=:siteId AND leadShip.id.protocolId=:protocolId")
})
public class LeadShip {


	@Id 
	@AttributeOverrides({ 
    	@AttributeOverride(name   = "personId", 
                       	   column = @Column(name="PERSON_ID", unique=false) ), 
        @AttributeOverride(name   = "siteId", 
                           column = @Column(name="SITE_ID", unique=false) ), 
        @AttributeOverride(name   = "protocolId", 
                           column = @Column(name="PROTOCOL_ID", unique=false) )                           
    }) 
    private LeadShipId id;

    /**
     * The current of the lead scientist.
     */
    @Column(name="CURRENT",nullable=false,unique=false)
    private boolean current;

    @Temporal(TemporalType.DATE) 
    @Column(name="START_DATE",nullable=false,unique=false)
    private Date startDate;
       
    @Temporal(TemporalType.DATE) 
    @Column(name="END_DATE",nullable=true,unique=false)
    private Date endDate;       
   
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public LeadShip() {
    	super();
    }


	public LeadShip(Site site, Protocol protocol, Person person) {
		this.id = new LeadShipId(site.getId(), protocol.getId(), person.getId());
		this.current = true;
		this.startDate = new Date();
	}


	public LeadShipId getId() {
		return id;
	}


	public Date getStartDate() {
		return startDate;
	}
	
	
	public Date getEndDate() {
		return endDate;
	}


    /**
     * Get the status of this site.
     *
     * @return the status of the site.
     */
    public boolean isCurrent() {
        return current;
    }

    /**
     * Set the status of this site.
     *
     * @param status the status of the site.
     */
    public void setCurrent(boolean current) {
        this.current = current;
    }



    /**
     * Produce a human-readable representation of the person.
     *
     * @return a textual description of the person.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("id").append("='").append(getId()).append("' ");
        builder.append("start-date").append("='").append(startDate).append("' ");
        if (endDate != null) 
        	builder.append("end-date").append("='").append(endDate).append("' ");
        builder.append("current").append("='").append(current).append("' ");
        //builder.append("lead scientist").append("='").append(getId()).append("' ");
            
        builder.append("]");
      
        return builder.toString();
    }

}
