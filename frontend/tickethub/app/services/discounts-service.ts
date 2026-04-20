const API_URL = "/api/v1/admin/discounts";

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

export async function getDiscounts(): Promise<Discount[]> {
  const res = await fetch(`${API_URL}`);
  if (!res.ok) throw new Error("Error al obtener descuentos");
  return res.json();
}

export async function getDiscount(id: string): Promise<Discount> {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Descuento no encontrado");
  return res.json();
}

export async function createDiscount(data: DiscountCreateDTO): Promise<Discount> {
  const res = await fetch(`${API_URL}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al crear descuento");
  return res.json();
}

export async function updateDiscount(id: string, data: DiscountCreateDTO): Promise<Discount> {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar descuento");
  return res.json();
}

export async function deleteDiscount(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Error al eliminar descuento");
}
