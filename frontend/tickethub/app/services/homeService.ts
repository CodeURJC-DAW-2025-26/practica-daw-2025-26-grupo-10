import type { IndexResponse } from "~/models/IndexResponse";

export const API_URL = "/api/v1";

export async function getIndexData(): Promise<IndexResponse> {
  const res = await fetch(`${API_URL}/public/index`);
  if (!res.ok) {
    throw new Response("Error cargando la página principal", { status: res.status });
  }
  return res.json();
}
