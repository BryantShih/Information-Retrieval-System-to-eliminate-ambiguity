import java.util.*;

public class Doc_format {

	private String ID;
	private String Title;
	private String Summary;
	private String Body;
	private String URL;
	private boolean Is_relevant;
	private HashMap<String, Integer>Tf_vector = new HashMap<String, Integer>(); 
	//private Setting_constant setting = new Setting_constant(); 
	
	public void set_ID (String id) {
		this.ID = id;
	}
	public void set_Title (String title) {
		this.Title = title;
	}
	public void set_Summary (String summary) {
		this.Summary = summary;
	}
	public void set_Body (String body) {
		this.Body = body;
	}
	public void set_URL (String url) {
		this.URL = url;
	}
	public void set_Ir (boolean b) {
		this.Is_relevant = b;
	}
	public void Add_HS (String str) {
		
		if (this.Tf_vector.containsKey(str)) {
			Tf_vector.put(str,Tf_vector.get(str)+1);
		}
		else {
			Tf_vector.put(str, 1);
		}
	}
	public String get_ID () {
		return ID;
	}
	public String get_title () {
		return Title;
	}
	public String get_Summary () {
		return Summary;
	}
	public String get_Body () {
		return Body;
	}
	public String get_URL () {
		return URL;
	}
	public boolean get_Ir () {
		return Is_relevant;
	}
	public HashMap<String, Integer> get_Tfv(){
		return Tf_vector;
	}
	public void clear(){
		Tf_vector = new HashMap<String, Integer>();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stu
		
	}

}
