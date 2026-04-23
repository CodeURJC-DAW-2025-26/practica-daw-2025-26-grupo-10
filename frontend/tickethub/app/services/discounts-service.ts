import { API_URL } from "~/services/homeService";
import type Discount from "~/models/Discount";
import type DiscountCreate from "~/models/DiscountCreate";

const DISCOUNTS_URL = `${API_URL}/admin/discounts`;

export async function getDiscounts(): Promise<Discount[]> {
  const res = await fetch(`${DISCOUNTS_URL}`);
  if (!res.ok) throw new Error("Error al obtener descuentos");
  const data = await res.json();
  return data.content;
}

export async function getDiscount(id: string): Promise<Discount> {
  const res = await fetch(`${DISCOUNTS_URL}/${id}`);
  if (!res.ok) throw new Error("Descuento no encontrado");
  return await res.json();
}

export async function createDiscount(data: DiscountCreate): Promise<Discount> {
  const res = await fetch(`${DISCOUNTS_URL}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al crear descuento");
  return await res.json();
}

export async function updateDiscount(id: string, data: DiscountCreate): Promise<Discount> {
  const res = await fetch(`${DISCOUNTS_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar descuento");
  return await res.json();
}

export async function deleteDiscount(id: number): Promise<void> {
  const res = await fetch(`${DISCOUNTS_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Error al eliminar descuento");
}
