import type { SessionBasic } from "./SessionBasic";
import type TicketBasic from "./TicketBasic";

export interface PurchaseBasic {
    purchaseID: number;
    tickets: TicketBasic[];
    session: SessionBasic;
    clientId: number;
    totalPrice: number;

}
