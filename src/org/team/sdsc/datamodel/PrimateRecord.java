package org.team.sdsc.datamodel;


import java.util.*;
import java.text.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM primate record.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="primate_taxonomies")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllPrimateRecords", 
              query="from PrimateRecord as record order by record.id")
})
public class PrimateRecord {

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

    @Column(name="census_number")
    private Integer censusNumber;
	
    @Column(name="census_type")
    private String censusType;
	
    @Column(name="family", nullable=true, unique=false)
    private String family;
	
    @Column(name="genus", nullable=true, unique=false)
    private String genus;
	
    @Column(name="species", nullable=true, unique=false)
    private String species;
	
    @Column(name="subspecies", nullable=true, unique=false)
    private String subspecies;

    //@Column(name="morphospecies", nullable=true, unique=false)
    @Transient
    private String morphospecies;

    @Column(name="individuals")
    private Integer individuals;
	
    @Column(name="adult_males")
    private Integer adultMales;

    @Column(name="adult_females")
    private Integer adultFemales;

    @Column(name="male_juveniles")
    private Integer maleJuveniles;
    
    @Column(name="female_juveniles")
    private Integer femaleJuveniles;

    @Column(name="non_total_adults")
    private Integer nonTotalAdults;

    @Column(name="non_total_juveniles")
    private Integer nonTotalJuveniles;

    @Column(name="infants")
    private Integer infants;

    @Column(name="non_identifiable")
    private Integer nonidentifiable;

    @Column(name="distance_to_primate")
    private Float distanceToPrimate;

    @Column(name="distance_angle")
    private Float distanceAngle;

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="observed_at",nullable=false,unique=false)
    private Date observedAt;	

    @Column(name="observed_by")
    private String observedBy;

    @Column(name="composite_stamp")
    private String compositeStamp;

    @Column(name="comments")
    private String comments ;

    @Column(name="ima_x_coordinate")
    private Integer imaXCoordinate;
    
    @Column(name="ima_y_coordinate")
    private Integer imaYCoordinate;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_event_at")
    private Date lastEventAt;
    
    @Column(name="last_event_by")
    private Integer lastEventBy;

    @Column(name="collection_event")
    private String event;	
	
    @Column(name="observed_time")
    private String observedTime;

    @Column(name="census_time")
    private String censusTime;

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


    @Display(title="Observation Date", var="ObservationDate", index=2, width=100)    
    public String getObservationDate() {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	return formatter.format(observedAt);
    }

    
  
    @Display(title="Observation Time", var="ObservationTime", index=3, width=100)    
    public String getObservedTime() {
    	return observedTime;
    }


    @Display(title="Sampling Event",  var="SamplingEvent",index=4, width=100)
    public String getEvent() {
    	return event;
    }
   	  
   	
    @Display(title="Observers Name", var="ObserversName", index=5, width=100)
    public String getObservedBy() {
    	return observedBy;
    }

    
    @Display(title="Family", index=6, width=100)  
    public String getFamily() {
    	return family;
    }
    
    
    @Display(title="Genus", index=7, width=100)  
    public String getGenus() {
    	return genus;
    }
    
    
    @Display(title="Species", index=8, width=100)  
    public String getSpecies() {
    	return species;
    } 
 
 
    @Display(title="Subspecies", index=9, width=100)  
    public String getSubspecies() {
    	return subspecies;
    } 
  
 
    @Display(title="Number of Individuals", var="NumberOfIndividuals", index=10, width=100)  
    public Integer getIndividuals() {
	return individuals;
    } 

    @Display(title="Adult Males", var="AdultMales", index=11, width=100)
    public Integer getAdultMales() {
	return adultMales;
    }

    @Display(title="Adult Females", var="AdultFemales", index=12, width=100)
    public Integer getAdultFemales() {
	return adultFemales;
    }

    @Display(title="Male Juveniles", var="MaleJuveniles", index=13, width=100)
    public Integer getMaleJuveniles() {
	return maleJuveniles;
    }

    @Display(title="Female Juveniles", var="FemaleJuveniles", index=14, width=100)
    public Integer getFemaleJuveniles() {
	return femaleJuveniles;
    }

    @Display(title="Infants", index=15, width=100)
    public Integer getInfants() {
	return infants;
    }

    @Display(title="Nonidentifiable", index=16, width=100)
    public Integer getNonidentifiable() {
	return nonidentifiable;
    }


    @Display(title="Distance to Primate", var="DistanceToPrimate", index=17, width=100)  
    public Float getDistanceToPrimate() {
	return distanceToPrimate;
    }
 	
 	
    @Display(title="Distance to Angle", var="DistanceToAngle", index=18, width=100)  
    public Float getDistanceAngle() {
	return distanceAngle;
    }
 	

    @Display(title="Comments", index=19, width=100)
    public String getComments() {
    	return comments;
    }
    
    
    @Display(title="Block X Coordinate", var="BlockXCoordinate", index=20, width=100) 
    public Integer getImaXCoordinate() {
	return imaXCoordinate;
    }
 	
 	
    @Display(title="Block Y Coordinate", var="BlockYCoordinate", index=21, width=100)  
    public Integer getImaYCoordinate() {
	return imaYCoordinate;
    }


    public SamplingUnit getSamplingUnit() {
    	return collectionship.getSamplingUnit();
    }



    static public String getSortPath(String string) {
	String result = "id";
	if (string.equals("Id")) {
	    result = "id";
	} else if (string.equals("ObservationDate")) {
	    result = "observedAt";
        } else if (string.equals("ObservationTime")) {
	    result = "observedTime";
        } else if (string.equals("SamplingEvent")) {
	    result = "event";
        } else if (string.equals("ObserversName")) {
	    result = "observedBy";
        } else if (string.equals("Family")) {
	    result = "family";
        } else if (string.equals("Genus")) {
	    result = "genus";
        } else if (string.equals("Species")) {
	    result = "species";
        } else if (string.equals("Subspecies")) {
	    result = "subspecies";
        } else if (string.equals("NumberOfIndividuals")) {
	    result = "individuals";
        } else if (string.equals("AdultMales")) {
	    result = "adultMales";
        } else if (string.equals("AdultFemales")) {
	    result = "adultFemales";
        } else if (string.equals("MaleJuveniles")) {
	    result = "maleJuveniles";
        } else if (string.equals("FemaleJuveniles")) {
	    result = "femaleJuveniles";
        } else if (string.equals("Infants")) {
	    result = "infants";
        } else if (string.equals("Nonidentifiable")) {
	    result = "nonidentifiable";
        } else if (string.equals("DistanceToPrimate")) {
	    result = "distanceToPrimate";
        } else if (string.equals("DistanceToAngle")) {
	    result = "distanceAngle";
        } else if (string.equals("Comments")) {
	    result = "comments";
        } else if (string.equals("BlockXCoordinate")) {
	    result = "collectionship.samplingUnit.imaXCoordiate";
        } else if (string.equals("BlockYCoordinate")) {
	    result = "collectionship.samplingUnit.imaYCoordiate";
        } else if (string.equals("SamplingUnitName")) {
	    result = "collectionship.samplingUnit.name";
        } else if (string.equals("Latitude")) {
	    result = "collectionship.samplingUnit.lat";
        } else if (string.equals("Longitude")) {
	    result = "collectionship.samplingUnit.lat";
        } else if (string.equals("Method")) {
	    result = "collectionship.samplingUnit.method";
        } else if (string.equals("SiteName")) {
	    result = "collectionship.samplingUnit.site.name";
        } else if (string.equals("BlockName")) {
	    result = "collectionship.samplingUnit.block.name";
        } else if (string.equals("ProtocolVersion")) {
	    result = "collectionship.samplingUnit.protocolFamily";
        } else if (string.equals("ReviewResult")) {
	    result = "reviewResult";
        } else if (string.equals("ReviewComment")) {
	    result = "reviewComment";
        } 
	return result;
    }

    


    public String setValue(String name, String value, String username) throws Exception {
	String result = null;
	if (name.equals("Family")) {
	    result = family;
	    family = value;
	} else if (name.equals("Genus")) {
	    result = genus;
	    genus = value;
	} else if (name.equals("Species")) {
	    result = species;
	    species = value;
	} else if (name.equals("Subspecies")) {
	    result = subspecies;
	    subspecies = value;
        } else if (name.equals("NumberOfIndividuals")) {
	    if ( individuals != null) result = individuals.toString();
	    individuals = new Integer(value);
        } else if (name.equals("AdultMales")) {
	    if ( adultMales != null) result = adultMales.toString();
	    adultMales = new Integer(value);
        } else if (name.equals("AdultFemales")) {
	    if ( adultFemales != null) result = adultFemales.toString();
	    adultFemales = new Integer(value);
        } else if (name.equals("MaleJuveniles")) {
	    if ( maleJuveniles != null) result = maleJuveniles.toString();
	    maleJuveniles = new Integer(value);
        } else if (name.equals("FemaleJuveniles")) {
	    if ( femaleJuveniles != null) result = femaleJuveniles.toString();
	    femaleJuveniles = new Integer(value);
        } else if (name.equals("Infants")) {
	    if ( infants != null) result = infants.toString();
	    infants = new Integer(value);
        } else if (name.equals("Nonidentifiable")) {
	    if ( nonidentifiable != null) result = nonidentifiable.toString();
	    nonidentifiable = new Integer(value);
        } else if (name.equals("Comments")) {
	    result = comments;
	    comments = value;
	} else if (name.equals("ObservationDate")) {
	    if (observedAt != null) result = observedAt.toString();
	    int index = value.indexOf("GMT");
	    if (index != -1) value = value.substring(0, index);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    observedAt = formatter.parse(value);
        } else if (name.equals("ObservationTime")) {
	    result = observedTime;
 	    observedTime = value;
        } else if (name.equals("ObserversName")) {
	    result = observedBy;
	    observedBy = value;    
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