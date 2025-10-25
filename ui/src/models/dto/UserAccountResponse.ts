import type UserAccount from "../domain/UserAccount.ts";

export default interface UserAccountResponse {
    userAccount: UserAccount;
}