

package org.team.sdsc.datamodel.util;

import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import jxl.*;
import jxl.write.WritableWorkbook;
import jxl.write.WritableCell;
import org.team.sdsc.datamodel.*;

public class CameraTrapUtil {


    public static String  validateCameraTrapDataExcel(String site,  String fileName, int start, int limit) throws Exception {

	Network network = NetworkFactory.newInstance();
	Site aSite = network.getSites().get(0);
	network.close();

	// parse the uploaded excel file
	java.io.File file = new java.io.File(fileName);
	Workbook wb = null;

	// if the file is not available, wait up to 4 seconds
	int wait = 3;
	while (wait > 0) {
	    try {
		if (wait != 3) Thread.sleep(2000);
		wb = Workbook.getWorkbook(file);
		wait = 0;
	    } catch (Exception ex) {
		wait--;
	    }
	}
	if (wb == null) wb = Workbook.getWorkbook(file);
 
	Sheet[] sheets = wb.getSheets(); 

        // validate the entry form
	if (sheets.length == 0) {
	    throw new DataUploadException("The Data Entry Form contains no sheet");
	}

	ArrayList stdColumnNames = new ArrayList();
	stdColumnNames.add("Site");
	stdColumnNames.add("Sampling Period");
	stdColumnNames.add("Camera Trap Point ID");
	stdColumnNames.add("Camera Trap Number");
	stdColumnNames.add("Camera Serial Number");
	stdColumnNames.add("Memory Card Serial Number");
	stdColumnNames.add("Start-Year");
	stdColumnNames.add("Start-Month");
	stdColumnNames.add("Start-Day");
	stdColumnNames.add("Start-Hour");
	stdColumnNames.add("Start-Mins");
	stdColumnNames.add("First Name");
	stdColumnNames.add("Last Name");
	stdColumnNames.add("End-Year");
	stdColumnNames.add("End-Month");
	stdColumnNames.add("End-Day");
	stdColumnNames.add("End-Hour");
	stdColumnNames.add("End-Mins");
	stdColumnNames.add("First Name Pickup");
	stdColumnNames.add("Last Name Pickup");
	stdColumnNames.add("Camera Working?");
	stdColumnNames.add("Camera Trap Missing?");
	stdColumnNames.add("Case Damage?");
	stdColumnNames.add("Camera Damage?");
	stdColumnNames.add("Card Damage?");
	stdColumnNames.add("Notes");

	ArrayList columnNames = new ArrayList();
	StringBuffer sb = new StringBuffer();
	boolean foundData = false;

	for (int i=0; i<sheets.length; i++) {
	
	    int rowNumber = sheets[i].getRows();
	    int columnNumber = sheets[i].getColumns();

	    /*
	    System.out.println("==============================");
	    System.out.println("sheet name = "+sheets[i].getName());
	    System.out.println("column length = "+columnNumber);
	    */

	    if (rowNumber == 0) {
		System.out.println("The Sheet Is Empty");
		continue; 
	    }

	    if (columnNumber < columnNames.size()) {		
		continue;
	    } else {
		// get the header 
		try {
		    for (int c=0; c<columnNumber; c++) {
			Cell cell = sheets[i].getCell(c, 6);
			if (cell!= null && cell.getType() == CellType.NUMBER) {       
			    throw new DataUploadException("Wrong header of the column "+(i+1));
			} else if (cell != null) {
			    String str = cell.getContents();
			    if (str.equals("")) {
				throw new DataUploadException("Wrong header of the column "+(i+1));
			    }

			    if (!str.toLowerCase().equals(((String)stdColumnNames.get(c)).toLowerCase())) {

				if (str.toLowerCase().equals("first name") && 
				    ((String)stdColumnNames.get(c)).equals("First Name Pickup")) {
				} else if (str.toLowerCase().equals("last name") && 
					   ((String)stdColumnNames.get(c)).equals("Last Name Pickup")) {
				} else {
				    //System.out.println("----> no : "+str+"   "+stdColumnNames.get(c));
				    throw new DataUploadException("Wrong header of the column "+(i+1));
				}
			    } else {
				//System.out.println("----> yes: "+str+"   "+stdColumnNames.get(c));
			    }
			    columnNames.add(str);
			} else {
			    throw new DataUploadException("Wrong header of the column "+(i+1));
			}

		    }
		
		} catch (Exception ex) {
		    if (columnNames.size() != stdColumnNames.size()) {
			columnNames.clear();
			continue;
		    }
		}

	    }

	    sb.append("    result: [\n");

	    foundData = true;
	    int errNum = 0;
	    int errLine = -1;
	    int realRow = 0;

	    for (int r=7; r<rowNumber; r++) {

		int bp = sb.length();

		if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
		    if (r != 7+start) sb.append("        ,\n");
		    sb.append("        {\n");
		}

	        String sYear = "";
		String sMonth = "";
		String sDay = "";
		String sHour = "";
                String sMins = "";

	        String eYear = "";
		String eMonth = "";
		String eDay = "";
		String eHour = "";
                String eMins = "";

		boolean blank = true;

		//System.out.println("-------------------------------");
		//System.out.println("row = "+r);
		
		for (int c=0; c<stdColumnNames.size(); c++) {
		    Cell cell = sheets[i].getCell(c, r);
		    String columnName = (String)stdColumnNames.get(c);

		    //System.out.println("column name = "+columnName+", value="+cell.getContents());

		    if (cell!= null && cell.getType() == CellType.NUMBER) { 
			NumberCell nc = (NumberCell)cell; 
			blank = false;

			if (columnName.equals("Start-Year")) {

			    sYear = cell.getContents();

			} else if (columnName.equals("Start-Month")) {

			    sMonth = cell.getContents();

			} else if (columnName.equals("Start-Day")) {

			    sDay = cell.getContents();

			} else if (columnName.equals("Start-Hour")) {

			    sHour = cell.getContents();

			} else if (columnName.equals("Start-Mins")) {

			    sMins = cell.getContents();

			} else if (columnName.equals("End-Year")) {

			    eYear = cell.getContents();

			} else if (columnName.equals("End-Month")) {

			    eMonth = cell.getContents();

			} else if (columnName.equals("End-Day")) {

			    eDay = cell.getContents();

			} else if (columnName.equals("End-Hour")) {

			    eHour = cell.getContents();

			} else if (columnName.equals("End-Mins")) {

			    eMins = cell.getContents();
			    
			} else if (cell != null) {

			    double value = nc.getValue();

			    if (columnName.equals("Sampling Period")) {
				int year = (int)value;
				String tmp = cell.getContents();
				int index = tmp.indexOf(".");
				if (index == -1) {

				} else {
				    String dd = tmp.substring(index+1);
				    if (year < 2003 || year > (new java.util.Date()).getYear() + 1900 || dd == null || !dd.matches("^0\\d$") ) {
					errNum++;
					if (errLine < 0) errLine = r;
				    } else {

				    }
				}

			    } else if (columnName.equals("Camera Trap Number")) {
				if (value < 0 || value > 100) {
				    errNum++;
				    if (errLine < 0) errLine = r;
				}

			    } else if (columnName.equals("Memory Card Serial Number")) {
				if (value < 0 || value > 1000) {
				    errNum++;
				    if (errLine < 0) errLine = r;
				}
				
			    } else if (value < 0) {
				errNum++;
				if (errLine < 0) errLine = r;
			    }

			    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
				if (c != 0) sb.append(",\n");
				sb.append("          "+columnName.replaceAll(" ", "_").replace('-', '_')+": "+value);
			    }

			}

		    } else if (cell != null) {

			String str = cell.getContents();
			if (str == null || str.equals("NULL")) {
			    if (columnName.equals("Site")                 || 
				columnName.equals("Camera Trap Point ID") ||
				columnName.equals("Camera Serial Number") ||
				columnName.equals("First Name")           ||
				columnName.equals("Last Name")            ||
				columnName.equals("First Name Pickup")    ||
				columnName.equals("Last Name Pickup")) {
				errNum++;
                                if (errLine < 0) errLine = r;
			    }
			    continue;
			}

			if (!str.equals("")) {
			    blank = false;
			} else {
			    continue;
			}

			if (columnName.equals("Site")) {

			    if (!str.equals(aSite.getAbbv())) {
				errNum++;
				if (errLine < 0) errLine = r;
			    } 

			    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
				if (c != 0) sb.append(",\n");
				sb.append("          Site: '"+str+"'");
			    }
			    
			} else {

			    if (columnName.equals("Camera Trap Point ID")) {
				if (!str.matches("CT\\-"+aSite.getAbbv()+"\\-\\d\\-\\d\\d")) {
				    errNum++;
				    if (errLine < 0) errLine = r;
				}
			    } 

			    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
				if (c != 0) sb.append(",\n");
				String tmp = columnName.replaceAll(" ", "_").replace('?', '_').replace('\n', ' ').replace('-', '_');
				sb.append("          "+tmp+": '"+str+"'");
			    }

			}

		    }

		}	
			    
		try {
		    DateFormat formatter = new SimpleDateFormat("d/M/yy/H/m");
		    sMonth = Integer.parseInt(sMonth) < 10 ? "0"+sMonth : sMonth;
		    sDay = Integer.parseInt(sDay) < 10 ? "0"+sDay : sDay;
		    sHour = Integer.parseInt(sHour) < 10 ? "0"+(Integer.parseInt(sHour)) : ""+(Integer.parseInt(sHour));
		    sMins = Integer.parseInt(sMins) < 10 ? "0"+sMins : sMins;

		    java.util.Date date = (java.util.Date)formatter.parse(sDay+"/"+sMonth+"/"+sYear+"/"+Integer.parseInt(sHour)+"/"+sMins);
		    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
			sb.append(",\n");
			sb.append("          Start_Date: '"+sYear+"-"+sMonth+"-"+sDay+"',\n");
			sb.append("          Start_Time: '"+sHour+":"+sMins+"'");
		    }
		} catch (Exception e) {
		    if (!sYear.equals("") || !sMonth.equals("") || !sDay.equals("") || !sHour.equals("") || !sMins.equals("")) {
			errNum++;
			if (errLine < 0) errLine = r;
		    }
		}

		try {
		    DateFormat formatter = new SimpleDateFormat("d/M/yy/H/m");
		    eMonth = Integer.parseInt(eMonth) < 10 ? "0"+eMonth : eMonth;
		    eDay = Integer.parseInt(eDay) < 10 ? "0"+eDay : eDay;
		    eHour = Integer.parseInt(eHour) < 10 ? "0"+(Integer.parseInt(eHour)) : ""+(Integer.parseInt(eHour));
		    eMins = Integer.parseInt(eMins) < 10 ? "0"+eMins : eMins;

		    java.util.Date date = (java.util.Date)formatter.parse(eDay+"/"+eMonth+"/"+eYear+"/"+Integer.parseInt(eHour)+"/"+eMins);
		    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
			sb.append(",\n");
			sb.append("          End_Date: '"+eYear+"-"+eMonth+"-"+eDay+"',\n");
			sb.append("          End_Time: '"+eHour+":"+eMins+"'");
		    }

		} catch (Exception e) {
		    if (!sYear.equals("") || !sMonth.equals("") || !sDay.equals("") || !sHour.equals("") || !sMins.equals("")) {
			errNum++;
			if (errLine < 0) errLine = r;
		    }

		    if (!eYear.equals("") || !eMonth.equals("") || !eDay.equals("") || !eHour.equals("") || !eMins.equals("")) {
			errNum++;
			if (errLine < 0) errLine = r;
		    }
		}

		if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
		    sb.append("\n        }\n");
		}

	        int ep = sb.length();

		if (!blank) {
		    realRow++;
		} else {
		    sb.delete(bp, ep);
		}

	    }

	    sb.append("    ],\n");
	    sb.append("   error: "+errNum+",\n");
	    
	    if (errLine > 0) {
	        sb.append("   line: "+errLine+",\n");
	    }

	    sb.append("   sheet: "+i+"\n");  
	    sb.append("}\n");

	    //System.out.println("==============================");
	    //System.out.println(columnNames);
	    columnNames.clear();

            StringBuffer sb1 = new StringBuffer();
	    sb1.append("{\n");
	    sb1.append("    totalCount: "+(realRow)+",\n");
	    sb = sb1.append(sb);

	    break;

	}

	if (!foundData) {
	    throw new DataUploadException("No Camera Trap  data was found");
	}

	return sb.toString();

    }




    public static String  validatePhotoDataExcel(String site,  String fileName, int start, int limit) throws Exception {

	Network network = NetworkFactory.newInstance();
	Site aSite = network.getSites().get(0);
	network.close();

	// parse the uploaded excel file
	java.io.File file = new java.io.File(fileName);
	Workbook wb = null;

	// if the file is not available, wait up to 4 seconds
	int wait = 3;
	while (wait > 0) {
	    try {
		if (wait != 3) Thread.sleep(2000);
		wb = Workbook.getWorkbook(file);
		wait = 0;
	    } catch (Exception ex) {
		wait--;
	    }
	}
	if (wb == null) wb = Workbook.getWorkbook(file);
 
	Sheet[] sheets = wb.getSheets(); 

        // validate the entry form
	if (sheets.length == 0) {
	    throw new DataUploadException("The Data Entry Form contains no sheet");
	}

	ArrayList stdColumnNames = new ArrayList();

	stdColumnNames.add("Sampling Period");
	stdColumnNames.add("Camera Trap Point ID");
	stdColumnNames.add("Year");
	stdColumnNames.add("Month");
	stdColumnNames.add("Day");
	stdColumnNames.add("Hour");
	stdColumnNames.add("Mins");
	stdColumnNames.add("Raw Filename");
	stdColumnNames.add("Team Filename");
	stdColumnNames.add("Genus");
	stdColumnNames.add("Species");
	stdColumnNames.add("Binomial");
	stdColumnNames.add("Number of Animals");
	stdColumnNames.add("First Name");
	stdColumnNames.add("Last Name");
	stdColumnNames.add("Notes");

	ArrayList columnNames = new ArrayList();
	StringBuffer sb = new StringBuffer();
	boolean foundData = false;

	for (int i=0; i<sheets.length; i++) {
	
	    int rowNumber = sheets[i].getRows();
	    int columnNumber = sheets[i].getColumns();

	    //System.out.println("==============================");
	    //System.out.println("sheet name = "+sheets[i].getName());
	    //System.out.println("column length = "+columnNumber);

	    if (rowNumber == 0) {
		System.out.println("The Sheet Is Empty");
		continue; 
	    }

	    if (columnNumber < columnNames.size()) {		
		continue;
	    } else {
		// get the header 
		try {
		    for (int c=0; c<columnNumber; c++) {
			Cell cell = sheets[i].getCell(c, 6);

			if (cell!= null && cell.getType() == CellType.NUMBER) {       
			    throw new DataUploadException("Wrong header of the column "+(i+1));
			} else if (cell != null) {
			    String str = cell.getContents();
			    if (str.equals("")) {
				throw new DataUploadException("Wrong header of the column "+(i+1));
			    }

			    if (!str.toLowerCase().equals(((String)stdColumnNames.get(c)).toLowerCase())) {

				if (str.toLowerCase().equals("first name") && 
				    ((String)stdColumnNames.get(c)).equals("First Name Pickup")) {
				} else if (str.toLowerCase().equals("last name") && 
					   ((String)stdColumnNames.get(c)).equals("Last Name Pickup")) {
				} else {
				    //System.out.println("----> no : "+str+"   "+stdColumnNames.get(c));
				    throw new DataUploadException("Wrong header of the column "+(i+1));
				}
			    } else {
				//System.out.println("----> yes: "+str+"   "+stdColumnNames.get(c));
			    }
			    columnNames.add(str);
			} else {
			    throw new DataUploadException("Wrong header of the column "+(i+1));
			}

		    }
		
		} catch (Exception ex) {
		    if (columnNames.size() != stdColumnNames.size()) {
			columnNames.clear();
			continue;
		    }
		}

	    }

	    sb.append("    result: [\n");

	    foundData = true;
	    int errNum = 0;
	    int errLine = -1;
	    int realRow = 0;

	    for (int r=7; r<rowNumber; r++) {

		int bp = sb.length();

		if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
		    if (r != 7+start) sb.append("        ,\n");
		    sb.append("        {\n");
		}

	        String sYear = "";
		String sMonth = "";
		String sDay = "";
		String sHour = "";
                String sMins = "";

		boolean blank = true;

		for (int c=0; c<stdColumnNames.size(); c++) {
		    Cell cell = sheets[i].getCell(c, r);
		    String columnName = (String)stdColumnNames.get(c);

		    if (cell!= null && cell.getType() == CellType.NUMBER) { 
			NumberCell nc = (NumberCell)cell; 
			blank = false;

			if (columnName.equals("Year")) {

			    sYear = cell.getContents();

			} else if (columnName.equals("Month")) {

			    sMonth = cell.getContents();

			} else if (columnName.equals("Day")) {

			    sDay = cell.getContents();

			} else if (columnName.equals("Hour")) {

			    sHour = cell.getContents();

			} else if (columnName.equals("Mins")) {

			    sMins = cell.getContents();
			    
			} else if (cell != null) {

			    double value = nc.getValue();

			    if (columnName.equals("Sampling Period")) {
                                int year = (int)value;
                                String tmp = cell.getContents();
                                int index = tmp.indexOf(".");
                                if (index == -1) {

                                } else {
                                    String dd = tmp.substring(index+1);
                                    if (year < 2003 || year > (new java.util.Date()).getYear() + 1900 || dd == null || !dd.matches("^0\\d$") ) {
                                        errNum++;
                                        if (errLine < 0) errLine = r;
                                    } else {

                                    }
                                }

                            } else if (columnName.equals("Number of Animals")) {
                                if (value < 0 || value > 100) {
                                    errNum++;
                                    if (errLine < 0) errLine = r;
                                }

                            } else if (value < 0) {
				errNum++;
				if (errLine < 0) errLine = r;
			    }

			    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
				if (c != 0) sb.append(",\n");
				sb.append("          "+columnName.replaceAll(" ", "_").replace('-', '_')+": "+value);
			    }

			}

		    } else if (cell != null) {

			String str = cell.getContents();
			if (str == null || str.equals("NULL")) {
			    if (columnName.equals("Site")                 ||
                                columnName.equals("Camera Trap Point ID") ||
                                columnName.equals("Raw Filename")         ||
                                columnName.equals("Team Filename")        ||
                                columnName.equals("Genus")                ||
                                columnName.equals("Species")              ||
                                columnName.equals("Genus")                ||
                                columnName.equals("Binomial")             ||
                                columnName.equals("First Name")           ||
                                columnName.equals("Last Name")) {
                                errNum++;
                                if (errLine < 0) errLine = r;
                            }
			    continue;
			}

			if (!str.equals("")) {
			    blank = false;
			} else {
			    continue;
			}

			if (columnName.equals("Site")) {

			    if (!str.equals(aSite.getAbbv())) {
				errNum++;
				if (errLine < 0) errLine = r;
			    } 

			    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
				if (c != 0) sb.append(",\n");
				sb.append("          Site: '"+str+"'");
			    }


			} else if (columnName.equals("Year")) {

			    sYear = cell.getContents();

			} else if (columnName.equals("Month")) {

			    sMonth = cell.getContents();

			} else if (columnName.equals("Day")) {

			    sDay = cell.getContents();

			} else if (columnName.equals("Hour")) {

			    sHour = cell.getContents();

			} else if (columnName.equals("Mins")) {

			    sMins = cell.getContents();

			} else {

			    if (columnName.equals("Camera Trap Point ID")) {
                                if (!str.matches("CT\\-"+aSite.getAbbv()+"\\-\\d\\-\\d\\d")) {
                                    errNum++;
                                    if (errLine < 0) errLine = r;
                                }
                            }

			    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
				if (c != 0) sb.append(",\n");
				String tmp = columnName.replaceAll(" ", "_").replace('?', '_').replace('\n', ' ').replace('-', '_');
				sb.append("          "+tmp+": '"+str+"'");
			    }

			}

		    }

		}	
			    
		try {
		    DateFormat formatter = new SimpleDateFormat("d/M/yy/H/m");
		    sMonth = Integer.parseInt(sMonth) < 10 ? "0"+Integer.parseInt(sMonth) : sMonth;
		    sDay = Integer.parseInt(sDay) < 10 ? "0"+Integer.parseInt(sDay) : sDay;
		    sHour = Integer.parseInt(sHour) < 10 ? "0"+(Integer.parseInt(sHour)) : ""+(Integer.parseInt(sHour));
		    sMins = Integer.parseInt(sMins) < 10 ? "0"+Integer.parseInt(sMins) : sMins;

		    java.util.Date date = (java.util.Date)formatter.parse(sDay+"/"+sMonth+"/"+sYear+"/"+Integer.parseInt(sHour)+"/"+sMins);
		    if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
			sb.append(",\n");
			sb.append("          Date: '"+sYear+"-"+sMonth+"-"+sDay+"',\n");
			sb.append("          Time: '"+sHour+":"+sMins+"'");
		    }
		} catch (Exception e) {
		    if (!sYear.equals("") || !sMonth.equals("") || !sDay.equals("") || !sHour.equals("") || !sMins.equals("")) {
			errNum++;
			if (errLine < 0) errLine = r;
		    }
		}

		if (r >= Math.min(7+start, rowNumber) && r<Math.min(7+start+limit, rowNumber)) {
		    sb.append("\n        }\n");
		}

	        int ep = sb.length();

		if (!blank) {
		    realRow++;
		} else {
		    sb.delete(bp, ep);
		}

	    }

	    sb.append("    ],\n");
	    sb.append("   error: "+errNum+",\n");
	    
	    if (errLine > 0) {
	        sb.append("   line: "+errLine+",\n");
	    }

	    sb.append("   sheet: "+i+"\n");  
	    sb.append("}\n");

	    //System.out.println("==============================");
	    //System.out.println(columnNames);
	    columnNames.clear();

            StringBuffer sb1 = new StringBuffer();
	    sb1.append("{\n");
	    sb1.append("    totalCount: "+(realRow)+",\n");
	    sb = sb1.append(sb);

	    break;

	}

	if (!foundData) {
	    throw new DataUploadException("No Photo Data data was found");
	}

	return sb.toString();

    }





}