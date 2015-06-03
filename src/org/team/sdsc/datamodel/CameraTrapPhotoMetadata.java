
package org.team.sdsc.datamodel;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.drew.metadata.*;
import com.drew.metadata.jpeg.*;
import org.apache.sanselan.*;
import org.apache.sanselan.common.*;
import org.apache.sanselan.formats.jpeg.*;
import org.apache.sanselan.formats.tiff.*;
import org.apache.sanselan.formats.tiff.constants.*;
import org.team.sdsc.datamodel.annotation.*;
import javax.xml.bind.annotation.*;

/**
 * Represents a TEAM photo metadata.
 *
 * @author Kai Lin
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@Entity
@Table(name="TV_PHOTO_METADATA")
public class CameraTrapPhotoMetadata {

    /**
     * The synthetic database key associated with this photo metadata once it is
     * persisted to a database.
     */
    @XmlTransient
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    /**
     * The associated photo.
     */
    @XmlTransient
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn ( name = "PHOTO_ID" , nullable = false )
    private CameraTrapPhoto photo;

    @Column(name="exif_make")
    private String exifMake;

    @Column(name="exif_model")
    private String exifModel;

    @Column(name="exif_date_time")
    private Date exifDateTime;

    @Column(name="exif_date_time_original")
    private Date exifDateTimeOriginal;

    @Column(name="exif_date_time_digitized")
    private Date exifDateTimeDigitized;

    @Column(name="exif_flash")
    private Integer exifFlash;

    @Column(name="exif_metering_mode")
    private Integer exifMeteringMode;

    @Column(name="exif_exposure_time")
    private String exifExposureTime;

    @Column(name="exif_flash_name")
    private String exifFlashName;

    @Column(name="jpeg_comment_Trg")
    private String jpegCommentTrg;

    @Column(name="jpeg_comment_Dat")
    private Date jpegCommentDat;

    @Column(name="jpeg_comment_MP")
    private String jpegCommentMP;

    @Column(name="jpeg_comment_Tmp")
    private String jpegCommentTmp;

    @Column(name="jpeg_comment_Bat")
    private String jpegCommentBat;

    @Column(name="jpeg_comment_Ill")
    private String jpegCommentIll;

    @Column(name="jpeg_comment_Lbl")
    private String jpegCommentLbl;

    @Column(name="jpeg_comment_Nam")
    private String jpegCommentNam;

    @Column(name="jpeg_comment_SN")
    private String jpegCommentSN ;

    @Column(name="jpeg_comment_Ver")
    private String jpegCommentVer;


    public Integer getId() {
	return id;
    }


    public void setId(Integer aInt) {
	id = aInt;
    }


    public CameraTrapPhoto getPhoto() {
	return photo;
    }


    public void setPhoto(CameraTrapPhoto photo) {
	this.photo = photo;
    }


    public String getExifMake() {
	return exifMake;
    }


    public String getExifModel() {
	return exifModel;
    } 


    public Date getExifDateTime() {
	return exifDateTime;
    } 


    public Date getExifDateTimeOriginal() {
	return exifDateTimeOriginal;
    } 


    public Date getExifDateTimeDigitized() {
	return exifDateTimeDigitized;
    } 


    public Integer getExifFlash() {
	return exifFlash;
    } 


    public Integer getExifMeteringMode() {
	return exifMeteringMode;
    } 


    public String getExifExposureTime() {
	return exifExposureTime;
    } 


    public String getExifFlashName() {
	return exifFlashName;
    } 


    public String getJpegCommentTrg() {
	return jpegCommentTrg;
    } 

 
    public Date getJpegCommentDat() {
	return jpegCommentDat;
    } 

    public String getJpegCommentMP() {
	return jpegCommentMP;
    } 

 
    public String getJpegCommentTmp() {
	return jpegCommentTmp;
    } 

 
    public String getJpegCommentBat() {
	return jpegCommentBat;
    } 

 
    public String getJpegCommentIll() {
	return jpegCommentIll;
    } 

 
    public String getJpegCommentLbl() {
	return jpegCommentLbl;
    } 

 
    public String getJpegCommentNam() {
	return jpegCommentNam;
    } 

 
    public String getJpegCommentSN () {
	return jpegCommentSN;
    } 

    public String getJpegCommentVer() {
	return jpegCommentVer;
    } 


    public CameraTrapPhotoMetadata() {
    }


    public CameraTrapPhotoMetadata(File jpegFile) throws Exception{

	// get jpeg comments
	Metadata metadata = (new JpegCommentReader(jpegFile)).extract();
	JpegCommentDirectory directory = (JpegCommentDirectory)metadata.getDirectory(JpegCommentDirectory.class);
	StringBuffer sb = new StringBuffer();
	for (Iterator i=directory.getTagIterator(); i.hasNext(); ) {
	    Tag tag = (Tag)i.next();
	    if (tag.getTagName().equals("Jpeg Comment")) {
		byte[] bytes = directory.getByteArray(tag.getTagType());

		for (int j=0; j<bytes.length; j++) {
		    int aint = (int)bytes[j];
		    if (aint == 0) continue;
		    if (aint == 13) {
			sb.append("\n");
			continue;
		    }
		    sb.append(getchar(aint));
		}
		break;
	    }
	}

	String comment = sb.toString();
	StringTokenizer st = new StringTokenizer(comment, "\n");
	while (st.hasMoreTokens()) {
	    String token = st.nextToken();
	    int index = token.indexOf(":");
	    if (index != -1) {
		String name = token.substring(0, index);
		String value = token.substring(index+1);

		if (name.equals("Trg")) {
		    jpegCommentTrg = value;
		} else if (name.equals("Dat")) {
		    java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss aaa");
		    jpegCommentDat = (Date)formatter.parse(value);
		} else if (name.equals("MP")) {
		    jpegCommentMP = value;
		} else if (name.equals("Tmp")) {
		    jpegCommentTmp = value;
		} else if (name.equals("Bat")) {
		    jpegCommentBat = value;
		} else if (name.equals("Ill")) {
		    jpegCommentIll = value;
		} else if (name.equals("Lbl")) {
		    jpegCommentLbl = value;
		} else if (name.equals("Nam")) {
		    jpegCommentNam = value;
		} else if (name.equals("SN")) {
		    jpegCommentSN = value;
		} else if (name.equals("Ver")) {
		    jpegCommentVer = value ;
		}  
	    }
	}

	// get exif tags
	IImageMetadata metadata1 = Sanselan.getMetadata(jpegFile);
	if (metadata1 instanceof JpegImageMetadata) {

	    java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	    JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata1;
	    exifMake = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_MAKE);
	    exifModel = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_MODEL);

	    String string = getTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);
	    if (string != null && string.startsWith("'")) {
		string = string.substring(1, string.length()-2); 
		exifDateTime = formatter.parse(string);
	    }

	    string = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
	    if (string != null && string.startsWith("'")) {
		string = string.substring(1, string.length()-2); 
		exifDateTimeOriginal = formatter.parse(string);
	    }

	    string = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_CREATE_DATE);
	    if (string != null && string.startsWith("'")) {
		string = string.substring(1, string.length()-2); 
		exifDateTimeDigitized = formatter.parse(string);
	    }

	    try {
		string = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_FLASH);
		if (string != null) {
		    exifFlash = new Integer(getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_FLASH));
		}
	    } catch (Exception ex) {
	    }

	    try {
		string = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_METERING_MODE);
		if (string != null) {
		    exifMeteringMode = new Integer(getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_METERING_MODE));
		}
	    } catch (Exception ex) {
	    }

	    exifExposureTime = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_EXPOSURE_TIME);

	    String flash = getTagValue(jpegMetadata, TiffConstants.EXIF_TAG_FLASH);
	    if (flash != null) {
		int value = Integer.parseInt(flash);
		exifFlashName = getFlashName(value);
	    }
	    
	}

	/*
	if (exifDateTime == null          &&
	    exifDateTimeOriginal == null  &&
	    exifDateTimeDigitized == null) {
	*/
	if (true) {

	    // read by exiftool

	    //String cmd = "exiftool \""+jpegFile.getAbsolutePath().replace("\\", "/")+"\"";
	    //String[] args = new String[] { "sh", "-c", cmd };

	    String cmd = " ..\\webapps\\deskTEAM\\WEB-INF\\exiftool \""+jpegFile.getAbsolutePath().replace("\\", "/")+"\"";

	    String[] args = new String[] { cmd };
	    Process process = Runtime.getRuntime().exec(args[0]);

	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line = null;
	    while ((line = stdInput.readLine()) != null) {
		parseLine(line);
	    }
	    stdInput.close();
	    
	}
    }



    public void parseLine(String line) throws Exception{

	int index = line.indexOf(":");
	java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	if (index != -1) {
	    if (line.startsWith("Exposure Time")) {
		this.exifExposureTime = line.substring(index+1).trim();
	    } else if (line.startsWith("Date/Time Original")) {
		this.exifDateTimeOriginal = formatter.parse(line.substring(index+1).trim());		    
		this.exifDateTime = this.exifDateTimeOriginal;		    
		this.jpegCommentDat = this.exifDateTimeOriginal;
		this.exifDateTimeDigitized = this.exifDateTimeOriginal;
	    } else if (line.startsWith("Sequence")) {
		this.jpegCommentTrg = line.substring(index+1).trim();
	    } else if (line.startsWith("Moon Phase")) {
		this.jpegCommentMP = line.substring(index+1).trim();
	    } else if (line.startsWith("Ambient Temperature Fahrenheit")) {
		this.jpegCommentTmp = line.substring(index+1).trim();
	    } else if (line.startsWith("Ambient Temperature")) {
		this.jpegCommentTmp = line.substring(index+1).trim();
	    } else if (line.startsWith("Serial Number")) {
		this.jpegCommentSN = line.substring(index+1).trim();
	    } else if (line.startsWith("Infrared Illuminator")) {
		this.jpegCommentIll = line.substring(index+1).trim();
	    } else if (line.startsWith("User Label")) {
		this.jpegCommentLbl = line.substring(index+1).trim();
	    } else if (line.startsWith("File Name")) {
		this.jpegCommentNam = line.substring(index+1).trim();
	    }
	    
	}
	
    }


    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("Trg: "+jpegCommentTrg+"\n");
	sb.append("Dat: "+jpegCommentDat+"\n");
	sb.append("MP: "+jpegCommentMP+"\n");
	sb.append("Tmp: "+jpegCommentTmp+"\n");
	sb.append("Bat: "+jpegCommentBat+"\n");
	sb.append("Ill: "+jpegCommentIll+"\n");
	sb.append("Lbl: "+jpegCommentLbl+"\n");
	sb.append("Nam: "+jpegCommentNam+"\n");
	sb.append("SN: "+jpegCommentSN+"\n");
	sb.append("Ver: "+jpegCommentVer+"\n");
	sb.append("Make: "+exifMake+"\n");
	sb.append("Model: "+exifModel+"\n");
	sb.append("Date Time: "+exifDateTime+"\n");
	sb.append("Date Time Original: "+exifDateTimeOriginal+"\n");
	sb.append("Date Time Digitized: "+exifDateTimeDigitized+"\n");
	sb.append("Flash: "+exifFlash+"\n");
	sb.append("Metering Mode: "+exifMeteringMode+"\n");
	sb.append("Exposure Time: "+exifExposureTime+"\n");
	sb.append("Flash Name: "+exifFlashName+"\n");
	return sb.toString();
    }


    private static char getchar(int aint) {
	switch (aint) {

	case 32:  return ' '; 
	case 33:  return '!'; 
	case 34:  return '\"'; 
	case 35:  return '#'; 
	case 36:  return '$'; 
	case 37:  return '%'; 
	case 38:  return '&'; 
	case 39:  return '\''; 
	case 40:  return '('; 
	case 41:  return ')'; 
	case 42:  return '*'; 
	case 43:  return '+'; 
	case 44:  return ','; 
	case 45:  return '-'; 
	case 46:  return '.'; 
	case 47:  return '/'; 
	case 48:  return '0'; 
	case 49:  return '1'; 
	case 50:  return '2'; 
	case 51:  return '3'; 
	case 52:  return '4'; 
	case 53:  return '5'; 
	case 54:  return '6'; 
	case 55:  return '7'; 
	case 56:  return '8'; 
	case 57:  return '9'; 
	case 58:  return ':'; 
	case 59:  return ';'; 
	case 60:  return '<'; 
	case 61:  return '='; 
	case 62:  return '>'; 
	case 63:  return '?'; 
	case 64:  return '@'; 
	case 65:  return 'A'; 
	case 66:  return 'B'; 
	case 67:  return 'C'; 
	case 68:  return 'D'; 
	case 69:  return 'E'; 
	case 70:  return 'F'; 
	case 71:  return 'G'; 
	case 72:  return 'H'; 
	case 73:  return 'I'; 
	case 74:  return 'J'; 
	case 75:  return 'K'; 
	case 76:  return 'L'; 
	case 77:  return 'M'; 
	case 78:  return 'N'; 
	case 79:  return 'O';
	case 80:  return 'P';  
	case 81:  return 'Q'; 
	case 82:  return 'R'; 
	case 83:  return 'S'; 
	case 84:  return 'T';
	case 85:  return 'U'; 
	case 86:  return 'V'; 
	case 87:  return 'W'; 
	case 88:  return 'X';
	case 89:  return 'Y'; 
	case 90:  return 'Z';
	case 91:  return '['; 
	case 92:  return '\\'; 
	case 93:  return ']'; 
	case 94:  return '^'; 
	case 95:  return '_'; 
	case 96:  return '\''; 
	case 97:  return 'a'; 
	case 98:  return 'b'; 
	case 99:  return 'c'; 
	case 100:  return 'd'; 
	case 101:  return 'e'; 
	case 102:  return 'f'; 
	case 103:  return 'g'; 
	case 104:  return 'h'; 
	case 105:  return 'i'; 
	case 106:  return 'j'; 
	case 107:  return 'k'; 
	case 108:  return 'l'; 
	case 109:  return 'm'; 
	case 110:  return 'n'; 
	case 111:  return 'o';
	case 112:  return 'p';  
	case 113:  return 'q'; 
	case 114:  return 'r'; 
	case 115:  return 's'; 
	case 116:  return 't';
	case 117:  return 'u'; 
	case 118:  return 'v'; 
	case 119:  return 'w'; 
	case 120:  return 'x';
	case 121:  return 'y'; 
	case 122:  return 'z';

	default: 
	    return ' ';
	}
    }


    private static String getTagValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) throws ImageReadException, IOException {
	TiffField field = jpegMetadata.findEXIFValue(tagInfo);
	if (field == null)
	    return null;  
	else
	    return field.getValueDescription();
    }
    

    private static String getFlashName(int value) {

	if (value == ExifTagConstants.FLASH_VALUE_AUTO_DID_NOT_FIRE) {
	    return "Flash did not fire, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_AUTO_DID_NOT_FIRE_RED_EYE_REDUCTION) {
	    return "Flash did not fire, red eye reduction, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_AUTO_FIRED) {
	    return "Flash fired, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_AUTO_FIRED_RED_EYE_REDUCTION) {
	    return "Flash fired, red eye reduction, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_AUTO_FIRED_RED_EYE_REDUCTION_RETURN_DETECTED) {
	    return "Flash fired, red eye reduction, return detected, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_AUTO_FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED) {
	    return "Flash fired, red eye reduction, return not detected, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_AUTO_FIRED_RETURN_DETECTED) {
	    return "Flash fired, return detected, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_AUTO_FIRED_RETURN_NOT_DETECTED) {
	    return "Flash fired, return not detected, auto mode"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_FIRED) {
	    return "Flash fired"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION) {
	    return "Flash fired, red eye reduction"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_DETECTED) {
	    return "Flash fired, red eye reduction, return detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED) {
	    return "Flash fired, red eye reduction, return not detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_FIRED_RETURN_DETECTED) {
	    return "Flash fired, return detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_FIRED_RETURN_NOT_DETECTED) {
	    return "Flash fired, return not detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_NO_FLASH_FUNCTION) {
	    return "No flash function"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_OFF) {
	    return "Flash off"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_OFF_DID_NOT_FIRE_RETURN_NOT_DETECTED) {
	    return "Flash off, did not fire, return not detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_OFF_NO_FLASH_FUNCTION) {
	    return "Flash off, no flash function"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_OFF_RED_EYE_REDUCTION) {
	    return "Flash off, red eye reduction"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_ON) {
	    return "Flash on"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_ON_DID_NOT_FIRE) {
	    return "Flash on, did not fire"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_ON_RED_EYE_REDUCTION) {
	    return "Flash on, red eye reduction"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_ON_RED_EYE_REDUCTION_RETURN_DETECTED) {
	    return "Flash on, red eye reduction, return detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_ON_RED_EYE_REDUCTION_RETURN_NOT_DETECTED) {
	    return "Flash on, red eye reduction, return not detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_ON_RETURN_DETECTED) {
	    return "Flash on , return detected"; 
	} else if (value == ExifTagConstants.FLASH_VALUE_ON_RETURN_NOT_DETECTED) {
	    return "Flash on, return not detected"; 
	} else {
	    return null;
	}

    }


    static public void main(String[] args) throws Exception{

	// read by exiftool
	String cmd = "..\\webapps\\deskTEAM\\WEB-INF\\exiftool.exe ";
	args = new String[] { cmd };

	System.out.println("cmd = "+cmd);

	Process process = Runtime.getRuntime().exec(args);
	       
	BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	String line = null;
	while ((line = stdInput.readLine()) != null) {
	    //parseLine(line);
	    System.out.println(line);
	}
	stdInput.close();

    }


}
