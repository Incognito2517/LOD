package LOD;

import org.jinstagram.auth.*;
import org.jinstagram.auth.oauth.*;
import org.jinstagram.auth.model.*;
import org.jinstagram.*;
import org.jinstagram.entity.locations.*;
import org.jinstagram.entity.common.*;
import org.jinstagram.entity.users.feed.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DigitalFRATour{

    public DigitalFRATour() {
        createUIComponents();
    }
    JTextArea eingehend;
    public void createUIComponents() {
		String[] cities = {"Frankfurt", "Düsseldorf", "Mannheim", "München", "Berlin", "Hamburg", "Köln", "London"};
        Map<String, Koordinaten> map = new HashMap<String, Koordinaten>();
        map.put("Frankfurt", new Koordinaten(50.1109221, 8.6821267));
        map.put("Düsseldorf", new Koordinaten(51.2277411, 6.7734556));
        map.put("Mannheim", new Koordinaten(49.45623,11.07841)); //x 49.487459   y  8.466039
        map.put("München", new Koordinaten(48.135125, 11.581981));
        map.put("Berlin", new Koordinaten(52.520007, 13.404954));
        map.put("Hamburg", new Koordinaten(53.551085, 9.993682));
        map.put("Köln", new Koordinaten(50.937531, 6.960279));
        map.put("London", new Koordinaten(51.5073509, -0.1277583));

		JComboBox comboBox1 = new JComboBox(cities);
		JPanel panel1 = new JPanel(new BorderLayout());
		JButton button1 = new JButton("Ok");
		eingehend = new JTextArea(15, 20);
        eingehend.setFont(eingehend.getFont().deriveFont(24f));
        eingehend.setLineWrap(true);
        eingehend.setEditable(false);
        DefaultCaret caret = (DefaultCaret)eingehend.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane fScroller = new JScrollPane(eingehend);
        fScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        fScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        button1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mEvent) {
                double latitude = 0;
                double longitude = 0;
                for (Map.Entry<String, Koordinaten> pair : map.entrySet())
                {
                    if(pair.getKey().equals((String) comboBox1.getSelectedItem())){
                        String key = pair.getKey();
                        Koordinaten value = pair.getValue();
                        latitude = value.latitude;
                        longitude = value.longitude;
                    }
                }
                los(latitude, longitude);
            }
        });
		JFrame frame = new JFrame("Fotospot");
		panel1.add(comboBox1, BorderLayout.NORTH);
		panel1.add(button1, BorderLayout.SOUTH);
		panel1.add(new JLabel(new ImageIcon("C:\\Users\\klatta\\Pictures\\Google maps.png")), BorderLayout.WEST);
		panel1.add(fScroller, BorderLayout.CENTER);
		frame.add(panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 700);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void los(double latitude, double longitude){
		InstagramService service = new InstagramAuthService()
				.apiKey("4572d75237ca43a3a6ea6e832ddf05fc")
				.apiSecret("750f6520a671419b9123171c8b90c238")
				.callback("http://www.imbit.dhbw-mannheim.de")
				.scope("public_content")
				.build();

		//String authorizationUrl = service.getAuthorizationUrl();
		//System.out.println(authorizationUrl);

		//74970787c4624ef99b369da64406234d
		//Verifier verifier = new Verifier("74970787c4624ef99b369da64406234d");
		//accessToken = service.getAccessToken(verifier);
		Token accessToken = new Token("5457069397.4572d75.60764f13fe2b484e912962ad9bb32bad","");
		//System.out.println(accessToken.getToken() + " " + accessToken.getSecret());

		Instagram instagram = new Instagram(accessToken);

		//double latitude = 49.45623;
		//double longitude = 11.07841;
		try{
			LocationSearchFeed searchFeed = instagram.searchLocation(latitude, longitude, 4500);
			//eingehend.append("Schleife 1" + "\n");

			// Liefert Vorschläge
			for (Location location : searchFeed.getLocationList()) {
				/*eingehend.append("Foto 1" + "\n");
				//System.out.println("id : " + location.getId());
				eingehend.append("id : " + location.getId() + "\n");
				//System.out.println("name : " + location.getName());
				eingehend.append("name : " + location.getName() + "\n");
				//System.out.println("latitude : " + location.getLatitude());
				eingehend.append("latitude : " + location.getLatitude() + "\n");
				//System.out.println("longitude : " + location.getLongitude());
				eingehend.append("longitude : " + location.getLongitude() + "\n");
				eingehend.append("" + "\n");*/
			}
		}catch(Exception e){System.out.println("Fehler: " + e.getMessage());}

		int likeCounter = 0;
		double lonImage = 0;
		double latImage = 0;
		try{
			//LocationSearchFeed searchFeed = instagram.searchLocation(latitude, longitude);
			MediaFeed feed = instagram.searchMedia(latitude, longitude, 4500);
			List<MediaFeedData> feeds = feed.getData();
			//System.out.println(feeds.size());

			eingehend.append("Schleife 2" + "\n");

			//Info über Fotos
			for (int i = 0; i<feeds.size(); i++){
				if(likeCounter <= feeds.get(i).getLikes().getCount()){
					likeCounter = feeds.get(i).getLikes().getCount();
					lonImage = feeds.get(i).getLocation().getLongitude();
					latImage = feeds.get(i).getLocation().getLatitude();
				}
				eingehend.append("Foto1" + "\n");
				eingehend.append("id : " + feeds.get(i).getId() + "\n");
				eingehend.append("comments : " + feeds.get(i).getComments() + "\n");
				eingehend.append("likes : " + feeds.get(i).getLikes() + "\n");
				eingehend.append("Location: " + feeds.get(i).getLocation() + "\n");
				eingehend.append("" + "\n");
			}
		}catch(Exception e){System.out.println("Fehler: " + e.getMessage());}

		String[] parkplatz = Parkplatz.parkplatzsuche(latImage, lonImage);

		// Parkplatz zu dem Foto mit meistens Likes
		eingehend.append(parkplatz[0] + " ist das nächste Parkhaus in " + parkplatz[1].substring(0, 5) + " km." + "\n");


	}

    public static void main(String[] args) {
        new DigitalFRATour();
	}
}
