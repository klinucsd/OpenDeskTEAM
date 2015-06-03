package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;

/**
 * Represents a TEAM site.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="PROTOCOL_FAMILY")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllProtocolFamilies", 
              query="from ProtocolFamily as protocolFamily order by protocolFamily.name")
})
public class ProtocolFamily {

    /**
     * The synthetic database key associated with this protocol once it is
     * persisted to a database.
     */
    @Id
    @Column(name="PROTOCOL_ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    /**
     * The name by which this protocol is known.
     */
    @Column(name="PROTOCOL_NAME",nullable=false,unique=false)
    private String name;
  
      
    /**
     * Empty constructor required for this class to be a valid JavaBean.
     */
    public ProtocolFamily() {}


    /**
     * Create a new Protocol.
     *
     * @param name the name by which the protocol is known.
     */
    public ProtocolFamily(String name) {
        this.name = name;
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
     * Produce a human-readable representation of the protocol.
     *
     * @return a textual description of the protocol.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("name").append("='").append(getName()).append("' ");
        builder.append("]");

        return builder.toString();
    }

}
