import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
        layout("layout/MainLayout.tsx", [
                index("routes/public/home.tsx"),

                // Artists
                route("/admin/artists", "routes/admin/manage-artists.tsx"),
                route("/admin/artists/new", "routes/admin/create-artist.tsx", { id: "artist-create" }),
                route("/admin/artists/:id", "routes/admin/create-artist.tsx", { id: "artist-edit" }),
                route("/admin/statistics", "routes/admin/Statistics.tsx"),

                route("/public/artists", "routes/public/artists.tsx"),
                route("/public/artists/:id", "routes/public/artist.tsx"),
                // Events
                route("/admin", "routes/admin/dashboard.tsx"),
                route("/public/events", "routes/public/events.tsx"),
                route("/admin/events", "routes/admin/manage-events.tsx"),
                route("/public/events/:id", "routes/public/event.tsx"),
                route("/admin/events/new", "routes/admin/create-event.tsx", { id: "event-new" }),
                route("/admin/events/:id", "routes/admin/create-event.tsx", { id: "event-edit" }),
                route("/admin/events/:id/sessions", "routes/admin/manage-sessions.tsx"),

                // Users
                route("/admin/users", "routes/admin/manage-users.tsx"),
                route("/admin/users/edit/:id", "routes/admin/edit-user.tsx"),

                // Discounts
                route("/admin/discounts", "routes/admin/discounts/manage-discounts.tsx"),
                route("/admin/discounts/new", "routes/admin/discounts/create-discount.tsx", { id: "discount-new" }),
                route("/admin/discounts/:id", "routes/admin/discounts/create-discount.tsx", { id: "discount-edit" }),

                // Zones
                route("/admin/events/:eventId/zones", "routes/admin/events/manage-zones.tsx"),
                route("/admin/events/:eventId/zones/new", "routes/admin/events/create-zone.tsx", { id: "zone-new" }),
                route("/admin/events/:eventId/zones/:id/edit", "routes/admin/events/create-zone.tsx", { id: "zone-edit" }),

                // Purchases
                route("/public/purchase/:eventId", "routes/public/purchase.tsx"),
                route("/public/confirmation/:purchaseId", "routes/public/confirmation.tsx"),

                //login
                route("/public/login", "routes/public/login.tsx"),
                route("/public/signup", "routes/public/signup.tsx"),

                //client Stuff
                route("/clients/profile", "routes/client/profile.tsx"),
                route("/purchases/me", "routes/client/purchases.tsx"),
                route("/clients/me/password", "routes/client/change-password.tsx"),
                route("/clients/profile/edit", "routes/client/change-profile.tsx"),

                // Errors
                route("403", "routes/error403.tsx"),
                route("500", "routes/error500.tsx"),
                route("*", "routes/not-found.tsx"),
        ]),
] satisfies RouteConfig;

