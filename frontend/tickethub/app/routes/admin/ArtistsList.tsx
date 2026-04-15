import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ArtistListUI, { type Artist } from "../../components/admin/ArtistsListUI";


export default function ArtistList() {
    const [artists, setArtists] = useState<Artist[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.get(`/api/v1/admin/artists`)
            .then(res => {
                setArtists(res.data.content);
                setLoading(false);
            }).catch(err => {
                console.error("Error fetching artists:", err);
                setLoading(false);
                alert("Error al cargar los artistas. Por favor, inténtalo de nuevo más tarde.");
            });
    }, []);

    const handleDelete = async (id: number) => {
        const confirmDelete = window.confirm("¿Estás seguro de que deseas eliminar este artista? Esta acción no se puede deshacer.");
        if (confirmDelete) {
            try {
                await axios.delete(`/api/v1/admin/artists/${id}`);
                setArtists(artists.filter(artist => artist.artistID !== id));
            } catch (error) {
                alert("Hubo un error al eliminar el artista.");
            }
        }
    };

    if (loading) {
        return <div className="container my-5 text-center">Cargando lista de artistas...</div>;
    }

    return (
        <ArtistListUI
            artists={artists}
            onDelete={handleDelete}
        />
    );
}