import { useLoaderData, useNavigate, useParams } from "react-router";
import axios from "axios";
import ArtistFormUI from "~/components/admin/ArtistFormUI";
import type { Artist } from "~/components/admin/ArtistFormUI";

const emptyArtist: Artist = { artistName: "", info: "", instagram: "", twitter: "" };

export async function loader({ params }: { params: { id?: string } }) {
    if (!params.id) return { artist: emptyArtist, isEditMode: false };
    const res = await axios.get(`/api/v1/admin/artists/${params.id}`);
    return { artist: res.data as Artist, isEditMode: true };
}

export default function ArtistFormRoute() {
    const { artist, isEditMode } = useLoaderData<typeof loader>();
    const { id } = useParams();
    const navigate = useNavigate();

    const handleSave = async (data: Artist, image: File | null) => {
        const formData = new FormData();
        formData.append("data", new Blob([JSON.stringify(data)], { type: "application/json" }));
        if (image) formData.append("image", image);

        try {
            const url = isEditMode ? `/api/v1/admin/artists/${id}` : "/api/v1/admin/artists";
            const method = isEditMode ? "put" : "post";
            await axios[method](url, formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
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