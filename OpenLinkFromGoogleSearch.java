package mainPackage;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

class BrowserHelper{
		public static String SEARCH_STRING="LexisNexis";
		public static String SEARCH_URL="http://www.google.com/search?q=";
		public static String SEARCH_LINK = "href=\"http";
		public static String START_SEARCH_TAB="<title>";
		public static String END_SEARCH_TAB="</title>";
		public static int NUMBER_LINK_FOR_OPEN=3;
		
		private ArrayList<String>  limkOpen = new ArrayList<String>();
		public BrowserHelper() {		}
		
		public void OpenBrowser(String oUrl){
			
			Desktop desktop = java.awt.Desktop.getDesktop();
			URI oURL;
			try {
				oURL = new URI(oUrl);
				desktop.browse(oURL);
			} catch (URISyntaxException e) {			
				e.printStackTrace();
				}
			 catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		public void SearchLink() {
		   this.OpenBrowser(SEARCH_URL + SEARCH_STRING);
			String strForCheck="";	
			try {			
					URL rURL = new URL(SEARCH_URL + SEARCH_STRING);			
					HttpURLConnection httpCon = (HttpURLConnection) rURL.openConnection();
					httpCon.addRequestProperty("User-Agent", "Mozilla/4.76");			
					
					try (BufferedReader reader = new BufferedReader(
				          new InputStreamReader(httpCon.getInputStream())))//  reader.lines().forEach(System.out::println);
					{
						  for (String line = reader.readLine(); line != null; line = reader.readLine()) {
							strForCheck+=line;	
							//System.out.println(line);
						 }				
						reader.close();	
					}
			      
				    } catch (MalformedURLException me) {
				        System.out.println("MalformedURLException: " + me);
				    } catch (IOException ioe) {
				        System.out.println("IOException: " + ioe);
			 } 	
			
			 
			 String tmpStr=strForCheck;
			 int linlLength, fromSearch,i=0;
			
			while(strForCheck.length()>0) {			
				
				 fromSearch = strForCheck.indexOf(SEARCH_LINK);
				 tmpStr = strForCheck.substring(fromSearch);
				 
				 linlLength = tmpStr.indexOf('?');
				 
				 limkOpen.add(strForCheck.substring((fromSearch+6),(fromSearch+linlLength)));
				 //System.out.println(limkOpen.get(i));
				 
				 fromSearch+=limkOpen.get(i++).length();
				
				 if(fromSearch<tmpStr.length())
					 strForCheck=tmpStr.substring(fromSearch);
				 else
					 break;
			 }	 
			this.SearchTitele();
	 }	
		
	public void SearchTitele() {
	        this.OpenBrowser(limkOpen.get(NUMBER_LINK_FOR_OPEN));	
	        
			String strHtml="";	
			try {	
				
				URL url = new URL(limkOpen.get(NUMBER_LINK_FOR_OPEN));	
				
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.addRequestProperty("User-Agent", "user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
				httpCon.addRequestProperty("Sccept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				httpCon.addRequestProperty("Accept-Language", "en-US,en;q=0.5");
				httpCon.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
				httpCon.addRequestProperty("Connection", "keep-alive");
				httpCon.addRequestProperty("Upgrade-Insecure-Requests", "1");
				
									
			    BufferedReader reader;
			    reader = new BufferedReader(new InputStreamReader(url.openStream()));		    
			    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			    	strHtml+=line;	
					//System.out.println(line);
				 }				
				reader.close();	
			} catch (MalformedURLException e) {			
				e.printStackTrace();
			} catch (IOException e1) {			
				e1.printStackTrace();
			}  
			
			int startTitle = strHtml.indexOf(START_SEARCH_TAB)+ (START_SEARCH_TAB.length()-1);
			int endTitle = strHtml.indexOf(END_SEARCH_TAB);
			System.out.println("The link is : " + limkOpen.get(NUMBER_LINK_FOR_OPEN));
			System.out.println("The link's title is: " + strHtml.substring(startTitle,endTitle ));
		}
	}
public class OpenLinkFromGoogleSearch {
	public static void main(String[] args) {
		BrowserHelper opB=new BrowserHelper();
	    opB.SearchLink();
	}
}
