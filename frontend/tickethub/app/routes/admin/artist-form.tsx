import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router';
import axios from 'axios';

// Typescript Interface
interface Artist {
    artistName: string;
    info: string;
    instagram: string;
    twitter: string;
}

export default function ArtistForm() {
    const { id } = useParams();
    const navigate = useNavigate();
    const isEditMode = Boolean(id);

    const [artist, setArtist] = useState<Artist>({
        artistName: '',
        info: '',
        instagram: '',
        twitter: ''
    });
    const [image, setImage] = useState<File | null>(null);
    const [loading, setLoading] = useState(isEditMode);

    // If we're in edit mode, fetch the existing artist data
    useEffect(() => {
        if (isEditMode) {
            axios.get(`/api/v1/artists/${id}`)
                .then(res => {
                    setArtist(res.data);
                    setLoading(false);
                })
                .catch(err => console.error("Error fetching data:", err));
        }
    }, [id, isEditMode]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setArtist({ ...artist, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('artistName', artist.artistName);
        formData.append('info', artist.info);
        formData.append('instagram', artist.instagram);
        formData.append('twitter', artist.twitter);
        if (image) formData.append('image', image);

        try {
            if (isEditMode) {
                await axios.put(`/api/v1/artists/${id}`, formData);
            } else {
                await axios.post('/api/v1/artists', formData);
            }
            navigate('/admin/artists'); 
        } catch (error) {
            console.error("Submission error:", error);
        }
    };

    if (loading) return <div className="p-5">Cargando artista...</div>;

    return (
        <main className="container my-5">
            <h2 className="text-center mb-4">
                {isEditMode ? 'Editar Artista' : 'Crear Artista'}
            </h2>

            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="fw-bold">Nombre del Artista</label>
                    <input 
                        name="artistName" 
                        className="form-control" 
                        value={artist.artistName} 
                        onChange={handleChange} 
                        required 
                    />
                </div>

                <div className="mb-3">
                    <label className="fw-bold">Bio / Info</label>
                    <textarea 
                        name="info" 
                        className="form-control" 
                        value={artist.info} 
                        onChange={handleChange} 
                        rows={3} 
                    />
                </div>

                <div className="row">
                    <div className="col-md-6 mb-3">
                        <label className="fw-bold">Instagram</label>
                        <input name="instagram" className="form-control" value={artist.instagram} onChange={handleChange} />
                    </div>
                    <div className="col-md-6 mb-3">
                        <label className="fw-bold">Twitter</label>
                        <input name="twitter" className="form-control" value={artist.twitter} onChange={handleChange} />
                    </div>
                </div>

                <div className="mb-3">
                    <label className="fw-bold">Imagen</label>
                    <input 
                        type="file" 
                        className="form-control" 
                        onChange={(e) => setImage(e.target.files ? e.target.files[0] : null)} 
                    />
                </div>

                <div className="d-flex justify-content-between">
                    <Link to="/admin" className="btn btn-secondary">Cancelar</Link>
                    <button type="submit" className="btn btn-primary">
                        {isEditMode ? 'Actualizar' : 'Crear'}
                    </button>
                </div>
            </form>
        </main>
    );
}