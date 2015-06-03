package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;


/**
 * Represents a TEAM SamplingUnitStub.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="sampling_unit_observed_time")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllSamplingUnitStubs", 
              query="SELECT DISTINCT id, siteId FROM SamplingUnitStub as unit WHERE samplingAt<:endTime and samplingAt>:startTime")         
})
public class SamplingUnitStub {
	
	/**
     * The synthetic database key associated with this unit once it is
     * persisted to a database.
     */
    @Id
    @Column(name="sampling_unit_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

    /**
     * The sampling time.
     */
    @Column(name="sampling_at",nullable=false,unique=false)
    private Date samplingAt;
    
    @Column(name="site_id",nullable=false,unique=false)
    private Integer siteId;
    
    
    public Integer getId() {
    	return id;
    }
    
    
    public Date getSamplingAt() {
    	return samplingAt;
    }
    
    
    public Integer getSiteId() {
    	return siteId;
    }
    
}