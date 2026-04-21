import { useEffect } from "react";
import { useLoaderData } from "react-router";
import axios from "axios";
import { useAdminArtistsStore } from "~/store/adminArtistsStore";
import type { Artist } from "~/models/Artist";
import ArtistsManagementUI from "~/components/admin/ArtistsManagementUI";
import { adminArtistService } from "~/services/AdminArtistService";

export async function loader() {
    const artists = await adminArtistService.getAllArtists();
    return { initial: artists };
}

export default function ArtistsManagementRoute() {
    const { initial } = useLoaderData<typeof loader>();
    const { artists, reset, deleteArtist } = useAdminArtistsStore();

    // Set the initial data from the loader into the store when the component mounts
    useEffect(() => { reset(initial); }, []);

    return <ArtistsManagementUI artists={artists} onDelete={deleteArtist} />;
}