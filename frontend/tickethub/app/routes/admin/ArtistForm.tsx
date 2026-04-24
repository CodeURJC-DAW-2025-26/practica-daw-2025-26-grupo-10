import { useLoaderData, useNavigate, useParams } from "react-router";
import axios from "axios";
import ArtistFormUI from "~/components/admin/ArtistFormUI";
import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import { adminArtistService } from "~/services/AdminArtistService";


const emptyArtist: ArtistCreateUpdate = { artistName: "", info: "", instagram: "", twitter: "" };

// get "params" from what I give you -> what I give you is an object with a property called "params" and inside there's an optional "id"
export async function loader({ params }: { params: { id?: string } }) {
    if (!params.id) return { artist: emptyArtist, isEditMode: false };

    const res = await adminArtistService.getArtistById(params.id);
    return { artist: res, isEditMode: true };
}

export default function ArtistFormRoute() {
    const { artist, isEditMode } = useLoaderData<typeof loader>();
    const { id } = useParams();
    const navigate = useNavigate();

    const handleSave = async (data: ArtistCreateUpdate, image: File | null) => {
        try {
            if (isEditMode && id) {
                await adminArtistService.updateArtist(id, artist, image);
            } else {
                await adminArtistService.createArtist(artist, image);
            }
            navigate("/admin/artists");
        } catch {
            alert("Error al guardar el artista.");
        }
    };

    return (
        <ArtistFormUI
            initialData={artist}
            isEditMode={isEditMode}
            onSubmit={handleSave}
            onCancel={() => navigate("/admin/artists")}
        />
    );
}