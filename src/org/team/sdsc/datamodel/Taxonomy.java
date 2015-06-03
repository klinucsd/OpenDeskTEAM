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
@Table(name="taxonomic_authority")
public class Taxonomy {

    /**
     * The synthetic database key associated with this site once it is
     * persisted to a database.
     */
    @Id
    @Column(name="ta_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name="kingdom",nullable=true,unique=false)
    private String kingdom;

    @Column(name="phylum",nullable=true,unique=false)
    private String phylum;

    @Column(name="class",nullable=true,unique=false)
    private String clazz;

    @Column(name="order_team",nullable=true,unique=false)
    private String order;

    @Column(name="suborder",nullable=true,unique=false)
    private String suborder;

    @Column(name="infraorder",nullable=true,unique=false)
    private String infraorder;

    @Column(name="superfamily",nullable=true,unique=false)
    private String superfamily;

    @Column(name="family",nullable=true,unique=false)
    private String family;

    @Column(name="subfam",nullable=true,unique=false)
    private String subfam;

    @Column(name="tribe",nullable=true,unique=false)
    private String tribe;

    @Column(name="subtribe",nullable=true,unique=false)
    private String subtribe;

    @Column(name="genus",nullable=true,unique=false)
    private String genus;

    @Column(name="subgenus",nullable=true,unique=false)
    private String subgenus;

    @Column(name="species",nullable=true,unique=false)
    private String species;

    @Column(name="subspecies",nullable=true,unique=false)
    private String subspecies;
    
    public Taxonomy() {}


    public String getKingdom() {
	return kingdom;
    }

    public String getPhylum() {
	return phylum;
    }

}