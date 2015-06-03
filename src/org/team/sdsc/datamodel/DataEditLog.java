
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
@Table(name="data_edit_log")
public class DataEditLog {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="data_log_seq_gen")
    @SequenceGenerator(name="data_log_seq_gen", sequenceName="dataeditserial")
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

    @Column(name="event")
    private String event;

    @Column(name="data_id")
    private String dataId;

    @Column(name="column_name")
    private String columnName;

    @Column(name="column_value")
    private String newValue;

    @Column(name="old_column_value")
    private String oldValue;


    public DataEditLog() {
    }


    public DataEditLog(String username, Integer site, String protocol, String block,  
		       String event, String dataId, String columnName, String newValue, String oldValue) {
	this.username = username;
	this.site = site;
	this.protocol = protocol;
	this.block = block;
	this.event = event;
	this.dataId = dataId;
	this.columnName = columnName;
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
        builder.append("event").append("='").append(event).append("' ");
        builder.append("dataid").append("='").append(dataId).append("' ");
        builder.append("column").append("='").append(columnName).append("' ");
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


    public String getEvent() {
	return event;
    }


    public String getColumn() {
	return columnName;
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



}