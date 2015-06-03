package org.team.sdsc.datamodel;

import javax.persistence.*;
import java.util.*;

/**
 * Represents a TEAM data management event listener.
 *
 * @author Kai Lin
 */
@Entity
@Table(name="DATA_MANAGEMENT_EVENT_LISTENER")
public class DataManagementEventListener {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="EVENT_ID")
    private DataManagementEvent event;

    @OneToOne
    @JoinColumn(name="USER_ID")
    private Person person;

    @Column(name="is_primary_listener")
    private boolean primary;


    // get id
    public Long getId() {
	return id;
    }

    // get person
    public Person getPerson() {
	return person;
    }

    // get event
    public DataManagementEvent getEvent() {
	return event;
    }

    // is primary listener
    public boolean isPrimary() {
	return primary;
    }


}