import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
    layout("layout/MainLayout.tsx", [
        index("routes/home.tsx"), 
        
        route("admin/artists/add", "routes/admin/artist-form.tsx"),
        route("admin/artists/edit/:id", "routes/admin/artist-form.tsx")
    ]),
] satisfies RouteConfig;
