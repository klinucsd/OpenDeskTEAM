
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.text.*;

/**
 * Represents a data upload log.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="tv_upload_job")
public class CameraTrapUploadJob {

    @Id
    @Column(name="ID")
    //@GeneratedValue(strategy=GenerationType.AUTO, generator="tv_upload_job_seq_gen")
    //@SequenceGenerator(name="tv_upload_job_seq_gen", sequenceName="tv_upload_job_serial")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name="camera_trap_id")
    private CameraTrap cameraTrap;

    @Column(name="event")
    private String event;

    @OneToOne
    @JoinColumn(name="uploader_id")
    private Person uploader; 

    @Column(name="stage")
    private int stage;

    @Column(name="status")
    private String status;

    @Column(name="total_number")
    private int totalNumber;

    @Column(name="current_index")
    private int currentIndex;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="end_time")
    private Date endTime;

    public CameraTrapUploadJob(CameraTrap cameraTrap,
			       String event,
			       Person uploader,
			       int totalNumber) {
	this.cameraTrap = cameraTrap;
	this.event = event;
	this.uploader = uploader;
	this.stage = 1;
	this.totalNumber = totalNumber;
	this.currentIndex = 0;
	this.startTime = new Date();
	this.endTime = null;
    }


    public CameraTrapUploadJob() {
    }
    
    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
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

    public String getStatus() {
	return status;
    }
    
    public void setStatus(String status) {
	this.status = status;
    }

    public Person getUploader() {
	return uploader;
    }

    public void setUploader(Person Uploader) {
	this.uploader = uploader;
    }

    public int getStage() {
	return this.stage;
    }

    public void setStage(int stage) {
	this.stage = stage;
    }

    public int getTotalNumber() {
	return this.totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
	this.totalNumber = totalNumber;
    }

    public int getCurrentIndex() {
	return this.currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
	this.currentIndex = currentIndex;
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

    public boolean isFinished() {
	return this.endTime != null;
    }


}