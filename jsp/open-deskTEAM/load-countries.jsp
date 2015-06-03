<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>

<%
    String path = pageContext.getServletContext().getRealPath("WEB-INF");

    // start database
    String DRIVER = "com.mysql.jdbc.Driver";
    String databaseDir = path+"/database";
    int port = 3336;
    String dbName = "deskTEAMDB";

    String CONN_STRING = "jdbc:mysql:mxj://localhost:" + port + "/" + dbName //                                               
        + "?" + "server.basedir=" + databaseDir //                                                                            
        + "&" + "createDatabaseIfNotExist=true"//                                                                            
        + "&" + "server.initialize-user=true" //                     
        + "&" + "useUnicode=true" //
	+ "&" + "characterEncoding=UTF-8"
        ;

    //System.out.println(CONN_STRING);

    String USERNAME = "alice";
    String PASSWORD = "q93uti0opwhkd";
    Class.forName(DRIVER).newInstance();
    Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
    Statement statement = conn.createStatement();
    statement.execute("SET foreign_key_checks = 0;");     

 String sql = "";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(1, 0,  'Andorra')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(2, 0,  'Ascension Island')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(3, 0,  'United Arab Emirates')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(4, 0,  'Afghanistan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(5, 0,  'Antigua and Barbuda')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(6, 0,  'Anguilla')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(7, 0,  'Albania')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(8, 0,  'Armenia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(9, 0,  'Netherlands Antilles')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(10, 0,  'Angola')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(11, 0,  'Antarctica')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(12, 0,  'Argentina')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(13, 0,  'American Samoa')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(14, 0,  'Austria')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(15, 0,  'Australia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(16, 0,  'Aruba')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(17, 0,  'Aland Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(18, 0,  'Azerbaijan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(19, 0,  'Bosnia and Herzegovina')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(20, 0,  'Barbados')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(21, 0,  'Bangladesh')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(22, 0,  'Belgium')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(23, 0,  'Burkina Faso')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(24, 0,  'Bulgaria')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(25, 0,  'Bahrain')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(26, 0,  'Burundi')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(27, 0,  'Benin')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(28, 0,  'Bermuda')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(29, 0,  'Brunei Darussalam')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(30, 0,  'Bolivia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(31, 0,  'Brazil')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(32, 0,  'Bahamas')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(33, 0,  'Bhutan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(34, 0,  'Bouvet Island')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(35, 0,  'Botswana')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(36, 0,  'Belarus')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(37, 0,  'Belize')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(38, 0,  'Canada')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(39, 0,  'Cocos (Keeling) Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(40, 0,  'Democratic Republic of the Congo (formerly Zaire)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(41, 0,  'Central African Republic')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(42, 0,  'Congo (Republic of the Congo)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(43, 0,  'Switzerland (Confoederatio Helvetica)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(44, 0,  'Cote dIvoire (Ivory Coast)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(45, 0,  'Cook Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(46, 0,  'Chile')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(47, 0,  'Cameroon')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(48, 0,  'China')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(49, 0,  'Colombia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(50, 0,  'Costa Rica')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(51, 0,  'Serbia and Montenegro')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(52, 0,  'Cuba')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(53, 0,  'Cape Verde')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(54, 0,  'Christmas Island')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(55, 0,  'Cyprus')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(56, 0,  'Czech Republic')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(57, 0,  'Germany (Deutschland)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(58, 0,  'Djibouti')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(59, 0,  'Denmark')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(60, 0,  'Dominica')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(61, 0,  'Dominican Republic')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(62, 0,  'Algeria')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(63, 0,  'Ecuador')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(64, 0,  'Estonia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(65, 0,  'Egypt')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(66, 0,  'Western Sahara (formerly Spanish Sahara)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(67, 0,  'Eritrea')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(68, 0,  'Spain (Espana)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(69, 0,  'Ethiopia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(70, 0,  'Finland')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(71, 0,  'Fiji')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(72, 0,  'Falkland Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(73, 0,  'Federated States of Micronesia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(74, 0,  'Faroe Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(75, 0,  'France')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(76, 0,  'Gabon')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(77, 0,  'Grenada')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(78, 0,  'Georgia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(79, 0,  'French Guiana')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(80, 0,  'Ghana')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(81, 0,  'Gibraltar')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(82, 0,  'Greenland')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(83, 0,  'Gambia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(84, 0,  'Guinea')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(85, 0,  'Guadeloupe')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(86, 0,  'Equatorial Guinea')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(87, 0,  'Greece')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(88, 0,  'South Georgia and the South Sandwich Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(89, 0,  'Guatemala')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(90, 0,  'Guam')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(91, 0,  'Guinea-Bissau')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(92, 0,  'Guyana')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(93, 0,  'Hong Kong')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(94, 0,  'Heard Island and McDonald Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(95, 0,  'Honduras')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(96, 0,  'Croatia (Hrvatska)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(97, 0,  'Haiti')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(98, 0,  'Hungary')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(99, 0,  'Indonesia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(100, 0,  'Ireland')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(101, 0,  'Israel')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(102, 0,  'India')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(103, 0,  'British Indian Ocean Territory (including Diego Garcia)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(104, 0,  'Iraq')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(105, 0,  'Iran')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(106, 0,  'Iceland')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(107, 0,  'Italy')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(108, 0,  'Jamaica')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(109, 0,  'Jordan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(110, 0,  'Japan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(111, 0,  'Kenya')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(112, 0,  'Kyrgyzstan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(113, 0,  'Cambodia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(114, 0,  'Kiribati')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(115, 0,  'Comoros')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(116, 0,  'Saint Kitts and Nevis')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(117, 0,  'North Korea')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(118, 0,  'South Korea')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(119, 0,  'Kuwait')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(120, 0,  'Cayman Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(121, 0,  'Kazakhstan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(122, 0,  'Lao P.D.R.')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(123, 0,  'Lebanon')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(124, 0,  'Saint Lucia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(125, 0,  'Liechtenstein')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(126, 0,  'Sri Lanka')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(127, 0,  'Liberia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(128, 0,  'Lesotho')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(129, 0,  'Lithuania')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(130, 0,  'Luxembourg')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(131, 0,  'Latvia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(132, 0,  'Libya')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(133, 0,  'Morocco')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(134, 0,  'Monaco')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(135, 0,  'Moldova')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(136, 0,  'Madagascar')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(137, 0,  'Marshall Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(138, 0,  'Former Yugoslav Republic of Macedonia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(139, 0,  'Mali')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(140, 0,  'Myanmar (Burma)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(141, 0,  'Mongolia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(142, 0,  'Macao (Macau)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(143, 0,  'Northern Mariana Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(144, 0,  'Martinique')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(145, 0,  'Mauritania')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(146, 0,  'Montserrat')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(147, 0,  'Malta')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(148, 0,  'Mauritius')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(149, 0,  'Maldives')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(150, 0,  'Malawi')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(151, 0,  'Mexico')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(152, 0,  'Malaysia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(153, 0,  'Mozambique')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(154, 0,  'Namibia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(155, 0,  'New Caledonia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(156, 0,  'Niger')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(157, 0,  'Norfolk Island')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(158, 0,  'Nigeria')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(159, 0,  'Nicaragua')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(160, 0,  'Netherlands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(161, 0,  'Norway')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(162, 0,  'Nepal')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(163, 0,  'Nauru')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(164, 0,  'Niue')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(165, 0,  'New Zealand')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(166, 0,  'Oman')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(167, 0,  'Panama')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(168, 0,  'Peru')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(169, 0,  'French Polynesia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(170, 0,  'Papua New Guinea')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(171, 0,  'Philippines')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(172, 0,  'Pakistan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(173, 0,  'Poland')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(174, 0,  'Saint Pierre and Miquelon')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(175, 0,  'Pitcairn Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(176, 0,  'Puerto Rico')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(177, 0,  'Palestinian Territory (West Bank and Gaza Strip)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(178, 0,  'Portugal')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(179, 0,  'Palau')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(180, 0,  'Paraguay')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(181, 0,  'Qatar')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(182, 0,  'Reunion')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(183, 0,  'Romania')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(184, 0,  'Russia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(185, 0,  'Rwanda')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(186, 0,  'Saudi Arabia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(187, 0,  'Solomon Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(188, 0,  'Seychelles')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(189, 0,  'Sudan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(190, 0,  'Sweden')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(191, 0,  'Singapore')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(192, 0,  'Saint Helena')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(193, 0,  'Slovenia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(194, 0,  'Svalbard and Jan Mayen Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(195, 0,  'Slovakia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(196, 0,  'Sierra Leone')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(197, 0,  'San Marino')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(198, 0,  'Senegal')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(199, 0,  'Somalia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(200, 0,  'Suriname')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(201, 0,  'Sao Tome and Principe')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(202, 0,  'El Salvador')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(203, 0,  'Sweden')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(204, 0,  'Syria')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(205, 0,  'Swaziland')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(206, 0,  'Turks and Caicos Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(207, 0,  'Chad (Tchad)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(208, 0,  'French Southern Territories')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(209, 0,  'Togo')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(210, 0,  'Thailand')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(211, 0,  'Tajikistan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(212, 0,  'Tokelau')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(213, 0,  'Timor-Leste (East Timor)')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(214, 0,  'Turkmenistan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(215, 0,  'Tunisia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(216, 0,  'Tonga')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(217, 0,  'Turkey')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(218, 0,  'Trinidad and Tobago')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(219, 0,  'Tuvalu')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(220, 0,  'Taiwan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(221, 0,  'Tanzania')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(222, 0,  'Ukraine')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(223, 0,  'Uganda')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(224, 0,  'United Kingdom')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(225, 0,  'United States Minor Outlying Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(226, 0,  'United States')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(227, 0,  'Uruguay')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(228, 0,  'Uzbekistan')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(229, 0,  'Vatican City')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(230, 0,  'Saint Vincent and the Grenadines')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(231, 0,  'Venezuela')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(232, 0,  'British Virgin Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(233, 0,  'U.S. Virgin Islands')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(234, 0,  'Vietnam')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(235, 0,  'Vanuatu')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(236, 0,  'Wallis and Futuna')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(237, 0,  'Samoa')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(238, 0,  'Yemen')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(239, 0,  'Mayotte')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(240, 0,  'South Africa')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(241, 0,  'Zambia')\n";
 sql += "INSERT INTO countries(country_id, continent_id, country_name) VALUES(242, 0,  'Zimbabwe')\n";

     BufferedReader reader = new BufferedReader(new StringReader(sql));
     String line = reader.readLine();

     try {
         while ( line != null) {
	     line = line.trim();
             if (!line.equals("")) {
	        if (line.startsWith("CREATE ")) {
		    try {
		       statement.execute(line);
		    } catch (Exception error) {}
		} else {
		    System.out.println("-----------------");
		    System.out.println(line);
                    statement.execute(line);
		}	
	     }  
             line = reader.readLine();
         }
     } catch (Exception e) {
	 throw e;
     } 

     statement.execute("SET foreign_key_checks = 1;");     
     conn.close();
     reader.close();
  


%>