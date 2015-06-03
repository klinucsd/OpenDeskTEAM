package org.team.sdsc.datamodel;

import java.util.*;
import java.text.*;
import org.hibernate.validator.*;
import javax.persistence.*;
import org.team.sdsc.datamodel.annotation.*;
import javax.xml.bind.annotation.*;


/**
 * Represents a TEAM block.
 *
 * @author Kai Lin
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@Entity
@Table(name="IMAS")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllBlocks", 
              query="from Block as block order by block.name")         
})
public class Block {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="ima_seq_gen")
    @SequenceGenerator(name="ima_seq_gen", sequenceName="imas_sequence")
    private Integer id;
 
    @Range(min=1, max=12)
    @Column(name="IMA_NAME",nullable=false,unique=false)
    private int index;
    
    @Column(name="IMA_DESCRIPTION",nullable=false,unique=false)
    private String name;
    
    @Column(name="AREA_TYPE",nullable=false,unique=false)
    private String type;
    
    @XmlTransient   
    @Temporal(TemporalType.DATE) 
    @Column(name="LAST_EVENT_AT",nullable=true,unique=false)
    private Date lastEventAt;
    
    @XmlTransient   
    @Column(name="LAST_EVENT_BY",nullable=true,unique=false)
    private Integer lastEventBy;
    
    @XmlTransient   
    @Column(name="METHOD_TEAM",nullable=true,unique=false)
    private String method;
    
    @XmlTransient   
    @Column(name="CORRECTION",nullable=true,unique=false)
    private String collection;
    
    @XmlTransient   
    @Column(name="COLLECTOR",nullable=true,unique=false)
    private String collector;
    
    @XmlTransient   
    @Column(name="PROJECTION_INFORMATION",nullable=true,unique=false)
    private String projection;
    
    @XmlTransient   
    @Column(name="UTM_ZONE",nullable=true,unique=false)
    private String utmZone;
    
    @XmlTransient   
    @Temporal(TemporalType.DATE) 
    @Column(name="INSTALLATION_DATE_FROM",nullable=true,unique=false)
    private Date installFrom;
    
    @Temporal(TemporalType.DATE) 
    @Column(name="INSTALLATION_DATE_TO",nullable=true,unique=false)
    private Date installTo;

    @Temporal(TemporalType.DATE) 
    @Column(name="COLLECTION_DATE",nullable=true,unique=false)
    private Date collectionDate;
    
    @XmlTransient   
    @Column(name="COMMENTS",nullable=true,unique=false)
    private String comments;
    
    @XmlTransient   
    @Column(name="BEARING_0_100",nullable=true,unique=false)
    private Float bearing0100;

    @XmlTransient   
    @Column(name="BEARING_100_0",nullable=true,unique=false)
    private Float bearing1000;

    @XmlTransient   
    @Column(name="PROPOSED_PT1_LAT",nullable=true,unique=false)
    private Double proposedPt1Lat;

    @XmlTransient   
    @Column(name="PROPOSED_PT1_LONG",nullable=true,unique=false)
    private Double proposedPt1Lon;
    
    //@XmlTransient   
    @Column(name="PT1_LAT",nullable=true,unique=false)
    private Double latPt1;
    
    //@XmlTransient   
    @Column(name="PT1_LONG",nullable=true,unique=false)
    private Double lonPt1;
    
    @XmlTransient   
    @Column(name="PT1_BP",nullable=true,unique=false)
    private String bpPt1;
    
    @XmlTransient   
    @Column(name="PT2_LAT",nullable=true,unique=false)
    private Double latPt2;
    
    @XmlTransient   
    @Column(name="PT2_LONG",nullable=true,unique=false)
    private Double lonPt2;
    
    @XmlTransient   
    @Column(name="PT2_BP",nullable=true,unique=false)
    private String bpPt2;
    
    @XmlTransient   
    @Column(name="PT3_LAT",nullable=true,unique=false)    
    private Double latPt3;
    
    @XmlTransient   
    @Column(name="PT3_LONG",nullable=true,unique=false)
    private Double lonPt3;
    
    @XmlTransient   
    @Column(name="PT3_BP",nullable=true,unique=false)
    private String bpPt3;
    
    @XmlTransient   
    @Column(name="PT4_LAT",nullable=true,unique=false)    
    private Double latPt4;
    
    @XmlTransient   
    @Column(name="PT4_LONG",nullable=true,unique=false)
    private Double lonPt4;
    
    @XmlTransient   
    @Column(name="PT4_BP",nullable=true,unique=false)
    private String bpPt4;
   
    /*
    @Column(name="SITE_ID",nullable=false,unique=false)
    private String siteId;     
    */
 
    @XmlTransient   
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SITE_Id")
    private Site site;
  
    /**
     * All sampling units at this block which are found in the database.
     */
    /*
    @OneToMany(mappedBy="block", fetch = FetchType.LAZY)
    @OrderBy("name")
    @JoinColumn(name="IMA_Id")    
    private Set<SamplingUnit> samplingUnits;
    */
  
    @XmlTransient     
    @Transient   
    private List<SamplingUnit> samplingUnits;  

    @XmlTransient   
    @Transient
    private Set<Protocol> protocols;
  
    @XmlTransient   
    @Temporal(TemporalType.DATE)
    @Column(name="gps_reading_date")
    private Date gpsReadingDate;

    @XmlTransient   
    @Column(name="gps_reading_first_name")
    private String gpsReadingFirstName;

    @XmlTransient   
    @Column(name="gps_reading_last_name")
    private String gpsReadingLastName;

    @XmlTransient   
    @Column(name="gps_number_of_reading")
    private Integer gpsNumberOfReading;

    @XmlTransient   
    @Column(name="gps_notes")
    private String gpsNotes;

    @XmlTransient   
    @Column(name="gps_make_and_model")
    private String gpsModel;

  
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public Block() {}
   
  
    /**
     * Get the name by which this block is known.
     *
     * @return the name of the block.
     */
    @Display(title="1ha Plot Number", var="BlockName", index=1, width=100)
    public String getName() {
        return name;
    }

    /**
     * Set the name by which this block is known.
     *
     * @param name the name of the block.
     */
    public void setName(String name) {
        this.name = name;
    }
  

    public int getIndex() {
	return this.index;
    }

 
    /*
 	public Set<SamplingUnit> getSamplingUnits() {
 		return samplingUnits;
 	}
 
 
 	public Set<SamplingUnit> getSamplingUnits(Protocol protocol) {
 		Set<SamplingUnit> units = new HashSet<SamplingUnit>();
 		for (Iterator i=samplingUnits.iterator(); i.hasNext(); ) {
 			SamplingUnit unit = (SamplingUnit)i.next();
 			if (unit.getProtocolFamily().getName().equals(protocol.getName())) {
 				units.add(unit);
 			}
 		}	
 		return units;
 	} 
    */
 

     /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this block
     *         is persisted.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this block
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }

 
    public List<SamplingUnit> getSamplingUnits() {
	return samplingUnits;
    }
	
	
    public void setSamplingUnits(List<SamplingUnit> samplingUnits) {
	this.samplingUnits = samplingUnits;
    }


    public Double getPoint1Latitude() {
	return latPt1;
    }


    public Double getPoint1Longitude() {
	return lonPt1;
    }


    public String getPoint1BlockPosition() {
	return bpPt1;
    }


    public void setPoint1Latitude(Double lat) {
	this.latPt1 = lat;
    }



    public void setPoint1Longitude(Double lon) {
	this.lonPt1 = lon;
    }



    public Double getPoint2Latitude() {
	return latPt2;
    }


    public Double getPoint2Longitude() {
	return lonPt2;
    }


    public String getPoint2BlockPosition() {
	return bpPt2;
    }


    public Double getPoint3Latitude() {
	return latPt3;
    }


    public Double getPoint3Longitude() {
	return lonPt3;
    }


    public String getPoint3BlockPosition() {
	return bpPt3;
    }


    public Double getPoint4Latitude() {
	return latPt4;
    }


    public Double getPoint4Longitude() {
	return lonPt4;
    }


    public String getPoint4BlockPosition() {
	return bpPt4;
    }


    public void setType(String type) {
	this.type = type;
    }


    public void setIndex(int index) {
	this.index = index;
    }


    public String getMethod() {
	return method;
    }


    public Block clone() {
	Block block = new Block();
	block.id = this.id;
	block.index = this.index;
	block.name = this.name;
	block.type = this.type;
	block.lastEventAt = this.lastEventAt;
	block.lastEventBy = this.lastEventBy;
	block.method = this.method;
	block.collection = this.collection;
	block.collector = this.collector;
	block.projection = this.projection;
	block.utmZone = this.utmZone;
	block.installFrom = this.installFrom;
	block.installTo = this.installTo;
	block.comments = this.comments;
	block.bearing0100 = this.bearing0100;
	block.latPt1 = this.latPt1;
	block.lonPt1 = this.lonPt1;
	block.bpPt1 = this.bpPt1;
	block.latPt2 = this.latPt2;
	block.lonPt2 = this.lonPt2;
	block.bpPt2 = this.bpPt2;
	block.latPt3 = this.latPt3;
	block.lonPt3 = this.lonPt3;
	block.bpPt3 = this.bpPt3;
	block.latPt4 = this.latPt4;
	block.lonPt4 = this.lonPt4;
	block.bpPt4 = this.bpPt4;
	//block.siteId = this.siteId;     
	block.site = this.site;
	block.samplingUnits = new ArrayList<SamplingUnit>();
	block.gpsReadingDate = gpsReadingDate;
	block.gpsReadingFirstName = gpsReadingFirstName;
	block.gpsReadingLastName = gpsReadingLastName;
	block.gpsNumberOfReading = gpsNumberOfReading;
	block.gpsNotes = gpsNotes;
	return block;
    }


    /**
     *  List all protocols installed in this site
     *
     */    
    public Set<Protocol> getProtocols() {
    	return protocols;
    }


    public void setProtocols(Set<Protocol> protocols) {
	this.protocols = protocols;
    }


    public void setSite(Site site) {
	this.site = site;
    }


    public void setProposedLatitude(Double lat) {
	this.proposedPt1Lat = lat;
    }


    public void setProposedLongitude(Double lon) {
	this.proposedPt1Lon = lon;
    }

  
    public Double getProposedLatitude() {
        return this.proposedPt1Lat;
    }


    public Double getProposedLongitude() {
        return this.proposedPt1Lon;
    }


    public Date getGpsReadingDate() {
	return gpsReadingDate;
    }


    public void setGpsReadingDate(Date date) {
	this.gpsReadingDate = date;
    }

    
    public String getGpsReadingFirstName() {
	return gpsReadingFirstName;
    }

    
    public void setGpsReadingFirstName(String firstName) {
        gpsReadingFirstName = firstName;
    }

    
    public String getGpsReadingLastName() {
	return gpsReadingLastName;
    }

    
    public void setGpsReadingLastName(String lastName) {
        gpsReadingLastName = lastName;
    }

    
    public Integer getGpsNumberOfReading() {
	return gpsNumberOfReading;
    }


    public void setGpsNumberOfReading(Integer aInt) {
	gpsNumberOfReading = aInt;
    }


    public String getGpsNotes() {
	return gpsNotes;
    }


    public void setGpsNotes(String notes) {
	this.gpsNotes = notes;
    }


    public String getGpsModel() {
	return gpsModel;
    }


    public void setGpsModel(String model) {
	this.gpsModel = model;
    }


    public Float getBearing0100() {
	return this.bearing0100;
    }


    public Float getBearing1000() {
	return this.bearing1000;
    }


    public Date getInstallFrom() {
	return installFrom;
    }


    public Date getInstallTo() {
	return installTo;
    }
    

    public String getType() {
	return type;
    }


    public Date getCollectionDate() {
	return collectionDate;
    }


    static public String getSortPath(String string) {
	String result = "id";
	if (string.equals("Id")) {
	    result = "id";
	} else if (string.equals("index")) {
	    result = "index";
	} else if (string.equals("name")) {
	    result = "name";
	} else if (string.equals("proposed_point1_latitude")) {
	    result = "proposedPt1Lat";
	} else if (string.equals("proposed_point1_longitude")) {
	    result = "proposedPt1Lon";
	} else if (string.equals("point1_latitude")) {
	    result = "latPt1";
	} else if (string.equals("point1_longitude")) {
	    result = "lonPt1";
	} else if (string.equals("gps_reading_date")) {
	    result = "gpsReadingDate";
	} else if (string.equals("gps_number_of_reading")) {
	    result = "gpsNumberOfReading";
	} else if (string.equals("gps_notes")) {
	    result = "gpsNotes";
        } else if (string.equals("gps_make_and_model")) {
	    result = "gpsModel";
        } else if (string.equals("gps_reading_person")) {
	    result = "gpsReadingLastName";
        } else if (string.equals("bearing_0_100")) {
	    result = "bearing0100";
        } else if (string.equals("bearing_100_0")) {
	    result = "bearing1000";
        } else if (string.equals("install_from")) {
	    result = "installFrom";
        } else if (string.equals("install_to")) {
	    result = "installTo";
	}
	return result;
    }


    public void setValue(String name, String value) throws Exception {
	if (name.equals("point1_latitude")) {
	    if (value == null) {
		latPt1 = null;
	    } else {
		latPt1 = new Double(value);
	    }
	} else if (name.equals("point1_longitude")) {
	    if (value == null) {
		lonPt1 = null;
	    } else {
		lonPt1 = new Double(value);
	    }
	} else if (name.equals("proposed_point1_latitude")) {
	    if (value == null) {
		proposedPt1Lat = null;
	    } else {
		proposedPt1Lat = new Double(value);
	    }
	} else if (name.equals("proposed_point1_longitude")) {
	    if (value == null) {
		proposedPt1Lon = null;
	    } else {
		proposedPt1Lon = new Double(value);
	    }
	} else if (name.equals("method_team")) {
	    method = value;
	} else if (name.equals("gps_reading_date")) {
	    if (value == null) {
		gpsReadingDate = null;
	    } else {
		int index = value.indexOf("GMT");
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
		if (index != -1) value = value.substring(0, index);
		gpsReadingDate = formatter.parse(value);
	    }
	} else if (name.equals("gps_number_of_reading")) {
	    if (value == null) {
		gpsNumberOfReading = null;
	    } else {
		gpsNumberOfReading = new Integer(value);
	    }
	} else if (name.equals("gps_notes")) {
	    gpsNotes = value;
	} else if (name.equals("gps_reading_person")) {
	    if (value == null) {
		gpsReadingFirstName = null;
		gpsReadingLastName = null;
	    } else {
		int p = value.indexOf(",");
		if (p != -1) {
		    gpsReadingFirstName = value.substring(p+1);
		    gpsReadingLastName = value.substring(0, p);
		}
	    }
	} else if (name.equals("gps_make_and_model")) {
            gpsModel = value;
        } else if (name.equals("bearing_0_100")) {
	    if (value == null) {
		bearing0100 = null;
	    } else {
		bearing0100 = new Float(value);
	    }
        } else if (name.equals("bearing_100_0")) {
	    if (value == null) {
		bearing1000 = null;
	    } else {
		bearing1000 = new Float(value);
	    }
        } else if (name.equals("install_from")) {
	    if (value == null) {
		installFrom = null;
	    } else {
		int index = value.indexOf("GMT");
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
		if (index != -1) value = value.substring(0, index);
		installFrom = formatter.parse(value);
	    }
        } else if (name.equals("install_to")) {
	    if (value == null) {
		installTo = null;
	    } else {
		int index = value.indexOf("GMT");
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
		if (index != -1) value = value.substring(0, index);
		installTo = formatter.parse(value);
	    }
	}
    }

 
    /**
     * Produce a human-readable representation of the block.
     *
     * @return a textual description of the block.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("name").append("='").append(name).append("' ");
        builder.append("index").append("='").append(index).append("' ");
        builder.append("type").append("='").append(type).append("' ");
        builder.append("site").append("='").append(site.getName()).append("' ");
        builder.append("last-event-at").append("='").append(lastEventAt).append("' ");
        builder.append("last-event-by").append("='").append(lastEventBy).append("' ");
        
        /*
        builder.append("sampling units").append("='"); 
        for (Iterator i=samplingUnits.iterator(); i.hasNext(); ) {
        	SamplingUnit aUnit = (SamplingUnit)i.next();
        	builder.append(aUnit.getName()).append(" ");
        }
        builder.append("' ");	
        */
        
        builder.append("]");
      
        return builder.toString();
    }

   
   
    
}