import java.util.*;

public class Idf_file {
	
	private HashMap<String, HashMap<String, ArrayList<Integer>>> hm = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
	
	public void add_idf(String id, String str, int position){
		if (hm.containsKey(str)){
			if(hm.get(str).containsKey(id)){
				hm.get(str).get(id).add(position);
			}
			else{
				ArrayList<Integer> init = new ArrayList<Integer>();
				init.add(position);
				hm.get(str).put(id, init );
			}
			
		}
		else{
			HashMap<String, ArrayList<Integer>> init_ht = new HashMap<String, ArrayList<Integer>>();
			ArrayList<Integer> init_al = new ArrayList<Integer>();
			init_al.add(position);
			init_ht.put(id, init_al);
			hm.put(str, init_ht );
		}
	}
//	public HashMap<String, HashMap<String, ArrayList<Integer>>> get_idf(){
//		return hm;
//	}
	
	public int get_tf_num(String str, int id){
		return hm.get(str).get(id).size();
	}
	public int get_idf_num(String str){
		return hm.get(str).size(); 
	}
	public HashMap<String, HashMap<String, ArrayList<Integer>>> get_idf(){
		return hm;
	}
//	public void clear(){
//		hm = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
//	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
