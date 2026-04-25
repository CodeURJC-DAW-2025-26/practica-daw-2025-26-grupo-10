import { Link, useNavigate } from "react-router";
import { changeProfile, getProfileFormInformation, changeProfileImage } from "~/services/user-service";
import type { ChangeProfileBasic } from "~/models/UserBasic";
import { useEffect, useState, useActionState } from "react";
import { useStore } from "~/store/useStore";

export default function ChangeProfile() {
    const [isLoadingInitial, setIsLoadingInitial] = useState(true);
    const [initialError, setInitialError] = useState<string | null>(null);
    const [profileInformation, setProfileInfo] = useState<ChangeProfileBasic>();

    const refreshUser = useStore((state) => state.refreshUser);
    const user = useStore((state) => state.user);
    const navigate = useNavigate();

    async function handleLoadInformation() {
        setInitialError(null);
        try {
            const data = await getProfileFormInformation();
            setProfileInfo(data);
        } catch (error) {
            setInitialError(error instanceof Error ? error.message : "Error al cargar la información");
        } finally {
            setIsLoadingInitial(false);
        }
    }

    useEffect(() => {
        handleLoadInformation();
    }, []);

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
                if (!user?.userID) {
                    throw new Error("No se ha encontrado el ID del usuario actual");
                }
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

    if (isLoadingInitial) return <p>Cargando información del perfil...</p>;
    if (initialError) return <p style={{ color: "red" }}>{initialError}</p>;

    return (
        <div>
            <h2>Editar perfil</h2>

            {formState?.error && <p style={{ color: "red" }}>{formState.error}</p>}

            <form action={formAction}>
                <input type="hidden" name="version" value={profileInformation?.version} />

                <div>
                    <label>Nombre</label>
                    <input type="text" name="name" defaultValue={profileInformation?.name} required />
                </div>
                <div>
                    <label>Apellido</label>
                    <input type="text" name="surname" defaultValue={profileInformation?.surname} required />
                </div>
                <div>
                    <label>Username</label>
                    <input type="text" name="username" defaultValue={profileInformation?.username} required />
                </div>
                <div>
                    <label>Email</label>
                    <input type="email" name="email" defaultValue={profileInformation?.email} required />
                </div>
                <div>
                    <label>Teléfono</label>
                    <input type="text" name="phone" defaultValue={profileInformation?.phone} required />
                </div>
                <div>
                    <label>Edad</label>
                    <input type="number" name="age" defaultValue={profileInformation?.age} required />
                </div>

                <div>
                    <label>Foto de perfil</label>
                    <input type="file" name="imageFile" accept=".jpg, .jpeg, .png" />
                </div>

                <div>
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? "Actualizando..." : "Actualizar"}
                    </button>

                    <Link to="/clients/profile">
                        <button type="button" disabled={isSubmitting}>Cancelar</button>
                    </Link>
                </div>
            </form>
        </div>
    );
}