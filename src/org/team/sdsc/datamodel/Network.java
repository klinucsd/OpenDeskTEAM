
package org.team.sdsc.datamodel;

import java.util.*;
import java.text.*;
import java.io.*;
import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.validator.*;
import org.team.sdsc.datamodel.annotation.*;
import au.com.bytecode.opencsv.*;


/**
 * Represents a TEAM Network.
 *
 * @author Kai Lin
 */
public class Network {

	    
    /**
     * a query session
     **/
    private Session session;	
    private Transaction transaction;

    static protected List<Site> activeSites;
    static protected List<Institution> leaderInstitutions;
    static protected List<WeatherStationStub> weatherStationStubs;

    /**
     * Retrieve all sites.
     *
     * @return a list of {@link Site}s.
     */
    public List<Site> getSites() {
	List<Site> sites = new ArrayList<Site>();
	Query query = session.getNamedQuery("org.team.sdsc.datamodel.AllSites");
	sites = query.list();
	return sites;
    }   


    /**
     * get all active sites 
     *
     * @return a list of active {@link Site}s.
     **/
    public List<Site> getActiveSites() {
	
	List<Site> activeSites = new ArrayList<Site>();
	Query query = session.getNamedQuery("org.team.sdsc.datamodel.siteByStatus");
	query.setText("status", "active");
	activeSites  = query.list();

	query = session.getNamedQuery("org.team.sdsc.datamodel.AllCountries");
	List<Country> countries  = query.list();
        
	for (Site site : activeSites) {
	    //Integer aInt = site.getCountryId();

	    Integer aInt = site.getCountry().getId();

	    if (aInt != null) {
		for (Country country : countries) {
		    if (country.getId().intValue() == aInt.intValue()) {
			site.setCountry(country);
			break;
		    }
		}
	    }
	}

    	return activeSites;
    }
    

    /**
     * Retrieve all sites with the status.
     *
     * @param status a status.
     * @return a list of {@link Site}s.
     */
    public List<Site> getSiteByStatus(String status) {
        Query query = session.getNamedQuery("org.team.sdsc.datamodel.siteByStatus");
        query.setText("status", status);
        List<Site> sites = query.list();
        return sites;
    }   
    
    
    /**
     * Retrieve all sites with id.
     *
     * @param id a site id.
     * @return a list of {@link Site}s.
     */
    public Site getSiteById(int id) {
    	Site aSite = null;
        Query query = session.getNamedQuery("org.team.sdsc.datamodel.siteById");
        query.setInteger("id", id);
        List<Site> sites = query.list();
        if (!sites.isEmpty()) {
	    aSite = sites.get(0);
        }
        
        return aSite;
    }   
 
    /*
    public Site getSiteByShortName(String name) {
    	Site aSite = null;
        Query query = session.createQuery("From Site AS site WHERE site.shortName='"+name+"'");
        List<Site> sites = query.list();
        if (!sites.isEmpty()) {
	    aSite = sites.get(0);
        }
        
        return aSite;
    } 
    */  
  

    /**
     * Retrieve all persons.
     *
     * @return a list of {@link Person}s.
     */
    public List<Person> getPersons() {
        Query query = session.getNamedQuery("org.team.sdsc.datamodel.AllPersons");
        List<Person> persons = query.list();
        return persons;
    }   


    /**
     * Retrieve a person .
     *
     * @param id the id of a person
     * @return a {@link Person}.
     */
    public Person getPersonById(Integer id) {
    
    	Person aPerson = null;

	Query query = session.getNamedQuery("org.team.sdsc.datamodel.PersonById");
	query.setInteger("id", id);
	List list = query.list();
	if (list.size() > 0) {
	    aPerson = (Person)list.get(0);
	} 

        return aPerson;
    }   


    /**
     * Retrieve a person by sername .
     *
     * @param username the username of a person
     * @return a {@link Person}.
     */
    public Person getPersonByUsername(String username) {
    	Person aPerson = null;
    	Query query = session.getNamedQuery("org.team.sdsc.datamodel.PersonByUsername");
        query.setText("username", username);
        List list = query.list();
        if (list.size() > 0) {
	    aPerson = (Person)list.get(0);
        } 
        return aPerson;
    }   




    public List<Person> getPersons(Site site, Protocol protocol) {
	List<Person> persons = new ArrayList<Person>();
	for (Person person : getPersons()) {
	    for (Site aSite : person.getSites()) {
		if (aSite.getId().intValue() == site.getId().intValue()) {
		    boolean added = false;
		    boolean siteHasProtocol = false;		    
		    for (Protocol aProtocol : aSite.getProtocols()) {
			if (aProtocol.getName().equals(protocol.getName())) {
			    siteHasProtocol = true;
			    break;
			}
		    }
		    if (siteHasProtocol) {
			for (ProtocolFamily aProtocolFamily : person.getProtocolFamilies()) {
			    if (aProtocolFamily.getName().equals(protocol.getName())) {
				persons.add(person);
				added = true;
				break;
			    }
			}
		    }
		    break;
		}
	    }
	}

	return persons;
    }



    /**
     * Retrieve all protocols.
     *
     * @return a list of {@link Protocol}s.
     */
    public List<Protocol> getProtocols() {
        Query query = session.getNamedQuery("org.team.sdsc.datamodel.AllActiveProtocols");
        List<Protocol> protocols = query.list();
        return protocols;
    }   


    public ProtocolFamily getProtocolFamilyByName(String name) {
        Query query = session.createQuery("FROM ProtocolFamily as family WHERE family.name='"+name+"'");
        List records = query.list();
	return (ProtocolFamily)records.iterator().next();
    }



    /**
     * Retrieve a protocol.
     *
     * @return a {@link Protocol}.
     */
    public Protocol getProtocol(String name, double version) {
    	Query query = session.getNamedQuery("org.team.sdsc.datamodel.ProtocolByNameVersion");
        query.setText("name", name);
        query.setDouble("version", version);
        List protocols = query.list();
        if (protocols.size() > 0) {
	    return (Protocol)protocols.get(0);
        } else {
	    return null;
        }
    }   


    public Protocol getProtocol(int id) {
	Query query = session.getNamedQuery("org.team.sdsc.datamodel.ProtocolById");
        query.setInteger("id", id);
        List protocols = query.list();
        if (protocols.size() > 0) {
            return (Protocol)protocols.get(0);
        } else {
            return null;
        }
    }


    /**
     * Retrieve all leadships.
     *
     * @return a list of {@link LeadShip}s.
     */
    public List<LeadShip> getLeadShips() {
        Query query = session.getNamedQuery("org.team.sdsc.datamodel.AllLeadShips");
        List<LeadShip> leadships = query.list();
        return leadships;
    }   
 
 
    /**
     * Retrieve leadships for a protocol at a site.
     *
     * @return a list of {@link LeadShip}s.
     */
    public List<LeadShip> getLeaderships(Site site, Protocol protocol) {
        Query query = session.getNamedQuery("org.team.sdsc.datamodel.LeadShipsForProtocolAtSite");
        query.setInteger("siteId", site.getId());
        query.setInteger("protocolId", protocol.getId());
        List<LeadShip> leadships = query.list();
        return leadships;
    }   
 

    /**
     * Retrieve a map from person to a leadership.
     *
     * @return a Map from person to a leadership.
     */
    public Map<Person, LeadShip> getLeaderMap(Site site, Protocol protocol) {
    	HashMap<Person, LeadShip> map = new HashMap<Person, LeadShip>();
        List<LeadShip> leadships = getLeaderships(site, protocol);
        
        for (LeadShip leadship : leadships) {
	    Person person = getPerson(leadship);
	    map.put(person, leadship);
        }
        
        return map;
    }   




 
    /**
     * Retrieve all active protocols for the person.
     *
     * @param person a person in the network
     * @return a list of {@link Protocol}s.
     */   
    public Set<Protocol> getActiveProtocols(Person person) {
        HashSet<Protocol> result = new HashSet<Protocol>();
    
        List protocols = session.getNamedQuery("org.team.sdsc.datamodel.AllActiveProtocols").list();
      	Set<ProtocolFamily> protocolFamilies = person.getProtocolFamilies();
      
        for (ListIterator iter = protocols.listIterator(); iter.hasNext() ; ) {
            Protocol aProtocol = (Protocol)iter.next();
            for (Iterator i=protocolFamilies.iterator(); i.hasNext(); ) {
            	ProtocolFamily aProtocolFamily = (ProtocolFamily)i.next();
            	if (aProtocolFamily.getName().equals(aProtocol.getName())) {
		    result.add(aProtocol);
            	}
            }
        }
        return result;
    }
    
 
    /**
     * Retrieve the person of a leadship.
     *
     * @param leadship a leadship in the network
     * @return a {@link Person}.
     */   
    public Person getPerson(LeadShip leadship) { 	
	return getPersonById(leadship.getId().getPersonId());	
    }
 
  
    private String getDataName(Protocol protocol) {
	String dataName = "";
	if (protocol.getName().equals("Climate")) {
	    dataName = "Climate";
	} else if (protocol.getName().equals("Avian")) {
	    dataName = "Avian";
	} else if (protocol.getName().equals("Butterfly")) {
	    dataName = "Butterfly";
	} else if (protocol.getName().equals("Primate")) {
	    dataName = "Primate";
	} else if (protocol.getName().equals("Vegetation - Litterfall")) {
	    dataName = "Litterfall";
	} 			  	
	return dataName;
    }
 
 

    public Object getRecord(Protocol protocol, int id) {
	String dataName = getDataName(protocol);
        Query query = session.createQuery("FROM "+dataName+"Record as record WHERE record.id="+id);
        List records = query.list();
	return records.iterator().next();
    }


    public Object getTreeRecord(int id) {
        Query query = session.createQuery("FROM TreeRecord as record WHERE record.id="+id);
        List records = query.list();
	return records.iterator().next();
    }


    public Object getLianaRecord(int id) {
        Query query = session.createQuery("FROM LianaRecord as record WHERE record.id="+id);
        List records = query.list();
	return records.iterator().next();
    }



    public void saveOrUpdateObject(Object object) throws Exception {
        Transaction tx = transaction;
        try {
            session.saveOrUpdate(object);
        }  catch (Exception e) {
            if (tx!=null) tx.rollback();
            close();
            throw e;
        }
    }


    public void saveObject(Object object) throws Exception {
        Transaction tx = transaction;
        try {
            session.save(object);
        }  catch (Exception e) {
            if (tx!=null) tx.rollback();
            close();
            throw e;
        }
    }


    public void deleteObject(Object object) throws Exception {
        Transaction tx = transaction;
        try {
            session.delete(object);
        }  catch (Exception e) {
            if (tx!=null) tx.rollback();
            close();
            throw e;
        }
    }


    public void saveSite(Site object) throws Exception {
        Transaction tx = transaction;
        try {
            session.save(object);
        }  catch (Exception e) {
            if (tx!=null) tx.rollback();
            close();
            throw e;
        }
    }


    public void deleteSite(Site site) throws Exception {
        Transaction tx = transaction;
        try {
            session.delete(site);
        }  catch (Exception e) {
            if (tx!=null) tx.rollback();
            close();
            throw e;
        }
    }


    public void saveOrUpdate(Protocol protocol, Object object) throws Exception {
	Transaction tx = transaction;
	try {
	    if (protocol.getName().equals("Butterfly")) {
		session.update((ButterflyRecord)object);
	    } else if (protocol.getName().equals("Avian")) {
		session.update((AvianRecord)object);
	    } else if (protocol.getName().equals("Climate")) {
		session.update((ClimateRecord)object);
	    } else if (protocol.getName().equals("Primate")) {
		session.update((PrimateRecord)object);
	    } else if (protocol.getName().equals("Vegetation - Litterfall")) {
		session.update((LitterfallRecord)object);
	    } 
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void saveOrUpdate(Protocol protocol, Object object, DataEditLog log) throws Exception {

	Transaction tx = transaction;
	try {
	    if (protocol.getName().equals("Butterfly")) {
		session.update((ButterflyRecord)object);
		session.save(log);
	    } else if (protocol.getName().equals("Avian")) {
		session.update((AvianRecord)object);
		session.save(log);
	    } else if (protocol.getName().equals("Climate")) {
		session.update((ClimateRecord)object);
		session.save(log);
	    } else if (protocol.getName().equals("Primate")) {
		session.update((PrimateRecord)object);
		session.save(log);
	    } else if (protocol.getName().equals("Vegetation - Litterfall")) {
		session.update((LitterfallRecord)object);
		session.save(log);
	    }
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
        }
    }



    public void saveOrUpdate(SamplingUnit unit) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(unit);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void save(CameraTrapPhotoCollection collection) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(collection);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void save(CameraTrapUploadJob job) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(job);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void update(CameraTrapUploadJob job) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(job);
            session.flush();
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void update(Camera job) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(job);
            session.flush();
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void update(Person job) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(job);
            session.flush();
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void updateSite(Site site) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(site);
            session.flush();
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void save(CameraTrapPhoto photo) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(photo);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void save(Camera camera) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(camera);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void save(Person person) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(person);
	}  catch (Exception e) {
	    e.printStackTrace();
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }




    public void save(CameraTrapPhotoType type) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(type);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void save(CameraTrapPhotoMetadata metadata) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(metadata);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void saveOrUpdate(Block block) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(block);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }




    public void saveOrUpdate(SamplingUnit unit, SpatialEditLog log) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(unit);
	    session.save(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void saveOrUpdate(SpatialEditLog log) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void saveOrUpdate(DataUploadLog log) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void saveOrUpdate(Block block, BlockEditLog log) throws Exception {
	Transaction tx = transaction;
	try {
	    session.update(block);
	    session.save(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public List<DataEditLog> getDataEditLog(String siteId, String blockName, String protocolName, String event, 
					     int start, int length, String sort, String direction) {
	Query query = session.createQuery("FROM DataEditLog as record WHERE record.site="+siteId+" AND "+
					  " record.block='"+blockName+"' AND "+
					  " record.protocol='"+protocolName+"' AND "+
					  " record.event='"+event+"' "+
					  "ORDER BY "+sort+" "+direction);
	query.setFirstResult(start);
	query.setMaxResults(length);
	List records = query.list(); 	
	return (List<DataEditLog>)records;    
    }




    public int getDataEditLogTotalSize(String siteId, String blockName, String protocolName, String event) {
	Query query = session.createQuery("SELECT count(*) FROM DataEditLog as record WHERE record.site="+siteId+" AND "+
					  " record.block='"+blockName+"' AND "+
					  " record.protocol='"+protocolName+"' AND "+
					  " record.event='"+event+"' ");
	List records = query.list(); 	
	return ((Long)records.iterator().next()).intValue(); 

    }


    public List<SpatialEditLog> getSpatialEditLog(String siteId, String blockName, String protocolName, 
						  int start, int length, String sort, String direction) {
	Query query;
	if (blockName == null) {
	    query = session.createQuery("FROM SpatialEditLog as record WHERE record.site="+siteId+" AND "+
					" record.protocol='"+protocolName+"' "+
					"ORDER BY "+sort+" "+direction);
	} else {
	    query = session.createQuery("FROM SpatialEditLog as record WHERE record.site="+siteId+" AND "+
					" record.block='"+blockName+"' AND "+
					" record.protocol='"+protocolName+"' "+
					"ORDER BY "+sort+" "+direction);
	}
	query.setFirstResult(start);
	query.setMaxResults(length);
	List records = query.list(); 	
	return (List<SpatialEditLog>)records;    

    }



    public List<SpatialUploadLog> getSpatialUploadLog(Person person) {

        Set<Site> sites = person.getSites();
	Set<ProtocolFamily> protocolFamilies = person.getProtocolFamilies();

	String cond1 = null;
	for (Site site : sites) {
	    if (cond1 == null) {
		cond1 = "record.siteId="+site.getId();
	    } else {
		cond1 += " OR record.siteId="+site.getId();
	    }
	}

	String cond2 = null;
	for (ProtocolFamily pf : protocolFamilies) {
	    if (cond2 == null) {
		cond2 = "record.protocol='"+pf.getName()+"'";
	    } else {
		cond2 += " OR record.protocol='"+pf.getName()+"'";
	    }
	}
	
	if (cond2 != null) {
	    cond1 = " ("+cond1+") AND ("+cond2+") ";
	}
	
	String queryStr;
	if (cond1 == null) {
	    return new ArrayList<SpatialUploadLog>();
	} else {
	    queryStr = 
		"FROM SpatialUploadLog as record "+
		"WHERE "+cond1+
		"ORDER BY record.proposeUploadTime, record.gpxUploadTime DESC ";

	    Query query = session.createQuery(queryStr);
	    List records = query.list(); 	
	    return (List<SpatialUploadLog>)records;    

	}

    }


    public List<SpatialUploadLog> getSpatialUploadLog() {
	Query query = session.createQuery("FROM SpatialUploadLog as record "+
					  "ORDER BY record.proposeUploadTime, record.gpxUploadTime DESC ");
	List records = query.list(); 	
	return (List<SpatialUploadLog>)records;    
    }


    public List<DataUploadLog> getDataUploadLog() {
	Query query = session.createQuery("FROM DataUploadLog as record ORDER BY record.id DESC ");
	List records = query.list(); 	
	return (List<DataUploadLog>)records;    
    }



    public List<DataUploadLog> getDataUploadLog(Person person) {

        Set<Site> sites = person.getSites();
	Set<ProtocolFamily> protocolFamilies = person.getProtocolFamilies();

	String cond1 = null;
	for (Site site : sites) {
	    if (cond1 == null) {
		cond1 = "record.siteId="+site.getId();
	    } else {
		cond1 += " OR record.siteId="+site.getId();
	    }
	}

	String cond2 = null;
	for (ProtocolFamily pf : protocolFamilies) {
	    if (cond2 == null) {
		cond2 = "record.protocol='"+pf.getName()+"'";
	    } else {
		cond2 += " OR record.protocol='"+pf.getName()+"'";
	    }
	}
	
	if (cond2 != null) {
	    cond1 = " ("+cond1+") AND ("+cond2+") ";
	}
	
	String queryStr;
	if (cond1 == null) {
	    return new ArrayList<DataUploadLog>();
	} else {
	    queryStr = 
		"FROM DataUploadLog as record "+
		"WHERE "+cond1+
		"ORDER BY record.id DESC ";
	    Query query = session.createQuery(queryStr);
	    List records = query.list(); 	
	    return (List<DataUploadLog>)records;    
	}

    }



    public DataUploadLog getDataUploadLog(String excelId) {
	Query query = session.createQuery("FROM DataUploadLog as record WHERE record.excelId="+excelId);
	List records = query.list(); 	
	if (!records.isEmpty()) {
	    return (DataUploadLog)records.iterator().next();
	}
	return null;
    }




    public DataEditLog getDataEditLogById(String id) {
	Query query = session.createQuery("FROM DataEditLog as record WHERE record.id="+id);
	List records = query.list(); 	
	if (!records.isEmpty()) {
	    return (DataEditLog)records.iterator().next();
	}
	return null;
    }


    public SpatialEditLog getSpatialEditLogById(String id) {
	Query query = session.createQuery("FROM SpatialEditLog as record WHERE record.id="+id);
	List records = query.list(); 	
	if (!records.isEmpty()) {
	    return (SpatialEditLog)records.iterator().next();
	}
	return null;
    }


    public BlockEditLog getBlockEditLogById(String id) {
	Query query = session.createQuery("FROM BlockEditLog as record WHERE record.id="+id);
	List records = query.list(); 	
	if (!records.isEmpty()) {
	    return (BlockEditLog)records.iterator().next();
	}
	return null;
    }


    public List<BlockEditLog> getBlockEditLog(String siteId, int start, int length, String sort, String direction) {
	Query query = session.createQuery("FROM BlockEditLog as record WHERE record.site="+siteId+" ORDER BY "+sort+" "+direction);
	query.setFirstResult(start);
	query.setMaxResults(length);
	List records = query.list(); 	
	return (List<BlockEditLog>)records;    

    }


    private String getCurrentValue(DataEditLog log) {
	Query query = session.createQuery("SELECT newValue FROM DataEditLog as record WHERE record.dataId='"+log.getDataId()+"' AND "+
					  " record.columnName='"+log.getColumn()+"' "+
					  "ORDER BY date DESC");
	List list = query.list();
	if (!list.isEmpty()) {
	    return (String)list.iterator().next();
	}
	return null;
    }


    private String getCurrentValue(SpatialEditLog log) {
	Query query = session.createQuery("SELECT newValue FROM SpatialEditLog as record WHERE record.dataId='"+log.getDataId()+"' AND "+
					  " record.columnName='"+log.getColumn()+"' "+
					  "ORDER BY date DESC");
	List list = query.list();
	if (!list.isEmpty()) {
	    return (String)list.iterator().next();
	}
	return null;
    }



    private String getCurrentValue(BlockEditLog log) {
	Query query = session.createQuery("SELECT newValue FROM BlockEditLog as record WHERE record.blockId='"+log.getBlockId()+"' AND "+
					  " record.columnName='"+log.getColumn()+"' "+
					  "ORDER BY date DESC");
	List list = query.list();
	if (!list.isEmpty()) {
	    return (String)list.iterator().next();
	}
	return null;
    }



    public void recoverOldValue(String username, String id) throws Exception {

	DataEditLog log = getDataEditLogById(id);
	String column = log.getColumn();
	String value = log.getOldValue();
        String currentValue = getCurrentValue(log);

	DataEditLog newlog = new DataEditLog(username, log.getSite(), log.getProtocol(), log.getBlock(), 
					     log.getEvent(), log.getDataId(), log.getColumn(), log.getOldValue(), currentValue);

	String protocolName = log.getProtocol();
	if (protocolName.equals("Vegetation - Trees")) {
	    TreeRecord record = (TreeRecord)getTreeRecord(Integer.parseInt(log.getDataId()));
	    record.setValue(column, value, username);
	    saveOrUpdateTree(record, newlog);
	} else if (protocolName.equals("Vegetation - Lianas")) {
	    LianaRecord record = (LianaRecord)getTreeRecord(Integer.parseInt(log.getDataId()));
	    record.setValue(column, value, username);
	    saveOrUpdateLiana(record, newlog);
	} else {
	    Site aSite = getSiteById(log.getSite().intValue());
	    Protocol aProtocol = aSite.getProtocol(protocolName);

	    if (aProtocol.getName().equals("Butterfly")) {
		ButterflyRecord record = (ButterflyRecord)getRecord(aProtocol, Integer.parseInt(log.getDataId()));
		record.setValue(column, value, username);
		saveOrUpdate(aProtocol, record, newlog);
	    } else if (aProtocol.getName().equals("Avian")) {
		AvianRecord record = (AvianRecord)getRecord(aProtocol, Integer.parseInt(log.getDataId()));
		record.setValue(column, value, username);
		saveOrUpdate(aProtocol, record, newlog);
	    } else if (aProtocol.getName().equals("Primate")) {
		PrimateRecord record = (PrimateRecord)getRecord(aProtocol, Integer.parseInt(log.getDataId()));
		record.setValue(column, value, username);
		saveOrUpdate(aProtocol, record, newlog);
	    } else if (aProtocol.getName().equals("Climate")) {
		ClimateRecord record = (ClimateRecord)getRecord(aProtocol, Integer.parseInt(log.getDataId()));
		record.setValue(column, value, username);
		saveOrUpdate(aProtocol, record, newlog);
	    } else if (aProtocol.getName().equals("Vegetation - Litterfall")) {
		LitterfallRecord record = (LitterfallRecord)getRecord(aProtocol, Integer.parseInt(log.getDataId()));
		record.setValue(column, value, username);
		saveOrUpdate(aProtocol, record, newlog);
	    } 
	}
    }

    
    public void recoverOldSpatialValue(String username, String id) throws Exception {
	SpatialEditLog log = getSpatialEditLogById(id);
	String column = log.getColumn();
	String value = log.getOldValue();
        String currentValue = getCurrentValue(log);
	SpatialEditLog newlog = new SpatialEditLog(username, log.getSite(), log.getProtocol(), log.getBlock(), log.getDataId(), 
						   log.getColumn(), log.getColumnHead(),  log.getOldValue(), currentValue);	
	SamplingUnit unit = getSamplingUnitById(Integer.parseInt(log.getDataId()));
	unit.setValue(column, value);
	saveOrUpdate(unit, newlog);
    }


    
    public void recoverOldBlockValue(String username, String id) throws Exception {
	BlockEditLog log = getBlockEditLogById(id);
	String column = log.getColumn();
	String value = log.getOldValue();
        String currentValue = getCurrentValue(log);
	BlockEditLog newlog = new BlockEditLog(username, log.getSite(), log.getBlockId(), log.getBlockName(), 
					       log.getColumn(), log.getColumnHead(),  log.getOldValue(), currentValue);	
	Block block = getBlockById(Integer.parseInt(log.getBlockId()));
	block.setValue(column, value);
	saveOrUpdate(block, newlog);
    }


    public void saveOrUpdate(DataEditLog log) throws Exception {
	Transaction tx = transaction;
	try {
	    session.save(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void saveOrUpdateTree(Object object) throws Exception {
	Transaction tx = transaction;
	try {
	    session.saveOrUpdate((TreeRecord)object);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    public void saveOrUpdateTree(Object object, DataEditLog log) throws Exception {
	Transaction tx = transaction;
	try {
	    session.saveOrUpdate((TreeRecord)object);
	    session.saveOrUpdate(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void saveOrUpdateLiana(Object object) throws Exception {
	Transaction tx = transaction;
	try {
	    session.saveOrUpdate((LianaRecord)object);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }


    public void saveOrUpdateLiana(Object object, DataEditLog log) throws Exception {
	Transaction tx = transaction;
	try {
	    session.saveOrUpdate((LianaRecord)object);
	    session.saveOrUpdate(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    close();
	    throw e;
	}
    }



    /**
     * Get data at a site for a protocol 
     *
     */
    public List getData(Site site, Protocol protocol, int start, int length)  {
	List records = new ArrayList();
	String dataName = getDataName(protocol);	 
	Query query = session.createQuery("FROM "+dataName+"Record as record WHERE record.collectionship.samplingUnit.site.id="+
					  site.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
    
     
    /**
     * Get data at sites for a protocol
     *
     */
    public List getData(List<Site> sites, Protocol protocol, int start, int length)  {
	List records = new ArrayList();
	String dataName = getDataName(protocol);	
	String hql = "FROM "+dataName+"Record as record WHERE ";
	boolean first = true;
	for (Site site : sites) {
	    if (first) {
		first = false;
	    } else {
		hql += " OR ";
	    }
	    hql += "record.collectionship.samplingUnit.site.id="+site.getId()+" ";
	}
	Query query = session.createQuery(hql);
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
 		  
	return records;    
    }
    
    
    /**
     * Get data at a block for a protocol
     *
     */ 	
    public List getData(Block block, Protocol protocol, int start, int length) {
	List records = new ArrayList();
	String dataName = getDataName(protocol);
	Query query = session.createQuery("FROM "+dataName+"Record as record WHERE record.collectionship.samplingUnit.block.id="+
					  block.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 		 	 
	return records;
    }



    
    /**
     * Get data at a block for a protocol and an event
     *
     */ 	
    public List getData(Block block, Protocol protocol, String event, int start, int length, String sortPath, boolean ascendent) {
	List records = new ArrayList();
	String dataName = getDataName(protocol);
	Query query = session.createQuery("FROM "+dataName+"Record as record "+
					  "WHERE record.collectionship.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' "+
					  "ORDER BY record."+sortPath+" "+(ascendent ? "ASC" : "DESC" ));
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 		 	 
	return records;
    }


    
    /**
     * Get tree data at a block for an event
     *
     */ 	
    public List getTreeData(Block block, String event, int start, int length, String sortPath, boolean ascendent) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM TreeRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' "+
					  "ORDER BY record."+sortPath+" "+(ascendent ? "ASC" : "DESC" ));
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 		 	 
	return records;
    }




    public List<TreeRecord> getTreeData(Block block, String event) {
	List<TreeRecord> records = new ArrayList();
	Query query = session.createQuery("FROM TreeRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' ");
	records = query.list();   
	return records;
    }


    public List<TreeRecord> getTreeData(Site site, String event) {
	List<TreeRecord> records = new ArrayList();
	Query query = session.createQuery("FROM TreeRecord as record "+
					  "WHERE record.samplingUnit.site.id="+site.getId() +" "+
					  "AND record.event='"+event+"' ");
	records = query.list();   
	return records;
    }


    public List<LianaRecord> getLianaData(Site site, String event) {
	List<LianaRecord> records = new ArrayList();
	Query query = session.createQuery("FROM LianaRecord as record "+
					  "WHERE record.samplingUnit.site.id="+site.getId() +" "+
					  "AND record.event='"+event+"' ");
	records = query.list();   
	return records;
    }


    public List<TreeRecord> getTreeData(TreePoint tree) {
	List<TreeRecord> records = new ArrayList();
	Query query = session.createQuery("FROM TreeRecord as record "+
					  "WHERE record.samplingUnit.id="+tree.getId());
	records = query.list();   
	return records;
    }


    public List<LianaRecord> getLianaData(LianaPoint liana) {
	List<LianaRecord> records = new ArrayList();
	Query query = session.createQuery("FROM LianaRecord as record "+
					  "WHERE record.samplingUnit.id="+liana.getId());
	records = query.list();   
	return records;
    }



    public List<LianaRecord> getLianaData(Block block, String event) {
	List<LianaRecord> records = new ArrayList();
	Query query = session.createQuery("FROM LianaRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' ");
	records = query.list();   
	return records;
    }



    public List getTreeData(String event, Block block, int subplot, int start, int length, String sortPath, boolean ascendent) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM TreeRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId()+" "+
					    "  AND record.samplingUnit.subplotNumber="+subplot +" "+
					    "AND record.event='"+event+"' "+
					  "ORDER BY record."+sortPath+" "+(ascendent ? "ASC" : "DESC" ));
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list();   
	return records;
    }

    
    /**
     * Get liana data at a block for an event
     *
     */ 	
    public List getLianaData(Block block, String event, int start, int length, String sortPath, boolean ascendent) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM LianaRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' "+
					  "ORDER BY record."+sortPath+" "+(ascendent ? "ASC" : "DESC" ));
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 		 	 
	return records;
    }



    public List getLianaData(String event, Block block, int subplot, int start, int length, String sortPath, boolean ascendent) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM LianaRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId()+" "+
					    "  AND record.samplingUnit.subplotNumber="+subplot +" "+
					    "AND record.event='"+event+"' "+
					  "ORDER BY record."+sortPath+" "+(ascendent ? "ASC" : "DESC" ));
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list();   
	return records;
    }


    /**
     * Get data at a sampling unit for a protocol and an event
     *
     */ 	
    public List getData(WeatherStation unit, Protocol protocol, String event, int start, int length, String sortPath, boolean ascendent) {
	List records = new ArrayList();
	String dataName = getDataName(protocol);
	Query query = session.createQuery("FROM "+dataName+"Record as record "+
					  "WHERE record.collectionship.samplingUnit.id="+unit.getId() +" "+
					  "AND year(record.collectedAt) || '.' || month(record.collectedAt) ='"+event+"' "+
					  "ORDER BY record."+sortPath+" "+(ascendent ? "ASC" : "DESC" ));
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 		 	 
	return records;
    }



    public int getTotalSize(Block block, Protocol protocol, String event) {
	String dataName = getDataName(protocol);
	Query query = session.createQuery("SELECT count(id) FROM "+dataName+"Record as record "+
					  "WHERE record.collectionship.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' ");
	List records = query.list(); 		 	 
	return ((Long)records.iterator().next()).intValue();
    }



    public int getTreeTotalSize(Block block, String event) {
	Query query = session.createQuery("SELECT count(id) FROM TreeRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' ");
	List records = query.list(); 		 	 
	return ((Long)records.iterator().next()).intValue();
    }


    public int getTreeTotalSize(String event, Block block, int subplot) {
	Query query = session.createQuery("SELECT count(id) FROM TreeRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					    "AND record.event='"+event+"' "+
					  "AND record.samplingUnit.subplotNumber="+subplot);
	List records = query.list();   
	return ((Long)records.iterator().next()).intValue();
    }


    public int getLianaTotalSize(Block block, String event) {
	Query query = session.createQuery("SELECT count(id) FROM LianaRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					  "AND record.event='"+event+"' ");
	List records = query.list(); 		 	 
	return ((Long)records.iterator().next()).intValue();
    }



    public int getLianaTotalSize(String event, Block block, int subplot) {
	Query query = session.createQuery("SELECT count(id) FROM LianaRecord as record "+
					  "WHERE record.samplingUnit.block.id="+block.getId() +" "+
					    "AND record.event='"+event+"' "+
					  "AND record.samplingUnit.subplotNumber="+subplot);
	List records = query.list();   
	return ((Long)records.iterator().next()).intValue();
    }


    public int getTotalSize(SamplingUnit unit, Protocol protocol, String event) {
	String dataName = getDataName(protocol);
	Query query = session.createQuery("SELECT count(id) FROM "+dataName+"Record as record "+
					  "WHERE record.collectionship.samplingUnit.id="+unit.getId() +" "+
					  "AND year(record.collectedAt) || '.' || month(record.collectedAt) ='"+event+"' ");
	List records = query.list(); 		 	 
	return ((Long)records.iterator().next()).intValue();
    }


 	
    /**
     * Get data at a sampling unit for a protocol
     *
     */ 	
    public List getData(SamplingUnit unit, Protocol protocol, int start, int length) {
	List records = new ArrayList();
	String dataName = getDataName(protocol);
	Query query = session.createQuery("FROM "+dataName+"Record as record WHERE record.collectionship.samplingUnit.id="+
					  unit.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list();
	return records;
    }


    /**
     * Get tree data at a site  
     *
     */
    public List getTreeData(Site site, int start, int length)  {
	List records = new ArrayList();
 		 
	// retrieve tree data
	Query query = session.createQuery("FROM TreeRecord as record WHERE record.samplingUnit.site.id="+site.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
    
    
    /**
     * Get tree data at a block  
     *
     */
    public List getTreeData(Block block, int start, int length)  {
	List records = new ArrayList();
 		 
	// retrieve tree data
	Query query = session.createQuery(
					  "FROM TreeRecord as record WHERE record.samplingUnit.block.id="+block.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
    
    
    /**
     * Get tree data at a unit  
     *
     */
    public List getTreeData(SamplingUnit unit, int start, int length)  {
	List records = new ArrayList();
 		 
	// retrieve tree data
	Query query = session.createQuery("FROM TreeRecord as record WHERE record.samplingUnit.id="+unit.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
     
    
    /**
     * Get liana data at a site  
     *
     */
    public List getLianaData(Site site, int start, int length)  {
	List records = new ArrayList();
 		 
	// retrieve tree data
	Query query = session.createQuery("FROM LianaRecord as record WHERE record.samplingUnit.site.id="+site.getId()+" order by record.collectedAt");
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
    
    /**
     * Get liana data at a block  
     *
     */
    public List getLianaData(Block block, int start, int length)  {
	List records = new ArrayList();
 		 
	// retrieve tree data
	Query query = session.createQuery("FROM LianaRecord as record WHERE record.samplingUnit.block.id="+block.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
    
    
    /**
     * Get liana data at a unit 
     *
     */
    public List getLianaData(SamplingUnit unit, int start, int length)  {
	List records = new ArrayList();
 		 
	// retrieve tree data
	Query query = session.createQuery("FROM LianaRecord as record WHERE record.samplingUnit.id="+unit.getId());
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
     
  
    public DataIterator getData(List<Site> sites, Protocol protocol)  {
	String dataName = getDataName(protocol);	
	String hql = "FROM "+dataName+"Record as record WHERE ";
	boolean first = true;
	for (Site site : sites) {
	    if (first) {
		first = false;
	    } else {
		hql += " OR ";
	    }
	    hql += "record.collectionship.samplingUnit.site.id="+site.getId()+" ";
	}
	Query query = session.createQuery(hql);
	return new DataIterator(this, query);    
    }  
  

    public DataIterator getData(Site site, Protocol protocol) {
	String dataName = getDataName(protocol);	 
	Query query = session.createQuery("FROM "+dataName+"Record as record WHERE record.collectionship.samplingUnit.site.id="+
					  site.getId());
	return new DataIterator(this, query); 
    }
	
	
    public DataIterator getData(Block block, Protocol protocol) {
	String dataName = getDataName(protocol);	 
	Query query = session.createQuery("FROM "+dataName+"Record as record WHERE record.collectionship.samplingUnit.block.id="+
					  block.getId());
	return new DataIterator(this, query); 
    }
	
		
    public DataIterator getData(SamplingUnit unit, Protocol protocol) {
	String dataName = getDataName(protocol);	 
	Query query = session.createQuery("FROM "+dataName+"Record as record WHERE record.collectionship.samplingUnit.id="+
					  unit.getId());
	return new DataIterator(this, query); 
    }
  
  
    public DataIterator getTreeData(Site site) {
	Query query = session.createQuery("FROM TreeRecord as record WHERE record.samplingUnit.site.id="+site.getId());
	return new DataIterator(this, query); 
    }
	
	
    public DataIterator getTreeData(Block block) {
	Query query = session.createQuery(
					  "FROM TreeRecord as record WHERE record.samplingUnit.block.id="+block.getId());		
	return new DataIterator(this, query); 
    }
	
	
    public DataIterator getTreeData(SamplingUnit unit) {
	Query query = session.createQuery("FROM TreeRecord as record WHERE record.samplingUnit.id="+unit.getId());
	return new DataIterator(this, query); 
    }	
	
	
    public DataIterator getLianaData(Site site) {
	Query query = session.createQuery("FROM LianaRecord as record WHERE record.samplingUnit.site.id="+site.getId());
	return new DataIterator(this, query); 
    }
	
	
    public DataIterator getLianaData(Block block) {
	Query query = session.createQuery("FROM LianaRecord as record WHERE record.samplingUnit.block.id="+block.getId());		
	return new DataIterator(this, query); 
    }
	
	
    public DataIterator getLianaData(SamplingUnit unit) {
	Query query = session.createQuery("FROM LianaRecord as record WHERE record.samplingUnit.id="+unit.getId());
	return new DataIterator(this, query); 
    }	
	
	
    public DataIterator getDataIterator(String hql) {
	Query query = session.createQuery(hql);
	return new DataIterator(this, query);
    }



    public void createCSV(File file, Site site, Protocol protocol, DataIterator iter) throws Exception {	
	createCSV(file, site, protocol, iter, false, ',');
    }


    public void createCSV(File file, List<Site> sites, Protocol protocol,  char delimit) throws Exception {

	FileWriter fw = new FileWriter(file, true);
	CSVWriter out = new CSVWriter(fw, delimit);
	ColumnModel model = getColumnModel(protocol);
	String[] headers = model.getHeaders();
	out.writeNext(headers);
	out.flush();
	out.close();

	for (Site site : sites) {
	    createCSV(file, site, protocol, getData(site, protocol), true, delimit);
	}
    }




    public void createLianaCSV(File file, List<Site> sites, char delimit) throws Exception {

	FileWriter fw = new FileWriter(file, true);
	CSVWriter out = new CSVWriter(fw, delimit);
	ColumnModel model = getLianaColumnModel();
	String[] headers = model.getHeaders();
	out.writeNext(headers);
	out.flush();
	out.close();

	for (Site site : sites) {
	    createLianaCSV(file, site, getLianaData(site), true, delimit);
	}

    }



    public void createTreeCSV(File file, List<Site> sites, char delimit) throws Exception {

	FileWriter fw = new FileWriter(file, true);
	CSVWriter out = new CSVWriter(fw, delimit);
	ColumnModel model = getTreeColumnModel();
	String[] headers = model.getHeaders();
	out.writeNext(headers);
	out.flush();
	out.close();

	for (Site site : sites) {
	    createTreeCSV(file, site, getTreeData(site), true, delimit);
	}

    }



    public void createCSV(File file, 
			  Site site, 
			  Protocol protocol, 
			  DataIterator iter, 
			  boolean append, 
			  char delimit) throws Exception {	
       
	FileWriter fw = new FileWriter(file, append);
	CSVWriter out = new CSVWriter(fw, delimit);
	ColumnModel model = getColumnModel(protocol);
			
	if (!append) {
	    // write the header of the column model
	    String[] headers = model.getHeaders();
	    out.writeNext(headers);
	}
			
	Map<Person, LeadShip> leaderMap = getLeaderMap(site, protocol);
	Institution leaderInstitution = getInstitutions(site).iterator().next();
						
	while (iter.hasNext()) {
	    if (protocol.getName().equals("Avian")) {
		AvianRecord record = (AvianRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		BirdPoint birdPoint = (BirdPoint)record.getSamplingUnit();
		objects.add(birdPoint);
		objects.add(birdPoint.getSite());
		objects.add(birdPoint.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		String[] strings = model.format(objects);	
		out.writeNext(strings);
	    } else if (protocol.getName().equals("Climate")) {
		ClimateRecord record = (ClimateRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		objects.add(record.getSamplingUnit());
		objects.add(protocol);
		objects.add(site);
		objects.add(leaderInstitution);
		objects.add(record.getLeader(leaderMap));
		String[] strings = model.format(objects);
		out.writeNext(strings);
	    } else if (protocol.getName().equals("Butterfly")) {
		ButterflyRecord record = (ButterflyRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		ButterflyTrap trap = (ButterflyTrap)record.getSamplingUnit();
		objects.add(trap);
		objects.add(trap.getSite());
		objects.add(trap.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		String[] strings = model.format(objects);	
		out.writeNext(strings);				
	    } else if (protocol.getName().equals("Primate")) {
		PrimateRecord record = (PrimateRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		PrimatePoint trap = (PrimatePoint)record.getSamplingUnit();
		objects.add(trap);
		objects.add(trap.getSite());
		objects.add(trap.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		String[] strings = model.format(objects);	
		out.writeNext(strings);				
	    } else if (protocol.getName().equals("Vegetation - Litterfall")) {
		LitterfallRecord record = (LitterfallRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		LitterfallTrap trap = (LitterfallTrap)record.getSamplingUnit();
		objects.add(trap);
		objects.add(trap.getSite());
		objects.add(trap.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		String[] strings = model.format(objects);
		out.writeNext(strings);				
	    } 
	}		
	out.flush();
	out.close();		
    }
	

    public void createLianaCSV(File file, Site site, DataIterator iter) throws Exception {
	createLianaCSV(file, site, iter, true, ',');
    }


    public void createLianaCSV(File file, Site site, DataIterator iter, boolean append, char delimit) throws Exception {
	Protocol protocol = site.getProtocolById(Protocol.TREELIANA);
	FileWriter fw = new FileWriter(file, append);
	CSVWriter out = new CSVWriter(fw, delimit);
	ColumnModel model = getLianaColumnModel();
			
	// write the header of the column model
	String[] headers = model.getHeaders();
	out.writeNext(headers);
	/*
	  for (int i=0; i<headers.length; i++) {
	  System.out.println("    -----------------------------------"); 
	  System.out.println("    column = "+headers[i]);
	  }
	*/
		
	Map<Person, LeadShip> leaderMap = getLeaderMap(site, protocol);
	Institution leaderInstitution = getInstitutions(site).iterator().next();
				
	while (iter.hasNext()) {
	    LianaRecord record = (LianaRecord)iter.next();
	    List<Object> objects = new ArrayList<Object>();
							
	    objects.add(record);					
	    SamplingUnit unit = record.getSamplingUnit();
	    LianaPoint lianaPoint = (LianaPoint)unit;
	    objects.add(lianaPoint);
	    objects.add(site);
	    objects.add(lianaPoint.getBlock());		
	    objects.add(protocol);		
	    objects.add(leaderInstitution);
	    objects.add(getLeader(record, leaderMap));
			
	    String[] strings = model.format(objects);	
	    out.writeNext(strings);
	}		
	out.flush();
	out.close();		
    }
	


    public void createTreeCSV(File file, Site site, DataIterator iter) throws Exception {
	createTreeCSV(file, site, iter, true, ',');
    }


	
    public void createTreeCSV(File file, Site site, DataIterator iter, boolean append, char delimit) throws Exception {
	Protocol protocol = site.getProtocolById(Protocol.TREELIANA);
	FileWriter fw = new FileWriter(file, append);
	CSVWriter out = new CSVWriter(fw, delimit);
	ColumnModel model = getTreeColumnModel();
			
	// write the header of the column model
	String[] headers = model.getHeaders();		
	out.writeNext(headers);
		
	/*
	  System.out.println("    -----------------------------------"); 
	  for (int i=0; i<headers.length; i++) {
	  System.out.println("    column = "+headers[i]);
	  }
	*/
		
	Map<Person, LeadShip> leaderMap = getLeaderMap(site, protocol);
	Institution leaderInstitution = getInstitutions(site).iterator().next();
				
	while (iter.hasNext()) {
	    TreeRecord record = (TreeRecord)iter.next();
	    List<Object> objects = new ArrayList<Object>();
		
	    objects.add(record);
	    SamplingUnit unit = record.getSamplingUnit();
	    try {
	        TreePoint treePoint = (TreePoint)unit;
	        objects.add(treePoint);
	        objects.add(site);
	        objects.add(treePoint.getBlock());		
	        objects.add(protocol);		
	        objects.add(leaderInstitution);
	        objects.add(getLeader(record, leaderMap));
			
	        String[] strings = model.format(objects);	
	        out.writeNext(strings);
            } catch (Exception ex) {
		System.out.println("=====> "+unit);
	    }
	}		
	out.flush();
	out.close();		
    }





    public String createJSON(Site site, Protocol protocol, Iterator iter, int totalSize) throws Exception {	

	ColumnModel model = getJSONColumnModel(protocol);
	Map<Person, LeadShip> leaderMap = getLeaderMap(site, protocol);
	Institution leaderInstitution = null;

	List<Institution> institutions = getInstitutions(site);
	if (!institutions.isEmpty()) leaderInstitution = institutions.iterator().next();
	
	boolean first = true;
	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+totalSize+",\n");
	sb.append("result: [\n");

	while (iter.hasNext()) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    if (protocol.getName().equals("Avian")) {
		AvianRecord record = (AvianRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		BirdPoint birdPoint = (BirdPoint)record.getSamplingUnit();
		objects.add(birdPoint);
		objects.add(birdPoint.getSite());
		objects.add(birdPoint.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		objects.add(record.getReview());
		String string = model.formatJSON(objects);	
		sb.append(string+"\n");
	    } else if (protocol.getName().equals("Climate")) {
		ClimateRecord record = (ClimateRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		objects.add(record.getSamplingUnit());
		objects.add(protocol);
		objects.add(site);
		objects.add(leaderInstitution);
		objects.add(record.getLeader(leaderMap));
		objects.add(record.getReview());
		String string = model.formatJSON(objects);	
		sb.append(string+"\n");
	    } else if (protocol.getName().equals("Butterfly")) {
		ButterflyRecord record = (ButterflyRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		ButterflyTrap trap = (ButterflyTrap)record.getSamplingUnit();
		objects.add(trap);
		objects.add(trap.getSite());
		objects.add(trap.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		objects.add(record.getReview());
                String string = model.formatJSON(objects);
                sb.append(string+"\n");
	    } else if (protocol.getName().equals("Primate")) {
		PrimateRecord record = (PrimateRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		PrimatePoint trap = (PrimatePoint)record.getSamplingUnit();
		objects.add(trap);
		objects.add(trap.getSite());
		objects.add(trap.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		objects.add(record.getReview());
                String string = model.formatJSON(objects);
                sb.append(string+"\n");
	    } else if (protocol.getName().equals("Vegetation - Litterfall")) {
		LitterfallRecord record = (LitterfallRecord)iter.next();
		List<Object> objects = new ArrayList<Object>();
		objects.add(record);
		LitterfallTrap trap = (LitterfallTrap)record.getSamplingUnit();
		objects.add(trap);
		objects.add(trap.getSite());
		objects.add(trap.getBlock());		
		objects.add(protocol);		
		objects.add(leaderInstitution);
		objects.add(getLeader(record, leaderMap));
		objects.add(record.getReview());
                String string = model.formatJSON(objects);
                sb.append(string+"\n");
	    } 
	}		
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }


    public String createJSONForTree(Site site, Iterator iter, int totalSize) throws Exception {	

	//Protocol protocol = site.getProtocolById(Protocol.TREELIANA);
	//ColumnModel model = getTreeJSONColumnModel();
	//Map<Person, LeadShip> leaderMap = getLeaderMap(site, protocol);
	//Institution leaderInstitution = getInstitutions(site).iterator().next();
	
	ColumnModel model = getTreeJSONColumnModel();

	boolean first = true;
	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+totalSize+",\n");
	sb.append("result: [\n");

	while (iter.hasNext()) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    TreeRecord record = (TreeRecord)iter.next();
	    List<Object> objects = new ArrayList<Object>();
	    objects.add(record);
	    TreePoint treePoint = (TreePoint)record.getSamplingUnit();
	    objects.add(treePoint);
	    
	    //objects.add(treePoint.getSite());
	    //objects.add(treePoint.getBlock());		
	    //objects.add(protocol);		
	    //objects.add(leaderInstitution);
	    //objects.add(getLeader(record, leaderMap));
	    //objects.add(record.getReview());
	    String string = model.formatJSON(objects);	
	    sb.append(string+"\n");
	}		
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }



    public String createJSONForLiana(Site site, Iterator iter, int totalSize) throws Exception {	

	//Protocol protocol = site.getProtocolById(Protocol.TREELIANA);
	//ColumnModel model = getLianaJSONColumnModel();
	//Map<Person, LeadShip> leaderMap = getLeaderMap(site, protocol);
	//Institution leaderInstitution = getInstitutions(site).iterator().next();
	
	ColumnModel model = getLianaJSONColumnModel();

	boolean first = true;
	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+totalSize+",\n");
	sb.append("result: [\n");

	while (iter.hasNext()) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    LianaRecord record = (LianaRecord)iter.next();
	    List<Object> objects = new ArrayList<Object>();
	    objects.add(record);
	    LianaPoint lianaPoint = (LianaPoint)record.getSamplingUnit();
	    objects.add(lianaPoint);
	    //objects.add(lianaPoint.getSite());
	    //objects.add(lianaPoint.getBlock());		
	    //objects.add(protocol);		
	    //objects.add(leaderInstitution);
	    //objects.add(getLeader(record, leaderMap));
	    //objects.add(record.getReview());
	    String string = model.formatJSON(objects);	
	    sb.append(string+"\n");
	}		
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }

  
    protected List getData(Query query, int start, int length)  {
	List records = new ArrayList();
	query.setFirstResult(start);
	query.setMaxResults(length);
	records = query.list(); 	
	return records;    
    }
     
  	
    /**
     * Get events at a site for a protocol
     *
     */ 		
    public List getEvents(Site site, Protocol protocol) {
	List records = new ArrayList();
	if (protocol.getName().equals("Climate")) {
	    // retrieve climate event
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT year(record.collectedAt), month(record.collectedAt) "+
				    "FROM ClimateRecord as record "+
				    "WHERE record.collectionship.samplingUnit.site.id = "+site.getId() +" "+
				    "ORDER BY year(record.collectedAt), month(record.collectedAt) ");
	    List values = query.list();
	    for (int i = 0; i<values.size(); i++) { 
		Object[] objs = (Object[])values.get(i);
		records.add(objs[0]+"."+objs[1]);
	    }  
	} else if (protocol.getName().equals("Avian")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM AvianRecord as record "+
				    "WHERE record.collectionship.samplingUnit.site.id = "+site.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Vegetation - Trees & Lianas")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM TreeRecord as record "+
				    "WHERE record.samplingUnit.site.id = "+site.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Butterfly")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM ButterflyRecord as record "+
				    "WHERE record.collectionship.samplingUnit.site.id = "+site.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Primate")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM PrimateRecord as record "+
				    "WHERE record.collectionship.samplingUnit.site.id = "+site.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Vegetation - Litterfall")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM LitterfallRecord as record "+
				    "WHERE record.collectionship.samplingUnit.site.id = "+site.getId());
	    records = query.list();
	} 	 		 
	return records;
    }
 	
 	
    /**
     * Get events at a block for a protocol
     *
     */ 		
    public List getEvents(Block block, Protocol protocol) {
	List records = new ArrayList();
	if (protocol.getName().equals("Climate")) {
	    // retrieve climate event
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT year(record.collectedAt), month(record.collectedAt) "+
				    "FROM ClimateRecord as record "+
				    "WHERE record.collectionship.samplingUnit.block.id = "+block.getId() +" "+
				    "ORDER BY year(record.collectedAt), month(record.collectedAt) ");
	    List values = query.list();
	    for (int i = 0; i<values.size(); i++) { 
		Object[] objs = (Object[])values.get(i);
		records.add(objs[0]+"."+objs[1]);
	    }  
	} else if (protocol.getName().equals("Avian")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM AvianRecord as record "+
				    "WHERE record.collectionship.samplingUnit.block.id = "+block.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Vegetation - Trees & Lianas")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM TreeRecord as record "+
				    "WHERE record.samplingUnit.block.id = "+block.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Butterfly")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM ButterflyRecord as record "+
				    "WHERE record.collectionship.samplingUnit.block.id = "+block.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Primate")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM PrimateRecord as record "+
				    "WHERE record.collectionship.samplingUnit.block.id = "+block.getId());
	    records = query.list();
	} else if (protocol.getName().equals("Vegetation - Litterfall")) { 		 
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT record.event "+
				    "FROM LitterfallRecord as record "+
				    "WHERE record.collectionship.samplingUnit.block.id = "+block.getId());
	    records = query.list();
	} 		 		 
	return records;
    }


    /**
     * Get events at a sampling unit for a protocol
     *
     */ 		
    public List getEvents(WeatherStation station, Protocol protocol) {
	List records = new ArrayList();
	if (protocol.getName().equals("Climate")) {
	    // retrieve climate event
	    Query query = 
		session.createQuery(
				    "SELECT DISTINCT year(record.collectedAt), month(record.collectedAt) "+
				    "FROM ClimateRecord as record "+
				    "WHERE record.collectionship.samplingUnit.id = "+station.getId() +" "+
				    "ORDER BY year(record.collectedAt), month(record.collectedAt) ");
	    List values = query.list();
	    for (int i = 0; i<values.size(); i++) { 
		Object[] objs = (Object[])values.get(i);
		records.add(objs[0]+"."+objs[1]);
	    }  
	}
	return records;
    }
 	 		
 	
    public ColumnModel getColumnModel(Protocol protocol) throws Exception {	
	List<Class> classes = new ArrayList<Class>();
	String protocolName = protocol.getName(); 
	if (protocolName.equals("Climate")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.ClimateRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.WeatherStation"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	} else if (protocolName.equals("Avian")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.AvianRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.BirdPoint"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	} else if (protocolName.equals("Butterfly")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.ButterflyRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.ButterflyTrap"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	} else if (protocolName.equals("Primate")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.PrimateRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.PrimatePoint"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	} else if (protocolName.equals("Vegetation - Litterfall")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.LitterfallRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.LitterfallTrap"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	}
	return new ColumnModel(classes);
    }
 	
 		
    public ColumnModel getTreeColumnModel() throws Exception {	
	List<Class> classes = new ArrayList<Class>();
	classes.add(Class.forName("org.team.sdsc.datamodel.TreeRecord"));
	classes.add(Class.forName("org.team.sdsc.datamodel.TreePoint"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	return new ColumnModel(classes);
    }
 	
 	
    public ColumnModel getLianaColumnModel() throws Exception {	
	List<Class> classes = new ArrayList<Class>();
	classes.add(Class.forName("org.team.sdsc.datamodel.LianaRecord"));
	classes.add(Class.forName("org.team.sdsc.datamodel.LianaPoint"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	return new ColumnModel(classes);
    }
 	


    public ColumnModel getJSONColumnModel(Protocol protocol) throws Exception {	
	List<Class> classes = new ArrayList<Class>();
	String protocolName = protocol.getName(); 
	if (protocolName.equals("Climate")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.ClimateRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.WeatherStation"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Review"));
	} else if (protocolName.equals("Avian")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.AvianRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.BirdPoint"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Review"));
	} else if (protocolName.equals("Butterfly")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.ButterflyRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.ButterflyTrap"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Review"));
	} else if (protocolName.equals("Primate")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.PrimateRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.PrimatePoint"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Review"));
	} else if (protocolName.equals("Vegetation - Litterfall")) {	
	    classes.add(Class.forName("org.team.sdsc.datamodel.LitterfallRecord"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.LitterfallTrap"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	    classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	    classes.add(Class.forName("org.team.sdsc.datamodel.Review"));
	}
	return new ColumnModel(classes);
    }

 		
    public ColumnModel getTreeJSONColumnModel() throws Exception {	
	List<Class> classes = new ArrayList<Class>();
	classes.add(Class.forName("org.team.sdsc.datamodel.TreeRecord"));
	classes.add(Class.forName("org.team.sdsc.datamodel.TreePoint"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	//classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Review"));
	return new ColumnModel(classes);
    }
 	
 	
    public ColumnModel getLianaJSONColumnModel() throws Exception {	
	List<Class> classes = new ArrayList<Class>();
	classes.add(Class.forName("org.team.sdsc.datamodel.LianaRecord"));
	classes.add(Class.forName("org.team.sdsc.datamodel.LianaPoint"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Site"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Block"));		
	//classes.add(Class.forName("org.team.sdsc.datamodel.Protocol"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Institution"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Person"));
	//classes.add(Class.forName("org.team.sdsc.datamodel.Review"));
	return new ColumnModel(classes);
    }

 	
    public List<Block> getBlocks(Site site) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM Block as block WHERE block.site.id="+site.getId()+" ORDER BY block.name");
	records = query.list();
	return records;
    }

 	
    public List<SamplingEvent> getSamplingEvents(Site site) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM SamplingEvent as event WHERE event.site.id="+site.getId()+" ORDER BY event.event");
	records = query.list();
	return records;
    }

 	
    public List<Camera> getCameras(Site site) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM Camera as camera WHERE camera.site.id="+site.getId()+" ORDER BY camera.serialNumber");
	records = query.list();
	return records;
    }

 	
    public Person getPersonByEmail(String email) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM Person as person WHERE person.email='"+email+"'");
	records = query.list();
	if (records.isEmpty()) {
	    return null;
	} else {
	    return (Person)records.get(0);
	}
    }





    public Integer getMinTrapId() {
	Query query = session.createQuery("SELECT min(unit.id)-1 FROM SamplingUnit as unit");
	Object object = query.list().get(0);
	if (object != null) {
	    return (Integer)object;
	} else {
	    return new Integer(-1);
	}
    }



    public Integer getMinPersonId() {
	Query query = session.createQuery("SELECT min(unit.id)-1 FROM Person as unit");
	Object object = query.list().get(0);
	if (object != null) {
	    return (Integer)object;
	} else {
	    return new Integer(-1);
	}
    }


 	
 	
    public List<Institution> getInstitutions(Site site) {
	List records = new ArrayList();
	Query query = session.createQuery("FROM Institution as institution WHERE institution.site.id="+site.getId());
	records = query.list();
	return records;
    }
 	


    public List<Institution> getLeaderInstitutions() {
	return leaderInstitutions;
    }


    public Institution getLeaderInstitution(SamplingUnit unit) {
	Institution result = null;
	for (Institution institution : leaderInstitutions) {
	    if (institution.getSite() != null && institution.getSite().getId().equals(unit.getSite().getId())) {
		result = institution;
		break;
	    }
	}
	return result;
    }


    public Institution getLeaderInstitution(Site site) {
	Institution result = null;
	for (Institution institution : leaderInstitutions) {
	    if (institution.getSite() != null && institution.getSite().getId().equals(site.getId())) {
		result = institution;
		break;
	    }
	}
	return result;
    }


    public SamplingUnit getSamplingUnitById(int id) {
	Query query = session.createQuery("FROM SamplingUnit as unit WHERE unit.id="+id);
	List records = query.list();
	if (records.size() > 0) {
	    return (SamplingUnit)records.iterator().next();
	} else {
	    return null;
	}
    }


    public SamplingEvent getSamplingEventById(int id) {
	Query query = session.createQuery("FROM SamplingEvent as event WHERE event.id="+id);
	List records = query.list();
	if (records.size() > 0) {
	    return (SamplingEvent)records.iterator().next();
	} else {
	    return null;
	}
    }


    public Camera getCameraById(int id) {
	Query query = session.createQuery("FROM Camera as camera WHERE camera.id="+id);
	List records = query.list();
	if (records.size() > 0) {
	    return (Camera)records.iterator().next();
	} else {
	    return null;
	}
    }




    public SamplingUnit getSamplingUnitByName(String name) {
	Query query = session.createQuery("FROM SamplingUnit as unit WHERE unit.name='"+name+"'");
	List records = query.list();
	if (records.size() > 0) {
	    return (SamplingUnit)records.iterator().next();
	} else {
	    return null;
	}
    }

 	
    public List<SamplingUnit> getSamplingUnits(Block block, Protocol protocol) {	
	List records = new ArrayList();
	Query query = session.createQuery("FROM SamplingUnit as unit WHERE unit.block.id="+block.getId()+" AND "+
					  "unit.protocolFamily.name = '"+protocol.getName()+"' ");
	records = query.list();
	return records;
    }


    public List<TreePoint> getTreePoints() {
	List records = new ArrayList();
	Query query = session.createQuery("FROM TreePoint as unit");
	records = query.list();
	return records;
    }



    public List<LianaPoint> getLianaPoints() {
	List records = new ArrayList();
	Query query = session.createQuery("FROM LianaPoint as unit");
	records = query.list();
	return records;
    }


    public List<SamplingUnit> getSamplingUnits(Site site, Protocol protocol) {	
	List records = new ArrayList();
	Query query = session.createQuery("FROM SamplingUnit as unit WHERE unit.site.id="+site.getId()+" AND "+
					  "unit.protocolFamily.name = '"+protocol.getName()+"' ");
	records = query.list();
	return records;
    }
 	
 	
    public List<SamplingUnit> getSamplingUnits(Block block) {	
	List records = new ArrayList();
	Query query = session.createQuery("FROM SamplingUnit as unit WHERE unit.block.id="+block.getId());
	records = query.list();
	return records;
    }


    public Block getBlockByName(String name) {
	Query query = session.createQuery("FROM Block as block WHERE block.name='"+name+"'");
	List records = query.list();
	if (records.size() > 0) {
	    return (Block)records.iterator().next();
	} else {
	    return null;
	}
    }


    public Block getBlockById(int id) {
	Query query = session.createQuery("FROM Block as block WHERE block.id='"+id+"'");
	List records = query.list();
	if (records.size() > 0) {
	    return (Block)records.iterator().next();
	} else {
	    return null;
	}
    }


    public List<Person> getPersons(Site site) {

	List<Person> persons = new ArrayList<Person>();
	Query query = session.createQuery("FROM Person as person");
	List<Person> records = query.list();
	for (Person person : records) {
	    for (Site aSite : person.getSites()) {
		if (site.getId().intValue() == aSite.getId().intValue()) {
		    persons.add(person);
		    break;
		}
	    }
	}

	return persons;
    }



    public List<String> getSamplingEvents() {
	List<String> result = new ArrayList<String>();
	Query query = session.createQuery("FROM SamplingEvent as event");
	List<SamplingEvent> events = query.list();
	for (SamplingEvent event : events) {
	    result.add(event.getEvent());
	}
	return result;
    }
    


    public long getCameraTrapPhotoFileSize(Site site, String event, String arrayName) {
	Query query = session.createQuery("SELECT sum(photo.size) FROM CameraTrapPhoto as photo "+
					  "WHERE photo.collection.event='"+event+"' AND "+
					  "photo.collection.cameraTrap.name like 'CT-"+site.getAbbv()+"-"+arrayName+"-%'");
	List list = query.list();
	if (list == null || list.isEmpty()) {
	    return 0;
	} else {
	    Long value = (Long)list.iterator().next();
	    if (value == null) {
		return 0;
	    } else {
		return value.longValue();
	    }
	}
    }


    public long getCameraTrapPhotoFileSize(Site site, String event) {
	Query query = session.createQuery("SELECT sum(photo.size) FROM CameraTrapPhoto as photo "+
					  "WHERE photo.collection.event='"+event+"' ");
	List list = query.list();
	if (list == null || list.isEmpty()) {
	    return 0;
	} else {
	    Long value = (Long)list.iterator().next();
	    if (value == null) {
		return 0;
	    } else {
		return value.longValue();
	    }
	}
    }


    public List<CameraTrapPhotoCollection> getCameraTrapPhotoCollections(Site site, String event, String arrayName) {
	Query query = session.createQuery("FROM CameraTrapPhotoCollection as collection WHERE collection.event='"+event+"' AND "+
					  " collection.cameraTrap.name like 'CT-"+site.getAbbv()+"-"+arrayName+"-%'");
	List<CameraTrapPhotoCollection> list = query.list();
	return list;
    }


    public List<CameraTrapPhotoCollection> getCameraTrapPhotoCollections(Site site, String event) {
	Query query = session.createQuery("FROM CameraTrapPhotoCollection as collection WHERE collection.event='"+event+"' ");
	List<CameraTrapPhotoCollection> list = query.list();
	return list;
    }



    public CameraTrapPhotoCollection getCameraTrapPhotoCollection(String event, String trapName) {
	Query query = session.createQuery("FROM CameraTrapPhotoCollection as collection WHERE collection.event='"+event+"' AND "+
                                          " collection.cameraTrap.name='"+trapName+"'");
        List<CameraTrapPhotoCollection> list = query.list();
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }


         
    public List<CameraTrap> getImportedCameraTraps(String event) {
	Query query = session.createQuery("SELECT collection.cameraTrap FROM CameraTrapPhotoCollection as collection WHERE collection.event='"+event+"'");
	List<CameraTrap> list = query.list();
	return list;
    }


    public List<CameraTrapPhoto> getCameraTrapPhoto(String trapName, String event) {
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.name='"+trapName+"'"+
					  " AND photo.collection.event='"+event+"'");
	List<CameraTrapPhoto> list = query.list();
	return list;
    }



    public CameraTrapPhoto getStartPhoto(String trapName, String event) {
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.name='"+trapName+"'"+
					  " AND photo.collection.event='"+event+"' "+
					  " AND photo.type.name='Start'");
	List<CameraTrapPhoto> list = query.list();
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }


    public CameraTrapPhoto getEndPhoto(String trapName, String event) {
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.name='"+trapName+"'"+
					  " AND photo.collection.event='"+event+"' "+
					  " AND photo.type.name='End'");
	List<CameraTrapPhoto> list = query.list();
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }


    public CameraTrapPhotoType getCameraTrapPhotoType(String name) {
	Query query = session.createQuery("FROM CameraTrapPhotoType as type WHERE type.name='"+name+"'");
	List<CameraTrapPhotoType> list = query.list();
	if (!list.isEmpty()) {
	    return list.get(0);
	} else {
	    return null;
	}
    }



    public CameraTrapPhoto getCameraTrapPhoto(String trapName, String event, String rawName) {
	
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.name='"+trapName+"'"+
					  " AND photo.collection.event='"+event+"'"+
					  " AND photo.rawName='"+rawName+"'");
	List<CameraTrapPhoto> list = query.list();
	CameraTrapPhoto photo = null;	
	if (!list.isEmpty()) photo = (CameraTrapPhoto)list.get(0); 
	return photo;

    }


    public List<CameraTrapPhoto> getLaterCameraTrapPhotos(String trapName, String event, Integer index) {
	
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.name='"+trapName+"'"+
					  " AND photo.collection.event='"+event+"'"+
					  " AND photo.id>"+index);
	List<CameraTrapPhoto> list = query.list();
	return list;

    }




    public CameraTrapPhoto getCameraTrapPhotoBefore(String trapName, String event, Integer index) {
	
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.name='"+trapName+"'"+
					  " AND photo.collection.event='"+event+"'"+
					  " AND photo.id="+(index-1));
	List<CameraTrapPhoto> list = query.list();
	CameraTrapPhoto photo = null;
        if (!list.isEmpty()) photo = (CameraTrapPhoto)list.get(0);
        return photo;
    }





    public CameraTrapPhoto getNextCameraTrapPhoto(CameraTrapPhoto ctp) {
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.event='"+ctp.getCollection().getEvent()+"' "+
					  " AND photo.collection.cameraTrap.name='"+ctp.getCollection().getCameraTrap().getName()+"' "+
					  " AND photo.rawName>'"+ctp.getRawName()+"' ORDER BY photo.rawName");
	query.setFirstResult(0);
	query.setMaxResults(2);
	List<CameraTrapPhoto> list = query.list();
	CameraTrapPhoto photo = null;	
	if (!list.isEmpty()) photo = (CameraTrapPhoto)list.get(0); 
	return photo;
    }



    public CameraTrapPhoto getPreviousCameraTrapPhoto(CameraTrapPhoto ctp) {

	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.event='"+ctp.getCollection().getEvent()+"' "+
					  " AND photo.collection.cameraTrap.name='"+ctp.getCollection().getCameraTrap().getName()+"' "+
					  " AND photo.rawName<'"+ctp.getRawName()+"' ORDER BY photo.rawName DESC");
	//Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.id<"+ctp.getId().intValue()+" ORDER BY photo.id DESC");
	query.setFirstResult(0);
        query.setMaxResults(2);
	List<CameraTrapPhoto> list = query.list();
	CameraTrapPhoto photo = null;	
	if (!list.isEmpty()) photo = (CameraTrapPhoto)list.get(0); 
	return photo;
    }



    public List<PhotoSpeciesRecord> getPhotoSpeciesRecords(CameraTrapPhoto photo) {
	Query query = session.createQuery("FROM PhotoSpeciesRecord as record WHERE record.photo.id="+photo.getId());
	return (List<PhotoSpeciesRecord>)query.list();
    }



    public Set<CameraTrapPhoto> getCameraTrapPhotoCluster(CameraTrapPhoto photo) {

	java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//System.out.println("\n\n------------------------------------------");
	//System.out.println(photo.getRawName()+"   "+formatter.format(photo.getTakenTime()));

	TreeSet<CameraTrapPhoto> result = new TreeSet<CameraTrapPhoto>(new CameraTrapPhotoComparator());

	// earlier photos
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE "+
					  "photo.collection.event='"+photo.getCollection().getEvent()+"' AND "+
                                          "photo.collection.cameraTrap.name='"+photo.getCollection().getCameraTrap().getName()+"' AND "+
					  "photo.takenTime > SUBTIME('"+formatter.format(photo.getTakenTime())+"', '00:10:00.0') AND "+
					  "photo.takenTime < '"+formatter.format(photo.getTakenTime())+"' "+
					  "ORDER BY photo.takenTime DESC");
	List<CameraTrapPhoto> photos = (List<CameraTrapPhoto>)query.list();
	CameraTrapPhoto last = photo;
	for (CameraTrapPhoto tmp : photos) {
	    if (last.getTakenTime().getTime() - tmp.getTakenTime().getTime() < 60000) {
		result.add(tmp);
		last = tmp;
	    } else {
		break;
	    }
	}

	//System.out.println("before result size = "+result.size());

	// same time photos
	query = session.createQuery("FROM CameraTrapPhoto as photo WHERE "+
				    "photo.collection.event='"+photo.getCollection().getEvent()+"' AND "+
				    "photo.collection.cameraTrap.name='"+photo.getCollection().getCameraTrap().getName()+"' AND "+
				    "photo.takenTime = '"+formatter.format(photo.getTakenTime())+"'");
	result.addAll(query.list());
	
	//System.out.println("same result size = "+result.size());

	// later photos
	/*
	System.out.println("FROM CameraTrapPhoto as photo WHERE "+
			   "photo.takenTime < ADDTIME('"+formatter.format(photo.getTakenTime())+"', '00:10:00.0') AND "+
			   "photo.takenTime > '"+formatter.format(photo.getTakenTime())+"' ORDER BY photo.takenTime");
	*/
	query = session.createQuery("FROM CameraTrapPhoto as photo WHERE "+
				    "photo.collection.event='"+photo.getCollection().getEvent()+"' AND "+
				    "photo.collection.cameraTrap.name='"+photo.getCollection().getCameraTrap().getName()+"' AND "+
				    "photo.takenTime < ADDTIME('"+formatter.format(photo.getTakenTime())+"', '00:10:00.0') AND "+
				    "photo.takenTime > '"+formatter.format(photo.getTakenTime())+"' ORDER BY photo.takenTime");
	photos = (List<CameraTrapPhoto>)query.list();
	last = photo;
	for (CameraTrapPhoto tmp : photos) {

	    //System.out.println("---------------------------");
	    //System.out.println("       "+tmp.getRawName()+"   "+tmp.getTakenTime()+"    "+(tmp.getTakenTime().getTime() - last.getTakenTime().getTime()));
	    if (tmp.getTakenTime().getTime() - last.getTakenTime().getTime() < 60000) {
		result.add(tmp);
		last = tmp;
	    } else {
		break;
	    }
	}

	//System.out.println("last result size = "+result.size());

	return result;
    }



    public int getCameraTrapTotalSize(Site site) {
	Query query = session.createQuery("SELECT count(*) FROM CameraTrap as trap WHERE trap.site.id="+site.getId());
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();
    }



    public int getCameraTrapPhotoTotalSize(CameraTrap trap, String event) {
	if (trap == null) return 0;
	Query query = session.createQuery("SELECT count(*) FROM CameraTrapPhotoCollection as collection WHERE collection.cameraTrap.id="+trap.getId()+" AND collection.event='"+event+"'");
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();
    }



    public int getCameraTrapPhotoTotalSize1(CameraTrap trap, String event) {
	if (trap == null) return 0;
	Query query = session.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.id="+trap.getId()+" AND photo.collection.event='"+event+"'");
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();
    }



    public int getUnprocessedPhotoTotalSize(CameraTrap trap, String event) {

        /*
	if (trap == null) return 0;
	Query query = session.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.id="+trap.getId()+" AND photo.collection.event='"+event+"' AND photo.type is NULL");
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();
        */

	if (trap == null) return 0;

	int result = 1000000;

        // get all collections
	Query query = session.createQuery("FROM CameraTrapPhotoCollection as collection WHERE collection.cameraTrap.id="+trap.getId()+" AND collection.event='"+event+"'");
	List<CameraTrapPhotoCollection> collections = query.list();
        for (CameraTrapPhotoCollection collection : collections) {
	    Query query1 = session.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo WHERE photo.collection.id="+collection.getId()+" AND photo.collection.event='"+event+"' AND photo.type is NULL");
	    List list = query1.list();
            int aInt = ((Long)list.iterator().next()).intValue();
	    if (aInt < result) result = aInt;
	}
	return result;

    }





    public int getPhotoTotalSize(CameraTrap trap, String event) {
	Query query = session.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.id="+trap.getId()+" AND photo.collection.event='"+event+"'");
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();
    }


    public int getProcessedPhotoTotalSize() {
	Query query = session.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo WHERE NOT photo.type = NULL");
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();
    }


    public int getPhotoTotalSize() {
	Query query = session.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo");
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();
    }


    public CameraTrapUploadJob getCameraTrapUploadJob(CameraTrap trap, String event) {
	Query query = session.createQuery("FROM CameraTrapUploadJob as job "+
					  "WHERE job.cameraTrap.id="+trap.getId()+
					  " AND job.event='"+event+"'");
	List list = query.list();
	if (list.isEmpty()) {
	    return null;
	} else {
	    return (CameraTrapUploadJob)list.get(0);
	}
    }



    public void deleteCameraTrapPhotos(String siteAbbrev, String event, String array, String trap) throws Exception{
	Transaction tx = transaction;
	try {
	    String hql = 
		"FROM CameraTrapPhotoCollection as collection WHERE "+
		"collection.event='"+event+"' ";

	    if (trap != null) {
		hql += " AND collection.cameraTrap.name='"+trap+"'";
	    } else if (array != null) {
                //hql += " AND collection.cameraTrap.name like 'CT-"+siteAbbrev+"-"+array.substring(5)+"-%'";
		hql += " AND collection.cameraTrap.block.index = "+array.substring(5);
	    }

	    Query query = session.createQuery(hql);
	    List<CameraTrapPhotoCollection> collections = query.list();
	    for (CameraTrapPhotoCollection collection : collections) {

		// delete associated photos
		for (CameraTrapPhoto photo : collection.getPhotos()) {
		    for (PhotoSpeciesRecord record : photo.getRecords()) {
			session.delete(record);
		        //System.out.println("          delete species  "+record.getId()+", photo="+photo.getId());
		    }
		    CameraTrapPhotoMetadata metadata = photo.getMetadata();
		    if (metadata != null) {
			session.delete(metadata);
			//System.out.println("          delete metadata  "+metadata.getId()+", photo="+photo.getId());
		    }
		    session.delete(photo);
		    //System.out.println("      delete photo "+photo.getId());
		}

		// delete associated damage
		DamagedCameraTrap dct = collection.getDamagedCameraTrap();
		if (dct != null) session.delete(dct);

		session.delete(collection);
		//System.out.println("delete collection "+collection.getId());
	    }
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }
    


    public void deletePhoto(CameraTrapPhoto photo) throws Exception{
	Transaction tx = transaction;
	try {
	    for (PhotoSpeciesRecord record : photo.getRecords()) {
		session.delete(record);
	    }
	    CameraTrapPhotoMetadata metadata = photo.getMetadata();
	    if (metadata != null) {
		session.delete(metadata);
	    }
	    session.delete(photo);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }
    


    public List<CameraTrapPhoto> getBlankCameraTrapPhotos(String siteAbbrev, String event, String array, String trap) throws Exception{
	String hql = 
		"FROM CameraTrapPhoto as photo WHERE "+
		"photo.collection.event='"+event+"' AND "+
		"photo.type.name='Blank' ";

	if (trap != null) {
	    hql += " AND photo.collection.cameraTrap.name='"+trap+"'";
	} else if (array != null) {
	    hql += " AND photo.collection.cameraTrap.block.index = "+array.substring(5);
	}

	Query query = session.createQuery(hql);
	List<CameraTrapPhoto> photos = query.list();
	return photos;
    }



    public List<Equipment> getEquipments() {
	Query query = session.createQuery("FROM Equipment as equipment");
	List list = query.list();
	return (List<Equipment>)list;
    }



    public List<Camera> getCameras() {
	Query query = session.createQuery("FROM Camera as camera ORDER BY camera.serialNumber");
	List list = query.list();
	return (List<Camera>)list;
    }


    public List<Equipment> getEquipments(Site site, String type) {
	Query query = session.createQuery("FROM Equipment as equipment WHERE equipment.type='"+type+
					  "' AND equipment.site.id="+site.getId());
	List list = query.list();
	return (List<Equipment>)list;
    }


    public List<String> getGPSManufactureModel() {
	Query query = session.createQuery("SELECT DISTINCT (equipment.manufacturer || ' ' || equipment.model) FROM Equipment as equipment WHERE equipment.type='GPS' ORDER BY equipment.manufacturer || ' ' ||  equipment.model");
	List list = query.list();
	return (List<String>)list;
    }



    public String getJSONCameraTraps(Site site, int start, int limit, String sort, String dir) {

	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+getCameraTrapTotalSize(site)+",\n");
	sb.append("result: [\n");
	Query query = session.createQuery("FROM CameraTrap as trap WHERE trap.site.id="+site.getId()+" "+
                                          "ORDER BY "+sort+" "+dir);
	query.setFirstResult(start);
	query.setMaxResults(limit);
	List<CameraTrap> records = (List<CameraTrap>)query.list();
	boolean first = true;
	for (CameraTrap unit : records) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    
	    sb.append("   {");
	    
	    sb.append("id: '"+unit.getId()+"', ");

	    String unitName = unit.getName();
	    if (unitName != null) {
		sb.append("unit_name: '"+unitName+"', ");
	    }

	    Double lat = unit.getLatitude();
	    if (lat != null) {
		sb.append("latitude: '"+lat+"', ");
	    }

	    Double lon = unit.getLongitude();
	    if (lon != null) {
		sb.append("longitude: '"+lon+"', ");
	    }

	    Double plat = unit.getProposedLatitude();
	    if (plat != null) {
		sb.append("proposed_latitude: '"+plat+"', ");
	    }

	    Double plon = unit.getProposedLongitude();
	    if (plon != null) {
		sb.append("proposed_longitude: '"+plon+"', ");
	    }

	    sb.append("}");
	}
	
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();

    }



    public String getJSONCameraTrapData(CameraTrap trap, String event, int start, int limit, String sort, String dir) {

	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+getCameraTrapPhotoTotalSize(trap, event)+",\n");
	sb.append("result: [\n");

	if (trap != null) {
	    Query query = session.createQuery("FROM CameraTrapPhotoCollection as collection WHERE collection.cameraTrap.id="+trap.getId()+
					      " AND collection.event='"+event+"' "+
					      " ORDER BY "+sort+" "+dir);
	    query.setFirstResult(start);
	    query.setMaxResults(limit);
	    List<CameraTrapPhotoCollection> records = (List<CameraTrapPhotoCollection>)query.list();
	    boolean first = true;
	    for (CameraTrapPhotoCollection collection : records) {
		if (first) {
		    first = false;
		} else {
		    sb.append(",\n");
		}
	    
		sb.append("   {");
	    
		sb.append("Site: '"+trap.getSite().getName()+"', ");
		sb.append("Sampling_Period: '"+event+"', ");
		sb.append("Camera_Trap_Point_ID: '"+trap.getName()+"', ");
		sb.append("Camera_Serial_Number: '"+collection.getCameraSerialNumber()+"', ");
		sb.append("Memory_Card_Serial_Number: '"+collection.getMemoryCardSerialNumber()+"', ");
		sb.append("Set_Person: '"+replace(collection.getSetPerson().getFirstName())+"  "+replace(collection.getSetPerson().getLastName())+"', ");
		sb.append("Pick_Person: '"+replace(collection.getPickPerson().getFirstName())+"  "+replace(collection.getPickPerson().getLastName())+"', ");
		sb.append("Camera_Working_: "+collection.getWorking()+", ");

		CameraTrapPhoto startPhoto = getStartPhoto(trap.getName(), event);
		if (startPhoto != null) {
		    String takenTimeString = startPhoto.getTakenTimeString();
		    int index = takenTimeString.indexOf(" ");
		    if (index != -1) {
		        sb.append("Start_Date: '"+takenTimeString.substring(0, index).trim()+"', ");
		        sb.append("Start_Time: '"+takenTimeString.substring(index+1).trim()+"', ");
		    }
		}

		CameraTrapPhoto endPhoto = getEndPhoto(trap.getName(), event);
		if (endPhoto != null) {
		    String takenTimeString = endPhoto.getTakenTimeString();
		    int index = takenTimeString.indexOf(" ");
		    if (index != -1) {
		        sb.append("End_Date: '"+takenTimeString.substring(0, index).trim()+"', ");
		        sb.append("End_Time: '"+takenTimeString.substring(index+1).trim()+"', ");
		    }
		}

		if (!collection.getWorking()) {
		    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");       
		    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");       
		    sb.append("Start_Date: '"+dayFormat.format(collection.getStartTime())+"', ");
		    sb.append("Start_Time: '"+timeFormat.format(collection.getStartTime())+"', ");
		    sb.append("End_Date: '"+dayFormat.format(collection.getEndTime())+"', ");
		    sb.append("End_Time: '"+timeFormat.format(collection.getEndTime())+"', ");
		}

	        Boolean trapMissing = collection.getTrapMissing();
		if (trapMissing != null) {
		    sb.append("Camera_Trap_Missing_: "+trapMissing+", ");
		}

		Boolean caseDamaged = collection.getCaseDamaged();
		if (caseDamaged != null) {
		    sb.append("Case_Damage_: "+caseDamaged+", ");
		}

		Boolean cameraDamaged = collection.getCameraDamaged();
		if (cameraDamaged != null) {
		    sb.append("Camera_Damage_: "+cameraDamaged+", ");
		}

		Boolean cardDamaged = collection.getCardDamaged();
		if (cardDamaged != null) {
		    sb.append("Card_Damage_: "+cardDamaged+", ");
		}

		String notes = collection.getNotes();
		if (notes != null) {
		    sb.append("Notes: '"+replace(notes)+"', ");
		}

		DamagedCameraTrap dct = collection.getDamagedCameraTrap();
		if (dct != null) {

		    Boolean firing = dct.getIsFiring();
		    if (firing != null) {
			sb.append("Is_Firing: "+firing+", ");
		    }

		    Boolean humidity = dct.getHumidityInside();
		    if (humidity != null) {
			sb.append("Humidity_Inside_Camera: "+humidity+", ");
		    }

		    String followup = dct.getFollowUpSteps();
		    if (followup != null) {
			sb.append("Follow_Up: '"+replace(followup)+"', ");
		    }

		}


		sb.append("pad: ''");
		sb.append("}");
	    }

	}
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }






    public String getJSONPhotoData(CameraTrap trap, String event, int start, int limit, String sort, String dir) {

	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+getPhotoTotalSize(trap, event)+",\n");
	sb.append("result: [\n");
	Query query = session.createQuery("FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.id="+trap.getId()+
					  " AND photo.collection.event='"+event+"' "+
                                          " ORDER BY photo.rawName "+dir);
	query.setFirstResult(start);
	query.setMaxResults(limit);
	List<CameraTrapPhoto> photos = (List<CameraTrapPhoto>)query.list();
	boolean first = true;
	for (CameraTrapPhoto photo : photos) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    
	    sb.append("   {");
	    
	    sb.append("Sampling_Period: '"+event+"', ");
	    sb.append("Camera_Trap_Point_ID: '"+trap.getName()+"', ");
	    sb.append("Raw_Filename: '"+photo.getRawName()+"', ");
	    sb.append("Team_Filename: '"+photo.getTeamName()+"', ");

	    if (photo.getType() != null) {
	        sb.append("Type: '"+photo.getType().getName()+"', ");
	    }

	    if (photo.getNote() != null) {
		sb.append("Notes: '"+replace(photo.getNote())+"', ");
	    }

	    /*
	    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");       
	    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");       
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
	    sb.append("Date: '"+dayFormat.format(photo.getTakenTime())+"', ");
	    sb.append("Time: '"+timeFormat.format(photo.getTakenTime())+"', ");
	    sb.append("takentime: '"+formatter.format(photo.getTakenTime())+"', ");
	    */

	    String takenTimeString = photo.getTakenTimeString();
	    sb.append("takentime: '"+takenTimeString+"', ");

	    int index = takenTimeString.indexOf(" ");
	    if (index != -1) {
                sb.append("Date: '"+takenTimeString.substring(0, index).trim()+"', ");
	        sb.append("Time: '"+takenTimeString.substring(index+1).trim()+"', ");
	    }

	    Set<PhotoSpeciesRecord> records = photo.getRecords();
	    if (records != null && !records.isEmpty()) {
		PhotoSpeciesRecord record = records.iterator().next();
		sb.append("Genus: '"+record.getGenus()+"', ");
		sb.append("Species: '"+record.getSpecies()+"', ");
		sb.append("Binomial: '"+record.getGenus()+" "+record.getSpecies()+"', ");
		sb.append("Number_of_Animals: '"+record.getNumberOfAnimals()+"', ");
		sb.append("Identified_By: '"+replace(record.getIdentifiedBy().getFirstName())+" "+replace(record.getIdentifiedBy().getLastName())+"', ");
	    } else if (photo.getTypeIdentifiedBy() != null) {
		sb.append("Identified_By: '"+replace(photo.getTypeIdentifiedBy().getFirstName())+" "+replace(photo.getTypeIdentifiedBy().getLastName())+"', ");
	    }

	    CameraTrapPhotoMetadata metadata = photo.getMetadata();
	    if (metadata != null) {
		String trg = metadata.getJpegCommentTrg();
		if (trg != null) {
		    trg = trg.trim();
		} else {
		    trg = "Not found";
		}
		sb.append("trg: '"+trg+"', ");

		String flash = metadata.getExifFlashName();
		if (flash != null) {
		    flash = flash.trim();
		} else {
		    flash = "Not found";
		}
		sb.append("flash: '"+flash+"', ");

		String exposure = metadata.getExifExposureTime();
		if (exposure != null) {
		    exposure = exposure.trim();
		} else {
		    exposure = "Not found";
		}
		sb.append("exposure: '"+exposure+"', ");

		String temperature = metadata.getJpegCommentTmp();
		if (temperature != null) {
		    temperature = temperature.trim();
		} else {
		    temperature = "Not found";
		}
		sb.append("temperature: '"+temperature+"', ");
	    }
	    
	    sb.append("pad: ''");
	    sb.append("}");
	}

	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }





    /**
     * Find a leader scientist for this tree record
     *
     */
    public Person getLeader(TreeRecord record, Map<Person, LeadShip> leaderMap) {
	Person person = null;
  		
	for (Person aPerson : leaderMap.keySet()) {
	    LeadShip leader = leaderMap.get(aPerson);
	    if (leader.isCurrent()) {
		if (record.getCollectedAt().after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (record.getCollectedAt().after(leader.getStartDate()) &&
		       record.getCollectedAt().before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  		
	return person;
    }


    /**
     * Find a leader scientist for this avian record
     *
     */
    public Person getLeader(AvianRecord record, Map<Person, LeadShip> leaderMap) {
	Person person = null;
  		
	for (Person aPerson : leaderMap.keySet()) {
	    LeadShip leader = leaderMap.get(aPerson);
	    if (leader.isCurrent()) {
		if (record.getObservedAt().after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (record.getObservedAt().after(leader.getStartDate()) &&
		       record.getObservedAt().before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  		
	return person;
    }


    /**
     * Find a leader scientist for this liana record
     *
     */
    public Person getLeader(LianaRecord record, Map<Person, LeadShip> leaderMap) {
	Person person = null;
  		
	for (Person aPerson : leaderMap.keySet()) {
	    LeadShip leader = leaderMap.get(aPerson);
	    if (leader.isCurrent()) {
		if (record.getCollectedAt().after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (record.getCollectedAt().after(leader.getStartDate()) &&
		       record.getCollectedAt().before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  		
	return person;
    }


    /**
     * Find a leader scientist for this primate record
     *
     */
    public Person getLeader(PrimateRecord record, Map<Person, LeadShip> leaderMap) {
	Person person = null;
  		
	for (Person aPerson : leaderMap.keySet()) {
	    LeadShip leader = leaderMap.get(aPerson);
	    if (leader.isCurrent()) {
		if (record.getObservedAt().after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (record.getObservedAt().after(leader.getStartDate()) &&
		       record.getObservedAt().before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  		
	return person;
    }


    /**
     * Find a leader scientist for this butterfly record
     *
     */
    public Person getLeader(ButterflyRecord record, Map<Person, LeadShip> leaderMap) {

	Person person = null;
  		
	for (Person aPerson : leaderMap.keySet()) {
	    LeadShip leader = leaderMap.get(aPerson);
	    if (leader.isCurrent()) {
		if (record.getObservedAt().after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (record.getObservedAt().after(leader.getStartDate()) &&
		       record.getObservedAt().before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  		
	return person;
    }


    /**
     * Find a leader scientist for this litterfall record
     *
     */
    public Person getLeader(LitterfallRecord record, Map<Person, LeadShip> leaderMap) {
	Person person = null;
	if (record.getCollectedStart() == null) return null;
	for (Person aPerson : leaderMap.keySet()) {
	    LeadShip leader = leaderMap.get(aPerson);
	    if (leader.isCurrent()) {
		if (record.getCollectedStart().after(leader.getStartDate()))  {
		    person = aPerson;
		    break;
		}
	    } else if (record.getCollectedStart().after(leader.getStartDate()) &&
		       record.getCollectedStart().before(leader.getEndDate())) {
		person = aPerson;
		break;
	    }
	}
  		
	return person;
    }



    public void getSamplingUnits(Date start, Date end) {

	long s = new Date().getTime();

	Query query = session.getNamedQuery("org.team.sdsc.datamodel.AllSamplingUnitStubs");
	query.setDate("startTime", start);
	query.setDate("endTime", end);
	List records = query.list(); 	

	TreeSet uids = new TreeSet();         
	List sids = new ArrayList();         
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    Object[] ints = (Object[])i.next();
	    uids.add(ints[0]);
	}
         
	System.out.println("basic query time = "+(new Date().getTime() - s));  
		 
	List<Site> sites = activeSites;
	for (ListIterator iter = sites.listIterator() ; iter.hasNext() ; ) {
            
	    Site aSite = (Site)iter.next();
	    //System.out.println(aSite.getName());

	    List<Block> blocks = aSite.getBlocks();
	    int numberOfUnits = 0;
	    for (Iterator j=blocks.iterator(); j.hasNext(); ) {
		Block aBlock = (Block)j.next();
		//System.out.println("    "+aBlock.getName());
                	
		// find sampling units
		List<SamplingUnit> units = aBlock.getSamplingUnits();
		if (!units.isEmpty()) {
                    for (Iterator k=units.iterator(); k.hasNext(); ) {
			SamplingUnit aUnit = (SamplingUnit)k.next();
			if (uids.contains(aUnit.getId())) {
			    //System.out.println("         "+aUnit.getName());
			}
		    }
		}
	    }
	}    
         	
    }


    public static void showSearchResult(List<Site> sites) {

	for (Iterator i=sites.iterator(); i.hasNext();) {

	    Object object = i.next();	    
	    Site aSite = (Site)object;
	    System.out.println(aSite.getName());

	    for (Protocol protocol : aSite.getProtocols()) {
		System.out.println("    protocol: "+protocol.getName());
	    }

	    List<Block> blocks = aSite.getBlocks();
	    for (Iterator j=blocks.iterator(); j.hasNext(); ) {
		Block aBlock = (Block)j.next();
		System.out.println("    block: "+aBlock.getName());
                	
		// find sampling units
		List<SamplingUnit> units = aBlock.getSamplingUnits();
		if (!units.isEmpty()) {
                    for (Iterator k=units.iterator(); k.hasNext(); ) {
			SamplingUnit aUnit = (SamplingUnit)k.next();
			//System.out.println("         "+aUnit.getName());
		    }
		}
	    }
	}
    }   
    


    public List<Site> searchSamplingUnits() throws Exception{
	String startString = "Jan 1, 2003 1:00 AM";
	DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
	Date start = format.parse(startString);
	Date end = new Date();
	return searchSamplingUnits(start, end);
    }



    public List<Site> searchSamplingUnits(Date start, Date end) throws Exception{

	long s = new Date().getTime();

	Query query = session.getNamedQuery("org.team.sdsc.datamodel.AllSamplingUnitStubs");
	query.setDate("startTime", start);
	query.setDate("endTime", end);
	List records = query.list(); 	

	TreeSet uids = new TreeSet();         
	List sids = new ArrayList();         
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    Object[] ints = (Object[])i.next();
	    uids.add(ints[0]);
	}
         
	//System.out.println("basic query time = "+(new Date().getTime() - s));  
	List<Site> sites = activeSites;
	List<Site> qSites = new ArrayList<Site>();
	for (ListIterator iter = sites.listIterator() ; iter.hasNext() ; ) {
            
	    Site aSite = (Site)iter.next();
	    Site cSite = aSite.clone();
	    //System.out.println(aSite.getName());

	    List<Block> blocks = aSite.getBlocks();
	    List<Block> qBlocks = new ArrayList<Block>();
	    Set<Protocol> protocols = new TreeSet<Protocol>(new ProtocolComparator());
	    boolean foundBlock = false;
	    for (Iterator j=blocks.iterator(); j.hasNext(); ) {
		Block aBlock = (Block)j.next();
		Block cBlock = aBlock.clone();
		Set<Protocol> cProtocols = new TreeSet<Protocol>(new ProtocolComparator());
		//System.out.println("    "+aBlock.getName());
                	
		// find sampling units
		boolean foundUnit = false;
		List<SamplingUnit> units = aBlock.getSamplingUnits();
		if (!units.isEmpty()) {
                    List<SamplingUnit> qUnits = new ArrayList<SamplingUnit>();
                    for (Iterator k=units.iterator(); k.hasNext(); ) {
			SamplingUnit aUnit = (SamplingUnit)k.next();
			if (uids.contains(aUnit.getId())) {
			    qUnits.add(aUnit);
			    String protocolFamilyName = aUnit.getProtocolFamily().getName();
			    Protocol protocol = aSite.getProtocol(protocolFamilyName); 
			    protocols.add(protocol);
			    cProtocols.add(protocol);
			    foundUnit = true;
			    //System.out.println("         "+aUnit.getName());
			}
		    }			
		    if (foundUnit) {
			cBlock.setSamplingUnits(qUnits);
			cBlock.setProtocols(cProtocols);
			foundBlock = true;
			qBlocks.add(cBlock);
		    }
		}

	    }
	    if (foundBlock) {
		cSite.setBlocks(qBlocks);
		cSite.setProtocols(protocols);
		qSites.add(cSite);
	    }
	}    

	//System.out.println("elapse time = "+(new Date().getTime() - s));  
	return qSites;
         	
    }



    public List<Site> searchSamplingUnits(int[] siteIds, int[] protocolIds, Date start, Date end) throws Exception{

	long s = new Date().getTime();	
	List<Site> sites;
	if (start == null || end == null) {
	    sites = activeSites;
	} else {
	    sites = searchSamplingUnits(start, end);
	}

	//System.out.println("basic query time = "+(new Date().getTime() - s));  

	List<Site> qSites = new ArrayList<Site>();
	for (ListIterator iter = sites.listIterator() ; iter.hasNext() ; ) {
	    Site aSite = (Site)iter.next();
	    
	    boolean foundSite = false;
	    for (int i=0; i<siteIds.length; i++) {
		if (aSite.getId().intValue() == siteIds[i]) {
		    foundSite = true;
		    break;
		}
	    }

	    if (!foundSite) continue;
	    
	    Site cSite = aSite.clone();
	    //System.out.println(aSite.getName());

	    List<Block> blocks = aSite.getBlocks();
	    List<Block> qBlocks = new ArrayList<Block>();
	    Set<Protocol> protocols = new TreeSet<Protocol>(new ProtocolComparator());
	    boolean foundBlock = false;
	    for (Iterator j=blocks.iterator(); j.hasNext(); ) {
		Block aBlock = (Block)j.next();
		Block cBlock = aBlock.clone();
		Set<Protocol> cProtocols = new TreeSet<Protocol>(new ProtocolComparator());
		//System.out.println("    "+aBlock.getName());
                	
		// find sampling units
		boolean foundUnit = false;
		List<SamplingUnit> units = aBlock.getSamplingUnits();
		if (!units.isEmpty()) {
                    List<SamplingUnit> qUnits = new ArrayList<SamplingUnit>();
                    for (Iterator k=units.iterator(); k.hasNext(); ) {
			SamplingUnit aUnit = (SamplingUnit)k.next();
			String protocolFamilyName = aUnit.getProtocolFamily().getName();
			Protocol protocol = aSite.getProtocol(protocolFamilyName); 

			boolean foundProtocol = false;
			for (int i=0; i<protocolIds.length; i++) {
			    if (protocol.getId().intValue() == protocolIds[i]) {
				foundProtocol = true;
				break;
			    }
			}

			if (foundProtocol) {
			    qUnits.add(aUnit);
			    protocols.add(protocol);
			    cProtocols.add(protocol);
			    foundUnit = true;
			    //System.out.println("         "+aUnit.getName());
			}
		    }			
		    if (foundUnit) {
			cBlock.setSamplingUnits(qUnits);
			cBlock.setProtocols(cProtocols);
			foundBlock = true;
			qBlocks.add(cBlock);
		    }
		}

	    }
	    if (foundBlock) {
		cSite.setBlocks(qBlocks);
		cSite.setProtocols(protocols);
		qSites.add(cSite);
	    }
	}    

	//System.out.println("elapse time = "+(new Date().getTime() - s));  
	return qSites;
         	
    }


    public Date getStartTime(WeatherStation station) {
	for (WeatherStationStub stub : weatherStationStubs) {
	    if (station.getId().intValue() == stub.getId().intValue()) {
		return stub.getStart();
	    }
	}
	return null;
    }



    public Date getEndTime(WeatherStation station) {
	for (WeatherStationStub stub : weatherStationStubs) {
	    if (station.getId().intValue() == stub.getId().intValue()) {
		return stub.getEnd();
	    }
	}
	return null;
    }



    public void close() {
	transaction.commit(); 
	//session.close();
    }




 	
    /**
     * create a network from the database
     **/
    public Network(Session session, Transaction transaction) {        

        this.session = session;
        this.transaction = transaction;
        
        /*
        if (activeSites == null) {
	    activeSites = getActiveSites();
	    for (ListIterator iter = this.activeSites.listIterator() ; iter.hasNext() ; ) {
		Site aSite = (Site)iter.next();
		List<Block> blocks = getBlocks(aSite);
		int numberOfUnits = 0;
		for (Iterator j=blocks.iterator(); j.hasNext(); ) {
		    Block aBlock = (Block)j.next();
                	
		    // find sampling units
		    List<SamplingUnit> units = getSamplingUnits(aBlock);
		    aBlock.setSamplingUnits(units);
		}
		aSite.setBlocks(blocks);   
	    }
	}

	if (leaderInstitutions == null) {
	    leaderInstitutions = new ArrayList<Institution>();
	    Query query = session.createQuery("FROM Institution as institution ORDER BY institution.site.id");
	    List records = query.list();
	    Site site = null;
	    for (Iterator i=records.iterator(); i.hasNext(); ) {
		Institution institution = (Institution)i.next();
		if (institution.getSite() != null) {
		    if (site != null) {
			if (site.getId().equals(institution.getSite().getId())) {
			    continue;
			} else {
			    site = institution.getSite();
			    leaderInstitutions.add(institution);
			}
		    } else {
			site = institution.getSite();
			leaderInstitutions.add(institution);
		    }
		}
	    }
        }
	*/

	/*
	if (weatherStationStubs == null) {
	    Query query = session.createQuery("FROM WeatherStationStub");
	    weatherStationStubs = query.list(); 
	}
	*/

    }
   

    public void flush() {
 
	activeSites = getActiveSites();
	for (ListIterator iter = this.activeSites.listIterator() ; iter.hasNext() ; ) {
	    Site aSite = (Site)iter.next();
	    List<Block> blocks = getBlocks(aSite);
	    int numberOfUnits = 0;
	    for (Iterator j=blocks.iterator(); j.hasNext(); ) {
		Block aBlock = (Block)j.next();
                	
		// find sampling units
		List<SamplingUnit> units = getSamplingUnits(aBlock);
		aBlock.setSamplingUnits(units);
	    }
	    aSite.setBlocks(blocks);   
	}


	leaderInstitutions = new ArrayList<Institution>();
	Query query = session.createQuery("FROM Institution as institution ORDER BY institution.site.id");
	List records = query.list();
	Site site = null;
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    Institution institution = (Institution)i.next();
	    if (institution.getSite() != null) {
		if (site != null) {
		    if (site.getId().equals(institution.getSite().getId())) {
			continue;
		    } else {
			site = institution.getSite();
			leaderInstitutions.add(institution);
		    }
		} else {
		    site = institution.getSite();
		    leaderInstitutions.add(institution);
		}
	    }
	}

	query = session.createQuery("FROM WeatherStationStub");
	weatherStationStubs = query.list(); 
    }


    public List getDataEditReviewSummary(String username) {

	Query query = session.createQuery("FROM DataEditReviewSummary ORDER BY site, protocol, block, event");
	List records = query.list(); 	
	return (List<DataEditReviewSummary>)records;    

    }


    public String  getJSONDataEditReviewSummary(String username) {

	StringBuffer sb = new StringBuffer();
	Query query = session.createQuery("FROM DataEditReviewSummary ORDER BY site, protocol, block, event");
	List<DataEditReviewSummary> records = (List<DataEditReviewSummary>)query.list(); 	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
	
	sb.append("{  totalCount: "+records.size()+",\n");
	sb.append("result: [\n");
	
	boolean first = true;
	for (DataEditReviewSummary summary : records) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    
	    sb.append("   {");

	    Integer siteId = summary.getSite();
	    String siteName = "";
	    for (Site site : activeSites) {
		if (site.getId().intValue() == siteId.intValue()) {
		    siteName = site.getName();
		    break;
		}
	    }

	    sb.append("site: '"+siteName+"', ");
	    sb.append("protocol: '"+summary.getProtocol()+"', ");
	    sb.append("block: '"+summary.getBlock()+"', ");
	    sb.append("event: '"+summary.getEvent()+"', ");

	    Date tmp = summary.getReviewDate();
	    if (tmp != null) sb.append("reviewed_at: '"+sdf.format(tmp)+"', ");

	    tmp = summary.getEditDate();
	    if (tmp != null) sb.append("edited_at: '"+sdf.format(tmp)+"' ");

	    sb.append("}");
	}

	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }

   
    public int getSamplingUnitTotalSize(String siteId, String protocolName, String blockName) {

	Query query;
	if (protocolName.equals("Climate")) {
	    if (blockName == null || !blockName.equals("null")) {
		query = session.createQuery("SELECT count(*) FROM WeatherStation AS unit WHERE unit.site.id="+siteId);
	    } else {
		query = session.createQuery("SELECT count(*) FROM WeatherStation AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.name='"+blockName+"' ");
	    }
	} else {
	    if (blockName != null && !blockName.equals("null")) {
		query = session.createQuery("SELECT count(*) FROM SamplingUnit AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.block.name='"+blockName+"' AND "+
					    "unit.protocolFamily.name='"+protocolName+"' ");
	    } else {
		query = session.createQuery("SELECT count(*) FROM SamplingUnit AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.protocolFamily.name='"+protocolName+"' ");
	    }
	}

	List list = query.list();
	return ((Long)list.iterator().next()).intValue();

    }



    public String getJSONSamplingUnits(String siteId, String protocolName, String blockName, 
				       int start, int limit, String sortPath, String dir) {

	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+getSamplingUnitTotalSize(siteId, protocolName, blockName)+",\n");
	sb.append("result: [\n");

	Query query;
	if (protocolName.equals("Climate")) {
	    if (blockName != null && !blockName.equals("null")) {
		System.out.println("FROM WeatherStation AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.name='"+blockName+"' "+
					    "ORDER BY unit."+sortPath+" "+dir);
		query = session.createQuery("FROM WeatherStation AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.name='"+blockName+"' "+
					    "ORDER BY unit."+sortPath+" "+dir);
	    } else {
		System.out.println("FROM WeatherStation AS unit WHERE unit.site.id="+siteId+" "+
				   "ORDER BY unit."+sortPath+" "+dir);

		query = session.createQuery("FROM WeatherStation AS unit WHERE unit.site.id="+siteId+" "+
					    "ORDER BY unit."+sortPath+" "+dir);
	    }
	} else {
	    if (blockName != null && !blockName.equals("null")) {
		System.out.println("FROM SamplingUnit AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.block.name='"+blockName+"' AND "+
					    "unit.protocolFamily.name='"+protocolName+"' "+
					    "ORDER BY unit."+sortPath+" "+dir);
		query = session.createQuery("FROM SamplingUnit AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.block.name='"+blockName+"' AND "+
					    "unit.protocolFamily.name='"+protocolName+"' "+
					    "ORDER BY unit."+sortPath+" "+dir);
	    } else {
		System.out.println("FROM SamplingUnit AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.protocolFamily.name='"+protocolName+"' "+
					    "ORDER BY unit."+sortPath+" "+dir);
		query = session.createQuery("FROM SamplingUnit AS unit WHERE unit.site.id="+siteId+" AND "+
					    "unit.protocolFamily.name='"+protocolName+"' "+
					    "ORDER BY unit."+sortPath+" "+dir);
	    }
	}

	query.setFirstResult(start);
	query.setMaxResults(limit);
	List<SamplingUnit> records = (List<SamplingUnit>)query.list();

	boolean first = true;
	for (SamplingUnit unit : records) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    
	    sb.append("   {");
	    
	    sb.append("id: '"+unit.getId()+"', ");

	    String unitName = unit.getName();
	    if (unitName != null) {
		sb.append("unit_name: '"+unitName+"', ");
	    }

	    if (unit instanceof WeatherStation) {
		sb.append("unit_type: 'Weather Station', ");
	    } else if (unit instanceof CameraTrap) {
		sb.append("unit_type: 'Camera Trap', ");
	    } else if (unit instanceof BirdPoint) {
		sb.append("unit_type: 'Bird Point', ");
	    } else if (unit instanceof ButterflyTrap) {
		sb.append("unit_type: 'Butterfly Trap', ");
	    } else if (unit instanceof PrimatePoint) {
		sb.append("unit_type: 'Primate Point', ");
	    } else if (unit instanceof LianaPoint) {
		sb.append("unit_type: 'Liana Point', ");
	    } else if (unit instanceof LitterfallTrap) {
		sb.append("unit_type: 'Litterfall Trap', ");
	    } else if (unit instanceof TreePoint) {
		sb.append("unit_type: 'Tree Point', ");
	    } else if (unit instanceof LianaPoint) {
		sb.append("unit_type: 'Liana Point', ");
	    } 

	    Double plat = unit.getProposedLatitude();
	    if (plat != null) {
		sb.append("proposed_latitude: '"+plat+"', ");
	    }

	    Double plon = unit.getProposedLongitude();
	    if (plon != null) {
		sb.append("proposed_longitude: '"+plon+"', ");
	    }

	    Double lat = unit.getLatitude();
	    if (lat != null) {
		sb.append("latitude: '"+lat+"', ");
	    }

	    Double lon = unit.getLongitude();
	    if (lon != null) {
		sb.append("longitude: '"+lon+"', ");
	    }

	    if (unit.getBlock() != null) {
		sb.append("ima_name: '"+unit.getBlock().getName()+"', ");
	    }

	    Integer x = unit.getImaXCoordinate();
	    if (x != null) {
		sb.append("ima_x_coordinate: '"+x+"', ");
	    }

	    Integer y = unit.getImaYCoordinate();
	    if (y != null) {
		sb.append("ima_y_coordinate: '"+y+"', ");
	    }

	    String method = unit.getMethod();
	    if (method != null) {
		sb.append("method_team: '"+method+"', ");
	    }

	    Date gpsDate = unit.getGpsReadingDate();
	    if (gpsDate != null) {
		sb.append("gps_reading_date: '"+gpsDate+"', ");
	    }

	    Integer gpsNumberOfReading = unit.getGpsNumberOfReading();
	    if (gpsNumberOfReading != null) {
		sb.append("gps_number_of_reading: '"+gpsNumberOfReading+"', ");
	    }

	    String gpsNotes = unit.getGpsNotes();
	    if (gpsNotes != null) {
		sb.append("gps_notes: '"+gpsNotes+"', ");
	    }

	    String gpsReadingFirstName = unit.getGpsReadingFirstName();
	    String gpsReadingLastName = unit.getGpsReadingLastName();
            if (gpsReadingFirstName != null && gpsReadingLastName != null) {
		sb.append("gps_reading_person: '"+gpsReadingLastName+", "+gpsReadingFirstName+"', ");
	    }

	    String model = unit.getGpsModel();
	    if (model != null) {
		sb.append("gps_make_and_model: '"+model+"', ");
	    }
	    
	    sb.append("pad: ''");
	    sb.append("}");
	}
	
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }



    public int getBlockTotalSize(String siteId) {

	Query query = session.createQuery("SELECT count(*) FROM Block AS unit WHERE unit.site.id="+siteId);
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();

    }



    public int getPlotTotalSize(String siteId) {

	Query query = session.createQuery("SELECT count(*) FROM Block AS unit WHERE unit.type='VG' and unit.site.id="+siteId);
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();

    }



    public int getPureBlockTotalSize(String siteId) {

	Query query = session.createQuery("SELECT count(*) FROM Block AS unit WHERE unit.type='BLK' and unit.site.id="+siteId);
	List list = query.list();
	return ((Long)list.iterator().next()).intValue();

    }




    public String getJSONBlocks(String siteId, int start, int limit, String sortPath, String dir) {

	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+getBlockTotalSize(siteId)+",\n");
	sb.append("result: [\n");

	Query query = session.createQuery("FROM Block AS block WHERE block.site.id="+siteId+
					  " ORDER BY block."+sortPath+" "+dir);

	query.setFirstResult(start);
	query.setMaxResults(limit);
	List<Block> records = (List<Block>)query.list();

	boolean first = true;
	for (Block block : records) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    
	    sb.append("   {");
	    
	    sb.append("id: '"+block.getId()+"', ");
	    sb.append("index: '"+block.getIndex()+"', ");
	    
	    String blockName = block.getName();
	    if (blockName != null) {
		sb.append("name: '"+blockName+"', ");
	    }

	    Double plat = block.getProposedLatitude();
	    if (plat != null) {
		sb.append("proposed_point1_latitude: '"+plat+"', ");
	    }

	    Double plon = block.getProposedLongitude();
	    if (plon != null) {
		sb.append("proposed_point1_longitude: '"+plon+"', ");
	    }

	    Double lat = block.getPoint1Latitude();
	    if (lat != null) {
		sb.append("point1_latitude: '"+lat+"', ");
	    }

	    Double lon = block.getPoint1Longitude();
	    if (lon != null) {
		sb.append("point1_longitude: '"+lon+"', ");
	    }

	    Date gpsDate = block.getGpsReadingDate();
	    if (gpsDate != null) {
		sb.append("gps_reading_date: '"+gpsDate+"', ");
	    }

	    Integer gpsNumberOfReading = block.getGpsNumberOfReading();
	    if (gpsNumberOfReading != null) {
		sb.append("gps_number_of_reading: '"+gpsNumberOfReading+"', ");
	    }

	    String gpsNotes = block.getGpsNotes();
	    if (gpsNotes != null) {
		sb.append("gps_notes: '"+gpsNotes+"', ");
	    }

	    String gpsReadingFirstName = block.getGpsReadingFirstName();
	    String gpsReadingLastName = block.getGpsReadingLastName();
            if (gpsReadingFirstName != null && gpsReadingLastName != null) {
		sb.append("gps_reading_person: '"+gpsReadingLastName+", "+gpsReadingFirstName+"', ");
	    }

	    String model = block.getGpsModel();
	    if (model != null) {
		sb.append("gps_make_and_model: '"+model+"', ");
	    }

            Float bearing0100 = block.getBearing0100();
            if (bearing0100 != null) {
                sb.append("bearing_0_100: '"+bearing0100+"', ");
            }

            Float bearing1000 = block.getBearing1000();
            if (bearing1000 != null) {
                sb.append("bearing_100_0: '"+bearing1000+"', ");
            }

            Date installFrom = block.getInstallFrom();
            if (installFrom != null) {
                sb.append("install_from: '"+installFrom+"', ");
            }

            Date installTo = block.getInstallTo();
            if (installTo != null) {
                sb.append("install_to: '"+installTo+"', ");
            }

            sb.append("pad: '' ");

	    sb.append("}");
	}
	
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }




    public String getJSONPlots(String siteId, int start, int limit, String sortPath, String dir) {

	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+getPlotTotalSize(siteId)+",\n");
	sb.append("result: [\n");

	Query query = session.createQuery("FROM Block AS block WHERE block.type='VG' and  block.site.id="+siteId+
					  " ORDER BY block."+sortPath+" "+dir);

	query.setFirstResult(start);
	query.setMaxResults(limit);
	List<Block> records = (List<Block>)query.list();

	boolean first = true;
	for (Block block : records) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    
	    sb.append("   {");
	    
	    sb.append("id: '"+block.getId()+"', ");
	    sb.append("index: '"+block.getIndex()+"', ");
	    
	    String blockName = block.getName();
	    if (blockName != null) {
		sb.append("name: '"+blockName+"', ");
	    }

	    Double plat = block.getProposedLatitude();
	    if (plat != null) {
		sb.append("proposed_point1_latitude: '"+plat+"', ");
	    }

	    Double plon = block.getProposedLongitude();
	    if (plon != null) {
		sb.append("proposed_point1_longitude: '"+plon+"', ");
	    }

	    Double lat = block.getPoint1Latitude();
	    if (lat != null) {
		sb.append("point1_latitude: '"+lat+"', ");
	    }

	    Double lon = block.getPoint1Longitude();
	    if (lon != null) {
		sb.append("point1_longitude: '"+lon+"', ");
	    }

	    Date gpsDate = block.getGpsReadingDate();
	    if (gpsDate != null) {
		sb.append("gps_reading_date: '"+gpsDate+"', ");
	    }

	    Integer gpsNumberOfReading = block.getGpsNumberOfReading();
	    if (gpsNumberOfReading != null) {
		sb.append("gps_number_of_reading: '"+gpsNumberOfReading+"', ");
	    }

	    String gpsNotes = block.getGpsNotes();
	    if (gpsNotes != null) {
		sb.append("gps_notes: '"+gpsNotes+"', ");
	    }

	    String gpsReadingFirstName = block.getGpsReadingFirstName();
	    String gpsReadingLastName = block.getGpsReadingLastName();
            if (gpsReadingFirstName != null && gpsReadingLastName != null) {
		sb.append("gps_reading_person: '"+gpsReadingLastName+", "+gpsReadingFirstName+"', ");
	    }

	    String model = block.getGpsModel();
	    if (model != null) {
		sb.append("gps_make_and_model: '"+model+"', ");
	    }

            Float bearing0100 = block.getBearing0100();
            if (bearing0100 != null) {
                sb.append("bearing_0_100: '"+bearing0100+"', ");
            }

            Float bearing1000 = block.getBearing1000();
            if (bearing1000 != null) {
                sb.append("bearing_100_0: '"+bearing1000+"', ");
            }

            Date installFrom = block.getInstallFrom();
            if (installFrom != null) {
                sb.append("install_from: '"+installFrom+"', ");
            }

            Date installTo = block.getInstallTo();
            if (installTo != null) {
                sb.append("install_to: '"+installTo+"', ");
            }

            sb.append("pad: '' ");

	    sb.append("}");
	}
	
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }




    public String getJSONPureBlocks(String siteId, int start, int limit, String sortPath, String dir) {

	StringBuffer sb = new StringBuffer();
	sb.append("{  totalCount: "+getPureBlockTotalSize(siteId)+",\n");
	sb.append("result: [\n");

	Query query = session.createQuery("FROM Block AS block WHERE block.type='BLK' and  block.site.id="+siteId+
					  " ORDER BY block."+sortPath+" "+dir);

	query.setFirstResult(start);
	query.setMaxResults(limit);
	List<Block> records = (List<Block>)query.list();

	boolean first = true;
	for (Block block : records) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    
	    sb.append("   {");
	    
	    sb.append("id: '"+block.getId()+"', ");
	    sb.append("index: '"+block.getIndex()+"', ");
	    
	    String blockName = block.getName();
	    if (blockName != null) {
		sb.append("name: '"+blockName+"', ");
	    }

	    Double plat = block.getProposedLatitude();
	    if (plat != null) {
		sb.append("proposed_point1_latitude: '"+plat+"', ");
	    }

	    Double plon = block.getProposedLongitude();
	    if (plon != null) {
		sb.append("proposed_point1_longitude: '"+plon+"', ");
	    }

	    Double lat = block.getPoint1Latitude();
	    if (lat != null) {
		sb.append("point1_latitude: '"+lat+"', ");
	    }

	    Double lon = block.getPoint1Longitude();
	    if (lon != null) {
		sb.append("point1_longitude: '"+lon+"', ");
	    }

	    Date gpsDate = block.getGpsReadingDate();
	    if (gpsDate != null) {
		sb.append("gps_reading_date: '"+gpsDate+"', ");
	    }

	    Integer gpsNumberOfReading = block.getGpsNumberOfReading();
	    if (gpsNumberOfReading != null) {
		sb.append("gps_number_of_reading: '"+gpsNumberOfReading+"', ");
	    }

	    String gpsNotes = block.getGpsNotes();
	    if (gpsNotes != null) {
		sb.append("gps_notes: '"+gpsNotes+"', ");
	    }

	    String gpsReadingFirstName = block.getGpsReadingFirstName();
	    String gpsReadingLastName = block.getGpsReadingLastName();
            if (gpsReadingFirstName != null && gpsReadingLastName != null) {
		sb.append("gps_reading_person: '"+gpsReadingLastName+", "+gpsReadingFirstName+"', ");
	    }

	    String model = block.getGpsModel();
	    if (model != null) {
		sb.append("gps_make_and_model: '"+model+"', ");
	    }

            Float bearing0100 = block.getBearing0100();
            if (bearing0100 != null) {
                sb.append("bearing_0_100: '"+bearing0100+"', ");
            }

            Float bearing1000 = block.getBearing1000();
            if (bearing1000 != null) {
                sb.append("bearing_100_0: '"+bearing1000+"', ");
            }

            Date installFrom = block.getInstallFrom();
            if (installFrom != null) {
                sb.append("install_from: '"+installFrom+"', ");
            }

            Date installTo = block.getInstallTo();
            if (installTo != null) {
                sb.append("install_to: '"+installTo+"', ");
            }

            sb.append("pad: '' ");

	    sb.append("}");
	}
	
	sb.append("]\n");
	sb.append("}\n");
	return sb.toString();
    }


    public SpatialUploadLog getSpatialUploadLog(String siteId, String protocol) {
	Query query = session.createQuery("FROM SpatialUploadLog as log WHERE log.siteId="+siteId+" AND log.protocol='"+protocol+"'" );
	List records = query.list(); 	
	if (!records.isEmpty()) {
	    return (SpatialUploadLog)records.iterator().next();
	}
	return null;
    }



    public void deleteSpatialUpload(String siteId, String protocol, String path) {

	Transaction tx = transaction;
	try {
	    if (protocol.equals("Terrestrial Vertebrate")) {
		Query query = session.createQuery("FROM CameraTrap as unit WHERE unit.site.id="+siteId);
		List records = query.list();
		for (Iterator i=records.iterator(); i.hasNext(); ) {
		    CameraTrap trap = (CameraTrap)i.next();
		    System.out.println("----> delete = "+trap);
		    session.delete(trap);
		}
	    } else if (protocol.equals("Climate")) {
		Query query = session.createQuery("FROM WeatherStation as unit WHERE unit.site.id="+siteId);
		List records = query.list();
		for (Iterator i=records.iterator(); i.hasNext(); ) {
		    WeatherStation trap = (WeatherStation)i.next();
		    System.out.println("----> delete = "+trap);
		    session.delete(trap);
		}
	    } else if (protocol.equals("Vegetation - Trees & Lianas")) {
		Query query = session.createQuery("FROM Block as unit WHERE unit.site.id="+siteId);
		List records = query.list();
		for (Iterator i=records.iterator(); i.hasNext(); ) {
		    Block block = (Block)i.next();
		    System.out.println("----> delete = "+block);
		    session.delete(block);
		}

		query = session.createQuery("FROM BlockEditLog as log WHERE log.site="+siteId);
		List logs = query.list();
		for (Iterator i=logs.iterator(); i.hasNext(); ) {
		    BlockEditLog log = (BlockEditLog)i.next();
		    session.delete(log);
		}

	    }

	    Query query = session.createQuery("FROM SpatialUploadLog as log WHERE log.siteId="+siteId+" AND log.protocol='"+protocol+"'" );
	    List logs = query.list();
	    for (Iterator i=logs.iterator(); i.hasNext(); ) {
		SpatialUploadLog log = (SpatialUploadLog)i.next();
		session.delete(log);
	    }

	    query = session.createQuery("FROM SpatialEditLog as log WHERE log.site="+siteId+" AND log.protocol='"+protocol+"'" );
	    logs = query.list();
            for (Iterator i=logs.iterator(); i.hasNext(); ) {
                SpatialEditLog log = (SpatialEditLog)i.next();
                session.delete(log);
            }

	    // delete shapefiles
	    String dirName = path + "/data/shapefiles/"+siteId+"/"+protocol.replaceAll(" ", "_");
	    File dir = new File (dirName);
	    String[] files = dir.list();
	    for (int i=0; i<files.length; i++) {
		File tmp = new File(dir, files[i]);
		tmp.delete();
	    }
	    dir.delete();

	    // delete gpx
	    dirName = path + "/data/gpx/"+siteId+"/"+protocol.replaceAll(" ", "_");
	    dir = new File (dirName);
	    if (dir.exists()) {
		files = dir.list();
		for (int i=0; i<files.length; i++) {
		    File tmp = new File(dir, files[i]);
		    tmp.delete();
		}
		dir.delete();
	    }

        } catch (Exception  ex) {
            if (tx!=null) {
                tx.rollback();
	        close();
            }
        }

    }


    public void deleteDataUploadLog(String id) {
	Query query = session.createQuery("FROM DataUploadLog as log WHERE log.excelId="+id );
	List logs = query.list();
	for (Iterator i=logs.iterator(); i.hasNext(); ) {
	    DataUploadLog log = (DataUploadLog)i.next();
	    session.delete(log);
	}
    }


    public void delete(PhotoSpeciesRecord record) throws Exception {
	Transaction tx = transaction;
	try {
	    session.delete(record);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void delete(CameraTrapPhotoCollection collection) throws Exception {
	Transaction tx = transaction;
	try {
	    session.delete(collection);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public List getTaxonomyFamilyNames(String string) {

	Query query = session.createQuery("SELECT DISTINCT family FROM Taxonomy as taxa "+
					  "WHERE lower(taxa.family) like '"+string.toLowerCase()+"%' order by family");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String family = (String)i.next();
	    family = family.substring(0,1).toUpperCase()+family.substring(1).toLowerCase();
	    result.add(family);
	}
	return result;

    }



    public List getTaxonomyGenusNames(String family, String string) {

	Query query = session.createQuery("SELECT DISTINCT genus FROM Taxonomy as taxa "+
					  "WHERE lower(taxa.family) = '"+family.toLowerCase()+"' "+
					  "AND lower(taxa.genus) LIKE '"+string.toLowerCase()+"%' order by genus");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String genus = (String)i.next();
	    genus = genus.substring(0,1).toUpperCase()+genus.substring(1).toLowerCase();
	    result.add(genus);
	}
	return result;

    }






    public List getTaxonomySpeciesNames(String family, String genus, String string) {

	Query query = session.createQuery("SELECT DISTINCT species FROM Taxonomy as taxa "+
					  "WHERE lower(taxa.family) = '"+family.toLowerCase()+"' "+
					  "AND lower(taxa.genus) = '"+genus.toLowerCase()+"' "+
					  "AND lower(taxa.species) LIKE '"+string.toLowerCase()+"%' order by species");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String species = (String)i.next();
	    result.add(species.toLowerCase());
	}
	return result;

    }




    public List getGenusNames(String string) {

	Query query = session.createQuery("SELECT DISTINCT genus FROM GenusSpecies as gs "+
					  "WHERE lower(gs.genus) like '"+string.toLowerCase()+"%' order by genus");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String genus = (String)i.next();
	    genus = genus.substring(0,1).toUpperCase()+genus.substring(1).toLowerCase();
	    result.add(genus);
	}
	return result;

    }



    public List getVegetationFamilies(String string) {
	Query query = session.createQuery("SELECT DISTINCT family FROM TreeRecord as gs "+
					  "WHERE lower(gs.family) like '"+string.toLowerCase()+"%' order by family");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String family = (String)i.next();
	    family = family.substring(0,1).toUpperCase()+family.substring(1).toLowerCase();
	    result.add(family);
	}
	return result;
    }



    public List getVegetationGenuses(String family, String string) {
	Query query = session.createQuery("SELECT DISTINCT genus FROM TreeRecord as taxa "+
					  "WHERE lower(taxa.family) = '"+family.toLowerCase()+"' "+
					  "AND lower(taxa.genus) LIKE '"+string.toLowerCase()+"%' order by genus");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String genus = (String)i.next();
            genus = genus.substring(0,1).toUpperCase()+genus.substring(1).toLowerCase();
	    result.add(genus);
	}
	return result;
    }


    public List getVegetationSpecies(String family, String genus, String string) {
	Query query = session.createQuery("SELECT DISTINCT species FROM TreeRecord as taxa "+
					  "WHERE lower(taxa.family) = '"+family.toLowerCase()+"' "+
					  "  AND lower(taxa.genus) = '"+genus.toLowerCase()+"' "+
					  "  AND lower(taxa.species) LIKE '"+string.toLowerCase()+"%' order by species");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String species = (String)i.next();
	    result.add(species.toLowerCase());
	}
	return result;
    }



    public boolean hasGenus(String string) {

	return getGenusNames(string).size() != 0;

    }



    public List getSpeciesNames(String genus, String string) {

	Query query = session.createQuery("SELECT DISTINCT species FROM GenusSpecies as taxa "+
					  "WHERE lower(taxa.genus) = '"+genus.toLowerCase()+"' "+
					  "AND lower(taxa.species) LIKE '"+string.toLowerCase()+"%' order by species");
	List records = query.list();
	List result = new ArrayList();
	for (Iterator i=records.iterator(); i.hasNext(); ) {
	    String species = (String)i.next();
	    result.add(species.toLowerCase());
	}
	return result;

    }



    public boolean hasSpecies(String genus, String string) {

	return getSpeciesNames(genus, string).size() != 0;

    }


    public String createJSONDataEditLog(List<DataEditLog> list, int totalSize) {

	List<Site> sites = getActiveSites();
	StringBuffer sb = new StringBuffer();
	sb.append("{ totalCount: "+totalSize+",\n");
	sb.append("  result: [\n");

	boolean first = true;
	for (DataEditLog record : list) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    sb.append("   {");
	    sb.append("id: '"+record.getId()+"', ");
	    sb.append("username: '"+record.getUsername()+"', ");
	    sb.append("date: '"+record.getDate()+"', ");
	    sb.append("site: '"+getSiteName(sites, record.getSite())+"', ");
	    sb.append("protocol: '"+record.getProtocol()+"', ");
	    sb.append("block: '"+record.getBlock()+"', ");
	    sb.append("event: '"+record.getEvent()+"', ");
	    sb.append("dataid: '"+record.getDataId()+"', ");
	    sb.append("column: '"+record.getColumn()+"', ");
	    sb.append("newvalue: '"+record.getNewValue()+"', ");
	    sb.append("oldvalue: '"+record.getOldValue()+"', ");

	    if (record.getColumn().startsWith("Review")) {
		sb.append("recover_it: 'r"+record.getId()+"' ");
	    } else {
		sb.append("recover_it: 'e"+record.getId()+"' ");
	    }
	    sb.append("}");
	}

	sb.append("  ]\n");
        sb.append("}\n");

	return sb.toString();
    }



    private String getSiteName(List<Site> sites, Integer id) {
	for (Site site : sites) {
	    if (site.getId().equals(id)) {
		return site.getName();
	    }
	}
	return null;
    }




    public String createJSONSpatialEditLog(List<SpatialEditLog> list) {

	List<Site> sites = getSites();
	StringBuffer sb = new StringBuffer();
	sb.append("{ totalCount: "+list.size()+",\n");
	sb.append("  result: [\n");

	boolean first = true;
	for (SpatialEditLog record : list) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    sb.append("   {");
	    sb.append("id: '"+record.getId()+"', ");
	    sb.append("username: '"+record.getUsername()+"', ");
	    sb.append("date: '"+record.getDate()+"', ");
	    sb.append("site: '"+getSiteShortName(sites, record.getSite())+"', ");
	    sb.append("protocol: '"+record.getProtocol()+"', ");

	    if (record.getBlock() != null) {
		sb.append("block: '"+record.getBlock()+"', ");
	    }

	    sb.append("dataid: '"+record.getDataId()+"', ");
	    sb.append("column: '"+record.getColumnHead()+"', ");

	    String newvalue = record.getNewValue();
	    if (newvalue != null) sb.append("newvalue: '"+newvalue+"', ");

	    String oldvalue = record.getOldValue();
	    if (oldvalue != null) sb.append("oldvalue: '"+oldvalue+"', ");

	    sb.append("recover_it: 'e"+record.getId()+"' ");

	    sb.append("}");
	}

	sb.append("  ]\n");
        sb.append("}\n");
	return sb.toString();
    }



    public String createJSONBlockEditLog(List<BlockEditLog> list) {

	List<Site> sites = getSites();
	StringBuffer sb = new StringBuffer();
	sb.append("{ totalCount: "+list.size()+",\n");
	sb.append("  result: [\n");

	boolean first = true;
	for (BlockEditLog record : list) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    sb.append("   {");
	    sb.append("id: '"+record.getId()+"', ");
	    sb.append("username: '"+record.getUsername()+"', ");
	    sb.append("date: '"+record.getDate()+"', ");
	    sb.append("site: '"+getSiteShortName(sites, record.getSite())+"', ");
	    sb.append("block_name: '"+record.getBlockName()+"', ");
	    sb.append("block_id: '"+record.getBlockId()+"', ");
	    sb.append("column: '"+record.getColumnHead()+"', ");

	    String newvalue = record.getNewValue();
	    if (newvalue != null) sb.append("newvalue: '"+newvalue+"', ");

	    String oldvalue = record.getOldValue();
	    if (oldvalue != null) sb.append("oldvalue: '"+oldvalue+"', ");

	    sb.append("recover_it: 'e"+record.getId()+"' ");

	    sb.append("}");
	}

	sb.append("  ]\n");
        sb.append("}\n");
	return sb.toString();
    }


    public String createJSONSpatialUploadLog(List<SpatialUploadLog> list) {

	List<Site> sites = getSites();
	StringBuffer sb = new StringBuffer();
	sb.append("{ totalCount: "+list.size()+",\n");
	sb.append("  result: [\n");

	boolean first = true;
	for (SpatialUploadLog record : list) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    sb.append("   {");
	    sb.append("id: '"+record.getId()+"', ");
	    sb.append("site: '"+getSiteShortName(sites, record.getSiteId())+"', ");
	    sb.append("siteId: '"+record.getSiteId()+"', ");
	    sb.append("protocol: '"+record.getProtocol()+"', ");

	    sb.append("proposeUploaded: '"+record.isProposeUploaded()+"', ");
	    sb.append("proposeUploadTime: '"+record.getProposeUploadTime()+"', ");
	    sb.append("proposeUploadUsername: '"+record.getProposeUploadUsername()+"'");

	    Boolean bool = record.isGpxUploaded();
	    if (bool != null) {
		sb.append(", gpxUploaded: '"+bool+"'");
	    }

	    String date = record.getGpxUploadTime();
	    if (date != null) {
		sb.append(", gpxUploadTime: '"+date+"'");
	    }

	    String username = record.getGpxUploadUsername();
	    if (username != null) {
		sb.append(", gpxUploadUsername: '"+username+"'");
	    }

	    sb.append("}");
	}

	sb.append("  ]\n");
        sb.append("}\n");
	return sb.toString();
    }




    public String createJSONDataUploadLog(List<DataUploadLog> list) {

	List<Site> sites = getSites();
	StringBuffer sb = new StringBuffer();
	sb.append("{ totalCount: "+list.size()+",\n");
	sb.append("  result: [\n");

	boolean first = true;
	for (DataUploadLog record : list) {
	    if (first) {
		first = false;
	    } else {
		sb.append(",\n");
	    }
	    sb.append("   {");
	    sb.append("id: '"+record.getId()+"', ");
	    sb.append("username: '"+record.getUsername()+"', ");
	    sb.append("site_name: '"+getSiteShortName(sites, record.getSiteId())+"', ");
	    sb.append("site_id: '"+record.getSiteId()+"', ");
	    sb.append("protocol: '"+record.getProtocol()+"', ");
	    sb.append("excel_id: '"+record.getExcelId()+"', ");
	    sb.append("filename: '"+record.getFilename()+"', ");
	   
	    String saveDate = record.getSaveDate();
	    if (saveDate != null) {
		sb.append("save_date: '"+saveDate+"',");
	    }

	    String uploadDate = record.getUploadDate();
	    if (uploadDate != null) {
		sb.append("upload_date: '"+uploadDate+"',");
	    }

	    sb.append("action: '"+(saveDate != null)+"'");

	    sb.append("}");
	}

	sb.append("  ]\n");
        sb.append("}\n");
	return sb.toString();
    }



    private String getSiteShortName(List<Site> sites, Integer id) {
	for (Site site : sites) {
	    if (site.getId().equals(id)) {
		return site.getShortName();
	    }
	}
	return null;
    }



    public void save(PhotoSpeciesRecord record) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(record);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void save(GenusSpecies genus) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(genus);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void update(PhotoSpeciesRecord record) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(record);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void update(Block record) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(record);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void update(CameraTrapPhoto photo) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(photo);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void update(DamagedCameraTrap damage) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(damage);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void update(CameraTrapPhotoCollection collection) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(collection);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void save(ClimateRecord record) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(record);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void save(Collection collection) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(collection);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void update(Collection collection) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(collection);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void save(Collectionship collectionship) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(collectionship);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void update(DataUploadLog log) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(log);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void save(SamplingUnit unit) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(unit);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void save(SamplingEvent unit) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(unit);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void update(SamplingUnit unit) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(unit);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void update(SamplingEvent unit) throws Exception{
	Transaction tx = transaction;
	try {
	    session.update(unit);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }


    public void save(TreeRecord record) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(record);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void save(LianaRecord record) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(record);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void save(PlotMetadata pm) throws Exception{
	Transaction tx = transaction;
	try {
	    session.save(pm);
	}  catch (Exception e) {
	    if (tx!=null) tx.rollback();
	    throw e;
	}
    }



    public void evict(Object object) {
	session.evict(object);
    }



    public Integer getNextTreeRecordId() {
	Query query = session.createSQLQuery("SELECT nextval('treeserial')");
	List list = query.list();
	return new Integer(((java.math.BigInteger)list.get(0)).intValue());
    }


    public Integer getNextLianaRecordId() {
	Query query = session.createSQLQuery("SELECT nextval('lianaserial')");
	List list = query.list();
	return new Integer(((java.math.BigInteger)list.get(0)).intValue());
    }


    public DataManagementEvent getDataUploadEvent(int siteId, int familyId) {

    	DataManagementEvent event = null;

        Query query = session.createQuery("FROM DataManagementEvent as event WHERE "+
					  "event.name='Data Upload' AND "+
					  "event.site.id="+siteId+" AND "+
					  "event.protocolFamily.id="+familyId);
        List<DataManagementEvent> events = query.list();
        if (!events.isEmpty()) {
	    event = events.get(0);
        }
        return event;
    }



    public void getTemporalCoverage(Protocol protocol, Set<Site> sites, Date[] dates) {

	String siteCond = null;
	for (Site site : sites) {
	    if (siteCond == null) {
		siteCond = "record.collectionship.samplingUnit.site.id="+site.getId();
	    } else {
		siteCond += " OR record.collectionship.samplingUnit.site.id="+site.getId();
	    }
	}

	String queryString = null;
	if (protocol.getName().equals("Avian")) {
	    queryString = "SELECT min(record.observedAt), max(record.observedAt) FROM AvianRecord as record WHERE "+siteCond;
	} else if (protocol.getName().equals("Butterfly")) {
	    queryString = "SELECT min(record.observedAt), max(record.observedAt) FROM ButterflyRecord as record WHERE "+siteCond;
	} else if (protocol.getName().equals("Primate")) {
	    queryString = "SELECT min(record.observedAt), max(record.observedAt) FROM PrimateRecord as record WHERE "+siteCond;
	} else if (protocol.getName().equals("Vegetation - Litterfall")) {
	    queryString = "SELECT min(record.collectedStart), max(record.collectedStart) FROM LitterfallRecord as record WHERE "+siteCond;
	} else if (protocol.getName().equals("Vegetation - Trees & Lianas")) {
	    siteCond = null;
	    for (Site site : sites) {
		if (siteCond == null) {
		    siteCond = "record.samplingUnit.site.id="+site.getId();
		} else {
		    siteCond += " OR record.samplingUnit.site.id="+site.getId();
		}
	    }	    
	    queryString = "SELECT min(record.collectedAt), max(record.collectedAt) FROM TreeRecord as record WHERE "+siteCond;
	} else if (protocol.getName().equals("Climate") ) {
	    queryString = "SELECT min(record.collectedAt), max(record.collectedAt) FROM ClimateRecord as record WHERE "+siteCond;
	}

	Query query = session.createQuery(queryString);
	List values = query.list();

	for (int i = 0; i<values.size(); i++) { 
	    Object[] objs = (Object[])values.get(i);
	    dates[0] = (Date)objs[0];
	    dates[1] = (Date)objs[1];
	}  

    }



    public String validateExportCameraTrapPhotos(String event, CameraTrap trap) {

	String result = "{";
	result += " trap: '"+trap.getName()+"'\n";

	boolean hasError = false;

	// check the data has been imported
	int totalSize = getCameraTrapPhotoTotalSize(trap, event);
	if ( totalSize > 0) {

	    CameraTrapPhotoCollection collection = getCameraTrapPhotoCollection(event, trap.getName());
	    Boolean working = collection.getWorking();
	    if (working != null && !working.booleanValue()) {
		return null;
	    }

	    Boolean missing = collection.getTrapMissing();
	    if (missing != null && !missing.booleanValue()) {
		return null;
	    }

	    result += ",imported: true \n";

	    // check Start and End photo
	    if (getStartPhoto(trap.getName(), event) == null) {
		result += ",start: false \n";
		hasError = true;
	    } else {
                result += ",start: true \n";
	    }

	    if (getEndPhoto(trap.getName(), event) == null) {
		result += ",end: false \n";
		hasError = true;
	    } else {
		result += ",end: true \n";
	    }

	    // check the pictures haven't been annotated
	    int unprocessedSize = getUnprocessedPhotoTotalSize(trap, event);
	    if (unprocessedSize > 0) {
		result += ",unprocessed: "+unprocessedSize+"\n ";
		hasError = true;
	    }

	} else {
	    //result = "No photos for the camera trap "+trap.getName()+".";

	    // check whether this is a damaged camera trap


	    result += ",imported: false \n";
	    hasError = true;
	}

	result += "}";

	if (hasError) {
	    return result;
	} else {
	    return null;
	}
    }


    public Session getSession() {
	return session;
    }


    /**
     *
     *  Test method
     *
     **/
    public static void main(String[] args) throws Exception {
        
    	Network network = NetworkFactory.newInstance();
    	
        try {
         
    	    System.out.println("=======================================");
	    System.out.println("==  All Active Sites in The Network  ==");
	    System.out.println("=======================================");
            //List<Site> sites = network.getActiveSites();
            List<Site> sites = network.activeSites;
            for (ListIterator iter = sites.listIterator() ; iter.hasNext() ; ) {
            
            	System.out.println("------------------------------------");
		System.out.println("--         A TEAM Site            --");
            	System.out.println("------------------------------------");
                Site aSite = (Site)iter.next();
                System.out.println(aSite.getName());
                
                // get protocol information
                Set<Protocol> protocols = aSite.getProtocols();
                for (Iterator j=protocols.iterator(); j.hasNext(); ) {
		    Protocol aProtocol = (Protocol)j.next();
		    System.out.print("    "+aProtocol.getName()+" "+aProtocol.getVersion()+" (");
                	
		    // find the lead scientist
		    List<LeadShip> leadships = network.getLeaderships(aSite, aProtocol); 
		    boolean first = true;
		    for (Iterator k=leadships.iterator(); k.hasNext(); ) {
			LeadShip leadship = (LeadShip)k.next();
			Person person = network.getPerson(leadship);
			if (!first) {
			    System.out.print(", ");
			} else {
			    first = false;
			}
			System.out.print(person.getFirstName()+" "+person.getLastName());
		    }
		    System.out.println(")");
                }
                
                
                // get block information
                List<Block> blocks = aSite.getBlocks();
                int numberOfUnits = 0;
                for (Iterator j=blocks.iterator(); j.hasNext(); ) {
		    Block aBlock = (Block)j.next();
		    System.out.println("    "+aBlock.getName());
                	
		    // find sampling units
		    List<SamplingUnit> units = aBlock.getSamplingUnits();
		    if (!units.isEmpty()) {
			System.out.println("    sampling units"); 
                    	for (Iterator k=units.iterator(); k.hasNext(); ) {
			    SamplingUnit aUnit = (SamplingUnit)k.next();
			    System.out.print("         "+aUnit.getName());
			    if (aUnit.getLatitude() != null) {
				numberOfUnits++;
				System.out.println("["+aUnit.getLatitude()+", "+aUnit.getLongitude()+"]");
			    } else {
				System.out.println("");
			    }
			}
		    }
        		  
                }
                System.out.println("    sampling units = "+numberOfUnits);
                
                // get institutions
                System.out.println("    Institutions:");
                List<Institution> institutions = network.getInstitutions(aSite);
                for (Iterator j=institutions.iterator(); j.hasNext(); ) {
		    Institution aInstitution = (Institution)j.next();
		    System.out.println("        "+aInstitution.getName());
                }
                
            }


    	    System.out.println("=======================================");
	    System.out.println("== Get blocks for a protocol at site ==");
	    System.out.println("=======================================");

	    // get Volcan Barva site		    
            Site aSite = network.getSiteById(5);
            System.out.println(aSite);
                
            // get climate protocol    
            Protocol aProtocol = aSite.getProtocolById(5);
            System.out.println(aProtocol);
                
            // get block which contains weather stations    
            //Set<Block> blocks = aSite.getBlocks(aProtocol);
            List<Block> blocks = network.getBlocks(aSite);
            for (Iterator j=blocks.iterator(); j.hasNext(); ) {
               	Block aBlock = (Block)j.next();
                System.out.println("    "+aBlock.getName());
            }

	    if (!blocks.isEmpty()) {
		// get the first block
		Block aBlock = blocks.iterator().next();
		List<SamplingUnit> units = network.getSamplingUnits(aBlock, aProtocol);
				
				
		if (!units.isEmpty()) {
		    WeatherStation station = (WeatherStation)units.iterator().next();
		    System.out.println("    "+station.getName());	
					
		    // get leader institution
		    //Institution institution = aSite.getLeaderInstitution();
		    //System.out.println("    leader institution = "+institution);
					
		    // get leader scientists
		    List<LeadShip> leaders = network.getLeaderships(aSite, aProtocol);
		    for (LeadShip leader : leaders) {
			System.out.println("    leader scientist = "+leader);
		    }
					
		    // get events for a protocol at a unit
		    System.out.println("    -----------------------------------"); 
		    System.out.println("    events at "+station.getName()+" for "+aProtocol.getName());
		    List<String> events = network.getEvents(station, aProtocol);
		    for (String event: events) {
			System.out.println("    event = "+event);
		    }
					
		    // get events for a protocol at a block
		    System.out.println("    -----------------------------------"); 
		    System.out.println("    events at "+aBlock.getName()+" for "+aProtocol.getName());
		    events = network.getEvents(aBlock, aProtocol);
		    for (String event: events) {
			System.out.println("    event = "+event);
		    }
					
		    // get events for a protocol at a site
		    System.out.println("    -----------------------------------"); 
		    System.out.println("    events at "+aSite.getName()+" for "+aProtocol.getName());
		    events = network.getEvents(aSite, aProtocol);
		    for (String event: events) {
			System.out.println("    event = "+event);
		    }					
					
					
		    // get data from a sampling unit
		    System.out.println("    -----------------------------------"); 
		    List records = network.getData(station, aProtocol, 0, 10);
		    for (Iterator i=records.iterator(); i.hasNext(); ) {
			ClimateRecord record = (ClimateRecord)i.next();
			System.out.println("    record = "+record);
		    }
					
		    // get data from a block
		    System.out.println("    -----------------------------------"); 
		    records = network.getData(aBlock, aProtocol, 10, 10);
		    for (Iterator i=records.iterator(); i.hasNext(); ) {
			ClimateRecord record = (ClimateRecord)i.next();
			System.out.println("    record = "+record);
		    }
					
		    // get data from a site
		    System.out.println("    -----------------------------------"); 
		    ClassValidator climateValidator = new ClassValidator( ClimateRecord.class );
		    records = network.getData(aSite, aProtocol, 0, 100);
		    for (Iterator i=records.iterator(); i.hasNext(); ) {
			ClimateRecord record = (ClimateRecord)i.next();						
			System.out.println("    record = "+record);
						
			InvalidValue[] validationMessages = climateValidator.getInvalidValues(record);
			for (int j=0; j<validationMessages.length; j++) {
			    System.out.println("       message=" + validationMessages[j].getMessage());  
			    System.out.println("       propertyName=" + validationMessages[j].getPropertyName());  
			    System.out.println("       propertyPath=" + validationMessages[j].getPropertyPath());  
			    System.out.println("       value=" + validationMessages[j].getValue());  
			}
		    }
					
		}
	    }
			
	    /*
	      System.out.println("=======================================");
	      System.out.println("== Test download tree data at site ==");
	      System.out.println("=======================================");
		    
	      // get vegetation protocol    
	      aProtocol = aSite.getProtocolById(Protocol.TREELIANA);
	      System.out.println(aProtocol);

	      Date now = new Date();
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss_FSSS");
	      String pid = sdf.format(now);
            
	      String filename = "VT-"+aProtocol.getVersion()+"-"+pid+".csv";
	      File file = new File(filename);
	      network.createTreeCSV(file, aSite, network.getTreeData(aSite));
			
	      System.out.println("=======================================");
	      System.out.println("== Test download liana data at site  ==");
	      System.out.println("=======================================");
		    
	      // get vegetation protocol    
	      aProtocol = aSite.getProtocolById(Protocol.TREELIANA);
	      System.out.println(aProtocol);
            
	      System.out.println("    -----------------------------------"); 
	      List events = network.getEvents(aSite, aProtocol); 
	      for (Iterator i=events.iterator(); i.hasNext(); ) {
	      String event = (String)i.next();						
	      System.out.println("    event = "+event);
	      }

	      filename = "VL-"+aProtocol.getVersion()+"-"+pid+".csv";
	      file = new File(filename);
	      network.createLianaCSV(file, aSite, network.getLianaData(aSite));
												
	      System.out.println("=======================================");
	      System.out.println("== Test download avian data at site  ==");
	      System.out.println("=======================================");
		    
	      // get vegetation protocol    
	      aProtocol = aSite.getProtocolById(Protocol.AVIAN);
	      System.out.println(aProtocol);            

	      System.out.println("    -----------------------------------"); 
	      events = network.getEvents(aSite, aProtocol); 
	      for (Iterator i=events.iterator(); i.hasNext(); ) {
	      String event = (String)i.next();						
	      System.out.println("    event = "+event);
	      }
			
	      filename = aProtocol.getAbbrev()+"-"+aProtocol.getVersion()+"-"+pid+".csv";
	      file = new File(filename);
	      network.createCSV(file, aSite, aProtocol, network.getData(aSite, aProtocol));
		    
	      System.out.println("=========================================");
	      System.out.println("== Test download climate data at site  ==");
	      System.out.println("=========================================");
		    
	      // get vegetation protocol    
	      aProtocol = aSite.getProtocolById(Protocol.CLIMATE);
	      System.out.println(aProtocol);            

	      System.out.println("    -----------------------------------"); 
	      events = network.getEvents(aSite, aProtocol); 
	      for (Iterator i=events.iterator(); i.hasNext(); ) {
	      String event = (String)i.next();						
	      System.out.println("    event = "+event);
	      }
			
	      filename = aProtocol.getAbbrev()+"-"+aProtocol.getVersion()+"-"+pid+".csv";
	      file = new File(filename);
	      network.createCSV(file, aSite, aProtocol, network.getData(aSite, aProtocol));
		    		  
	      System.out.println("===========================================");
	      System.out.println("== Test download butterfly data at site  ==");
	      System.out.println("===========================================");
		    
	      // get vegetation protocol    
	      aProtocol = aSite.getProtocolById(Protocol.BUTTERFLY);
	      System.out.println(aProtocol);            

	      System.out.println("    -----------------------------------"); 
	      events = network.getEvents(aSite, aProtocol); 
	      for (Iterator i=events.iterator(); i.hasNext(); ) {
	      String event = (String)i.next();						
	      System.out.println("    event = "+event);
	      }
			
	      filename = aProtocol.getAbbrev()+"-"+aProtocol.getVersion()+"-"+pid+".csv";
	      file = new File(filename);
	      network.createCSV(file, aSite, aProtocol, network.getData(aSite, aProtocol));		  
		 
	      System.out.println("===========================================");
	      System.out.println("== Test download primate data at site  ==");
	      System.out.println("===========================================");
		    
	      aSite = network.getSiteById(1);
		    
	      // get vegetation protocol    
	      aProtocol = aSite.getProtocolById(Protocol.PRIMATE);
	      System.out.println(aProtocol);            

	      System.out.println("    -----------------------------------"); 
	      events = network.getEvents(aSite, aProtocol); 
	      for (Iterator i=events.iterator(); i.hasNext(); ) {
	      String event = (String)i.next();						
	      System.out.println("    event = "+event);
	      }
			
	      filename = aProtocol.getAbbrev()+"-"+aProtocol.getVersion()+"-"+pid+".csv";
	      file = new File(filename);
	      network.createCSV(file, aSite, aProtocol, network.getData(aSite, aProtocol));		  
		 
	      System.out.println("===========================================");
	      System.out.println("== Test download litterfall data at site ==");
	      System.out.println("===========================================");
		    
	      aSite = network.getSiteById(5);
		    
	      // get vegetation protocol    
	      aProtocol = aSite.getProtocolById(Protocol.LITTERFALL);
	      System.out.println(aProtocol);            

	      System.out.println("    -----------------------------------"); 
	      events = network.getEvents(aSite, aProtocol); 
	      for (Iterator i=events.iterator(); i.hasNext(); ) {
	      String event = (String)i.next();						
	      System.out.println("    event = "+event);
	      }
			
	      filename = aProtocol.getAbbrev()+"-"+aProtocol.getVersion()+"-"+pid+".csv";
	      file = new File(filename);
	      network.createCSV(file, aSite, aProtocol, network.getData(aSite, aProtocol));		  
	    */	 
		 		 
	    System.out.println("===========================================");
	    System.out.println("==   Test search by a time range         ==");
	    System.out.println("===========================================");

	    String startString = "Nov 4, 2003 8:14 PM";		    
	    String endString = "Dec 4, 2003 8:14 PM";
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
            Date start = format.parse(startString);
	    Date end = format.parse(endString);
	    sites = network.searchSamplingUnits(start, end);
	    System.out.println("found sites = "+sites.size());
	    showSearchResult(sites);
		 		 
	    System.out.println("===========================================");
	    System.out.println("==   Test search full time range         ==");
	    System.out.println("===========================================");
	    sites = network.searchSamplingUnits();	
	    System.out.println("found sites = "+sites.size());
	    showSearchResult(sites);


	    System.out.println("=======================================");
	    System.out.println("== Test download avian data at site  ==");
	    System.out.println("=======================================");
		    
	    // get vegetation protocol    
	    aProtocol = aSite.getProtocolById(Protocol.AVIAN);
	    System.out.println(aProtocol);            

	    sites = new ArrayList();
	    sites.add(network.getSiteById(1));
	    sites.add(network.getSiteById(5));
	    Date now = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss_FSSS");
	    String pid = sdf.format(now);

	    String filename = aProtocol.getAbbrev()+"-"+aProtocol.getVersion()+"-"+pid+".csv";
	    File file = new File(filename);
	    network.createCSV(file, sites, aProtocol, ',');


        } finally {
	    network.close();
        }
           
    }


    private String replace(String string) {

	StringBuffer sb = new StringBuffer();
	if (string == null) {
	    sb.append("");
	} else {
	    for (int i=0; i<string.length(); i++) {
		char c = string.charAt(i);
		if ((c == '\\') || (c == '"') || (c == '>')) { 
		    sb.append('\\'); 
		    sb.append(c); 
		} else if (c == '\b') { 
		    sb.append("\\b"); 
		} else if (c == '\t') { 
		    sb.append("\\t"); 
		} else if (c == '\n') {
		    sb.append("\\n"); 
		} else if (c == '\f') { 
		    sb.append("\\f"); 
		} else if (c == '\r') {
		    sb.append("\\r"); 
		} else if (c == '\'') {
		    sb.append("\\'"); 
		} else {
		    sb.append(c); 
		} 
	    } 
	} 
	return sb.toString();

    }



    
    public List<String> getVegetationCollectionEvents() {
        Query query = session.createQuery("SELECT DISTINCT record.event "+
                                          "FROM VegetationCollection as record "+
                                          "ORDER BY record.event");
        return query.list();
    }


    public VegetationCollection getVegetationCollection(String event, String blockName) {
        Query query = session.createQuery("FROM VegetationCollection as record "+
                                          "WHERE record.event='"+event+"' "+
                                          "  AND record.block.name='"+blockName+"'");
        List<VegetationCollection> result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }


    public List<Block> getVegetationCollectionPlots(String event) {
        Query query = session.createQuery("SELECT block "+
                                          "FROM VegetationCollection as record "+
                                          "WHERE record.event='"+event+"' ");
        return query.list();
    }

    public List<VegetationCollection> getVegetationCollections() {
        Query query = session.createQuery("FROM VegetationCollection as record ORDER BY record.event, record.block.name");
        List<VegetationCollection> result = query.list();
        return result;
    }


    public List<VegetationCollection> getVegetationCollections(String event) {
        Query query = session.createQuery("FROM VegetationCollection as record WHERE record.event='"+event+"' ORDER BY record.event");
        List<VegetationCollection> result = query.list();
        return result;
    }

    public List<VegetationEvent> getVegetationEvents() {
        Query query = session.createQuery("FROM VegetationEvent as record");
        return query.list();
    }


    public List<VegetationEvent> getVegetationEvents(String event) {
        Query query = session.createQuery("FROM VegetationEvent as record WHERE record.event='"+event+"' ");
        return query.list();
    }

    public Map<String, Integer> getValidCounts(String event, String plot) {

        HashMap<String, Integer> result = new HashMap<String, Integer>();

        // process trees                                                                                                                       
        Query query = session.createQuery("SELECT record.samplingUnit.subplotNumber, count(*) "+
                                          "FROM TreeRecord as record "+
                                          "WHERE record.samplingUnit.block.name='"+plot+"' "+
                                          "AND record.event='"+event+"' "+
                                          "AND record.valid=false "+
                                          "GROUP BY record.samplingUnit.subplotNumber");
        List list = query.list();
        for (int i=0; i<list.size(); i++) {
            Object[] objects = (Object[])list.get(i);
            String key = objects[0]+"";
            Integer val = ((Long)objects[1]).intValue();
            result.put(key, val);
        }

        // process lianas                                                                                                                      
        query = session.createQuery("SELECT record.samplingUnit.subplotNumber, count(*) "+
                                          "FROM LianaRecord as record "+
                                          "WHERE record.samplingUnit.block.name='"+plot+"' "+
                                          "AND record.event='"+event+"' "+
                                          "AND record.valid=false "+
				    "GROUP BY record.samplingUnit.subplotNumber");
        list = query.list();
        for (int i=0; i<list.size(); i++) {
            Object[] objects = (Object[])list.get(i);
            String key = objects[0]+"";
            Integer val = ((Long)objects[1]).intValue();

            Integer tmpInt = result.get(key);
            result.put(key, new Integer(tmpInt.intValue()+val.intValue()));
        }


	return result;
    }


    public int getMaxSamplingUnitId() {
        Query query = session.createQuery("SELECT max(unit.id) FROM SamplingUnit as unit");
        List<Integer> list = query.list();
        return list.get(0).intValue();
    }


    public Map getDefaultParameters(String event, String plot, String subplot) {

        Map map = new HashMap();
        Query query = session.createQuery("FROM TreeRecord as record "+
                                          "WHERE record.samplingUnit.block.name='"+plot+"' "+
                                          //"  AND record.samplingUnit.subplotNumber="+subplot +" "+                                           
                                          "  AND record.event='"+event+"' "+
                                          "  AND not record.collectedByFirstName is NULL "+
                                          "  AND not record.recordFirstName is NULL "+
                                          "  AND not record.collectedAt is NULL ");
        query.setMaxResults(1);
        List<TreeRecord> list = query.list();
        if (!list.isEmpty()) {
	    TreeRecord aRec = list.get(0);
            map.put("collectedAt", aRec.getCollectedAt());
            map.put("measuredFirstName", aRec.getCollectedByFirstName());
            map.put("measuredLastName", aRec.getCollectedByLastName());
            map.put("recordFirstName", aRec.getRecordFirstName());
            map.put("recordLastName", aRec.getRecordLastName());
        }

        if (map.isEmpty()) {
            query = session.createQuery("FROM LianaRecord as record "+
                                        "WHERE record.samplingUnit.block.name='"+plot+"' "+
                                        //"  AND record.samplingUnit.subplotNumber="+subplot +" "+                                             
                                        "  AND record.event='"+event+"' "+
                                        "  AND not record.collectedByFirstName is NULL "+
                                        "  AND not record.recordFirstName is NULL "+
                                        "  AND not record.collectedAt is NULL ");
            query.setMaxResults(1);
            List<LianaRecord> list1 = query.list();
            if (!list1.isEmpty()) {
                LianaRecord aRec = list1.get(0);
                map.put("collectedAt", aRec.getCollectedAt());
                map.put("measuredFirstName", aRec.getCollectedByFirstName());
                map.put("measuredLastName", aRec.getCollectedByLastName());
                map.put("recordFirstName", aRec.getRecordFirstName());
                map.put("recordLastName", aRec.getRecordLastName());
            }

        }

        return map;
    }


}