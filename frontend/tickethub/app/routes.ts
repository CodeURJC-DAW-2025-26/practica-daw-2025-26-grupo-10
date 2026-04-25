import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
    layout("layout/MainLayout.tsx", [
        index("routes/public/home.tsx"),
        // Artists
        route("/admin/artists", "routes/admin/ArtistsManagement.tsx"),
        route("/admin/artists/new", "routes/admin/ArtistForm.tsx", { id: "artist-create" }),
        route("/admin/artists/:id", "routes/admin/ArtistForm.tsx", { id: "artist-edit" }),
        route("/admin/statistics", "routes/admin/Statistics.tsx"),

        route("/public/artists", "routes/public/ArtistsList.tsx"),
        route("/public/artists/:id", "routes/public/ArtistDetail.tsx"),
        // Events
        route("/public/events", "routes/public/events.tsx"),
        route("/admin/events", "routes/admin/manage-events.tsx"),
        route("/public/events/:id", "routes/public/event.tsx"),
        route("/admin/events/create", "routes/admin/create-event.tsx"),
        route("/admin/events/edit/:id", "routes/admin/edit-event.tsx"),

        // Discounts
        route("admin/discounts", "routes/admin/discounts/manage-discounts.tsx"),
        route("admin/discounts/new", "routes/admin/discounts/create-discount.tsx", { id: "discount-new" }),
        route("admin/discounts/:id/edit", "routes/admin/discounts/create-discount.tsx", { id: "discount-edit" }),

        // Zones
        route("admin/events/:eventId/zones", "routes/admin/events/manage-zones.tsx"),
        route("admin/events/:eventId/zones/new", "routes/admin/events/create-zone.tsx", { id: "zone-new" }),
        route("admin/events/:eventId/zones/:id/edit", "routes/admin/events/create-zone.tsx", { id: "zone-edit" }),

        // Purchases
        route("public/purchase/:eventId", "routes/public/purchase.tsx"),
        route("public/confirmation/:purchaseId", "routes/public/confirmation.tsx"),

        // Errors
        route("*", "routes/not-found.tsx"),
        route("403", "routes/errors/error403.tsx")
    ]),
] satisfies RouteConfig;

