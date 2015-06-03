package org.team.sdsc.datamodel;

import java.text.*;
import java.util.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM avian record.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="bird_taxonomies")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllAvianRecords", 
              query="from AvianRecord as avianRecord order by avianRecord.id")
})
public class AvianRecord {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name="collection_sampling_unit_id")
    private Collectionship collectionship;    	

    @Column(name="collection_code")
    private Integer collectionCode;
   
    @Column(name="family", nullable=true, unique=false)
    private String family;
	
    @Column(name="genus", nullable=true, unique=false)
    private String genus;
	
    @Column(name="species", nullable=true, unique=false)
    private String species;

    @Column(name="start_time",nullable=true,unique=false)
    private String startTime;

    @Column(name="end_time",nullable=true,unique=false)
    private String endTime;

    @Column(name="distance")
    private Integer distance;	

    @Column(name="visual")
    private String visual;

    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name="observed_at",nullable=false,unique=false)
    private Date observedAt;

    @Column(name="comments")
    private String comments ;

    @Column(name="valid_stamp")
    private Integer validStamp;	

    @Column(name="valid_comments")
    private String validComments;	

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_event_at")
    private Date lastEventAt;
    
    @Column(name="last_event_by")
    private Integer lastEventBy;

    @Column(name="collection_event")
    private String event;	

    @Column(name="analog_id")
    private String analogId;	
	
    @Column(name="digital_id")
    private String digitalId;	

    @Column(name="first_name")  
    private String firstName;      
    
    @Column(name="last_name")  
    private String lastName;      
 
    @Column(name="auditory")  
    private String auditory;   
     
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

  
    @Display(title="Observation Start Time", var="ObservationStartTime", index=3, width=100)    
    public String getStartTime() {
    	return startTime;
    }
    
    
    @Display(title="Observation End Time", var="ObservationEndTime", index=4, width=100)    
    public String getEndTime() {
    	return endTime;
    }
    
    
    @Display(title="Family", index=5, width=100)  
    public String getFamily() {
    	return family;
    }
    
    
    @Display(title="Genus", index=6, width=100)  
    public String getGenus() {
    	return genus;
    }
    
    
    @Display(title="Species", index=7, width=100)  
    public String getSpecies() {
    	return species;
    } 
 
 	
    @Display(title="Auditory", index=8, width=100)  
    public String getAuditory() {
	return auditory;
    }
 	
 	
    @Display(title="Visual", index=9, width=100)  
    public String getVisual() {
	return visual;
    }
 	
 	
    @Display(title="Distance", index=10, width=100)  
    public Integer getDistance() {
	return distance;
    }
 	
 	
    @Display(title="Analog Sound Recording ID", var="AnalogSoundRecordingID", index=11, width=100)  
    public String getAnalogId() {
	return analogId;
    }
 	
 	 	
    @Display(title="Digital Sound Recording ID", var="DigitalSoundRecordingID", index=12, width=100)  
    public String getDigitalId() {
	return digitalId;
    }
 	
 	
    @Display(title="Names of Observers", var="NamesOfObservers", index=13, width=100)  
    public String getObserverName() {
	if (firstName == null) {
	    if (lastName == null) {
		return "";
	    } else {
		return lastName;
	    }
	} else if (lastName == null) {
	    return firstName;
	} else {
	    return firstName+" "+lastName;
	}
    } 	
 	
    @Display(title="Collection Code", var="CollectionCode", index=14, width=100) 
    public Integer getCollectionCode() {
    	return collectionCode;
    } 	
 	

    @Display(title="Comments", index=15, width=100)
    public String getComments() {
    	return comments;
    }
    
    
    @Display(title="Sampling Period", var="SamplingPeriod", index=16, width=100)
    public String getEvent() {
    	return event;
    }
   	  
  
    public SamplingUnit getSamplingUnit() {
    	return collectionship.getSamplingUnit();
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
		if (observedAt.after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (observedAt.after(leader.getStartDate()) &&
		       observedAt.before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  	
	return person;
    }
    


    public Review getReview() {
	return new Review(this.reviewerName, this.reviewComment, this.reviewResult); 
    }



    static public String getSortPath(String string) {
	String result = "id";
	if (string.equals("Id")) {
	    result = "id";
	} else if (string.equals("ObservationDate")) {
	    result = "observedAt";
        } else if (string.equals("ObservationStartTime")) {
	    result = "startTime";
        } else if (string.equals("ObservationEndTime")) {
	    result = "endTime";
        } else if (string.equals("Family")) {
	    result = "family";
        } else if (string.equals("Genus")) {
	    result = "genus";
        } else if (string.equals("Species")) {
	    result = "species";
        } else if (string.equals("Auditory")) {
	    result = "auditory";
        } else if (string.equals("Visual")) {
	    result = "visual";
        } else if (string.equals("Distance")) {
	    result = "distance";
        } else if (string.equals("AnalogSoundRecordingID")) {
	    result = "analogId";
        } else if (string.equals("DigitalSoundRecordingID")) {
	    result = "digitalId";
        } else if (string.equals("NamesOfObservers")) {
	    result = "id";
        } else if (string.equals("CollectionCode")) {
	    result = "collectionCode";
        } else if (string.equals("Comments")) {
	    result = "comments";
        } else if (string.equals("SamplingPeriod")) {
	    result = "event";
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
        } else if (name.equals("Auditory")) {
	    result = auditory;
	    auditory = value;
        } else if (name.equals("Visual")) {
	    result = visual;
	    visual = value;
        } else if (name.equals("Distance")) {
	    if (distance != null) result = distance.toString();
	    distance = Integer.parseInt(value);
        } else if (name.equals("AnalogSoundRecordingID")) {
	    result = analogId;
	    analogId = value;
        } else if (name.equals("DigitalSoundRecordingID")) {
	    result = digitalId;
	    digitalId = value;
        } else if (name.equals("NamesOfObservers")) {
	  
        } else if (name.equals("CollectionCode")) {
	    if (collectionCode != null) result = collectionCode.toString();
	    collectionCode = Integer.parseInt(value);
        } else if (name.equals("Comments")) {
	    result = comments;
	    comments = value;
	} else if (name.equals("ObservationDate")) {
	    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
	    if (observedAt != null) result = format.format(observedAt);
	    int index = value.indexOf("GMT");
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    if (index != -1) value = value.substring(0, index);
	    observedAt = formatter.parse(value);
        } else if (name.equals("ObservationStartTime")) {
	    result = startTime;
 	    startTime = value;
        } else if (name.equals("ObservationEndTime")) {
	    result = endTime;
	    endTime = value;
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
        builder.append("start time").append("='").append(startTime).append("' ");
        builder.append("end time").append("='").append(endTime).append("' ");
        builder.append("]");
      
        return builder.toString();
    }
        
    
}