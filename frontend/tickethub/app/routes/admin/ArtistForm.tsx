import React, { useState, useEffect } from "react";
import { useParams, useNavigate} from "react-router";
import axios from "axios";
import ArtistFormUI, { type Artist } from "../../components/admin/ArtistFormUI";


export default function ArtistFormRoute() {
    const { id } = useParams();
    const navigate = useNavigate();
    const isEditMode = Boolean(id);

    const [artist, setArtist] = useState<Artist>({
        artistName: "",
        info: "",
        instagram: "",
        twitter: "",
    });

    const [loading, setLoading] = useState(isEditMode);

    // If we're in edit mode, fetch the existing artist data
    useEffect(() => {
        if (isEditMode) {
            axios
                .get(`/api/v1/admin/artists/${id}`)
                .then((res) => {
                    setArtist(res.data);
                    setLoading(false);
                })
                .catch((err) => console.error("Error fetching data:", err));
        }
    }, [id, isEditMode]);


    // Handle form submission for both create and update
    // Image upload is optional, so we use FormData to send both JSON and file data
    const handleSave = async (artist: Artist, image: File | null) => {
        const formData = new FormData();
        const dataStr = JSON.stringify(artist);
        formData.append("data", new Blob([dataStr], { type: "application/json" }));

        if (image) formData.append("image", image);

        try {
            const url = isEditMode
                ? `/api/v1/admin/artists/${id}`
                : "/api/v1/admin/artists";
            const method = isEditMode ? "put" : "post";

            await axios[method](url, formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            navigate("/admin/artists");
        } catch (error) {
            console.error("Submission error:", error);
        }
    };

    if (loading) return <div className="p-5">Cargando artista...</div>;

    return (
        <ArtistFormUI
            initialData={artist}
            isEditMode={isEditMode}
            onSubmit={handleSave}
            onCancel={() => navigate("/admin/artists")}
        />

    );
}
