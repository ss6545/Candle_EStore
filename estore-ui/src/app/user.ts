import { Candle } from "./candle";
import { CreditCard } from "./credit-card";

export interface User {
    id: number;
    name: String;
    cart: Candle[];
    isAdmin: boolean;
    password: String;
    creditCard: CreditCard | null;
}
