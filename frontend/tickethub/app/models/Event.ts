export interface SessionBasic {
  sessionID: number;
  date: string;
}

export interface ZonePublic {
  id: number;
  name: string;
  capacity: number;
  price: number;
}

export interface DiscountPublic {
  discountID: number;
  discountName: string;
  amount: number;
  percentage: boolean;
}

export interface EventPublic {
  eventID: number;
  name: string;
  place: string;
  category: string;
  sessions: SessionBasic[];
  zones: ZonePublic[];
  discounts: DiscountPublic[];
  mainImage?: { imageID: number; imageName: string };
}
