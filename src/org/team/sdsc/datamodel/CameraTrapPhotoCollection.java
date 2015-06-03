
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
@Table(name="TV_CAMERA_TRAP_DATA")
public class CameraTrapPhotoCollection {

    /**
     * The synthetic database key associated with this collection once it is
     * persisted to a database.
     */
    @XmlTransient
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    /**
     * The Camera Trap by which this collection was taken
     */
    @OneToOne
    @JoinColumn(name="camera_trap_id")
    private CameraTrap cameraTrap;

    @Column(name="event")
    private String event;
    
    @Column(name="camera_serial_number")
    private String cameraSerialNumber;

    @Column(name="memory_card_serial_number")
    private String memoryCardSerialNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="starttime")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="endtime")
    private Date endTime;

    @OneToOne
    @JoinColumn(name="set_by")
    private Person setPerson;

    @OneToOne
    @JoinColumn(name="picked_by")
    private Person pickPerson;

    @Column(name="working")
    private Boolean working;

    @Column(name="trap_missing")
    private Boolean trapMissing;
    
    @Column(name="case_damaged")
    private Boolean caseDamaged;

    @Column(name="camera_damaged")
    private Boolean cameraDamaged;

    @Column(name="card_damaged")
    private Boolean cardDamaged;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "collection" )
    @OrderBy("takenTime")
    private Set<CameraTrapPhoto> photos;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "collection" )
    private DamagedCameraTrap damage;

    @Column(name="public_id")
    private Integer publicId;

    @Transient
    private String manufacturer;

    @Transient
    private String model;

    /**
     * The notes about this photo.
     */
    @Column(name="NOTES",nullable=false,unique=true)
    private String notes;


    public CameraTrapPhotoCollection() {
    }


    public Integer getId() {
	return id;
    }


    public void setId(Integer aInt) {
	id = aInt;
    }


    public Integer getPublicId() {
        return publicId;
    }


    public void setPublicId(Integer id) {
        this.publicId = id;
    }

    public CameraTrap getCameraTrap() {
	return cameraTrap;
    }


    public void setCameraTrap(CameraTrap trap) {
	this.cameraTrap = trap;
    }


    public String getEvent() {
	return event;
    }

    
    public void setEvent(String event) {
	this.event = event;
    }


    public String getCameraSerialNumber() {
	return cameraSerialNumber;
    }


    public void setCameraSerialNumber(String cameraSerialNumber) {
	this.cameraSerialNumber = cameraSerialNumber;
    }


    public String getMemoryCardSerialNumber() {
	return memoryCardSerialNumber;
    }


    public void setMemoryCardSerialNumber(String memoryCardSerialNumber) {
	this.memoryCardSerialNumber = memoryCardSerialNumber;
    }


    public Date getStartTime() {
	return startTime;
    }


    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }


    public Date getEndTime() {
	return endTime;
    }


    public void setEndTime(Date endTime) {
	this.endTime = endTime;
    }


    public Person getSetPerson() {
	return setPerson;
    }


    public void setSetPerson(Person setPerson) {
	this.setPerson = setPerson;
    }


    public Person getPickPerson() {
	return pickPerson;
    }


    public void setPickPerson(Person pickPerson) {
	this.pickPerson = pickPerson;
    }


    public Boolean getWorking() {
	return working;
    }


    public void setWorking(Boolean working) {
	this.working = working;
    }


    public Boolean getTrapMissing() {
	return trapMissing;
    }


    public void setTrapMissing(Boolean trapMissing) {
	this.trapMissing = trapMissing;
    }


    public Boolean getCaseDamaged() {
	return caseDamaged;
    }


    public void setCaseDamaged(Boolean caseDamaged) {
	this.caseDamaged = caseDamaged;
    }


    public Boolean getCameraDamaged() {
	return cameraDamaged;
    }


    public void setCameraDamaged(Boolean cameraDamaged) {
	this.cameraDamaged = cameraDamaged;
    }


    public Boolean getCardDamaged() {
	return cardDamaged;
    }


    public void setCardDamaged(Boolean cardDamaged) {
	this.cardDamaged = cardDamaged;
    }



    public String getNotes() {
	return notes;
    }


    public void setNotes(String notes) {
	this.notes = notes;
    }


    public Set<CameraTrapPhoto> getPhotos() {
	return photos;
    }


    public void setPhotos(Set<CameraTrapPhoto> photos) {
	this.photos = photos;
    }


    public DamagedCameraTrap getDamagedCameraTrap() {
	return damage;
    }


    public String getManufacturer() {
	return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
	this.manufacturer = manufacturer;
    }

    public String getModel() {
	return this.model;
    }

    public void setModel(String model) {
	this.model = model;
    }

    public CameraTrapPhotoCollection clone() {

	CameraTrapPhotoCollection result = new CameraTrapPhotoCollection();
	result.cameraTrap = this.cameraTrap;
	result.event = this.event;
	result.cameraSerialNumber = this.cameraSerialNumber;
	result.memoryCardSerialNumber = this.memoryCardSerialNumber;
	result.startTime = this.startTime;
	result.endTime = this.endTime;
	result.setPerson = this.setPerson;
	result.pickPerson = this.pickPerson;
        result.working = this.working;
	result.trapMissing = this.trapMissing;
	result.caseDamaged = this.caseDamaged;
	result.cameraDamaged = this.cameraDamaged;
	result.cardDamaged = this.cardDamaged; 
	result.notes = this.notes;
	return result;
    }

}