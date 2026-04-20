import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
  // ── Rutas Admin (con AdminHeader + AdminFooter) ─────────────────────────────
  layout("layout/MainLayout.tsx", [
    index("routes/home.tsx"),

    // Descuentos
    route("admin/discounts", "routes/admin/discounts/manage-discounts.tsx"),
    route("admin/discounts/new", "routes/admin/discounts/create-discount.tsx"),
    route("admin/discounts/:id/edit", "routes/admin/discounts/create-discount.tsx"),

    // Zonas (dentro de un evento)
    route("admin/events/:eventId/zones", "routes/admin/events/manage-zones.tsx"),
    route("admin/events/:eventId/zones/new", "routes/admin/events/create-zone.tsx"),
    route("admin/events/:eventId/zones/:id/edit", "routes/admin/events/create-zone.tsx"),
  ]),

  // ── Rutas Públicas (sin admin layout) ──────────────────────────────────────
  route("public/purchase/:eventId", "routes/public/purchase.tsx"),
  route("public/confirmation/:purchaseId", "routes/public/confirmation.tsx"),

  // ── 404 catch-all ──────────────────────────────────────────────────────────
  route("*", "routes/not-found.tsx"),
] satisfies RouteConfig;
