import type TicketBasic from "~/models/TicketBasic";
import type { SessionBasic } from "./SessionBasic";

export default interface Purchase {
  purchaseID: number;
  tickets: TicketBasic[];
  session: SessionBasic;
  clientId: number;
  totalPrice: number;
}
