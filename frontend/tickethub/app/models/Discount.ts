export interface DiscountBasic {
  discountName: string;
  amount: number;
  percentage: boolean;
}

export interface Discount extends DiscountBasic {
  discountID: number;
  events: { eventID: number; name: string }[];
  selected: boolean;
}

export interface DiscountCreateDTO {
  discountName: string;
  amount: number;
  percentage: boolean;
}
