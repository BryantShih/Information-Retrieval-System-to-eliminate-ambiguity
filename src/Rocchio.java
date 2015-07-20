import java.util.*;
import java.util.Map.Entry;

public class Rocchio {
	//get setting parameter
	private Setting_constant setting  = new Setting_constant();
	//Roccio vactor
	private HashMap<String, Double> query = new HashMap<String, Double>();
	
	public void Rocchio_init(String first_query){
		String[] init = first_query.toLowerCase().split(" ");
		for (int i = 0; i < init.length; i++){
			query.put(init[i], 10.0);
		}
	}
	//Compute Rocchio vector and return the top 2 highest scoring terms

	public HashMap<String,ArrayList<String>> get_expand_q(String current_query ,HashMap<String, HashMap<String, ArrayList<Integer>>> idf, ArrayList<Doc_format> doc_list, ArrayList<Integer> relevant_doclist, ArrayList<Integer> nonrelevant_doclist){
		int count = 0;
		ArrayList<String> expand_query = new ArrayList<String>();
		ArrayList<String> expand_query2 = new ArrayList<String>();
		HashMap<String,ArrayList<String>> return_query = new HashMap<String,ArrayList<String>>();
		
		//upadate current query list used to check the duplicate query in expand query
		String[] tokens = current_query.split("%20");
		HashMap<String, Boolean> current_query_list = new HashMap<String, Boolean>();
		
		for(int s = 0; s< tokens.length; s++) {
			current_query_list.put(tokens[s].toLowerCase(), true);
		}
		
		
		//stem
		//-----------------------------------------
		
		
		
		//initialize weight list and set vector for each key to 0.0
		//-----------------------------------------------------------
		HashMap<String,Double> weight_list = new HashMap<String,Double>();
		
		Set<Entry<String,HashMap<String, ArrayList<Integer>>>> entries = idf.entrySet();
		if(entries != null){
        	Iterator<Entry<String, HashMap<String, ArrayList<Integer>>>> iterator = entries.iterator();
        	//Map.Entry<String, HashMap<String, ArrayList<Integer>>> entry;
        	while(iterator.hasNext()){
        		//entry = iterator.next();
        		weight_list.put(iterator.next().getKey(), 0.0);
        		//Object value = entry.getValue();
//        		//System.out.println(  j + ":" + key);	
        	}
		}
		
		//Genearate vector for relevantDocsTFWeights and nonrelevantDocsTFWeights
		//-----------------------------------------------------------------------
		HashMap<String,Integer> relevantDocsTFWeights = new HashMap<String,Integer>();
		HashMap<String,Integer> nonrelevantDocsTFWeights = new HashMap<String,Integer>(); 
		
		//scan nonrelevant_doclist
		Set<Entry<String, Integer>> entries1;
		Iterator<Entry<String, Integer>> iterator1;
		Map.Entry<String, Integer> entry1;
		for(int i =0; i < nonrelevant_doclist.size(); i++){
			Doc_format doc = doc_list.get(nonrelevant_doclist.get(i)-1);
			entries1 = doc.get_Tfv().entrySet();
			iterator1 = entries1.iterator();
			while(iterator1.hasNext()){
				entry1 = iterator1.next();
				if (nonrelevantDocsTFWeights.containsKey(entry1.getKey())){
					nonrelevantDocsTFWeights.put(entry1.getKey(), nonrelevantDocsTFWeights.get(entry1.getKey()) + doc.get_Tfv().get(entry1.getKey()));
				}
				else{
					nonrelevantDocsTFWeights.put(entry1.getKey(),doc.get_Tfv().get(entry1.getKey()));
				}
			}
			
		}
		//scan relevant_doclist
		for(int i =0; i < relevant_doclist.size(); i++){
			Doc_format doc = doc_list.get(relevant_doclist.get(i)-1);
			entries1 = doc.get_Tfv().entrySet();
			iterator1 = entries1.iterator();
			
//			for(Entry<String, Integer> entry : entries1)	{
//				entry.getKey();
//			}
			
			while(iterator1.hasNext()){
				entry1 = iterator1.next();
				if (relevantDocsTFWeights.containsKey(entry1.getKey())){
					relevantDocsTFWeights.put(entry1.getKey(), relevantDocsTFWeights.get(entry1.getKey()) + doc.get_Tfv().get(entry1.getKey()));
				}
				else{
					relevantDocsTFWeights.put(entry1.getKey(), doc.get_Tfv().get(entry1.getKey()));
				}
			}
		}
		
		//Generate Rocchio vector
		//------------------------------------------	
		Iterator<Entry<String, HashMap<String, ArrayList<Integer>>>> iterator;
    	Map.Entry<String, HashMap<String, ArrayList<Integer>>> entry;
		
    	Set<Entry<String, ArrayList<Integer>>> entries_i;
		Iterator<Entry<String, ArrayList<Integer>>> iterator_i;
		Map.Entry<String, ArrayList<Integer>> entry_i;
		String key;
		double idf_num;
		
		entries = idf.entrySet();
		if(entries != null){
			iterator = entries.iterator();
        	while(iterator.hasNext()){
        		entry = iterator.next();
        		idf_num = Math.log( ((double)doc_list.size())/((double)idf.get(entry.getKey()).size()) );
        		
        		entries_i = idf.get(entry.getKey()).entrySet();
        		if(entries_i != null){
        			iterator_i = entries_i.iterator();
        			while(iterator_i.hasNext()){
        				entry_i = iterator_i.next();
        				key = entry_i.getKey();
        				if (doc_list.get(Integer.parseInt(key)-1).get_Ir() ){
        					weight_list.put(entry.getKey(), weight_list.get(entry.getKey())+ setting.beta * idf_num * (relevantDocsTFWeights.get(entry.getKey()) / relevant_doclist.size()));
        				}
        				else{
        					weight_list.put(entry.getKey(), weight_list.get(entry.getKey())- setting.gamma * idf_num * (nonrelevantDocsTFWeights.get(entry.getKey()) / nonrelevant_doclist.size()));
        				}
        			}
        		}
        		
        		//build new vector
            	if (query.containsKey(entry.getKey())){
            		query.put(entry.getKey(), setting.alpha * query.get(entry.getKey()) + weight_list.get(entry.getKey()));
            	}
            	else{
            		query.put(entry.getKey(), weight_list.get(entry.getKey()));
            	}
        	}
        	
		}
		//pick top 2 hieghest scoring terms in query and return
		count = 0;
		expand_query.clear();
		
		ArrayList<String> sorted = sortMapByValue((HashMap<String, Double>) query);
		
		//amplify the score
//		for(Entry<String, Double> entry_q: query.entrySet()){
//			query.put(entry_q.getKey(),entry_q.getValue()*100);
//			System.out.print(entry_q.getKey() + "  : ");
//			System.out.println(entry_q.getValue());
//		}
		//pick top 2 hieghest scoring terms/		
		for(int i = 0; i < sorted.size(); i++)	{
			if (current_query_list.containsKey(sorted.get(i).toLowerCase())){
				//do nothing
			}
			else{
				//compare the weight of top1 adn top2, if top1 is two times big than top2 then ignore top1.
				if (count == 1)	{
					if( query.get(expand_query.get(0)) /query.get(sorted.get(i)) >=2 ){
						break;
					}
				}
				expand_query.add(sorted.get(i));
				expand_query2.add(sorted.get(i));
				count++;
				if (count >= 2)
					break;
			}
		}
		
		return_query.put("1", expand_query);
		
		//resort the order of query
		//add current into expand_query before resorting.
		
		String[] init = current_query.toLowerCase().trim().split("%20");
		for (int i = 0; i < init.length; i++){
			expand_query2.add(init[i]);
		}
		//sorting the terms in expand_query by thier weight(high to low)
		String temp = new String();
		int q_i = 0;
		for (int q = 1; q < expand_query2.size(); q++){
			temp = expand_query2.get(q);
			q_i = q - 1;
			while(q_i >= 0 && query.get(temp) > query.get(expand_query2.get(q_i))){
				expand_query2.set(q_i + 1, expand_query2.get(q_i));
				q_i --;
			}
			expand_query2.set(q_i + 1, temp);
		}
		return_query.put("2", expand_query2);
		
		return return_query;
	}
	
	public ArrayList<String> sortMapByValue(HashMap<String, Double> map) {
		int size = map.size();
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(
				size);
		list.addAll(map.entrySet());
		
		ValueComparator vc = new ValueComparator();
		Collections.sort(list, vc);
		
		ArrayList<String> keys = new ArrayList<String>(size);
		
		for (int i = 0; i < size; i++) {
			keys.add(i, list.get(i).getKey());
		}
		return keys;
	}

	private class ValueComparator implements Comparator<Map.Entry<String, Double>> {
		public int compare(Map.Entry<String, Double> mp1, Map.Entry<String, Double> mp2) {
			return (int)(mp2.getValue() - mp1.getValue());
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
