
package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;


/**
 * Represents a TEAM photo type.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="TV_PHOTO_TYPE")
public class CameraTrapPhotoType {

    /**
     * The synthetic database key associated with this photo type once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    /**
     * The name by which this photo is known.
     */
    @Column(name="NAME",nullable=true,unique=false)
    private String name;


    public Integer getId() {
	return id;
    }


    public void setId(Integer aInt) {
	id = aInt;
    }


    public String getName() {
	return name;
    }


    public void setName(String name) {
	this.name = name;
    }

}
