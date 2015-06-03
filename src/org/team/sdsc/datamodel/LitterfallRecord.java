package org.team.sdsc.datamodel;


import java.util.*;
import java.text.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM litterfall record.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="litterfall_collection_details")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllLitterfallRecords", 
              query="from LitterfallRecord as record order by record.id")
})
public class LitterfallRecord {

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

    @Column(name="ima_id")
    private Integer imaId;

    @Column(name="collected_by")
    private String collectedBy;

    @Column(name="ground_exclusion")
    private String groundExclusion;
	
    @Column(name="large_leaves_weighed_at")
    private Date largeLeavesWeighedAt;	

    @Column(name="large_leaves_weight")
    private Float largeLeavesWeight;

    @Column(name="fine_wood_weighed_at")
    private Date fineWoodWeighedAt;	

    @Column(name="fine_wood_weight")
    private Float fineWoodWeight;

    @Column(name="basket_exclusion")
    private String basketExclusion;
	
    @Column(name="small_leaves_weighed_at")
    private Date smallLeavesWeighedAt;	

    @Column(name="small_leaves_weight")
    private Float smallLeavesWeight;

    @Column(name="woody_litter_weighed_at")
    private Date woodyLitterWeighedAt;	

    @Column(name="woody_litter_weight")
    private Float woodyLitterWeight;

    @Column(name="flowers_weighed_at")
    private Date flowersWeighedAt;	

    @Column(name="flowers_weight")
    private Float flowersWeight;

    @Column(name="fruits_weighed_at")
    private Date fruitsWeighedAt;	

    @Column(name="fruits_weight")
    private Float fruitsWeight;

    @Column(name="unidentified_material_weighed_at")
    private Date unidentifiedMaterialWeighedAt;	

    @Column(name="unidentified_material_weight")
    private Float unidentifiedMaterialWeight;

    //@Column(name="valid_record")
    @Transient
    private Integer validRecord;	

    @Column(name="valid_comments")
    private String validComments;	

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_event_at")
    private Date lastEventAt;
    
    @Column(name="last_event_by")
    private Integer lastEventBy;

    @Column(name="collection_event")
    private String event;	

    @Column(name="comments")
    private String comments ;

    @Column(name="number_of_ground_traps")
    private Integer numberOfGroundTraps;
	
    @Column(name="number_of_standing_traps")
    private Integer numberOfStandingTraps;
	
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name="collected_start",nullable=false,unique=false)
    private Date collectedStart;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name="collected_end",nullable=false,unique=false)
    private Date collectedEnd;

    @Column(name="reviewer_name")
    private String reviewerName;

    @Column(name="review_comment")
    private String reviewComment;

    @Column(name="review_result")
    private String reviewResult;    


    public SamplingUnit getSamplingUnit() {
    	return collectionship.getSamplingUnit();
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
    

    public Date getCollectedStart() {
	return collectedStart;
    }


    public Date getCollectedEnd() {
	return collectedEnd;
    }


    @Display(title="Date Sampling Started", var="DateSamplingStarted", index=2, width=150)
    public String getDateSamplingStarted() {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	return formatter.format(collectedStart);
    }


    @Display(title="Date Sampling Ended", var="DateSamplingEnded", index=3, width=150)
    public String getDateSamplingEnded() {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	return formatter.format(collectedEnd);
    }


    @Display(title="Sampling Period", var="SamplingPeriod", index=4, width=100)
    public String getEvent() {
    	return event;
    }
    
    @Display(title="Number of Standing Traps", var="NumberOfStandingTraps", index=5, width=100)
    public Integer getNumberOfStandingTraps() {
        return numberOfStandingTraps;
    }
    
    
    @Display(title="Small Leaves Weight", var="SmallLeavesWeight", index=6, width=100)
    public Float getSmallLeavesWeight() {
        return smallLeavesWeight;
    }
        
    
    @Display(title="Woody Litter less than 1cm (g)", var="WoodyLitterLessThan1cm", index=7, width=100)
    public Float getFineWoodWeight() {
        return fineWoodWeight;
    }
        
    
    @Display(title="Flowers (g)", var="Flowers", index=8, width=100)
    public Float getFlowersWeight() {
        return flowersWeight;
    }
        
    
    @Display(title="Fruits Seeds (g)", var="FruitsSeeds", index=9, width=100)
    public Float getFruitsWeight() {
        return fruitsWeight;
    }
        
    
    @Display(title="Unidentified Reproductive Material (g)", var="UnidentifiedReproductiveMaterial", index=10, width=100)
    public Float getUnidentifiedMaterialWeight() {
        return unidentifiedMaterialWeight;
    }
        
    
    @Display(title="Number of Ground Traps", var="NumberOfGroundTraps", index=11, width=100)
    public Integer getNumberOfGroundTraps() {
    	return numberOfGroundTraps;    
    }
        
    
    @Display(title="Large Leaves (g)", var="LargeLeaves", index=12, width=100)
    public Float getLargeLeavesWeight() {
    	return largeLeavesWeight;
    }
        
    /*
    @Display(title="Fine Wood 1-10cm (g)", index=13, width=100)
    public Float getFineWoodWeight () {
    	return fineWoodWeight;
    }
    */
 
    @Display(title="Comments", index=13, width=100)
    public String getComments() {
    	return comments;
    }
    

    public String setValue(String name, String value, String username) throws Exception {
	String result = null;
	if (name.equals("DateSamplingStarted")) {
	    if ( collectedStart != null) result = collectedStart.toString();
	    int index = value.indexOf("GMT");
	    if (index != -1) value = value.substring(0, index);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    collectedStart = formatter.parse(value);	    
	} else if (name.equals("DateSamplingEnded")) {
	    if ( collectedEnd != null) result = collectedEnd.toString();
	    int index = value.indexOf("GMT");
	    if (index != -1) value = value.substring(0, index);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    collectedEnd = formatter.parse(value);	    	   
	} else if (name.equals("SamplingPeriod")) {
	    result = event;
	    event = value;
        } else if (name.equals("NumberOfStandingTraps")) {
	    if ( numberOfStandingTraps != null) result = numberOfStandingTraps.toString();
	    numberOfStandingTraps = new Integer(value);
        } else if (name.equals("SmallLeavesWeight")) {
	    if ( smallLeavesWeight != null) result = smallLeavesWeight.toString();
	    smallLeavesWeight = new Float(value);
        } else if (name.equals("WoodyLitterLessThan1cm")) {
	    if ( fineWoodWeight != null) result = fineWoodWeight.toString();
	    fineWoodWeight = new Float(value);
        } else if (name.equals("Flowers")) {
	    if ( flowersWeight != null) result = flowersWeight.toString();
	    flowersWeight = new Float(value);
        } else if (name.equals("FruitsSeeds")) {
	    if ( fruitsWeight != null) result = fruitsWeight .toString();
	    fruitsWeight = new Float(value);
        } else if (name.equals("UnidentifiedReproductiveMaterial")) {
	    if ( unidentifiedMaterialWeight != null) result = unidentifiedMaterialWeight.toString();
	    unidentifiedMaterialWeight = new Float(value);
        } else if (name.equals("NumberOfGroundTraps")) {
	    if ( numberOfGroundTraps != null) result = numberOfGroundTraps.toString();
	    numberOfGroundTraps = new Integer(value);
        } else if (name.equals("LargeLeaves")) {
	    if ( largeLeavesWeight != null) result = largeLeavesWeight.toString();
	    largeLeavesWeight = new Float(value);
        } else if (name.equals("Comments")) {
	    result = comments;
	    comments = value;
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
        builder.append("collected start").append("='").append(collectedStart).append("' ");
        builder.append("]");
      
        return builder.toString();
    }
        
    
}