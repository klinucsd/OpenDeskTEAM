
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;
import javax.xml.bind.annotation.*;

/**
 * Represents a collection of photos from a camera trap.
 *
 * @author Kai Lin
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@XmlRootElement
@Entity
@Table(name="TV_DAMAGED_CAMERA_TRAP")
public class DamagedCameraTrap {

    /**
     * The synthetic database key associated with this collection once it is
     * persisted to a database.
     */
    @XmlTransient
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @XmlTransient
    @OneToOne
    @JoinColumn(name="camera_trap_id")
    private CameraTrapPhotoCollection collection;

    @Column(name="is_firing")
    private Boolean isFiring;

    @Column(name="humidity_inside")
    private Boolean humidityInside;
 
    @Column(name="follow_up_steps",nullable=false,unique=true)
    private String followUpSteps;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deploy_date")
    private Date deployDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="found_date")
    private Date foundDate;


    /*    
    @OneToOne
    @JoinColumn(name="camera_trap_id")
    private CameraTrap cameraTrap;

    @Column(name="camera_serial_number")
    private String cameraSerialNumber;


    @OneToOne
    @JoinColumn(name="found_by")
    private Person foundBy;

    @Column(name="notes",nullable=false,unique=true)
    private String notes;
    */

    public DamagedCameraTrap() {
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



    public Boolean getIsFiring() {
	return isFiring;
    }

    public void setIsFiring(Boolean isFiring) {
	this.isFiring = isFiring;
    }


    public Boolean getHumidityInside() {
	return humidityInside;
    }


    public void setHumidityInside(Boolean humidityInside) {
	this.humidityInside = humidityInside;
    }


    public String getFollowUpSteps() {
	return followUpSteps;
    }


    public void setFollowUpSteps(String followUpSteps) {
	this.followUpSteps = followUpSteps;
    }


    public Date getFoundDate() {
	return foundDate;
    }


    public void setFoundDate(Date foundDate) {
	this.foundDate = foundDate;
    }


    public Date getDeployDate() {
	return deployDate;
    }


    public void setDeployDate(Date deployDate) {
	this.deployDate = deployDate;
    }



    /*
    public CameraTrap getCameraTrap() {
	return cameraTrap;
    }

    public void setCameraTrap(CameraTrap trap) {
	this.cameraTrap = trap;
    }


    public String getCameraSerialNumber() {
	return cameraSerialNumber;
    }


    public void setCameraSerialNumber(String cameraSerialNumber) {
	this.cameraSerialNumber = cameraSerialNumber;
    }


    public Person getFoundBy() {
	return foundBy;
    }


    public void setFoundBy(Person foundBy) {
	this.foundBy = foundBy;
    }


    public String getNotes() {
	return notes;
    }


    public void setNotes(String notes) {
	this.notes = notes;
    }

    */



}