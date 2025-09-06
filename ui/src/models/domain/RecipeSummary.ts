export default interface RecipeSummary {
    id: number;
    title: string;
    description: string;
    author: {
        userId: string;
        userName: string;
    };
}
