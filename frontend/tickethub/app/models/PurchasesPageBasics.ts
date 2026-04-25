export interface TicketBasic {
    ticketID: number;
    code: string;
    ticketPrice: number;
    isActive: boolean;
}

export interface SessionBasic {
    sessionID: number;
    date: string;
    eventName: string;
}

export interface PurchaseBasic {
    purchaseID: number;
    tickets: TicketBasic[];
    session: SessionBasic;
    clientId: number;
    totalPrice: number;

}

