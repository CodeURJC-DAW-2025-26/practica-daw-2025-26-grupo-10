import { useEffect } from "react";
import { useLoaderData } from "react-router";
import axios from "axios";
import ArtistListUI from "~/components/admin/ArtistsManagementUI";
import { useAdminArtistsStore, type AdminArtist } from "~/store/adminArtistsStore";

export async function loader() {
    const res = await axios.get("/api/v1/admin/artists");
    return { initial: res.data.content as AdminArtist[] };
}

export default function ArtistListRoute() {
    const { initial } = useLoaderData<typeof loader>();
    const { artists, reset, deleteArtist } = useAdminArtistsStore();

    // Set the initial data from the loader into the store when the component mounts
    useEffect(() => { reset(initial); }, []);

    return <ArtistListUI artists={artists} onDelete={deleteArtist} />;
}