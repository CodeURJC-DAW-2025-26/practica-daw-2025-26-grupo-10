import type TicketBasic from "~/models/TicketBasic";

export default interface Zone {
  id: number;
  name: string;
  capacity: number;
  price: number;
  tickets: TicketBasic[];
  eventId: number;
  selected: boolean;
}
