import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router';
import axios from 'axios';
import ArtistUI, { type Artist, type EventShort } from "../../components/public/ArtistUI";

interface ArtistFullData extends Artist {
    eventsIncoming: EventShort[];
    lastEvents: EventShort[];
}

export default function Artist() {
    const { id } = useParams();
    const [artist, setArtist] = useState<ArtistFullData | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchArtistData = async () => {
            try {
                const res = await axios.get(`/api/v1/artists/${id}`);
                setArtist(res.data);
            } catch (err) {
                console.error("Error fetching artist data:", err);
                alert("Error al cargar los datos del artista. Por favor, inténtalo de nuevo más tarde.");
            } finally {
                setLoading(false);
            }
        };

        if (id) fetchArtistData();
    }, [id])

    if (loading) {
        return <div className="container my-5 text-center">Cargando lista de artistas...</div>;
    }

    if (!artist) {
        return <div className="container my-5 text-center">Artista no encontrado.</div>;
    }

    return (
        <ArtistUI
            artist={artist}
            eventsIncoming={artist.eventsIncoming || []}
            lastEvents={artist.lastEvents || []}
        />
    )
}
