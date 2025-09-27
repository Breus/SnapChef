export default interface AuthenticationDetails {
    userId: number;
    username: string;
    emailAddress: string;
    accessToken: string;
    refreshToken: string;
    refreshExpiresInSeconds: number;
}
