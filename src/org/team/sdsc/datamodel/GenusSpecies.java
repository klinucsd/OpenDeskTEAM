package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;
import org.team.sdsc.datamodel.annotation.*;

/**
 * Represents a TEAM Taxa.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="Genus_Species")
public class GenusSpecies {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name="genus",nullable=true,unique=false)
    private String genus;

    @Column(name="species",nullable=true,unique=false)
    private String species;

    public GenusSpecies(String genus, String species) {
	this.genus = genus;
	this.species = species;
    }


}