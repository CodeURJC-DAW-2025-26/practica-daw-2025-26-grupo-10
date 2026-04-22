export interface PurchaseCreateDTO {
  sessionId: number;
  zoneIds: number[];
  email: string;
}

export interface PurchaseConfirmation {
  purchaseID: number;
  totalPrice: number;
  session: {
    sessionID: number;
    date: string;
  };
  event?: {
    eventID: number;
    name: string;
  };
}
