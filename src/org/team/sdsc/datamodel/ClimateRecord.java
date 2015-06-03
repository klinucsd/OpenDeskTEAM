package org.team.sdsc.datamodel;

import java.text.*;
import java.util.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM climate record.
 *
 * @author Kai Lin
 */
@Entity 
@Table(name="CLIMATE_SAMPLES")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllClimateRecords", 
              query="from ClimateRecord as climateRecord order by climateRecord.id")
})
public class ClimateRecord {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="climate_seq_gen")
    @SequenceGenerator(name="climate_seq_gen", sequenceName="climatesampleserial")
    private Integer id;

    @OneToOne
    @JoinColumn(name="collection_sampling_unit_id")
    private Collectionship collectionship;    	
 
    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="collected_at",nullable=false,unique=false)
    private Date collectedAt;
   
    @NotNull
    @Column(name="collected_time",nullable=false,unique=false)
    private String collectedTime;
   
    @Edit(role="Data Collector")
    @Min(value=0)
    @Column(name="direct_solar_radiation")
    private Float directSolarRadiation;
    
    @Min(value=0)
    @Column(name="total_solar_radiation")
    private Float totalSolarRadiation;
    
    @Min(value=0)    
    @Column(name="diffuse_solar_radiation")
    private Float diffuseSolarRadiation;
    
    @Min(value=0)
    @Column(name="wind_speed")
    private Float windSpeed;
    
    @Range(min = 0, max=360)
    @Column(name="wind_direction")
    private Float windDirection;
    
    @Min(value=0)
    @Column(name="precipitation")
    private Float precipitation;
    
    @Min(value=-273, message="Dry temperature must be greater than or equal to -273")
    @Column(name="dry_temperature")
    private Float dryTemperature;
    
    @Min(value=0)
    @Column(name="relative_humidity")
    private Float relativeHumidity;
    
    @Min(value=-273)
    @Column(name="wet_temperature")
    private Float wetTemperature;
    
    @Min(value=0)
    @Column(name="soil_moisture")
    private Float soilMoisture;
    
    @Min(value=0)
    @Column(name="photosynthetic_photon_flux_density")
    private Float photosyntheticPhotonFluxDensity;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_event_at")
    private Date lastEventAt;
    
    @Column(name="last_event_by")
    private Integer lastEventBy;
             
    @Column(name="collected_by")  
    private String collectedBy;         
             
    @Column(name="comments")
    private String comments ;
    
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
  

    public void setCollectionship(Collectionship ship) {
	this.collectionship = ship;
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


    @Display(title="Observation Time", var="ObservationTime", index=3, width=100)    
    public String getCollectedTime() {
    	return collectedTime;
    }
    

    public void setCollectedTime(String time) {
        this.collectedTime = time;
    }


    @Display(title="Direct Solar Radiation", var="DirectSolarRadiation", index=4, width=100)
    public Float getDirectSolarRadiation() {
    	return directSolarRadiation;
    }

    
    public void setDirectSolarRadiation(Float value) {
        this.directSolarRadiation = value;
    }

    
    @Display(title="Total Solar Radiation", var="TotalSolarRadiation", index=5, width=100)
    public Float getTotalSolarRadiation() {
    	return totalSolarRadiation;
    }
    

    public void setTotalSolarRadiation(Float value) {
	this.totalSolarRadiation = value;
    }

    
    @Display(title="Diffuse Solar Radiation", var="DiffuseSolarRadiation", index=6, width=100)
    public Float getDiffuseSolarRadiation() {
    	return diffuseSolarRadiation;
    }
  
  
    public void setDiffuseSolarRadiation(Float value) {
	this.diffuseSolarRadiation = value;
    }


    @Display(title="Wind Speed", var="WindSpeed", index=7, width=100)
    public Float getWindSpeed() {
    	return windSpeed;
    }
    

    public void setWindSpeed(Float value) {
        this.windSpeed = value;
    }

    
    @Display(title="Wind Direction", var="WindDirection", index=8, width=100)
    public Float getWindDirection() {
    	return windDirection;
    }
    
  
    public void setWindDirection(Float value) {
        this.windDirection = value;
    }


    @Display(title="Precipitation", index=9, width=100)
    public Float getPrecipitation() {
    	return precipitation;
    }


    public void setPrecipitation(Float value) {
        this.precipitation = value;
    }


    @Display(title="Dry Temperature", var="DryTemperature", index=10, width=100)
    public Float getDryTemperature() {
    	return dryTemperature;
    }
  	

    public void setDryTemperature(Float value) {
        this.dryTemperature = value;
    }

       
    @Display(title="Wet Temperature", var="WetTemperature", index=11, width=100)
    public Float getWetTemperature() {
    	return wetTemperature;
    }
 
 
    @Display(title="Relative Humidity", var="RelativeHumidity", index=12, width=100)
    public Float getRelativeHumidity() {
    	return relativeHumidity;
    }
    

    public void setRelativeHumidity(Float value) {
        this.relativeHumidity = value;
    }

    
    @Display(title="Soil Moisture", var="SoilMoisture", index=13, width=100)
    public Float getSoilMoisture() {
    	return soilMoisture;
    }
  	
       
    @Display(title="Photosynthetic Photon Flux Density", var="PhotosyntheticPhotonFluxDensity", index=14, width=100)
    public Float getphotosyntheticPhotonFluxDensity() {
    	return photosyntheticPhotonFluxDensity;
    }    


    public void  setPhotosyntheticPhotonFluxDensity(Float value) {
        this.photosyntheticPhotonFluxDensity = value;
    }

    

    @Display(title="Observer", var="Observer", index=15, width=100)
    public String getCollectedBy() {
    	return collectedBy;
    }
  	    
           
    @Display(title="Comments", index=16, width=100)
    public String getComments() {
    	return comments;
    }
  	  

    public void setComments(String comments) {
	this.comments = comments;
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
	String result = "id";
	if (string.equals("Id")) {
	    result = "id";
	} else if (string.equals("ObservationDate")) {
	    result = "collectedAt";
        } else if (string.equals("ObservationTime")) {
	    result = "collectedTime";
        } else if (string.equals("DirectSolarRadiation")) {
	    result = "directSolarRadiation";
        } else if (string.equals("TotalSolarRadiation")) {
	    result = "totalSolarRadiation";
        } else if (string.equals("DiffuseSolarRadiation")) {
	    result = "diffuseSolarRadiation";
        } else if (string.equals("WindSpeed")) {
	    result = "windSpeed";
        } else if (string.equals("WindDirection")) {
	    result = "windDirection";
        } else if (string.equals("Precipitation")) {
	    result = "precipitation";
        } else if (string.equals("DryTemperature")) {
	    result = "dryTemperature";
        } else if (string.equals("WetTemperature")) {
	    result = "wetTemperature";
        } else if (string.equals("RelativeHumidity")) {
	    result = "relativeHumidity";
        } else if (string.equals("SoilMoisture")) {
	    result = "soilMoisture";
        } else if (string.equals("PhotosyntheticPhotonFluxDensity")) {
	    result = "photosyntheticPhotonFluxDensity";
        } else if (string.equals("Observer")) {
	    result = "collectedBy";
        } else if (string.equals("Comments")) {
	    result = "comments";
        } else if (string.equals("SamplingUnitName")) {
	    result = "collectionship.samplingUnit.name";
        } else if (string.equals("Latitude")) {
	    result = "collectionship.samplingUnit.lat";
        } else if (string.equals("Longitude")) {
	    result = "collectionship.samplingUnit.lat";
        } else if (string.equals("ReviewResult")) {
	    result = "reviewResult";
        } else if (string.equals("ReviewComment")) {
	    result = "reviewComment";
        } 
	return result;
    }



    public String setValue(String name, String value, String username) throws Exception {
	String result = null;
	if (name.equals("ObservationDate")) {
	    if (collectedAt != null) result = collectedAt.toString();
	    int index = value.indexOf("GMT");
	    if (index != -1) value = value.substring(0, index);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
	    collectedAt = formatter.parse(value);
        } else if (name.equals("ObservationTime")) {
	    result = collectedTime;
	    collectedTime = value;
        } else if (name.equals("DirectSolarRadiation")) {
	    if ( directSolarRadiation != null) result = directSolarRadiation.toString();
	    directSolarRadiation = Float.parseFloat(value);
        } else if (name.equals("TotalSolarRadiation")) {
	    if ( totalSolarRadiation != null) result = totalSolarRadiation.toString();
	    totalSolarRadiation = Float.parseFloat(value);
        } else if (name.equals("DiffuseSolarRadiation")) {
	    if ( diffuseSolarRadiation != null) result = diffuseSolarRadiation.toString();
	    diffuseSolarRadiation = Float.parseFloat(value);
        } else if (name.equals("WindSpeed")) {
	    if ( windSpeed != null) result = windSpeed.toString();
	    windSpeed = Float.parseFloat(value);
        } else if (name.equals("WindDirection")) {
	    if ( windDirection != null) result = windDirection.toString();
	    windDirection = Float.parseFloat(value);
        } else if (name.equals("Precipitation")) {
	    if ( precipitation != null) result = precipitation.toString();
	    precipitation = Float.parseFloat(value);
        } else if (name.equals("DryTemperature")) {
	    if ( dryTemperature != null) result = dryTemperature.toString();
	    dryTemperature = Float.parseFloat(value);
        } else if (name.equals("WetTemperature")) {
	    if ( wetTemperature != null) result = wetTemperature.toString();
	    wetTemperature = Float.parseFloat(value);
        } else if (name.equals("RelativeHumidity")) {
	    if ( relativeHumidity != null) result = relativeHumidity.toString();
	    relativeHumidity = Float.parseFloat(value);
        } else if (name.equals("SoilMoisture")) {
	    if ( soilMoisture != null) result = soilMoisture.toString();
	    soilMoisture = Float.parseFloat(value);
        } else if (name.equals("PhotosyntheticPhotonFluxDensity")) {
	    if ( photosyntheticPhotonFluxDensity != null) result = photosyntheticPhotonFluxDensity.toString();
	    photosyntheticPhotonFluxDensity = Float.parseFloat(value);
        } else if (name.equals("Observer")) {
	    result = collectedBy;
	    collectedBy = value;
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
        builder.append("dry-temperature").append("='").append(dryTemperature).append("' ");
        builder.append("]");
      
        return builder.toString();
    }
    
    
}