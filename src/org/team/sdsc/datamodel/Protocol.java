package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM site.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="PROTOCOLS_TEAM")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllProtocols", 
              query="from Protocol as protocol order by protocol.name"),
  @NamedQuery(name="org.team.sdsc.datamodel.ProtocolById", 
              query="from Protocol as protocol where protocol.id=:id"),
  @NamedQuery(name="org.team.sdsc.datamodel.AllActiveProtocols", 
              query="from Protocol as protocol where protocol.status=1 order by protocol.name"),
  @NamedQuery(name="org.team.sdsc.datamodel.ProtocolByNameVersion", 
              query="from Protocol as protocol where protocol.name=:name and protocol.version=:version")               
})
public class Protocol {

    public static int AVIAN = 2;
    public static int BUTTERFLY = 3;
    public static int CLIMATE = 5;
    public static int LITTERFALL = 6;
    public static int PRIMATE = 7;
    public static int TREELIANA = 8;

    /**
     * The synthetic database key associated with this protocol once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    /**
     * The name by which this protocol is known.
     */
    @Column(name="PROTOCOL_NAME",nullable=false,unique=false)
    private String name;
  
    /**
     * The version of this protocol.
     */
    @Column(name="PROTOCOL_VERSION",nullable=false,unique=false)
    private float version;
  
    /**
     * The status of this protocol.
     */
    @Column(name="PROTOCOL_STATUS",nullable=false,unique=true)
    private int status;
  
    /**
     * The description of this protocol.
     */
    @Column(name="PROTOCOL_DESCRIPTION",nullable=true,unique=true)
    private String description;

    /**
     * The family id of this protocol.
     */
    @Column(name="PROTOCOL_ID",nullable=false,unique=true)
    private Long familyId;

    /**
     * The abbreviation of this protocol.
     */
    @Column(name="PROTOCOL_ABBV",nullable=false,unique=true)
    private String abbv;
      
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public Protocol() {}

    /**
     * Create a new Protocol.
     *
     * @param name the name by which the protocol is known.
     * @param version the version of the protocol.
     * @param status the status of the protocol.	
     * @param description the descriptionof the protocol.
     */
    public Protocol(String name, float version, int status, String description) {
        this.name = name;
        this.version = version;
        this.status = status;
        this.description = description;
    }

    /**
     * Get the database key.
     *
     * @return the synthetic database key of the row in which this site
     *         is persisted.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the database key.
     *
     * @param id the synthetic database key of the row in which this site
     *           is persisted.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the name by which this protocol is known.
     *
     * @return the name of the protocol.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name by which this protocol is known.
     *
     * @param name the name of the protocol.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Get the version of this protocol.
     *
     * @return the version of the protocol.
     */
    public float getVersion() {
        return version;
    }

    /**
     * Set the version of this protocol.
     *
     * @param version the version of the protocol.
     */
    public void setVersion(float version) {
        this.version = version;
    }


    /**
     * Get the status of this protocol.
     *
     * @return the status of the protocol.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set the status of this protocol.
     *
     * @param status the status of the protocol.
     */
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Get the description by which this description is known.
     *
     * @return the description of the site.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this protocol.
     *
     * @param description the description of the protocol.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Display(title="Protocol Version", var="ProtocolVersion", index=1, width=100)
    public String getProtocolNameVersion() {
	return name+" "+version;
    }


    public String getAbbrev() {
        /*
	if (name.equals("Climate")) {
	    return "CL";
	} else if (name.equals("Butterfly")) {
	    return "BT";
	} else if (name.equals("Vegetation - Litterfall")) {
	    return "LF";
	} else if (name.equals("Primate")) {
	    return "PR";
	} else if (name.equals("Vegetation - Trees & Lianas")) {
	    return "TL";
	} else if (name.equals("Avian")) {
	    return "AV";
	} else if (name.equals("Terrestrial Vertebrate")) {
	    return "TV";
	} else {
	    return "OT";
	}	
	*/

	return abbv;
    }
	

    /**
     * Produce a human-readable representation of the protocol.
     *
     * @return a textual description of the protocol.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("name").append("='").append(getName()).append("' ");
        builder.append("version").append("='").append(getVersion()).append("' ");
        builder.append("status").append("='").append(getStatus()).append("' ");
        builder.append("description").append("='").append(getDescription()).append("' ");
        builder.append("]");
      
        return builder.toString();
    }

}
