import { useLoaderData, useNavigate, useParams } from "react-router";
import ArtistFormUI from "~/components/admin/ArtistFormUI";
import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import { updateArtist, createArtist, getArtistById } from "~/services/AdminArtistService";


const emptyArtist: ArtistCreateUpdate = { artistName: "", info: "", instagram: "", twitter: "" };

export async function clientLoader({ params }: { params: { id?: string } }) {

    if (!params.id || params.id === "new") {
        return { artist: emptyArtist, isEditMode: false };
    }

    try {
        const res = await getArtistById(params.id);
        return { artist: res, isEditMode: true };
    } catch (error) {

        console.error("Error cargando artista:", error);
        return { artist: emptyArtist, isEditMode: false };
    }
}

export default function ArtistFormRoute() {
    const { artist, isEditMode } = useLoaderData<typeof clientLoader>();
    const { id } = useParams();
    const navigate = useNavigate();

    const handleSave = async (data: ArtistCreateUpdate, image: File | null) => {
        try {
            if (isEditMode && id) {

                await updateArtist(id, data, image);
            } else {
                await createArtist(data, image);
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