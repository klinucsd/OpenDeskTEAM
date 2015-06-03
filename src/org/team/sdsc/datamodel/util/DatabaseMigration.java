
package org.team.sdsc.datamodel.util;

import java.util.*;
import org.team.sdsc.datamodel.*;

public class DatabaseMigration {

    private Network network;
    private Site site;
    private Person owner;

    private static String comma = ",";
    private static String quote = "'";
    private static String nullString = "NULL";
    private static String treePointTypeString = "Tree Point";
    private static String lianaPointTypeString = "Liana Point";
    private static String cameraTrapTypeString = "Camera Trap";


    public DatabaseMigration(Network network, Site site, Person owner) {
	this.network = network;
	this.site = site;
	this.owner = owner;
    }


    public String getSQLStatements() {
	StringBuffer sb = new StringBuffer();
	getProtocols(sb);
	getCountries(sb);    
	getSite(sb);
	getSiteProtocol(sb);
	getBlocks(sb);
	getSamplingUnits(sb);
	getPersons(sb);
	getOwner(sb);
	return sb.toString();
    }


    private void getProtocols(StringBuffer sb) {

	sb.append("DELETE FROM protocols_team;\n");

	for (Protocol protocol : site.getProtocols()) {

	    if (!protocol.getName().equals("Vegetation - Trees & Lianas") && 
		!protocol.getName().equals("Terrestrial Vertebrate")) 
		continue;

	    sb.append("INSERT INTO protocols_team(id, protocol_id, protocol_name, protocol_version, protocol_status, protocol_abbv, protocol_description) VALUES(");

	    // id
	    sb.append(protocol.getId());
	    sb.append(comma);
	    
	    // protocol_id
	    sb.append(network.getProtocolFamilyByName(protocol.getName()).getId());
	    sb.append(comma);

	    // protocol_name
	    sb.append(quote);
	    sb.append(protocol.getName());
	    sb.append(quote);
	    sb.append(comma);

	    // protocol_version
	    sb.append(protocol.getVersion());
	    sb.append(comma);

	    // protocol_status
	    sb.append(protocol.getStatus());
	    sb.append(comma);

	    // protocol_abbv
	    if (protocol.getAbbrev() != null) {
		sb.append(quote);
		sb.append(protocol.getAbbrev());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // protocol_description
	    if (protocol.getDescription() != null) {
		sb.append(quote);
		sb.append(protocol.getDescription());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }

	    sb.append(");\n");
	}

    }


    private void getCountries(StringBuffer sb) {

    }


    private void getSite(StringBuffer sb) {

	sb.append("DELETE FROM sites_team;\n");
	sb.append("INSERT INTO sites_team(site_id, site_name, time_zone, site_abbv, site_status,  short_name, latitude, longitude, last_event_at, last_event_by, country_id) VALUES(");

	// site_id
	sb.append(site.getId());
	sb.append(comma);

	// site_name
	sb.append(quote);
	sb.append(site.getName());
	sb.append(quote);
	sb.append(comma);

	// time_zone
	if (site.getTimeZone() != null) {
	    sb.append(quote);
	    sb.append(site.getTimeZone());
	    sb.append(quote);
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// site_abbv
	if (site.getAbbv() != null) {
	    sb.append(quote);
	    sb.append(site.getAbbv());
	    sb.append(quote);
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// site_status
	if (site.getStatus() != null) {
	    sb.append(quote);
	    sb.append(site.getStatus());
	    sb.append(quote);
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// short_name
	if (site.getShortName() != null) {
	    sb.append(quote);
	    sb.append(site.getShortName());
	    sb.append(quote);
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// latitude
	if (site.getLatitude() != null) {
	    sb.append(site.getLatitude());
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// longitude
	if (site.getLongitude() != null) {
	    sb.append(site.getLongitude());
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// last_event_at
	if (site.getLastEventAt() != null) {
	    sb.append(quote);
	    sb.append(site.getLastEventAt());
	    sb.append(quote);
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// last_event_by
	if (site.getLastEventBy() != null) {
	    sb.append(site.getLastEventBy());
	} else {
	    sb.append(nullString);
	}
	sb.append(comma);

	// country_id
	if (site.getCountryId() != null) {
	    sb.append(site.getCountryId());
	} else {
	    sb.append(nullString);
	}

	sb.append(");\n");
	
    }


    private void getSiteProtocol(StringBuffer sb) {

	sb.append("DELETE FROM site_protocol;\n");
	for (Protocol protocol : site.getProtocols()) {
	    sb.append("INSERT INTO site_protocol(site_id, protocol_id, protocol_version_id) VALUES(");    
	    
	    // site_id
	    sb.append(site.getId());
	    sb.append(comma);
	    
	    // protocol_id
	    sb.append(network.getProtocolFamilyByName(protocol.getName()).getId());
	    sb.append(comma);

	    // protocol_version_id
	    sb.append(protocol.getVersion());

	    sb.append(");\n");
	}

    }


    private void getBlocks(StringBuffer sb) {

	sb.append("DELETE FROM imas;\n");

	for (Block block : network.getBlocks(site)) {

	    if (block.getType().equals("BLK")) continue;

	    sb.append("INSERT INTO imas(id, site_id, ima_name, ima_description, area_type, pt1_lat, pt1_long, pt1_bp, pt2_lat, pt2_long, pt2_bp, pt3_lat, pt3_long, pt3_bp, pt4_lat, pt4_long, pt4_bp, bearing_0_100, proposed_pt1_lat, proposed_pt1_long, bearing_100_0, gps_reading_date) VALUES(");

	    // id
	    sb.append(block.getId());
	    sb.append(comma);

	    // site_id       
	    sb.append(site.getId());
	    sb.append(comma);     
	    
	    // ima_name
	    sb.append(block.getIndex());
	    sb.append(comma);
            		   
	    // ima_description
	    sb.append(quote);
	    sb.append(block.getName());
	    sb.append(quote);
	    sb.append(comma);            		   

	    // area_type
	    sb.append(quote);
	    sb.append("VG");
	    sb.append(quote);
	    sb.append(comma);            		   

	    // pt1_lat 
	    if (block.getPoint1Latitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint1Latitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }         
	    sb.append(comma);    		   

	    // pt1_long 
	    if (block.getPoint1Longitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint1Longitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }     
	    sb.append(comma);      		   

	    // pt1_bp           
	    if (block.getPoint1BlockPosition() != null) {
		sb.append(quote);
		sb.append(block.getPoint1BlockPosition());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);     		   

	    // pt2_lat 
	    if (block.getPoint2Latitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint2Latitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }         
	    sb.append(comma);    		   

	    // pt2_long 
	    if (block.getPoint2Longitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint2Longitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }     
	    sb.append(comma);      		   

	    // pt2_bp           
	    if (block.getPoint2BlockPosition() != null) {
		sb.append(quote);
		sb.append(block.getPoint2BlockPosition());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);     		   

	    // pt3_lat 
	    if (block.getPoint3Latitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint3Latitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }         
	    sb.append(comma);    		   

	    // pt3_long 
	    if (block.getPoint3Longitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint3Longitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }     
	    sb.append(comma);      		   

	    // pt3_bp           
	    if (block.getPoint3BlockPosition() != null) {
		sb.append(quote);
		sb.append(block.getPoint3BlockPosition());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);     		   

	    // pt4_lat 
	    if (block.getPoint4Latitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint4Latitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }         
	    sb.append(comma);    		   

	    // pt4_long 
	    if (block.getPoint4Longitude() != null) {
		sb.append(quote);
		sb.append(block.getPoint4Longitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }     
	    sb.append(comma);      		   

	    // pt4_bp           
	    if (block.getPoint4BlockPosition() != null) {
		sb.append(quote);
		sb.append(block.getPoint4BlockPosition());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);     		   

	    // bearing_0_100
	    if (block.getBearing0100() != null) {
		sb.append(quote);
		sb.append(block.getBearing0100());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }          
	    sb.append(comma);           		   

	    // proposed_pt1_lat
	    if (block.getProposedLatitude() != null) {
		sb.append(quote);
		sb.append(block.getProposedLatitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }         
	    sb.append(comma);            		   

	    // proposed_pt1_long
	    if (block.getProposedLongitude() != null) {
		sb.append(quote);
		sb.append(block.getProposedLongitude());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }         
	    sb.append(comma);            		   

	    // bearing_100_0
	    if (block.getBearing1000() != null) {
		sb.append(quote);
		sb.append(block.getBearing0100());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }        
	    sb.append(comma);            		   

	    // gps_reading_date
	    if (block.getGpsReadingDate() != null) {
		sb.append(quote);
		sb.append(block.getGpsReadingDate());
		sb.append(quote);
	    } else {
		sb.append(nullString);
	    }        

	    sb.append(");\n");

	}
    }


    private void getSamplingUnits(StringBuffer sb) {

	sb.append("DELETE FROM sampling_units;\n");
	for (Protocol protocol : site.getProtocols()) {
	    if (!protocol.getName().equals("Vegetation - Trees & Lianas") && 
		!protocol.getName().equals("Terrestrial Vertebrate")) 
		continue;

	    for (SamplingUnit unit : network.getSamplingUnits(site, protocol)) {
	
		sb.append("INSERT INTO sampling_units(id, unit_name, unit_type, latitude, longitude, proposed_latitude, proposed_longitude, method_team, subplot_number, plot_x_coordinate, plot_y_coordinate, ima_x_coordinate, ima_y_coordinate, tree_number, stratum, trap_number, distance, bearing, protocol_id, site_id, ima_id, gps_reading_date, gps_reading_first_name, gps_reading_last_name, gps_number_of_reading, gps_notes, gps_make_and_model, x_stake, y_stake) VALUES(");

		// id
		sb.append(unit.getId());
		sb.append(comma);

		// unit_name
		sb.append(quote);
		sb.append(unit.getName());
		sb.append(quote);
		sb.append(comma);

		// unit_type
		sb.append(quote);
		if (unit instanceof TreePoint) {
		    sb.append(treePointTypeString);
		} else if (unit instanceof LianaPoint) {
                    sb.append(lianaPointTypeString);
                } else if (unit instanceof CameraTrap) {
                    sb.append(cameraTrapTypeString);
                } 
		sb.append(quote);
		sb.append(comma);

		// latitude
		if (unit.getLatitude() != null) {
		    sb.append(unit.getLatitude());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);
		
		// longitude
		if (unit.getLongitude() != null) {
		    sb.append(unit.getLongitude());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// proposed_latitude
		if (unit.getProposedLatitude() != null) {
		    sb.append(unit.getProposedLatitude());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// proposed_longitude
		if (unit.getProposedLongitude() != null) {
		    sb.append(unit.getProposedLongitude());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// method_team
		if (unit.getMethod() != null) {
		    sb.append(quote);
		    sb.append(unit.getMethod());
		    sb.append(quote);
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// subplot_number
		if (unit.getSubplotNumber() != null) {
		    sb.append(unit.getSubplotNumber());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);
		
		// plot_x_coordinate
		if (unit.getPlotXCoordinate() != null) {
		    sb.append(unit.getPlotXCoordinate());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// plot_y_coordinate
		if (unit.getPlotYCoordinate() != null) {
		    sb.append(unit.getPlotYCoordinate());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// ima_x_coordinate
		if (unit.getImaXCoordinate() != null) {
		    sb.append(unit.getImaXCoordinate());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// ima_y_coordinate
		if (unit.getImaYCoordinate() != null) {
		    sb.append(unit.getImaYCoordinate());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// tree_number
		if (unit.getTreeNumber() != null) {
		    sb.append(unit.getTreeNumber());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// stratum
		if (unit.getStratum() != null) {
		    sb.append(quote);
		    sb.append(unit.getStratum());
		    sb.append(quote);
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// trap_number
		if (unit.getTrapNumber() != null) {
		    sb.append(unit.getTrapNumber());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// distance
		if (unit.getDistance() != null) {
		    sb.append(unit.getDistance());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// bearing
		if (unit.getBearing() != null) {
		    sb.append(unit.getBearing());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// protocol_id
		sb.append(protocol.getId());
		sb.append(comma);		

		// site_id
		sb.append(site.getId());
		sb.append(comma);		

                // ima_id
		if (unit.getBlock() != null) {
		    sb.append(unit.getBlock().getId());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// gps_reading_date
		if (unit.getGpsReadingDate() != null) {
		    sb.append(quote);
		    sb.append(unit.getGpsReadingDate());
		    sb.append(quote);
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// gps_reading_first_name
		if (unit.getGpsReadingFirstName() != null) {
		    sb.append(quote);
		    sb.append(unit.getGpsReadingFirstName());
		    sb.append(quote);
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// gps_reading_last_name
		if (unit.getGpsReadingLastName() != null) {
		    sb.append(quote);
		    sb.append(unit.getGpsReadingLastName());
		    sb.append(quote);
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// gps_number_of_reading
		if (unit.getGpsNumberOfReading() != null) {
		    sb.append(unit.getGpsNumberOfReading());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// gps_notes
		if (unit.getGpsNotes() != null) {
		    sb.append(quote);
		    sb.append(unit.getGpsNotes());
		    sb.append(quote);
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// gps_make_and_model
		if (unit.getGpsModel() != null) {
		    sb.append(quote);
		    sb.append(unit.getGpsModel());
		    sb.append(quote);
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// x_stake
		if (unit.getStakeX() != null) {
		    sb.append(unit.getStakeX());
		} else {
		    sb.append(nullString);
		}
		sb.append(comma);

		// y_stake
		if (unit.getStakeY() != null) {
		    sb.append(unit.getStakeY());
		} else {
		    sb.append(nullString);
		}

		sb.append(");\n");

	    }	  
	}
    }


    private void getPersons(StringBuffer sb) {

	sb.append("DELETE FROM person;\n");
	for (Person person : network.getPersons(site)) {
	    
	    sb.append("INSERT INTO person(person_id, username, email, first_name, last_name, address, city, province, postal_code, country, phone_number, web_role, active, timestamp) VALUES(");

	    // person_id
	    sb.append(person.getId());
	    sb.append(comma);	

	    // username
	    sb.append(quote);	
	    sb.append(person.getUserName());
	    sb.append(quote);	
	    sb.append(comma);	
	    
	    // email
	    if (person.getEmail() != null) {
		sb.append(quote);	
		sb.append(person.getEmail());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // first_name
	    if (person.getFirstName() != null) {
		sb.append(quote);	
		sb.append(person.getFirstName());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // last_name
	    if (person.getLastName() != null) {
		sb.append(quote);	
		sb.append(person.getLastName());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // address
	    if (person.getAddress() != null) {
		sb.append(quote);	
		sb.append(person.getAddress());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // city
	    if (person.getCity() != null) {
		sb.append(quote);	
		sb.append(person.getCity());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // province
	    if (person.getProvince() != null) {
		sb.append(quote);	
		sb.append(person.getProvince());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // postal_code
	    if (person.getPostalCode() != null) {
		sb.append(quote);	
		sb.append(person.getPostalCode());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // country
	    if (person.getCountry() != null) {
		sb.append(quote);	
		sb.append(person.getCountry());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // phone_number
	    if (person.getPhoneNumber() != null) {
		sb.append(quote);	
		sb.append(person.getPhoneNumber());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // web_role
	    if (person.getWebRole() != null) {
		sb.append(quote);	
		sb.append(person.getWebRole());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // active
	    if (person.isActive()) {
		sb.append(person.isActive());
	    } else {
		sb.append(nullString);
	    }
	    sb.append(comma);

	    // timestamp
	    if (person.getTimestamp() != null) {
		sb.append(quote);	
		sb.append(person.getTimestamp());
		sb.append(quote);	
	    } else {
		sb.append(nullString);
	    }

	    sb.append(");\n");    
	}

    }


    private void getOwner(StringBuffer sb) {

    }



}