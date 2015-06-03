
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
@XmlAccessorType ( XmlAccessType.FIELD )
@Entity
@Table(name="TV_PHOTO")
public class CameraTrapPhoto {

    /**
     * The synthetic database key associated with this photo once it is
     * persisted to a database.
     */
    @XmlTransient
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    /**
     * The taken time of this photo.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="TAKEN_Time")
    private Date takenTime;


    /**
     * The taken time string of this photo.
     */
    @Column(name="TAKEN_Time_STRING")
    private String takenTimeString;


    /**
     * The raw name by which this photo is known.
     */
    @Column(name="RAW_NAME",nullable=false,unique=false)
    private String rawName;

    /**
     * The team name by which this photo is known.
     */
    @Column(name="TEAM_NAME",nullable=false,unique=true)
    private String teamName;

    /**
     * The notes about this photo.
     */
    @Column(name="NOTE",nullable=true)
    private String note;


    /**
     * The size of this photo.
     */
    @Column(name="SIZE",nullable=true)
    private Integer size;

    /**
     * The Camera Trap by which this photo was taken
     */
    @XmlTransient
    @OneToOne
    @JoinColumn(name="camera_trap_data_id")
    private CameraTrapPhotoCollection collection;

    /**
     * associated animal identification
     */
    @XmlElement(name="PhotoSpeciesRecord")
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "photo" )
    @OrderBy("id")
    private Set<PhotoSpeciesRecord> records;

    /**
     * The Camera Trap photo type
     */
    @OneToOne
    @JoinColumn(name="photo_type_id")
    private CameraTrapPhotoType type;

    /**
     * The Camera Trap photo type identifier
     */
    @OneToOne
    @JoinColumn(name="photo_type_identified_by")
    private Person typeIdentifiedBy;

    @OneToOne( mappedBy = "photo" )
    private CameraTrapPhotoMetadata metadata;

    @Column(name="public_id")
    private Integer publicId;

    public CameraTrapPhoto() {
    }


    public Integer getId() {
	return id;
    }


    public void setId(Integer aInt) {
	id = aInt;
    }


    public CameraTrapPhotoCollection getCollection() {
	return collection;
    }


    public void setCollection(CameraTrapPhotoCollection collection) {
	this.collection = collection;
    }


    
    public Date getTakenTime() {
	return takenTime;
    }


    public void setTakenTime(Date takenTime) {
	this.takenTime = takenTime;
    }


    
    public String getTakenTimeString() {
	return takenTimeString;
    }


    public void setTakenTimeString(String takenTimeString) {
	this.takenTimeString = takenTimeString;
    }



    public String getRawName() {
	return rawName;
    }


    public void setRawName(String rawName) {
	this.rawName = rawName;
    }


    public String getTeamName() {
	return teamName;
    }

    
    public void setTeamName(String teamName) {
	this.teamName = teamName;
    }


    public String getNote() {
	return note;
    }


    public void setNote(String note) {
	this.note = note;
    }


    public Set<PhotoSpeciesRecord> getRecords() {
	return records;
    }


    public void setRecords(Set<PhotoSpeciesRecord> records) {
	this.records = records;
    }

    
    public CameraTrapPhotoType getType() {
	return type;
    }


    public void setType(CameraTrapPhotoType type) {
	this.type = type;
    }

    
    public Person getTypeIdentifiedBy() {
	return this.typeIdentifiedBy;
    }


    public void setTypeIdentifiedBy(Person person) {
	this.typeIdentifiedBy = person;
    }   


    public CameraTrapPhotoMetadata getMetadata() {
	return metadata;
    }


    public Integer getSize() {
	return size;
    }


    public void setSize(Integer size) {
	this.size = size;
    }
	

    public CameraTrapPhoto clone() {
	CameraTrapPhoto result = new CameraTrapPhoto();

	result.takenTime = this.takenTime;
	result.takenTimeString = this.takenTimeString;
	result.rawName = this.rawName;
	result.teamName = this.teamName;
	result.note = this.note;
	result.size = this.size;
	result.collection = this.collection;
	result.records = this.records;
	result.type = this.type;
	result.typeIdentifiedBy = this.typeIdentifiedBy;
	result.metadata = this.metadata;

	return result;
    }
    
    public Integer getPublicId() {
        return publicId;
    }

    public void setPublicId(Integer id) {
        this.publicId = id;
    }


}
