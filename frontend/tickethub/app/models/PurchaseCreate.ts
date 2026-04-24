import type TicketSelection from "~/models/TicketSelection";

export default interface PurchaseCreate {
  sessionID: number;
  selections: TicketSelection[];
  name: string;
}
