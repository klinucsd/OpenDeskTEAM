package org.team.sdsc.datamodel;

import java.util.*;
import java.text.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;

@Entity
@Table(name="vegetation_sampling_event")
public class VegetationEvent {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name="plot_id")
    private Block block;

    @Column(name="event")
    private String event;

    public Integer getId() {
	return this.id;
    }

    public Block getBlock() {
	return this.block;
    }

    public String getEvent() {
	return this.event;
    }
    
    public void setBlock(Block block) {
        this.block = block;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}
