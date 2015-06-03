
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;
import javax.xml.bind.annotation.*;

/**
 * Represents a TEAM photo.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="TV_PHOTO_ANIMAL")
public class PhotoSpeciesRecord {

    /**
     * The synthetic database key associated with this record once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    @XmlTransient
    private Integer id;

    /**
     * The associated photo.
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn ( name = "PHOTO_ID" , nullable = false )
    private CameraTrapPhoto photo;

    /**
     * The genus name by which this photo is known.
     */
    @Column(name="GENUS",nullable=false,unique=true)
    private String genus;

    /**
     * The species name by which this photo is known.
     */
    @Column(name="SPECIES",nullable=false,unique=true)
    private String species;

    /**
     * The number_of_animals in this photo.
     */
    @Column(name="NUMBER_OF_ANIMALS",nullable=true,unique=true)
    private Integer numberOfAnimals;

    /**
     * The associated person.
     */
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn ( name = "identified_by" )
    private Person identifiedBy;

    @Column (name = "uncertainty")
    private String uncertainty = "Absolutely sure";

    public PhotoSpeciesRecord() {
    }


    public PhotoSpeciesRecord(CameraTrapPhoto photo, String genus, String species, Integer number, Person person) {
	this.photo = photo;
	this.genus = genus;
	this.species = species;
	this.numberOfAnimals = number;
	this.identifiedBy = person;
    }


    public Integer getId() {
	return id;
    }


    public void setId(Integer aInt) {
	id = aInt;
    }


    public String getGenus() {
	return genus;
    }


    public void setGenus(String genus) {
	this.genus = genus;
    }


    public String getSpecies() {
	return species;
    }

    
    public void setSpecies(String species) {
	this.species = species;
    }

    
    public Integer getNumberOfAnimals() {
	return numberOfAnimals;
    }


    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }


    public CameraTrapPhoto getPhoto() {
	return photo;
    }


    public void setCameraTrapPhoto(CameraTrapPhoto photo) {
	this.photo = photo;
    }


    public Person getIdentifiedBy() {
	return identifiedBy;
    }


    public void setIdentifiedBy(Person identifiedBy) {
	this.identifiedBy = identifiedBy;
    }


    public String getUncertainty() {
	return this.uncertainty;
    }


    public void setUncertainty(String uncertainty) {
	this.uncertainty = uncertainty;
    }

}