package org.team.sdsc.datamodel;


import java.util.*;
import java.text.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM liana record.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="liana1ha_taxonomies")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllLianaRecords", 
              query="from LianaRecord as lianaRecord order by lianaRecord.id")
})
public class LianaRecord {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="collection_sampling_unit_id")
    private Integer id;
    //@GeneratedValue(strategy=GenerationType.AUTO)

    @Column(name="id")
    private Integer sid;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name="collection_sampling_units", 
               joinColumns={@JoinColumn(name="id")},
               inverseJoinColumns={@JoinColumn(name="sampling_unit_id")})
    private SamplingUnit samplingUnit;
    
    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name="observed_at",nullable=false,unique=false)
    private Date collectedAt;	
	
    @Column(name="family", nullable=true, unique=false)
    private String family;
	
    @Column(name="genus", nullable=true, unique=false)
    private String genus;
	
    @Column(name="species", nullable=true, unique=false)
    private String species;
	
    @Transient
    private String morphospecies;

    @Column(name="max_diameter")
    private Float maxDiameter;

    @Column(name="comments")
    private String comments ;

    //@Column(name="valid_record")
    @Transient
    private Boolean validRecord;
	
    @Column(name="valid_comments")
    private String valid_comments;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_event_at")
    private Date lastEventAt;
    
    @Column(name="last_event_by")
    private Integer lastEventBy;
             	
    @Column(name="mortality_date")
    private Date mortalityDate;		
	
    @Column(name="collection_event")
    private String event;		

    @Column(name="observed_by_first_name")  
    private String collectedByFirstName;      
    
    @Column(name="observed_by_last_name")  
    private String collectedByLastName;      

    @Column(name="location_codes")
    private String locationCodes;
	
    @Column(name="condition_codes")
    private String conditionCodes;

    @Column(name="diameter")
    private Float diameter;

    @Column(name="pom_dmax")
    private Float pomDmax;
		
    @Column(name="voucher")
    private String voucher;

    @Column(name="reviewer_name")
    private String reviewerName;

    @Column(name="review_comment")
    private String reviewComment;

    @Column(name="review_result")
    private String reviewResult;    


    // for protocol 1.5

    @Column(name="pom")
    private Float pom;
	
    @Column(name="new_diameter")
    private Float newDiameter;
	
    @Column(name="new_pom_height")
    private Float newPomHeight;

    @Column(name="location_code")
    private String locationCode;

    @Column(name="dead_codes")
    private String deadCodes;

    @Column(name="record_by_first_name")
    private String recordFirstName;

    @Column(name="record_by_last_name")
    private String recordLastName;

    
    public SamplingUnit getSamplingUnit() {
	return samplingUnit;
    }
   
   
    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this record
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }
  
  
    /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this record
     *         is persisted.
     */
    @Display(title="Id", index=1, width=50)
    public Integer getId() {
        return sid;
    }


    public Date getCollectedAt() {
    	return collectedAt;
    }


    public void setCollectedAt(Date date) {
	this.collectedAt = date;
    }

    @Display(title="Observation Date", var="ObservationDate", index=2, width=100)    
    public String getObservationDate() {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	return formatter.format(collectedAt);
    }


    @Display(title="Family", index=3, width=100)  
    public String getFamily() {
    	return family;
    }
    
    @Display(title="Genus", index=4, width=100)  
    public String getGenus() {
    	return genus;
    }
    
    @Display(title="Species", index=5, width=100)  
    public String getSpecies() {
    	return species;
    }
    
    @Display(title="Names of Collectors", var="NamesOfCollectors", index=6, width=100)  
    public String getObserverName() {
    	return collectedByFirstName+" "+collectedByLastName;  
    }
    
    @Display(title="Diameter at 1.3m", var="DiameterAt13m", index=7, width=100)      
    public Float getDiameter() {
    	return diameter;
    }
    
    @Display(title="Max Diameter", var="MaxDiameter", index=8, width=100)  
    public Float getMaxDiameter() {
    	return maxDiameter;
    }    

    @Display(title="Max Diameter POM", var="MaxDiameterPOM", index=9, width=100)  
    public Float getMaxDiameterPOM() {
    	return pomDmax;
    }    
     
    @Display(title="Condition Codes", var="ConditionCodes", index=10, width=100) 
    public String getConditionCodes() {
    	return conditionCodes;
    }
     
    @Display(title="Location Codes", var="LocationCodes", index=11, width=100) 
    public String getLocationCodes() {
    	return locationCodes;
    }

    @Display(title="Sampling Period", var="SamplingPeriod", index=12, width=100)
    public String getEvent() {
    	return event;
    }

    @Display(title="Comments", index=13, width=100)
    public String getComments() {
    	return comments;
    }
    
  
    /**
     * Find a leader scientist for this record
     *
     */
    public Person getLeader(Map<Person, LeadShip> leaderMap) {
	Person person = null;
  		
	for (Person aPerson : leaderMap.keySet()) {
	    LeadShip leader = leaderMap.get(aPerson);
	    if (leader.isCurrent()) {
		if (collectedAt.after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (collectedAt.after(leader.getStartDate()) &&
		       collectedAt.before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  		
	return person;
    }



    static public String getSortPath(String string) {
	String result = "sid";
	if (string.equals("Id")) {
	    result = "sid";
	} else if (string.equals("ObservationDate")) {
	    result = "collectedAt";
        } else if (string.equals("Family")) {
	    result = "family";
        } else if (string.equals("Genus")) {
	    result = "genus";
        } else if (string.equals("Species")) {
	    result = "species";
	} else if (string.equals("NamesOfCollectors")) {
	    result = "collectedByFirstName";
	} else if (string.equals("DiameterAt13m")) {
	    result = "diameter";
	} else if (string.equals("MaxDiameter")) {
	    result = "maxDiameter";
	} else if (string.equals("MaxDiameterPOM")) {
	    result = "pomDmax";
	} else if (string.equals("ConditionCodes")) {
	    result = "conditionCodes";
	} else if (string.equals("LocationCodes")) {
	    result = "locationCodes";
	} else if (string.equals("SamplingPeriod")) {
	    result = "event";
	} else if (string.equals("Comments")) {
	    result = "comments";
	} else if (string.equals("SamplingPeriod")) {
	    result = "event";
        } else if (string.equals("SamplingUnitName")) {
	    result = "samplingUnit.name";
        } else if (string.equals("Latitude")) {
	    result = "samplingUnit.lat";
        } else if (string.equals("Longitude")) {
	    result = "samplingUnit.lat";
        } else if (string.equals("Method")) {
	    result = "samplingUnit.method";
        } else if (string.equals("SiteName")) {
	    result = "samplingUnit.site.name";
        } else if (string.equals("BlockName")) {
	    result = "samplingUnit.block.name";
        } else if (string.equals("ProtocolVersion")) {
	    result = "samplingUnit.protocolFamily";
        } else if (string.equals("ReviewResult")) {
	    result = "reviewResult";
        } else if (string.equals("ReviewComment")) {
	    result = "reviewComment";
        }         
	return result;
    }




    public void setValue(String name, String value, String username) throws Exception {
	if (name.equals("ObservationDate")) {
	    int index = value.indexOf("GMT");
	    if (index != -1) value = value.substring(0, index);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    collectedAt = formatter.parse(value);	    
	} else if (name.equals("Family")) {
	    family = value;
	} else if (name.equals("Genus")) {
	    genus = value;
	} else if (name.equals("Species")) {
	    species = value;
	} else if (name.equals("DiameterAt13m")) {
	    diameter = Float.parseFloat(value);
	} else if (name.equals("MaxDiameter")) {
	    maxDiameter = Float.parseFloat(value);
	} else if (name.equals("MaxDiameterPOM")) {
	    pomDmax = Float.parseFloat(value);
	} else if (name.equals("ConditionCodes")) {
	    conditionCodes = value;
	} else if (name.equals("LocationCodes")) {
	    locationCodes = value;
	} else if (name.equals("SamplingPeriod")) {
	    event = value;
        } else if (name.equals("Comments")) {
	    comments = value;
        } else if (name.equals("ReviewResult")) {
	    reviewResult = value;
	    reviewerName = username;
        } else if (name.equals("ReviewComment")) {
	    reviewComment = value;
	    reviewerName = username;
        } 
    }

 
    public Review getReview() {
	return new Review(this.reviewerName, this.reviewComment, this.reviewResult); 
    }
   
 
    public Integer getSID() {
	return sid;
    }


    public void setSID(Integer sid) {
	this.sid = sid;
    }



    public void setFamily(String family) {
	this.family = family;
    }



    public void setGenus(String genus) {
	this.genus = genus;
    }


    public void setSpecies(String species) {
	this.species = species;
    }


    public void setDiameter(Float diameter) {
	this.diameter = diameter;
    } 


    public void setPom(Float pom) {
	this.pom = pom;
    }


    public void setEvent(String event) {
	this.event = event;
    }


    public void setComments(String comments) {
	this.comments = comments;
    } 



    public String getDeadCodes() {
	return this.deadCodes;
    }


    public void setDeadCodes(String deadCodes) {
	this.deadCodes = deadCodes;
    }


    public String getLocationCode() {
	return locationCode;
    }


    public void setLocationCode(String locationCode) {
	this.locationCode = locationCode;
    }


    public String getRecordFirstName() {
	return this.recordFirstName;
    }


    public void setRecordFirstName(String rFirstName) {
	this.recordFirstName = rFirstName;
    }

    public String getRecordLastName() {
	return this.recordLastName;
    }


    public void setRecordLastName(String rLastName) {
	this.recordLastName = rLastName;
    }

    public void setConditionCodes(String codes) {
	this.conditionCodes = codes;
    }

 
    public String getVoucher() {
	return voucher;
    }


    public void setVoucher(String voucher) {
	this.voucher = voucher;
    }


    public String getCollectedByFirstName() {
	return collectedByFirstName;
    }


    public void setCollectedByFirstName(String name) {
        this.collectedByFirstName = name;
    }


    public String getCollectedByLastName() {
	return collectedByLastName;
    }


    public void setCollectedByLastName(String name) {
        this.collectedByLastName = name;
    }


    public void setNewDiameter(Float newDiameter) {
       this.newDiameter = newDiameter;
    }
    

    public void setNewPomHeight(Float newPomHeight) {
        this.newPomHeight = newPomHeight ;
    }


    /**
     * Produce a human-readable representation of the record.
     *
     * @return a textual description of the site.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("id").append("='").append(id).append("' ");
        builder.append("diameter").append("='").append(diameter).append("' ");
        builder.append("]");
      
        return builder.toString();
    }
    

}