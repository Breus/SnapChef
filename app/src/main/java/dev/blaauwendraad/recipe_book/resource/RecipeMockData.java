package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.dto.IngredientDTO;
import dev.blaauwendraad.recipe_book.dto.PreparationStepDTO;
import dev.blaauwendraad.recipe_book.dto.RecipeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains mock recipe data that was previously in RecipeResource.
 * It provides static methods to access the mock data.
 */
public class RecipeMockData {
    
    /**
     * Returns a list of mock recipes with their ingredients and preparation steps.
     * 
     * @return List of RecipeDTO objects
     */
    public static List<RecipeDTO> getMockRecipes() {
        // Create a list to hold our mocked recipes
        List<RecipeDTO> recipes = new ArrayList<>();
        
        // Create ingredients for first recipe - Classic Chocolate Chip Cookies
        List<IngredientDTO> cookiesIngredients = new ArrayList<>();
        cookiesIngredients.add(new IngredientDTO("All-purpose flour", "2 1/4 cups", 1));
        cookiesIngredients.add(new IngredientDTO("Baking soda", "1 tsp", 2));
        cookiesIngredients.add(new IngredientDTO("Salt", "1 tsp", 3));
        cookiesIngredients.add(new IngredientDTO("Butter", "1 cup", 4));
        cookiesIngredients.add(new IngredientDTO("Brown sugar", "3/4 cup", 5));
        cookiesIngredients.add(new IngredientDTO("White sugar", "1/4 cup", 6));
        cookiesIngredients.add(new IngredientDTO("Eggs", "2 large", 7));
        cookiesIngredients.add(new IngredientDTO("Vanilla extract", "2 tsp", 8));
        cookiesIngredients.add(new IngredientDTO("Chocolate chips", "2 cups", 9));
        
        // Create preparation steps for first recipe
        List<PreparationStepDTO> cookiesSteps = new ArrayList<>();
        cookiesSteps.add(new PreparationStepDTO("Preheat oven to 375°F (190°C).", 1));
        cookiesSteps.add(new PreparationStepDTO("In a medium bowl, whisk together flour, baking soda, and salt.", 2));
        cookiesSteps.add(new PreparationStepDTO("In a large bowl, cream together butter and both sugars until light and fluffy.", 3));
        cookiesSteps.add(new PreparationStepDTO("Beat in eggs one at a time, then add vanilla extract.", 4));
        cookiesSteps.add(new PreparationStepDTO("Gradually mix in the flour mixture until just combined.", 5));
        cookiesSteps.add(new PreparationStepDTO("Fold in chocolate chips.", 6));
        cookiesSteps.add(new PreparationStepDTO("Drop rounded tablespoons of dough onto ungreased baking sheets.", 7));
        cookiesSteps.add(new PreparationStepDTO("Bake for 9-11 minutes or until golden brown around edges.", 8));
        cookiesSteps.add(new PreparationStepDTO("Cool on baking sheet for 2 minutes before transferring to wire rack.", 9));
        
        // Create first recipe - Classic Chocolate Chip Cookies
        RecipeDTO cookies = new RecipeDTO(
            1,
            "Classic Chocolate Chip Cookies",
            "Soft and chewy chocolate chip cookies that are perfect for any occasion. A family favorite recipe passed down through generations.",
            "John Doe",
            cookiesIngredients,
            cookiesSteps
        );
        
        // Create ingredients for second recipe - Homemade Pizza Margherita
        List<IngredientDTO> pizzaIngredients = new ArrayList<>();
        pizzaIngredients.add(new IngredientDTO("Pizza dough", "1 ball", 1));
        pizzaIngredients.add(new IngredientDTO("Crushed tomatoes", "1/2 cup", 2));
        pizzaIngredients.add(new IngredientDTO("Fresh mozzarella", "8 oz", 3));
        pizzaIngredients.add(new IngredientDTO("Fresh basil leaves", "1/4 cup", 4));
        pizzaIngredients.add(new IngredientDTO("Olive oil", "2 tbsp", 5));
        pizzaIngredients.add(new IngredientDTO("Salt", "to taste", 6));
        pizzaIngredients.add(new IngredientDTO("Black pepper", "to taste", 7));
        
        // Create preparation steps for second recipe
        List<PreparationStepDTO> pizzaSteps = new ArrayList<>();
        pizzaSteps.add(new PreparationStepDTO("Preheat oven to 500°F (260°C) with pizza stone if available.", 1));
        pizzaSteps.add(new PreparationStepDTO("Roll out pizza dough on floured surface to desired thickness.", 2));
        pizzaSteps.add(new PreparationStepDTO("Transfer dough to pizza pan or parchment paper.", 3));
        pizzaSteps.add(new PreparationStepDTO("Spread crushed tomatoes evenly over dough, leaving 1-inch border.", 4));
        pizzaSteps.add(new PreparationStepDTO("Tear mozzarella into pieces and distribute over sauce.", 5));
        pizzaSteps.add(new PreparationStepDTO("Drizzle with olive oil and season with salt and pepper.", 6));
        pizzaSteps.add(new PreparationStepDTO("Bake for 10-12 minutes until crust is golden and cheese is bubbly.", 7));
        pizzaSteps.add(new PreparationStepDTO("Remove from oven and immediately top with fresh basil leaves.", 8));
        pizzaSteps.add(new PreparationStepDTO("Let cool for 2-3 minutes before slicing and serving.", 9));
        
        // Create second recipe - Homemade Pizza Margherita
        RecipeDTO pizza = new RecipeDTO(
            2,
            "Homemade Pizza Margherita", 
            "Authentic Italian pizza with fresh tomatoes, mozzarella, and basil. Simple ingredients that create an amazing flavor combination.",
            "Maria Rossi",
            pizzaIngredients,
            pizzaSteps
        );
        
        // Create ingredients for third recipe - Creamy Chicken Alfredo
        List<IngredientDTO> alfredoIngredients = new ArrayList<>();
        alfredoIngredients.add(new IngredientDTO("Fettuccine pasta", "1 lb", 1));
        alfredoIngredients.add(new IngredientDTO("Chicken breast", "2 lbs", 2));
        alfredoIngredients.add(new IngredientDTO("Heavy cream", "1 cup", 3));
        alfredoIngredients.add(new IngredientDTO("Parmesan cheese", "1 cup grated", 4));
        alfredoIngredients.add(new IngredientDTO("Butter", "4 tbsp", 5));
        alfredoIngredients.add(new IngredientDTO("Garlic", "3 cloves minced", 6));
        alfredoIngredients.add(new IngredientDTO("Salt", "to taste", 7));
        alfredoIngredients.add(new IngredientDTO("Black pepper", "to taste", 8));
        alfredoIngredients.add(new IngredientDTO("Fresh parsley", "2 tbsp chopped", 9));
        
        // Create preparation steps for third recipe
        List<PreparationStepDTO> alfredoSteps = new ArrayList<>();
        alfredoSteps.add(new PreparationStepDTO("Cook fettuccine according to package directions. Reserve 1 cup pasta water.", 1));
        alfredoSteps.add(new PreparationStepDTO("Season chicken with salt and pepper, then cook in large skillet until golden and cooked through.", 2));
        alfredoSteps.add(new PreparationStepDTO("Remove chicken and let rest, then slice into strips.", 3));
        alfredoSteps.add(new PreparationStepDTO("In same skillet, melt butter and sauté garlic for 1 minute.", 4));
        alfredoSteps.add(new PreparationStepDTO("Add heavy cream and bring to gentle simmer.", 5));
        alfredoSteps.add(new PreparationStepDTO("Gradually whisk in Parmesan cheese until smooth.", 6));
        alfredoSteps.add(new PreparationStepDTO("Add cooked pasta and chicken to sauce, tossing to combine.", 7));
        alfredoSteps.add(new PreparationStepDTO("Add pasta water as needed to achieve desired consistency.", 8));
        alfredoSteps.add(new PreparationStepDTO("Garnish with fresh parsley and serve immediately.", 9));
        
        // Create third recipe - Creamy Chicken Alfredo
        RecipeDTO alfredo = new RecipeDTO(
            3,
            "Creamy Chicken Alfredo",
            "Rich and creamy pasta dish with tender chicken and a luxurious alfredo sauce. Perfect for a comforting dinner.",
            "Chef James",
            alfredoIngredients,
            alfredoSteps
        );
        
        // Create ingredients for fourth recipe - Fresh Garden Salad
        List<IngredientDTO> saladIngredients = new ArrayList<>();
        saladIngredients.add(new IngredientDTO("Mixed greens", "6 cups", 1));
        saladIngredients.add(new IngredientDTO("Cherry tomatoes", "1 cup halved", 2));
        saladIngredients.add(new IngredientDTO("Cucumber", "1 large diced", 3));
        saladIngredients.add(new IngredientDTO("Red onion", "1/4 cup sliced", 4));
        saladIngredients.add(new IngredientDTO("Olive oil", "3 tbsp", 5));
        saladIngredients.add(new IngredientDTO("Balsamic vinegar", "1 tbsp", 6));
        saladIngredients.add(new IngredientDTO("Dijon mustard", "1 tsp", 7));
        saladIngredients.add(new IngredientDTO("Salt", "to taste", 8));
        saladIngredients.add(new IngredientDTO("Black pepper", "to taste", 9));
        
        // Create preparation steps for fourth recipe
        List<PreparationStepDTO> saladSteps = new ArrayList<>();
        saladSteps.add(new PreparationStepDTO("Wash and dry all vegetables thoroughly.", 1));
        saladSteps.add(new PreparationStepDTO("In a large bowl, combine mixed greens, tomatoes, cucumber, and red onion.", 2));
        saladSteps.add(new PreparationStepDTO("In a small bowl, whisk together olive oil, balsamic vinegar, and Dijon mustard.", 3));
        saladSteps.add(new PreparationStepDTO("Season dressing with salt and pepper to taste.", 4));
        saladSteps.add(new PreparationStepDTO("Drizzle dressing over salad just before serving.", 5));
        saladSteps.add(new PreparationStepDTO("Toss gently to coat all ingredients evenly.", 6));
        saladSteps.add(new PreparationStepDTO("Serve immediately for best texture and flavor.", 7));
        
        // Create fourth recipe - Fresh Garden Salad
        RecipeDTO salad = new RecipeDTO(
            4,
            "Fresh Garden Salad",
            "Light and refreshing salad with mixed greens, vegetables, and a simple vinaigrette. Perfect as a side dish or light meal.",
            "John Doe",
            saladIngredients,
            saladSteps
        );
        
        // Add recipes to the list
        recipes.add(cookies);
        recipes.add(pizza);
        recipes.add(alfredo);
        recipes.add(salad);
        
        return recipes;
    }
}