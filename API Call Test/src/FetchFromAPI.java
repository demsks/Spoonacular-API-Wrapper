import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FetchFromAPI {

	//These are needed for the API connection to succeed
	private final String USER_AGENT = "Mozilla/5.0";
	private final String API_KEY = "4La0d7DdzBmshTuDWzkODqADfK4up1vuJkljsnpNaMfLWInYch";


	public ArrayList<Recipe> getRecipeData(String[] ingredients) throws Exception {

		//Proper url format below
		//"https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=apples%2Cflour%2Csugar&limitLicense=false&number=3&ranking=1";
		final String base = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=";
		final String tail = "&limitLicense=false&number=3&ranking=1";
		
		StringBuilder recStrBldr = new StringBuilder(""); 
		String seperator = "%2C";
		int numIngredients = ingredients.length;
		int lastIngredient = numIngredients-1;
		//Build the ingredientString based on the above url format using %2C for seperators
		for(int k = 0; k < numIngredients; k++ ) {
			String curIng = ingredients[k];
			recStrBldr.append(curIng);
			
			if(k!=lastIngredient) {
				recStrBldr.append(seperator);
			}
		}
		
		String url = base+recStrBldr.toString()+tail;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("X-Mashape-Key", API_KEY);

//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		
		String line;
		//StringBuffer response = new StringBuffer();

		try {
		    while ((line = reader.readLine()) != null) {
		        sb.append(line);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		    	reader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		String resultString = sb.toString();
		//System.out.println(resultString + "\n");
		
		ArrayList<Recipe> MatchedRecipeList = new ArrayList<Recipe>();
		
		String[] parsed = resultString.split(Pattern.quote("}"));
		for(int i = 0; i < parsed.length-1; i++){
			//System.out.println(a);
			//Split each recipe by ,
			String idTitleAr[] = parsed[i].split(Pattern.quote(","));
			//Extract the id and recipe name
			String id = null;
			String recName = null;
			String instructions = "None Found";
			//Because of how we split, we have to do below.
			if(i==0) { id = idTitleAr[0]; recName = idTitleAr[1];}
			else 	 { id = idTitleAr[1]; recName = idTitleAr[2];}
			//Get only the id and the recipeName
			id = id.replaceAll("\\D+","");
			recName = recName.split(Pattern.quote(":"))[1].replaceAll("\"", "");
			instructions = getInstructions(id);
			
			MatchedRecipeList.add(new Recipe(id, recName, instructions));
			
//			System.out.println(id);	System.out.println(recName);	System.out.println(instructions);	System.out.println("\n");
			
		}
		
		return MatchedRecipeList;
	}

	private String getInstructions(String recipeId) throws Exception {

	//Proper url format listed below.
	//"https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/479101/information?includeNutrition=false"
	String base = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
	String Id = recipeId;
	String tail = "/information?includeNutrition=false";
	String url = base+Id+tail;
	
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	// optional default is GET
	con.setRequestMethod("GET");

	//add request header
	con.setRequestProperty("User-Agent", USER_AGENT);
	con.setRequestProperty("X-Mashape-Key", API_KEY);

	int responseCode = con.getResponseCode();
//	System.out.println("\nSending 'GET' request to URL : " + url);
//	System.out.println("Response Code : " + responseCode);

	BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	StringBuilder sb = new StringBuilder();
	
	String line;
	//StringBuffer response = new StringBuffer();

	try {
	    while ((line = reader.readLine()) != null) {
	        sb.append(line);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
	    	reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	String resultString = sb.toString();
	String instructions = "None Found";
	//print result
	//System.out.println(resultString + "\n");
	
	String[] parsed = resultString.split(Pattern.quote("instructions"));
	for(int i = 0; i < parsed.length-1; i++){
		String target[] = parsed[1].replaceAll("\"", "").split(Pattern.quote(",analyzedInstructions"));
		instructions = target[0].replace(":", "");	
		//System.out.println(instructions);

		
	}
	return instructions;
}


	
	
}

