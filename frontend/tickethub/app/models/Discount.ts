import type EventBasic from "~/models/EventBasic";

export default interface Discount {
  discountID: number;
  discountName: string;
  percentage: boolean;
  amount: number;
  events: EventBasic[];
  selected: boolean;
}
