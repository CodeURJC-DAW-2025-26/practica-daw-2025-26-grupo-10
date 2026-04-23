import { API_URL } from "~/services/homeService";
import type PurchaseCreate from "~/models/PurchaseCreate";
import type Purchase from "~/models/Purchase";

const PURCHASES_URL = `${API_URL}/public/purchases`;

export async function savePurchase(data: PurchaseCreate): Promise<Purchase> {
  const res = await fetch(`${PURCHASES_URL}/save`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al procesar la compra");
  return await res.json();
}

export async function getPurchase(purchaseId: string): Promise<Purchase> {
  const res = await fetch(`${PURCHASES_URL}/${purchaseId}`);
  if (!res.ok) throw new Error("Compra no encontrada");
  return await res.json();
}

export function getDownloadUrl(purchaseId: string | number): string {
  return `/api/v1/public/purchases/download/${purchaseId}`;
}
