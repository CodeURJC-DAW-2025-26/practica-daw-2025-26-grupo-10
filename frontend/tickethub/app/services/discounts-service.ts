import type { Discount, DiscountCreateDTO } from "~/models/Discount";

const API_URL = "/api/v1/admin/discounts";

export async function getDiscounts(): Promise<Discount[]> {
  const res = await fetch(`${API_URL}`);
  if (!res.ok) throw new Error("Error al obtener descuentos");
  return await res.json();
}

export async function getDiscount(id: string): Promise<Discount> {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Descuento no encontrado");
  return await res.json();
}

export async function createDiscount(data: DiscountCreateDTO): Promise<Discount> {
  const res = await fetch(`${API_URL}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al crear descuento");
  return await res.json();
}

export async function updateDiscount(id: string, data: DiscountCreateDTO): Promise<Discount> {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar descuento");
  return await res.json();
}

export async function deleteDiscount(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Error al eliminar descuento");
}
