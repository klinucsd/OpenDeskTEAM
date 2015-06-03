
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
@Table(name="block_edit_log")
public class BlockEditLog {

    /**
     * The synthetic database key associated with this log once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator="block_log_seq_gen")
    @SequenceGenerator(name="block_log_seq_gen", sequenceName="blockeditserial")
    private Integer id;

    @Column(name="username")
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date")
    private Date date;

    @Column(name="site")
    private Integer site;

    @Column(name="block_name")
    private String blockName;

    @Column(name="block_id")
    private String blockId;

    @Column(name="column_name")
    private String columnName;

    @Column(name="column_head")
    private String columnHead;

    @Column(name="column_value")
    private String newValue;

    @Column(name="old_column_value")
    private String oldValue;


    public BlockEditLog() {
    }


    public BlockEditLog(String username, Integer site, String blockId, String blockName, 
			String columnName, String columnHead, String newValue, String oldValue) {
	this.username = username;
	this.site = site;
	this.blockId = blockId;
	this.blockName = blockName;
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
        builder.append("blockid").append("='").append(blockId).append("' ");
        builder.append("blockName").append("='").append(blockName).append("' ");
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


    public String getColumn() {
	return columnName;
    }


    public String getColumnHead() {
	return columnHead;
    }


    public String getBlockId() {
	return blockId;
    }


    public String getBlockName() {
	return blockName;
    }


    public String getNewValue() {
	return newValue;
    }


    public String getOldValue() {
	return oldValue;
    }


}