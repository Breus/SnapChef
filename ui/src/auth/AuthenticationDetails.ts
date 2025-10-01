export default interface AuthenticationDetails {
    userId: number;
    username: string;
    emailAddress: string;
    accessToken: string;
    expiresInSeconds: number;
    refreshToken: string;
    refreshExpiresInSeconds: number;
}
