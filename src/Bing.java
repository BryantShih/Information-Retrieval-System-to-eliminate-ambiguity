import java.util.*;
//import java.util.Map.Entry;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;

public class Bing {

    public static void main(String[] args) {
    	   	
    	String accountKey = args[0];
    	double target_precision = Double.parseDouble(args[1]);
	
    	//parameters initiation
    	//======================================================
    	//dictionary used to store inverted term frquency
    	Idf_file idf;
    	//list used to store the document
    	ArrayList<Doc_format> doc_list; 
    	ArrayList<Integer> relevant_doclist;
    	ArrayList<Integer> nonrelevant_doclist;
    	HashMap<String,ArrayList<String>> get_expand_query;
    	//get setting parameters
    	Setting_constant setting = new Setting_constant();
    	setting.setHash();
    	//get setting parameters
    	BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
    	//Rocchio algorithm
    	Rocchio rocchio = new Rocchio();
    	double current_precision = 0;
    	
        String expand_query_str = "";
        
        //initialize Rocchio object
        
        // pass in the search terms
        StringBuilder stringB = new StringBuilder();
        stringB.append(args[2]);
        int k = 3;
        
    	while(k < args.length)	{
    		stringB.append(" ");
    		stringB.append(args[k]);
    		k++;
    	}
	
    	String combination = stringB.toString();
        rocchio.Rocchio_init(combination);
    	String searchText = combination;
    	
//    	System.out.println("*******These are the terms********");
//    	System.out.println(combination);
//    	System.out.println("*************************");
    	  	
    	String cover;
        searchText = searchText.replaceAll(" ", "%20");
		
        byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);
        URL url;
        
        try {      	
	        while( current_precision < target_precision){ //current_precision != 0
	        	
	        	cover = searchText.replaceAll( "%20", " ");
	        	// print info
	        	System.out.println("Parameter:");
	            System.out.println("Client Key = " + accountKey);
	            System.out.println("Query      = " + cover.toLowerCase());
	            System.out.println("Precision  = " + target_precision);
	            System.out.println("URL:" + setting.bing_url + "Query%27" + setting.search_text + "%27&$top=" + setting.search_num + "&$");
	            System.out.println("Number of result: " + setting.search_num);
	            System.out.println("Bing Search Result");
	           
	        	
	        	//--------------------------------Bing search start-----------------------------	
		        url = new URL("https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query=%27" + searchText + "%27&$top=" + setting.search_num + "&$format=Atom");
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
		       
		        //conn.setRequestProperty("Accept", "application/json");
		
		        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		        StringBuilder sb = new StringBuilder();
		        String output;
		        //System.out.println("Output from Server .... \n");
		        char[] buffer;
		        
		        while ((output = br.readLine()) != null) {
		            sb.append(output);     
		        } 
		        
		        // disconnect 
		        conn.disconnect(); 
		        //---------------------------------Bing search end-----------------------------
	        
		        //new iteration and reset 
		        //----------------------------------------------------
		        current_precision = 0; 
		        relevant_doclist = new ArrayList<Integer>();
		    	nonrelevant_doclist = new ArrayList<Integer>();
		    	doc_list = new ArrayList<Doc_format>();
		    	idf = new Idf_file();
		        //Idf_file.clear();
		    	
		    	
		        for(int i = 1; i < 11; i++)	{
		        	//new a class Doc_format oblect
		        	Doc_format doc = new Doc_format();
		        	int entryFirst = sb.indexOf("<entry>");
		            int entryLast = sb.indexOf("</entry>");
		            entryLast += 8;
		            
		        	// clear the buffer
		        	buffer = new char[4096];
		        	
			        sb.getChars(entryFirst, entryLast, buffer, 0);
			        
			        System.out.println("\n--------------------------------------------------------------");
			        System.out.println("Result " + i );
			        //System.out.println("--------------------------------------------------------------");
			        
			        //System.out.println(buffer);    
			        String str = new String(buffer);
		
			        // title 
			        //---------------------------------------------
			        int title = str.indexOf("<d:Title");
			        title += 29;
			        int titleEnd = str.indexOf("</d:Title>");		        
			        char[] buffer2 = new char[2048];
			        str.getChars(title, titleEnd, buffer2, 0);	       
			        String str_title = new String(buffer2);	  
			        System.out.println("[");
			        System.out.print(" Title        :");
			        str_title = Jsoup.parse(str_title).text();    
			        System.out.println(str_title); 
			        doc.set_ID(Integer.toString(i));
			        doc.set_Title(str_title);
			        
			        // summary
			        //---------------------------------------------
			        int description = str.indexOf("<d:Description");
			        description += 35;
			        int descriptionEnd = str.indexOf("</d:Description>");		        
			        char[] buffer3 = new char[2048];
			        str.getChars(description, descriptionEnd, buffer3, 0);	  
			        String str_summary = new String(buffer3);		          
			        System.out.print(" Summary      :");
			        str_summary = Jsoup.parse(str_summary).text();    
			        System.out.println(str_summary);
			        doc.set_Summary(str_summary);
			        
			        // URL 
			        //----------------------------------------------
			        int urlurl = str.indexOf("<d:Url");
			        urlurl += 27;
			        int urlEnd = str.indexOf("</d:Url>");
			        char[] buffer4 = new char[2048];
			        str.getChars(urlurl, urlEnd, buffer4, 0);	 
			        String str_URL = new String(buffer4);
			        System.out.print(" URL          :");
			        str_URL = Jsoup.parse(str_URL).text();    
			        System.out.println(str_URL);
			        System.out.println("]");
			        doc.set_URL(str_URL);
			        
			        String remaining = sb.substring(entryLast);      
		        	sb.delete(0, sb.length());
		        	sb.append(remaining);
			        
		        	
			        //User feeback  
			        //----------------------------------------------
			        boolean feedback_lock = true;
			        while(feedback_lock){
			        	System.out.print("Relevant (Y/N)?");
				        //@SuppressWarnings("resource")
						String feedback = bufferReader.readLine();
			        	//String test = "Y";
				        //System.out.printf("%s", scanner.next());
				        if (feedback.equals("Y")|| feedback.equals("y")){
				        	doc.set_Ir(true);
				        	relevant_doclist.add(i);
				        	feedback_lock = false;
				        }
				        else{
				        	if (feedback.equals("N")  || feedback.equals("n")){
				        		doc.set_Ir(false);
				        		nonrelevant_doclist.add(i);
				        		feedback_lock = false;
				        	}
				        	else{
				        		System.out.print("Error, please enter again. Relevant (Y/N)?");
				        		//scanner = new Scanner(System.in);
				        	}
				        }
			        }

			        doc_list.add(doc);
		        }
		        //===============================================
		        
		        //html parser     
		        
		        
		        //precision
		        for (int j = 0; j < doc_list.size(); j++){
		        	if ( doc_list.get(j).get_Ir() ) {
		        		current_precision++;
		        	}
		        }    
		        current_precision = current_precision / setting.search_num;
		        //SHOW INFO
		        System.out.println("======================");
		        System.out.println("FEEDBACK SUMMARY");
		        System.out.println("Query       =" + searchText.toLowerCase().replace("%20", " "));
		        System.out.println("Precison    =" +  current_precision);		   
		        //If precision = 0 then stop the program.
		        
		        if(current_precision < target_precision){
		        	System.out.println("Still below desired precision of " + target_precision);
		        	
		        	if(current_precision == 0){
		        		System.out.println("Below desired precision, but can no longer augment query");
		        		return;
		        	}
		        }
		        else{
		        	System.out.println("Desired precision reached, done");
		        	return;
		        }
		        //System.out.println(current_precision);
		        
		        System.out.println("indcexing...");
		        System.out.println("indcexing...");
		        
		        //IR PROCESSING
		        //---------------------------------------------------------
		        
		        for(int j = 0; j < doc_list.size(); j++){
		        	//tokenization
		        	//-------------------------------------------------------------------------------------
		        	String[] token1 = doc_list.get(j).get_Summary().toLowerCase().split(setting.delims);
		        	String[] token2 = doc_list.get(j).get_title().toLowerCase().split(setting.delims);
		        	String[] token3 = doc_list.get(j).get_URL().toLowerCase().split(setting.delims);
		        	
		        	ArrayList<String> token_all = new ArrayList<String>(token1.length + token2.length + token3.length);
		            Collections.addAll(token_all, token1);
		            Collections.addAll(token_all, token2);
		            Collections.addAll(token_all, token3);
		            String[] token = token_all.toArray(new String[token_all.size()]);
		            
		        	for(int i = 0; i< token.length; i++){
		        		if (token[i].equals("") || token[i].length() <= 1 || token[i].length() > 10 || setting.stop_words.containsKey(token[i])){// use is_number()? 
		        			//do nothing
		        		}
		        		else{
		        			doc_list.get(j).Add_HS(token[i]); //Term frequency for specific document
		        			//System.out.println(doc_list.get(j).get_ID());  			
		        			idf.add_idf(doc_list.get(j).get_ID(), token[i], i); //Inverted term frquency for whole document set in this iteration.
		        			
		        		}
		        	}        	
		        }
		        get_expand_query = rocchio.get_expand_q(searchText, idf.get_idf(), doc_list, relevant_doclist,  nonrelevant_doclist);	        
		        expand_query_str = "";
		        for(int g = 0; g < get_expand_query.get("1").size(); g++){
		        	if(g == 0){ 
		        		expand_query_str += get_expand_query.get("1").get(g);
		        	}
		        	else{
		        		expand_query_str += "%20" +  get_expand_query.get("1").get(g);
		        	}
		        }
		        //show info
		        cover = expand_query_str.replaceAll("%20", " ");
		        System.out.println("menting by  " + cover);
		        
		        expand_query_str = "";
		        for(int g = 0; g < get_expand_query.get("2").size(); g++){
		        	if(g == 0){ 
		        	expand_query_str += get_expand_query.get("2").get(g);
		        	}
		        	else{
		        		expand_query_str += "%20" +  get_expand_query.get("2").get(g);
		        	}
		        }
		        searchText = expand_query_str;
		    
	        }//while
        }//try
        catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
			
    }        
}

