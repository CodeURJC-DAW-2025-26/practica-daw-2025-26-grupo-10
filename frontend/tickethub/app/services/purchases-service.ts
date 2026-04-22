import type { PurchaseCreateDTO, PurchaseConfirmation } from "~/models/Purchase";

const API_URL = "/api/v1/public/purchases";

export async function savePurchase(data: PurchaseCreateDTO): Promise<PurchaseConfirmation> {
  const res = await fetch(`${API_URL}/save`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al procesar la compra");
  return await res.json();
}

export async function getPurchase(purchaseId: string): Promise<PurchaseConfirmation> {
  const res = await fetch(`${API_URL}/${purchaseId}`);
  if (!res.ok) throw new Error("Compra no encontrada");
  return await res.json();
}

export function getDownloadUrl(purchaseId: string | number): string {
  return `/api/v1/public/purchases/download/${purchaseId}`;
}
