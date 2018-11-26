import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws Exception {
		
		String[] ingredients = { "apples", "flour", "sugar"};
		

		//This is how we call the API. We make a new instance of the FetchFromApi class, pass the ingredients, and store the results in a local ArrayList<Recipe>
		FetchFromAPI http = new FetchFromAPI();
		ArrayList<Recipe> recipes = http.getRecipeData(ingredients);
		
		//Test print to show recipes ArrayList is being populated and its contents are accessible
		for(Recipe r: recipes) {
			System.out.println(r.getId());
			System.out.println(r.getRecipeName());
			System.out.println(r.getRecipeInstructions() + "\n");
		}

	}

}
