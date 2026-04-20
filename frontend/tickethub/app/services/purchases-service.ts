const API_URL = "/api/v1/public/purchases";

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

export async function savePurchase(data: PurchaseCreateDTO): Promise<PurchaseConfirmation> {
  const res = await fetch(`${API_URL}/save`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al procesar la compra");
  return res.json();
}

export async function getPurchase(purchaseId: string): Promise<PurchaseConfirmation> {
  const res = await fetch(`${API_URL}/${purchaseId}`);
  if (!res.ok) throw new Error("Compra no encontrada");
  return res.json();
}

export function getDownloadUrl(purchaseId: string | number): string {
  return `${API_URL}/download/${purchaseId}`;
}
