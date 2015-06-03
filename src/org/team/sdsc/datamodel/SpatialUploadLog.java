
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.text.*;

/**
 * Represents a spatial upload log.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="spatial_upload_log")
public class SpatialUploadLog {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="spatial_upload_log_seq_gen")
    @SequenceGenerator(name="spatial_upload_log_seq_gen", sequenceName="SpatialUploadLogSerial")
    private Integer id;

    @Column(name="site_id")
    private Integer siteId;

    @Column(name="protocol")
    private String protocol;

    @Column(name="propose_uploaded")
    private Boolean proposeUploaded;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="propose_upload_date")
    private Date proposeUploadTime;

    @Column(name="propose_upload_username")
    private String proposeUploadUsername;

    @Column(name="gpx_uploaded")
    private Boolean gpxUploaded;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="gpx_upload_date")
    private Date gpxUploadTime;

    @Column(name="gpx_upload_username")
    private String gpxUploadUsername;


    public SpatialUploadLog() {
    }


    public SpatialUploadLog(String username, Integer siteId, String protocol) {
	this.siteId = siteId;
	this.protocol = protocol;
	this.proposeUploaded = new Boolean(true);
	this.proposeUploadTime = new Date();
	this.proposeUploadUsername = username;
    }


    public void setGpxUpload(String username) {
	this.gpxUploaded = new Boolean(true);
	this.gpxUploadTime = new Date();
        this.gpxUploadUsername = username;
    }



    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("id").append("='").append(id).append("' ");
        builder.append("siteId").append("='").append(siteId).append("' ");
        builder.append("protocol").append("='").append(protocol).append("' ");

        builder.append("propose uploaded").append("='").append(proposeUploaded).append("' ");
        builder.append("propose upload time").append("='").append(proposeUploadTime).append("' ");
        builder.append("propose upload username").append("='").append(proposeUploadUsername).append("' ");

        builder.append("gpx uploaded").append("='").append(gpxUploaded).append("' ");
        builder.append("gpx upload time").append("='").append(gpxUploadTime).append("' ");
        builder.append("gpx upload username").append("='").append(gpxUploadUsername).append("' ");

        builder.append("]");
        return builder.toString();
    }



    public Integer getId() {
	return id;
    }


    public Integer getSiteId() {
	return siteId;
    }


    public String getProtocol() {
	return protocol;
    }


    public Boolean isProposeUploaded() {
	return proposeUploaded;
    }


    public String getProposeUploadTime() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(proposeUploadTime);  
    }   


    public String getProposeUploadUsername() {
	return proposeUploadUsername;
    }



    public Boolean isGpxUploaded() {
	return gpxUploaded;
    }


    public String getGpxUploadTime() {
	if (gpxUploadTime != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    return sdf.format(gpxUploadTime);
	} else {
	    return null;
	}
    }   


    public String getGpxUploadUsername() {
	return gpxUploadUsername;
    }


    static private String getSiteName(List<Site> sites, Integer id) {
	for (Site site : sites) {
	    if (site.getId().equals(id)) {
		return site.getName();
	    }
	}
	return null;
    }



}