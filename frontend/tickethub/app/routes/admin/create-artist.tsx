import { useEffect, useState } from "react";
import { useLoaderData, useNavigate, useParams } from "react-router";
import ArtistFormUI from "~/components/admin/ArtistFormUI";
import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import type { ImageBasic } from "~/models/ImageBasic";
import { updateArtist, createArtist, getArtistById, deleteArtistImage, postArtistImage, updateArtistImage } from "~/services/AdminArtistService";


const emptyArtist: ArtistCreateUpdate = { artistName: "", info: "", instagram: "", twitter: "" };

export async function clientLoader({ params }: { params: { id?: string } }) {
    if (!params.id) return {artist: emptyArtist, isEditing: false, artistID: null, artistImage: null}
    const res = await getArtistById(params.id);
    return {
        artist: {artistName: res.artistName, info: res.info, instagram: res.instagram, twitter: res.twitter},
        isEditing: true,
        artistID: Number(params.id),
        artistImage: res.artistImage ?? null};
}

export default function ArtistFormRoute() {
    const { artist: initialArtist, isEditing, artistID, artistImage: initialImage } = useLoaderData<typeof clientLoader>();
    const { id } = useParams();
    const navigate = useNavigate();

    const [artistImage, setArtistImage] = useState<ImageBasic | null>(initialImage);
    const [state, setState] = useState({ success: false, error: null as string | null });

    useEffect(() => {
        if (state.success) {
            navigate("/admin/artists");
        }
    }, [state.success, navigate]);

    async function handleSave(data: ArtistCreateUpdate, image: File | null) {
        try {
            if (isEditing && id) {
                await updateArtist(id, data);
                if (image) {
                    if (artistImage) {
                        await updateArtistImage(Number(id), image)
                    } else {
                        await postArtistImage(Number(id), image)
                    }
                }
            } else {
                await createArtist(data, image);
            }
            setState({ success: true, error: null });
        } catch (err) {
            console.error(err);
            setState({ success: false, error: isEditing ? "Error al guardar los cambios" : "Error al crear el evento" });
        }
    }

    async function handleDeleteImage() {
        if (!artistID) return;
        try {
            await deleteArtistImage(artistID);
            setArtistImage(null);
        } catch (err) {
            console.error(err);
        }
    }

    return (
        <ArtistFormUI
            initialData={initialArtist}
            isEditMode={isEditing}
            onSubmit={handleSave}
            onCancel={() => navigate("/admin/artists")}
            artistID={artistID}
            artistImage = {artistImage}
            onDeleteImage = {handleDeleteImage}
            error = {state.error}
        />
    );
}