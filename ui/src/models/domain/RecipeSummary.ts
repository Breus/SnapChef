export default interface RecipeSummary {
    id: number;
    title: string;
    description: string;
    author: {
        id: number;
        username: string;
    };
}
