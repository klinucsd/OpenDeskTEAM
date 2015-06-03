
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import java.text.*;

/**
 * Represents an edit log.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="spatial_edit_log")
public class SpatialEditLog {

    /**
     * The synthetic database key associated with this log once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="spatial_log_seq_gen")
    @SequenceGenerator(name="spatial_log_seq_gen", sequenceName="spatialeditserial")
    private Integer id;

    @Column(name="username")
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date")
    private Date date;

    @Column(name="site")
    private Integer site;

    @Column(name="protocol")
    private String protocol;

    @Column(name="block")
    private String block;

    @Column(name="data_id")
    private String dataId;

    @Column(name="column_name")
    private String columnName;

    @Column(name="column_head")
    private String columnHead;

    @Column(name="column_value")
    private String newValue;

    @Column(name="old_column_value")
    private String oldValue;


    public SpatialEditLog() {
    }


    public SpatialEditLog(String username, Integer site, String protocol, String block, String dataId, 
			  String columnName, String columnHead, String newValue, String oldValue) {
	this.username = username;
	this.site = site;
	this.protocol = protocol;
	this.block = block;
	this.dataId = dataId;
	this.columnName = columnName;
	this.columnHead = columnHead;
	this.newValue = newValue;
	this.oldValue = oldValue;
	this.date = new Date();
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName()).append("@");
        builder.append(Integer.toHexString(hashCode())).append(" [");
        builder.append("id").append("='").append(id).append("' ");
        builder.append("username").append("='").append(username).append("' ");
        builder.append("date").append("='").append(date).append("' ");
        builder.append("site").append("='").append(site).append("' ");
        builder.append("protocol").append("='").append(protocol).append("' ");
        builder.append("block").append("='").append(block).append("' ");
        builder.append("dataid").append("='").append(dataId).append("' ");
        builder.append("column").append("='").append(columnName).append("' ");
        builder.append("columnhead").append("='").append(columnHead).append("' ");
        builder.append("newvalue").append("='").append(newValue).append("' ");
        builder.append("oldvalue").append("='").append(oldValue).append("' ");
        builder.append("]");
        return builder.toString();
    }



    public Integer getId() {
	return id;
    }


    public String getUsername() {
	return username;
    }


    public String getDate() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);  
    }


    public Date getDateObject() {
        return date;  
    }


    public Integer getSite() {
	return site;
    }


    public String getBlock() {
	return block;
    }


    public String getProtocol() {
	return protocol;
    }


    public String getColumn() {
	return columnName;
    }


    public String getColumnHead() {
	return columnHead;
    }


    public String getDataId() {
	return dataId;
    }


    public String getNewValue() {
	return newValue;
    }


    public String getOldValue() {
	return oldValue;
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