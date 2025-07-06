export interface Recipe {
  id: number;
  title: string;
  description: string;
  author_id: number;
  ingredients: Ingredient[];
  steps: Step[];
}

export interface Ingredient {
  id: number;
  recipe_id: number;
  name: string;
  quantity: string;
  position: number;
}

export interface Step {
  id: number;
  recipe_id: number;
  description: string;
  position: number;
}

export interface User {
  id: number;
  username: string;
  email: string;
}

export const mockUsers: User[] = [
  { id: 1, username: "chef_maria", email: "maria@example.com" },
  { id: 2, username: "baker_john", email: "john@example.com" },
  { id: 3, username: "home_cook", email: "cook@example.com" }
];

export const mockRecipes: Recipe[] = [
  {
    id: 1,
    title: "Classic Chocolate Chip Cookies",
    description: "Soft and chewy chocolate chip cookies that are perfect for any occasion. A family favorite recipe passed down through generations.",
    author_id: 1,
    ingredients: [
      { id: 1, recipe_id: 1, name: "All-purpose flour", quantity: "2 1/4 cups", position: 1 },
      { id: 2, recipe_id: 1, name: "Baking soda", quantity: "1 tsp", position: 2 },
      { id: 3, recipe_id: 1, name: "Salt", quantity: "1 tsp", position: 3 },
      { id: 4, recipe_id: 1, name: "Butter", quantity: "1 cup", position: 4 },
      { id: 5, recipe_id: 1, name: "Brown sugar", quantity: "3/4 cup", position: 5 },
      { id: 6, recipe_id: 1, name: "White sugar", quantity: "1/4 cup", position: 6 },
      { id: 7, recipe_id: 1, name: "Eggs", quantity: "2 large", position: 7 },
      { id: 8, recipe_id: 1, name: "Vanilla extract", quantity: "2 tsp", position: 8 },
      { id: 9, recipe_id: 1, name: "Chocolate chips", quantity: "2 cups", position: 9 }
    ],
    steps: [
      { id: 1, recipe_id: 1, description: "Preheat oven to 375°F (190°C).", position: 1 },
      { id: 2, recipe_id: 1, description: "In a medium bowl, whisk together flour, baking soda, and salt.", position: 2 },
      { id: 3, recipe_id: 1, description: "In a large bowl, cream together butter and both sugars until light and fluffy.", position: 3 },
      { id: 4, recipe_id: 1, description: "Beat in eggs one at a time, then add vanilla extract.", position: 4 },
      { id: 5, recipe_id: 1, description: "Gradually mix in the flour mixture until just combined.", position: 5 },
      { id: 6, recipe_id: 1, description: "Fold in chocolate chips.", position: 6 },
      { id: 7, recipe_id: 1, description: "Drop rounded tablespoons of dough onto ungreased baking sheets.", position: 7 },
      { id: 8, recipe_id: 1, description: "Bake for 9-11 minutes or until golden brown around edges.", position: 8 },
      { id: 9, recipe_id: 1, description: "Cool on baking sheet for 2 minutes before transferring to wire rack.", position: 9 }
    ]
  },
  {
    id: 2,
    title: "Homemade Pizza Margherita",
    description: "Authentic Italian pizza with fresh tomatoes, mozzarella, and basil. Simple ingredients that create an amazing flavor combination.",
    author_id: 2,
    ingredients: [
      { id: 10, recipe_id: 2, name: "Pizza dough", quantity: "1 ball", position: 1 },
      { id: 11, recipe_id: 2, name: "Crushed tomatoes", quantity: "1/2 cup", position: 2 },
      { id: 12, recipe_id: 2, name: "Fresh mozzarella", quantity: "8 oz", position: 3 },
      { id: 13, recipe_id: 2, name: "Fresh basil leaves", quantity: "1/4 cup", position: 4 },
      { id: 14, recipe_id: 2, name: "Olive oil", quantity: "2 tbsp", position: 5 },
      { id: 15, recipe_id: 2, name: "Salt", quantity: "to taste", position: 6 },
      { id: 16, recipe_id: 2, name: "Black pepper", quantity: "to taste", position: 7 }
    ],
    steps: [
      { id: 10, recipe_id: 2, description: "Preheat oven to 500°F (260°C) with pizza stone if available.", position: 1 },
      { id: 11, recipe_id: 2, description: "Roll out pizza dough on floured surface to desired thickness.", position: 2 },
      { id: 12, recipe_id: 2, description: "Transfer dough to pizza pan or parchment paper.", position: 3 },
      { id: 13, recipe_id: 2, description: "Spread crushed tomatoes evenly over dough, leaving 1-inch border.", position: 4 },
      { id: 14, recipe_id: 2, description: "Tear mozzarella into pieces and distribute over sauce.", position: 5 },
      { id: 15, recipe_id: 2, description: "Drizzle with olive oil and season with salt and pepper.", position: 6 },
      { id: 16, recipe_id: 2, description: "Bake for 10-12 minutes until crust is golden and cheese is bubbly.", position: 7 },
      { id: 17, recipe_id: 2, description: "Remove from oven and immediately top with fresh basil leaves.", position: 8 },
      { id: 18, recipe_id: 2, description: "Let cool for 2-3 minutes before slicing and serving.", position: 9 }
    ]
  },
  {
    id: 3,
    title: "Creamy Chicken Alfredo",
    description: "Rich and creamy pasta dish with tender chicken and a luxurious alfredo sauce. Perfect for a comforting dinner.",
    author_id: 3,
    ingredients: [
      { id: 17, recipe_id: 3, name: "Fettuccine pasta", quantity: "1 lb", position: 1 },
      { id: 18, recipe_id: 3, name: "Chicken breast", quantity: "2 lbs", position: 2 },
      { id: 19, recipe_id: 3, name: "Heavy cream", quantity: "1 cup", position: 3 },
      { id: 20, recipe_id: 3, name: "Parmesan cheese", quantity: "1 cup grated", position: 4 },
      { id: 21, recipe_id: 3, name: "Butter", quantity: "4 tbsp", position: 5 },
      { id: 22, recipe_id: 3, name: "Garlic", quantity: "3 cloves minced", position: 6 },
      { id: 23, recipe_id: 3, name: "Salt", quantity: "to taste", position: 7 },
      { id: 24, recipe_id: 3, name: "Black pepper", quantity: "to taste", position: 8 },
      { id: 25, recipe_id: 3, name: "Fresh parsley", quantity: "2 tbsp chopped", position: 9 }
    ],
    steps: [
      { id: 19, recipe_id: 3, description: "Cook fettuccine according to package directions. Reserve 1 cup pasta water.", position: 1 },
      { id: 20, recipe_id: 3, description: "Season chicken with salt and pepper, then cook in large skillet until golden and cooked through.", position: 2 },
      { id: 21, recipe_id: 3, description: "Remove chicken and let rest, then slice into strips.", position: 3 },
      { id: 22, recipe_id: 3, description: "In same skillet, melt butter and sauté garlic for 1 minute.", position: 4 },
      { id: 23, recipe_id: 3, description: "Add heavy cream and bring to gentle simmer.", position: 5 },
      { id: 24, recipe_id: 3, description: "Gradually whisk in Parmesan cheese until smooth.", position: 6 },
      { id: 25, recipe_id: 3, description: "Add cooked pasta and chicken to sauce, tossing to combine.", position: 7 },
      { id: 26, recipe_id: 3, description: "Add pasta water as needed to achieve desired consistency.", position: 8 },
      { id: 27, recipe_id: 3, description: "Garnish with fresh parsley and serve immediately.", position: 9 }
    ]
  },
  {
    id: 4,
    title: "Fresh Garden Salad",
    description: "Light and refreshing salad with mixed greens, vegetables, and a simple vinaigrette. Perfect as a side dish or light meal.",
    author_id: 1,
    ingredients: [
      { id: 26, recipe_id: 4, name: "Mixed greens", quantity: "6 cups", position: 1 },
      { id: 27, recipe_id: 4, name: "Cherry tomatoes", quantity: "1 cup halved", position: 2 },
      { id: 28, recipe_id: 4, name: "Cucumber", quantity: "1 large diced", position: 3 },
      { id: 29, recipe_id: 4, name: "Red onion", quantity: "1/4 cup sliced", position: 4 },
      { id: 30, recipe_id: 4, name: "Olive oil", quantity: "3 tbsp", position: 5 },
      { id: 31, recipe_id: 4, name: "Balsamic vinegar", quantity: "1 tbsp", position: 6 },
      { id: 32, recipe_id: 4, name: "Dijon mustard", quantity: "1 tsp", position: 7 },
      { id: 33, recipe_id: 4, name: "Salt", quantity: "to taste", position: 8 },
      { id: 34, recipe_id: 4, name: "Black pepper", quantity: "to taste", position: 9 }
    ],
    steps: [
      { id: 28, recipe_id: 4, description: "Wash and dry all vegetables thoroughly.", position: 1 },
      { id: 29, recipe_id: 4, description: "In a large bowl, combine mixed greens, tomatoes, cucumber, and red onion.", position: 2 },
      { id: 30, recipe_id: 4, description: "In a small bowl, whisk together olive oil, balsamic vinegar, and Dijon mustard.", position: 3 },
      { id: 31, recipe_id: 4, description: "Season dressing with salt and pepper to taste.", position: 4 },
      { id: 32, recipe_id: 4, description: "Drizzle dressing over salad just before serving.", position: 5 },
      { id: 33, recipe_id: 4, description: "Toss gently to coat all ingredients evenly.", position: 6 },
      { id: 34, recipe_id: 4, description: "Serve immediately for best texture and flavor.", position: 7 }
    ]
  }
];

export const getUserById = (id: number): User | undefined => {
  return mockUsers.find(user => user.id === id);
};