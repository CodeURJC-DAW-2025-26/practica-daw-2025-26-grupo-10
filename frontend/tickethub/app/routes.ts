import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
    layout("layout/MainLayout.tsx", [
        index("routes/home.tsx"), 

        // Admin routes
        route("admin/artists", "routes/admin/ArtistsManagement.tsx"),
        route("admin/artists/add", "routes/admin/ArtistForm.tsx"),
        route("admin/artists/edit/:id", "routes/admin/ArtistForm.tsx"),
        route("admin/statistics", "routes/admin/Statistics.tsx"),

        // Public routes
        route("public/artists", "routes/public/ArtistsList.tsx"),
        route("public/artists/:id", "routes/public/ArtistDetail.tsx"),

        // Error routes
        route("403", "routes/errors/error403.tsx")
    ]),
] satisfies RouteConfig;

