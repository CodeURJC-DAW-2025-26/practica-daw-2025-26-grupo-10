import React, { useState, useEffect } from "react";
import { Container, Form, Button } from "react-bootstrap";
import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";

interface Props {
  initialData: ArtistCreateUpdate;
  isEditMode: boolean;
  onSubmit: (artist: ArtistCreateUpdate, image: File | null) => void;
  onCancel: () => void;
}

export default function ArtistFormUI({ initialData, isEditMode, onSubmit, onCancel }: Props) {
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

        <div className="d-flex justify-content-between mt-4">
          <Button type="button" variant="secondary" onClick={onCancel}>Cancelar</Button>
          <Button type="submit" variant="primary">{isEditMode ? "Actualizar" : "Crear"}</Button>
        </div>
      </Form>
    </Container>
  );
}
