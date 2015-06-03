package org.team.sdsc.datamodel;

import java.util.*;
import java.text.*;
import java.lang.annotation.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;

@Entity
@Table(name="vegetation_collection")
public class VegetationCollection {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name="plot_id")
    private Block block;

    @Column(name="event")
    private String event;

    @Column(name="count")
    private Integer count;

    @Column(name="saved")
    private boolean saved = false;

    public Integer getId() {
	return this.id;
    }

    public Block getBlock() {
	return this.block;
    }

    public String getEvent() {
	return this.event;
    }

    public Integer getCount() {
	return this.count;
    }

    public boolean getSaved() {
	return this.saved;
    }
    
    public void setBlock(Block block) {
        this.block = block;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
