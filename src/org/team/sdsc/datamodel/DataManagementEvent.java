package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;

/**
 * Represents a TEAM data management event.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="DATA_MANAGEMENT_EVENT")
public class DataManagementEvent {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="NAME", nullable=false, unique=false)
    private String name;

    @OneToOne
    @JoinColumn(name="SITE_Id")
    private Site site;

    @OneToOne
    @JoinColumn(name="PROTOCOL_Id")
    private ProtocolFamily protocolFamily;

    @OneToMany(mappedBy="event")
    private Set<DataManagementEventListener> listeners;


    // get id
    public Long getId() {
	return id;
    }

    // get name
    public String getName() {
	return name;
    }

    // get site
    public Site getSite() {
	return site;
    }

    // get protocol family
    public ProtocolFamily getProtocolFamily() {
	return protocolFamily;
    }

    // get listeners
    public Set<DataManagementEventListener> getListeners() {
	return listeners;
    }


    public Set<Person> getPrimaryListeners() {
	Set<Person> result = new HashSet<Person>();
	for (DataManagementEventListener listener : listeners) {
	    if (listener.isPrimary()) result.add(listener.getPerson());
	}
	return result;
    }


    public Set<Person> getCopiedListeners() {
	Set<Person> result = new HashSet<Person>();
	for (DataManagementEventListener listener : listeners) {
	    if (!listener.isPrimary()) result.add(listener.getPerson());
	}
	return result;
    }







}