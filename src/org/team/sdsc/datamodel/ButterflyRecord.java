package org.team.sdsc.datamodel;


import java.util.*;
import java.text.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM butterfly record.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="butterfly_taxonomies")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllButterflyRecords", 
              query="from ButterflyRecord as record order by record.id")
})
public class ButterflyRecord {

    /**
     * The synthetic database key associated with this record once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name="collection_sampling_unit_id")
    private Collectionship collectionship;    	

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="observed_at",nullable=false,unique=false)
    private Date observedAt;	

    @Column(name="observed_by")
    private String observedBy;
 
    @Column(name="individual_Id")
    private Integer individualId;

    @Column(name="family", nullable=true, unique=false)
    private String family;
	
    @Column(name="subfamily", nullable=true, unique=false)
    private String subfamily;
	
    @Column(name="genus", nullable=true, unique=false)
    private String genus;
	
    @Column(name="species", nullable=true, unique=false)
    private String species;
	
    @Column(name="subspecies", nullable=true, unique=false)
    private String subspecies;

    @Column(name="tribe", nullable=true, unique=false)
    private String tribe;

    //@Column(name="morphospecies", nullable=true, unique=false)
    @Transient
    private Integer morphospecies;

    @Column(name="gender")
    private Character gender;

    @Column(name="recapture")
    private Character recapture;
	
    @Column(name="envelope")
    private String envelope;
	
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name="identified_at",nullable=false,unique=false)
    private Date identifiedAt;
		
    @Column(name="identified_by")
    private String identifiedBy;
	
    @Column(name="composite_stamp")
    private String compositeStamp;

    @Column(name="comments")
    private String comments ;

    //@Column(name="valid_record")
    @Transient
    private Boolean validRecord;	

    @Column(name="valid_comments")
    private String validComments;	

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_event_at")
    private Date lastEventAt;
    
    @Column(name="last_event_by")
    private Integer lastEventBy;

    @Column(name="collection_event")
    private String event;	
	
    @Column(name="observed_time")
    private String observedTime;

    @Column(name="reviewer_name")
    private String reviewerName;
    
    @Column(name="review_comment")
    private String reviewComment;

    @Column(name="review_result")
    private String reviewResult;


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
        return id;
    }
    
    
    public Date getObservedAt() {
    	return observedAt;
    }

	
    @Display(title="Date Captured", var="DateCaptured", index=2, width=100)    
    public String getCapturedDate() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(observedAt);
    }
	

    @Display(title="Time Captured", var="TimeCaptured", index=3, width=100)    
    public String getCapturedTime() {
	SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return sdf.format(observedAt);
    }
	
          
    @Display(title="Family", index=4, width=100)  
    public String getFamily() {
    	return family;
    }
    
    
    @Display(title="Subfamily", index=5, width=100)  
    public String getSubfamily() {
    	return subfamily;
    }
    
        
    @Display(title="Genus", index=6, width=100)  
    public String getGenus() {
    	return genus;
    }
    
    
    @Display(title="Species", index=7, width=100)  
    public String getSpecies() {
    	return species;
    } 
 
 
    @Display(title="Subspecies", index=8, width=100)  
    public String getSubspecies() {
    	return subspecies;
    } 
 
 
    @Display(title="Gender", index=9, width=100)  
    public Character getGender() {
 	return gender;
    }
 

    @Display(title="Date Identified", var="DateIdentified", index=10, width=100)  
    public Date getIdentifiedAt() {
        return identifiedAt;
    }
 	
 	
    @Display(title="Identifier Name", var="IdentifierName", index=11, width=100)  
    public String getIdentifiedBy() {
 	return identifiedBy;
    }
 
 
    @Display(title="Comments", index=12, width=100)
    public String getComments() {
    	return comments;
    }
     

    @Display(title="Sampling Period", var="SamplingPeriod", index=13, width=100)
    public String getEvent() {
    	return event;
    }

	
    @Display(title="Envelope", index=14, width=100)
    public String getEnvelope() {
    	return envelope;
    }


    @Display(title="Recapture", index=15, width=100)  
    public Character getRecapture() {
 	return recapture;
    }


    public SamplingUnit getSamplingUnit() {
    	return collectionship.getSamplingUnit();
    }
    


    public String  setValue(String name, String value, String username) throws Exception {
	String result = null;
	if (name.equals("Family")) {
	    result = family;
	    family = value;
	} else if (name.equals("Subfamily")) {
	    result = subfamily;
	    subfamily = value;
	} else if (name.equals("Genus")) {
	    result = genus;
	    genus = value;
	} else if (name.equals("Species")) {
	    result = species;
	    species = value;
	} else if (name.equals("Subspecies")) {
	    result = subspecies;
	    subspecies = value;
	} else if (name.equals("Gender")) {
	    if (gender != null) result = gender.toString();
	    gender = value.charAt(0);
	} else if (name.equals("DateCaptured")) {
	    if (observedAt != null) result = observedAt.toString();
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    int index = value.indexOf("GMT");
	    if (index != -1) value = value.substring(0, index);
	    observedAt = formatter.parse(value);
	} else if (name.equals("DateIdentified")) {
	    if (identifiedAt != null) result = identifiedAt.toString();
	    int index = value.indexOf("GMT");
	    if (index != -1) value = value.substring(0, index);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    identifiedAt = formatter.parse(value);;
	} else if (name.equals("IdentifiedName")) {
	    result = identifiedBy;
	    identifiedBy = value;
	} else if (name.equals("Recapture")) {
	    if (recapture != null) result = recapture.toString(); 
	    recapture = value.charAt(0);   
        } else if (name.equals("ReviewResult")) {
	    result = reviewResult;
	    reviewResult = value;
	    reviewerName = username;
        } else if (name.equals("ReviewComment")) {
	    result = reviewComment;
	    reviewComment = value;
	    reviewerName = username;
        }

	return result;
    }


    static public String getSortPath(String string) {
	String result = "id";
	if (string.equals("Id")) {
	    result = "id";
	} else if (string.equals("DateCaptured")) {
	    result = "observedAt";
        } else if (string.equals("TimeCaptured")) {
	    result = "observedAt";
        } else if (string.equals("Family")) {
	    result = "family";
        } else if (string.equals("Subfamily")) {
	    result = "subfamily";
        } else if (string.equals("Genus")) {
	    result = "genus";
        } else if (string.equals("Species")) {
	    result = "species";
        } else if (string.equals("Gender")) {
	    result = "gender";
        } else if (string.equals("DateIdentified")) {
	    result = "identifiedAt";
        } else if (string.equals("IdentifierName")) {
	    result = "identifiedBy";
        } else if (string.equals("Comments")) {
	    result = "comments";
        } else if (string.equals("SamplingPeriod")) {
	    result = "event";
        } else if (string.equals("Recapture")) {
	    result = "recapture";
        } else if (string.equals("SamplingUnitName")) {
	    result = "collectionship.samplingUnit.name";
        } else if (string.equals("Latitude")) {
	    result = "collectionship.samplingUnit.lat";
        } else if (string.equals("Longitude")) {
	    result = "collectionship.samplingUnit.lat";
        } else if (string.equals("Method")) {
	    result = "collectionship.samplingUnit.method";
        } else if (string.equals("ImaXCoordinate")) {
	    result = "collectionship.samplingUnit.imaXCoordiate";
        } else if (string.equals("ImaYCoordinate")) {
	    result = "collectionship.samplingUnit.imaYCoordiate";
        } else if (string.equals("Stratum")) {
	    result = "collectionship.samplingUnit.stratum";
        } else if (string.equals("TrapNumber")) {
	    result = "collectionship.samplingUnit.trapNumber";
        } else if (string.equals("SiteName")) {
	    result = "collectionship.samplingUnit.site.name";
        } else if (string.equals("ReviewResult")) {
	    result = "reviewResult";
        } else if (string.equals("ReviewComment")) {
	    result = "reviewComment";
        } 

	return result;
    }


    public Review getReview() {
	return new Review(this.reviewerName, this.reviewComment, this.reviewResult); 
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
        builder.append("observed date").append("='").append(observedAt).append("' ");
        builder.append("]");
      
        return builder.toString();
    }


}