package org.team.sdsc.datamodel;


import javax.persistence.*;
import java.util.*;


/**
 * Represents a TEAM collectionship.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="COLLECTION_SAMPLING_UNITS")
@NamedQueries(
{
  @NamedQuery(name="org.team.sdsc.datamodel.AllCollectionships", 
              query="from Collectionship as collectionship "),
  @NamedQuery(name="org.team.sdsc.datamodel.CollectionshipById", 
              query="from Collectionship as collectionship where collectionship=:id")              
})
public class Collectionship{

    /**
     * The synthetic database key associated with this  once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="collection_unit_seq_gen")
    @SequenceGenerator(name="collection_unit_seq_gen", sequenceName="collectionsamplingunitserial")
    private Integer id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="collection_id")
    private Collection collection;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sampling_unit_id")
    private SamplingUnit samplingUnit;


    public Collectionship() {
    }

    public Collectionship(Collection collection, SamplingUnit samplingUnit) {
	this.collection = collection;
	this.samplingUnit = samplingUnit;
    }


    public Integer getId() {
	return id;
    }

    
    public SamplingUnit getSamplingUnit() {
    	return samplingUnit;
    }

    public void setSamplingUnit(SamplingUnit unit) {
	this.samplingUnit = unit;
    }
        

    public Collection getCollection() {
	return collection;
    }


    public void setCollection(Collection collection) {
	this.collection = collection;
    }



    
    /**
     * Produce a human-readable representation.
     *
     * @return a textual description of the collectionship.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("id").append("='").append(id).append("' ");
        builder.append("collection").append("='").append(collection.getId()).append("' ");
        builder.append("sampling unit").append("='").append(samplingUnit.getName()).append("' ");   
        builder.append("]");
     
        return builder.toString();
    }

}