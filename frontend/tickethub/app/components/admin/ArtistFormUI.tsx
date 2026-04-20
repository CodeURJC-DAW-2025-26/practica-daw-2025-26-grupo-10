// Components don't know about routes, API calls, or any of that. 
// They just receive data through props and display it, and they send data back to the parent component (route) through callbacks (like onSubmit and onCancel).
import React, { useState, useEffect } from "react";

export interface Artist {
  artistName: string;
  info: string;
  instagram: string;
  twitter: string;
}

// Props are similar to mocks, but they are the actual data that the parent component will pass to this child component.
interface Props {
  initialData: Artist;
  isEditMode: boolean;
  onSubmit: (artist: Artist, image: File | null) => void;
  onCancel: () => void;
}

export default function ArtistFormUI({
  initialData,
  isEditMode,
  onSubmit,
  onCancel,
}: Props) {
  const [artist, setArtist] = useState<Artist>(initialData);
  const [image, setImage] = useState<File | null>(null);

  // Just in case initialData is none at first, we want to set it when it changes (like when we fetch it from the server)
  useEffect(() => {
    setArtist(initialData);
  }, [initialData]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    setArtist({ ...artist, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // We send de data to the parent component through the onSubmit prop, which will handle the API call and navigation
    // This way we keep this component focused on just the UI and form logic.
    onSubmit(artist, image);
  };

  return (
    <div className="container my-5">
      <h2 className="text-center mb-4">
        {isEditMode ? "Editar Artista" : "Crear Artista"}
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
            <input
              name="instagram"
              className="form-control"
              value={artist.instagram}
              onChange={handleChange}
            />
          </div>
          <div className="col-md-6 mb-3">
            <label className="fw-bold">Twitter</label>
            <input
              name="twitter"
              className="form-control"
              value={artist.twitter}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="mb-3">
          <label className="fw-bold">Imagen</label>
          <input
            type="file"
            className="form-control"
            onChange={(e) =>
              setImage(e.target.files ? e.target.files[0] : null)
            }
          />
        </div>

        <div className="d-flex justify-content-between mt-4">
          {/** We don't use Link here because Components don't know about routes */}
          <button
            type="button"
            onClick={onCancel}
            className="btn btn-secondary"
          >
            Cancelar
          </button>

          <button type="submit" className="btn btn-primary">
            {isEditMode ? "Actualizar" : "Crear"}
          </button>
        </div>
      </form>
    </div>
  );
}
