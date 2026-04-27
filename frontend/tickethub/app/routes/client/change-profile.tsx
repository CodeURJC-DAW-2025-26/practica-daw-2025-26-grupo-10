import { Link, useLoaderData, useNavigate } from "react-router";
import { changeProfile, getProfileFormInformation, changeProfileImage } from "~/services/user-service";
import type { ChangeProfileBasic } from "~/models/UserBasic";
import { useActionState } from "react";
import { useStore } from "~/store/useStore";
import { Container, Card, Form, Button, Alert, Row, Col } from "react-bootstrap";

export async function clientLoader() {
  const profileInformation = await getProfileFormInformation();
  return { profileInformation };
}

export default function ChangeProfile() {
    const {profileInformation} = useLoaderData<typeof clientLoader>();

    const refreshUser = useStore((state) => state.refreshUser);
    const user = useStore((state) => state.user);
    const navigate = useNavigate();

    async function handleSubmit(prevState: any, formData: FormData) {
        try {
            const changeProfileBasic: ChangeProfileBasic = {
                version: parseInt(formData.get("version") as string),
                name: formData.get("name") as string,
                surname: formData.get("surname") as string,
                username: formData.get("username") as string,
                email: formData.get("email") as string,
                phone: formData.get("phone") as string,
                age: parseInt(formData.get("age") as string)
            };
            await changeProfile(changeProfileBasic);
            const imageFile = formData.get("imageFile") as File;
            if (imageFile && imageFile.size > 0) {
                if (!user?.userID) throw new Error("No se ha encontrado el ID del usuario actual");
                await changeProfileImage(user.userID, imageFile);
            }
            await refreshUser();
            navigate("/clients/profile");
            return null;
        } catch (error) {
            return { error: error instanceof Error ? error.message : "Error al actualizar el perfil" };
        }
    }

    const [formState, formAction, isSubmitting] = useActionState(handleSubmit, null);

    return (
        <Container className="my-5">
            <Row className="justify-content-center">
                <Col md={7}>
                    <Card>
                        <Card.Body>
                            <h2 className="mb-4">Editar perfil</h2>
                            {formState?.error && <Alert variant="danger">{formState.error}</Alert>}
                            <Form action={formAction}>
                                <input type="hidden" name="version" value={profileInformation?.version} />
                                <Form.Group className="mb-3">
                                    <Form.Label>Nombre</Form.Label>
                                    <Form.Control type="text" name="name" defaultValue={profileInformation?.name} required />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Apellido</Form.Label>
                                    <Form.Control type="text" name="surname" defaultValue={profileInformation?.surname} required />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Username</Form.Label>
                                    <Form.Control type="text" name="username" defaultValue={profileInformation?.username} required />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control type="email" name="email" defaultValue={profileInformation?.email} required />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Teléfono</Form.Label>
                                    <Form.Control type="text" name="phone" defaultValue={profileInformation?.phone} />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Edad</Form.Label>
                                    <Form.Control type="number" name="age" defaultValue={profileInformation?.age} />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Foto de perfil</Form.Label>
                                    <Form.Control type="file" name="imageFile" accept=".jpg, .jpeg, .png" />
                                </Form.Group>
                                <div className="d-flex gap-2">
                                    <Button type="submit" variant="primary" disabled={isSubmitting}>
                                        {isSubmitting ? "Actualizando..." : "Actualizar"}
                                    </Button>
                                    <Link to="/clients/profile" className="btn btn-outline-secondary">
                                        Cancelar
                                    </Link>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}
