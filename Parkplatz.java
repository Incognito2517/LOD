package LOD;


import java.net.URL;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.lang.Math.*;

public class Parkplatz {

public static String[] parkplatzsuche(double latitude, double longitude){
    double lat = latitude;
    double lon = longitude;
    double mindist = 100;
    
    String[] result = {null, null};
    
    URL statisch = null;
    URL dynamisch = null;
    
    try{
    statisch = new URL("http://offenedaten.frankfurt.de/dataset/e821f156-69cf-4dd0-9ffe-13d9d6218597/resource/eac5ca3d-4285-48f4-bfe3-d3116a262e5f/download/parkdatensta.xml");
    dynamisch = new URL("http://offenedaten.frankfurt.de/dataset/912fe0ab-8976-4837-b591-57dbf163d6e5/resource/48378186-5732-41f3-9823-9d1938f2695e/download/parkdatendyn.xml");
     }catch(Exception e){
    System.out.println(e.getMessage());
    }
    if ((statisch != null) && (dynamisch !=null)){
        try{ //Input aus der URL in ein document übernehmen und normalisieren
            InputStream statischIS = statisch.openStream();
            InputStream dynamischIS = dynamisch.openStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document docStatisch = dBuilder.parse(statischIS);
            Document docDynamisch = dBuilder.parse(dynamischIS);
            docStatisch.getDocumentElement().normalize();
            docDynamisch.getDocumentElement().normalize();
        //statische 
            NodeList staticList = docStatisch.getElementsByTagName("parkingFacility");


            NodeList dynoList = docDynamisch.getElementsByTagName("parkingFacilityStatus");
        //dynamische Liste durchgehen und alle offenen mit freie Plätzen aussuchen
            for (int i = 1; i<dynoList.getLength(); i=i+2){
                Node n_Status = dynoList.item(i);
                Node n_ID = dynoList.item(i-1);

            //System.out.println(n_Status.getFirstChild().getNodeValue());
                if(n_Status.getFirstChild().getNodeValue().equals("open") && n_ID.getChildNodes().item(5).getTextContent() != null){
                    
                 //System.out.println(n_ID.getChildNodes().getLength());
                 //System.out.println(n_ID.getChildNodes().item(0).getTextContent());
                 // ID des freien Parkplatzes System.out.println(n_ID.getChildNodes().item(1).getAttributes().item(0).getTextContent());
                 
                 String s1 = n_ID.getChildNodes().item(1).getAttributes().item(0).getTextContent();
                 //ID des freien Parkplatzes in String gespeichert   
                 for (int ii = 0; ii<staticList.getLength(); ii=ii+1){
                 Node nn = staticList.item(ii);
            //System.out.println(nn.getFirstChild().getNextSibling().getTextContent());
                 //Passende ID aus der statischen liste suchen   
                 String s2 = nn.getFirstChild().getNextSibling().getTextContent();
                    
                    //Matching der freien Parkplätze auf die passenden statischen Daten um lat und long des parkhauses herauszufinden.
                    
                    if(s1.contains(s2)){
                    double latParkhaus = Double.parseDouble(nn.getChildNodes().item(15).getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(1).getTextContent());
                    double lonParkhaus = Double.parseDouble(nn.getChildNodes().item(15).getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(3).getTextContent());
                    
                    double distKM = Haversine.haversine(lat, lon, latParkhaus, lonParkhaus);
                    System.out.println(distKM);
                    if(distKM<=mindist)
                    {mindist = distKM; 
                    result[0] = s2; // Name
                    result[1] = String.valueOf(mindist); // Entfernung in km}
                    
                    }
                    
                    }
                 
                 //System.out.println(n_ID.getChildNodes().item(5).getTextContent());
                }
    }}
    //System.out.println(result[0] + " " + result[1]);
    return result;    
        
        
        
        
        
    }catch(Exception e){System.out.println(e.getMessage()); return result;}
    
}else{  
    return result;}
}}
