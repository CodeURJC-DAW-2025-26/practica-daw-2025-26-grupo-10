import React, { useState, useEffect } from "react";
import { Container, Form, Button, Alert } from "react-bootstrap";
import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import type { ImageBasic } from "~/models/ImageBasic";

interface Props {
  initialData: ArtistCreateUpdate;
  isEditMode: boolean;
  onSubmit: (artist: ArtistCreateUpdate, image: File | null) => void;
  onCancel: () => void;
  artistID: number | null;
  artistImage: ImageBasic | null;
  onDeleteImage: () => void;
  error: string | null
}

export default function ArtistFormUI({ initialData, isEditMode, onSubmit, onCancel, artistID, artistImage, onDeleteImage, error }: Props) {
  const [artist, setArtist] = useState<ArtistCreateUpdate>(initialData);
  const [image, setImage] = useState<File | null>(null);

  useEffect(() => { setArtist(initialData); }, [initialData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setArtist({ ...artist, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(artist, image);
  };

  return (
    <Container className="my-5">
      <h2 className="text-center mb-4">
        {isEditMode ? "Editar Artista" : "Crear Artista"}
      </h2>
      
      {error && <Alert variant="danger">{error}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Nombre del Artista</Form.Label>
          <Form.Control name="artistName" value={artist.artistName} onChange={handleChange} required />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Bio / Info</Form.Label>
          <Form.Control as="textarea" name="info" value={artist.info} onChange={handleChange} rows={3} />
        </Form.Group>

        <Form.Group as={Form.Group} className="mb-3">
          <Form.Label className="fw-bold">Instagram</Form.Label>
          <Form.Control name="instagram" value={artist.instagram} onChange={handleChange} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Twitter</Form.Label>
          <Form.Control name="twitter" value={artist.twitter} onChange={handleChange} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Imagen</Form.Label>
          <Form.Control type="file" onChange={(e) => setImage((e.target as HTMLInputElement).files?.[0] ?? null)} />
        </Form.Group>

        {isEditMode && (
          <div className="mb-3">
            <label className="fw-bold">Imagen actual</label>
            {artistImage ? (
              <div className="row g-3 mt-1">
                <div className="col-6 col-md-3">
                  <div className="image-overlay-container border">
                    <button
                      type="button"
                      className="btn-delete-overlay"
                      onClick={onDeleteImage}
                      title="Eliminar imagen"
                    >
                      <i className="bi bi-trash-fill"></i>
                      <span className="fw-bold small">ELIMINAR</span>
                    </button>
                    <img
                      src={`/api/v1/public/artists/${artistID}/image`}
                      className="img-event-admin"
                      alt={artist.artistName}
                    />
                  </div>
                </div>
              </div>
            ) : (
              <p className="text-muted mt-1">No hay imagen para este artista.</p>
            )}
          </div>
        )}

        <div className="d-flex justify-content-between mt-4">
          <Button type="button" variant="secondary" onClick={onCancel}>Cancelar</Button>
          <Button type="submit" variant="primary">{isEditMode ? "Actualizar" : "Crear"}</Button>
        </div>
      </Form>
    </Container>
  );
}
