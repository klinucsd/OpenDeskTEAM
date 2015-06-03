
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
@Table(name="data_upload_log")
public class DataUploadLog {

    /**
     * The synthetic database key associated with this log once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="data_upload_log_seq_gen")
    @SequenceGenerator(name="data_upload_log_seq_gen", sequenceName="DataUploadSerial")
    private Integer id;

    @Column(name="username")
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="upload_date")
    private Date uploadDate;

    @Column(name="site_id")
    private Integer siteId;

    @Column(name="protocol")
    private String protocol;

    @Column(name="excel_id")
    private Long excelId;

    @Column(name="filename")
    private String filename;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="save_date")
    private Date saveDate;

    @Column(name="station")
    private String station;

    public DataUploadLog() {
    }


    public DataUploadLog(String username, Integer siteId, String protocol, Long excelId, String filename) {
	this.username = username;
	this.siteId = siteId;
	this.protocol = protocol;
	this.excelId = excelId;
	this.filename = filename;
	this.uploadDate = new Date();
    }



    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("id").append("='").append(id).append("' ");
        builder.append("siteId").append("='").append(siteId).append("' ");
        builder.append("protocol").append("='").append(protocol).append("' ");
        builder.append("username").append("='").append(username).append("' ");
        builder.append("excel id").append("='").append(excelId).append("' ");
        builder.append("filename").append("='").append(filename).append("' ");
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


    public String getUsername() {
	return username;
    }						


    public void setUsername(String username) {
	this.username = username;
    }


    public String getUploadDate() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(uploadDate);  
    }   


    public String getSaveDate() {
	if (saveDate != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    return sdf.format(saveDate);
	} else {
	    return null;
	}
    }   


    public void setSaveDate(Date date) {
	this.saveDate = date;
    }


    public Long getExcelId() {
	return excelId;
    }


    public String getFilename() {
	return this.filename;
    }


    public String getStation() {
	return station;
    }


    public void setStation(String station) {
	this.station = station;
    }


}