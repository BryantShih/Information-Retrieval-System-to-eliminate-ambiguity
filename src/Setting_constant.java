
import java.util.*;      

public class Setting_constant {

	//private final int threads = 1;
	public final String delims = "[\\s^,=?!:@<>()\"\'\\-;&_|.\\/]+"; //\s
	public final String bing_key = "dFkQ9zzsXuD5Mvqsb3jyPvIuPYgTzzOGhiB33K9OPLo";
	public final String bing_url = "https,//api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?";
	public final int search_num = 10;
	public final String search_text = "San";
	public boolean stem = false;
	public boolean stem_in_racchio = false;
	public double alpha = 1.0;
	public double beta = 0.8;
	public double gamma = 0.1;
	public Hashtable<String, Boolean>stop_words = new Hashtable<String, Boolean>(); 
	public int expand_num = 2;
	
	public void setHash(){
		stop_words.put("about" , true);
		stop_words.put("above" , true);
		stop_words.put("after" , true);
		stop_words.put("again" , true);
		stop_words.put("against" , true);
		stop_words.put("all" , true);
		stop_words.put("am" , true);
		stop_words.put("an" , true);
		stop_words.put("and" , true);
		stop_words.put("any" , true);
		stop_words.put("are" , true);
		stop_words.put("aren" , true);
		stop_words.put("as" , true);
		stop_words.put("at" , true);
		stop_words.put("be" , true);
		stop_words.put("because" , true);
		stop_words.put("been" , true);
		stop_words.put("before" , true);
		stop_words.put("being" , true);
		stop_words.put("below" , true);
		stop_words.put("between" , true);
		stop_words.put("both" , true);
		stop_words.put("but" , true);
		stop_words.put("by" , true);
		stop_words.put("can" , true);
		stop_words.put("cannot" , true);
		stop_words.put("could" , true);
		stop_words.put("couldn" , true);
		stop_words.put("did" , true);
		stop_words.put("didn" , true);
		stop_words.put("do" , true);
		stop_words.put("does" , true);
		stop_words.put("doesn" , true);
		stop_words.put("doing" , true);
		stop_words.put("don" , true);
		stop_words.put("down" , true);
		stop_words.put("during" , true);
		stop_words.put("each" , true);
		stop_words.put("few" , true);
		stop_words.put("for" , true);
		stop_words.put("from" , true);
		stop_words.put("further" , true);
		stop_words.put("had" , true);
		stop_words.put("hadn" , true);
		stop_words.put("has" , true);
		stop_words.put("hasn" , true);
		stop_words.put("have" , true);
		stop_words.put("haven" , true);
		stop_words.put("having" , true);
		stop_words.put("he" , true);
		stop_words.put("her" , true);
		stop_words.put("here" , true);
		stop_words.put("here" , true);
		stop_words.put("hers" , true);
		stop_words.put("herself" , true);
		stop_words.put("him" , true);
		stop_words.put("himself" , true);
		stop_words.put("his" , true);
		stop_words.put("how" , true);
		stop_words.put("how" , true);
		stop_words.put("if" , true);
		stop_words.put("in" , true);
		stop_words.put("into" , true);
		stop_words.put("is" , true);
		stop_words.put("isn" , true);
		stop_words.put("it" , true);
		stop_words.put("its" , true);
		stop_words.put("itself" , true);
		stop_words.put("let" , true);
		stop_words.put("me" , true);
		stop_words.put("more" , true);
		stop_words.put("most" , true);
		stop_words.put("mustn" , true);
		stop_words.put("my" , true);
		stop_words.put("myself" , true);
		stop_words.put("no" , true);
		stop_words.put("nor" , true);
		stop_words.put("not" , true);
		stop_words.put("of" , true);
		stop_words.put("off" , true);
		stop_words.put("on" , true);
		stop_words.put("once" , true);
		stop_words.put("only" , true);
		stop_words.put("or" , true);
		stop_words.put("other" , true);
		stop_words.put("ought" , true);
		stop_words.put("our" , true);
		stop_words.put("ours" , true);
		stop_words.put("ourselves" , true);
		stop_words.put("out" , true);
		stop_words.put("over" , true);
		stop_words.put("own" , true);
		stop_words.put("same" , true);
		stop_words.put("shan" , true);
		stop_words.put("she" , true);
		stop_words.put("should" , true);
		stop_words.put("shouldn" , true);
		stop_words.put("so" , true);
		stop_words.put("some" , true);
		stop_words.put("such" , true);
		stop_words.put("than" , true);
		stop_words.put("that" , true);
		stop_words.put("the" , true);
		stop_words.put("their" , true);
		stop_words.put("theirs" , true);
		stop_words.put("them" , true);
		stop_words.put("themselves" , true);
		stop_words.put("then" , true);
		stop_words.put("there" , true);
		stop_words.put("these" , true);
		stop_words.put("they" , true);
		stop_words.put("this" , true);
		stop_words.put("those" , true);
		stop_words.put("through" , true);
		stop_words.put("to" , true);
		stop_words.put("too" , true);
		stop_words.put("under" , true);
		stop_words.put("until" , true);
		stop_words.put("up" , true);
		stop_words.put("very" , true);
		stop_words.put("was" , true);
		stop_words.put("wasn" , true);
		stop_words.put("we" , true);
		stop_words.put("were" , true);
		stop_words.put("weren" , true);
		stop_words.put("what" , true);
		stop_words.put("when" , true);
		stop_words.put("where" , true);
		stop_words.put("which" , true);
		stop_words.put("while" , true);
		stop_words.put("who" , true);
		stop_words.put("whom" , true);
		stop_words.put("why" , true);
		stop_words.put("with" , true);
		stop_words.put("would" , true);
		stop_words.put("wouldn" , true);
		stop_words.put("you" , true);
		stop_words.put("your" , true);
		stop_words.put("yours" , true);
		stop_words.put("yourself" , true);
		stop_words.put("yourselves" , true);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}