import { Link } from "react-router";
import { changePassword } from "~/services/user-service";
import { useActionState, useState } from "react";
import type { ChangePasswordBasic } from "~/models/UserBasic";

interface ActionState {
    success?: string;
    error?: string;
}

export default function ChangePassword() {

    async function handleUpdatePassword(prevState: ActionState | null, formData: FormData) {
        const oldPassword = formData.get("oldPassword") as string;
        const newPassword = formData.get("newPassword") as string;
        const confirmationPassword = formData.get("confirmationPassword") as string;

        const changePasswordBasic: ChangePasswordBasic = {
            oldPassword,
            newPassword,
            confirmationPassword
        }

        try {
            const response = await changePassword(changePasswordBasic);
            return { success: response };
        } catch (error: any) {
            return { error: error.message };
        }

    }

    const [state, updatePassword, isPending] = useActionState(handleUpdatePassword, null);
    return (
        <div>
            <h2>Cambiar contraseña</h2>
            <form action={updatePassword}>
                <div>
                    <label>Contraseña actual</label>
                    <input type="password" name="oldPassword" required />
                </div>
                <br />
                <div>
                    <label>Nueva Contraseña</label>
                    <input type="password" name="newPassword" required />
                </div>
                <br />
                <div>
                    <label>Repetir nueva contraseña</label>
                    <input type="password" name="confirmationPassword" required />
                </div>
                <br />
                <div>
                    {state?.success && <p style={{ color: "green", fontWeight: "bold" }}>{state.success}</p>}
                    {state?.error && <p style={{ color: "red", fontWeight: "bold" }}>{state.error}</p>}

                    <button type="submit" disabled={isPending}>
                        {isPending ? "Actualizando..." : "Actualizar"}
                    </button>

                    <Link to="/clients/profile" style={{ marginLeft: '10px' }}>
                        <button>
                            Volver al perfil
                        </button>
                    </Link>
                </div>
            </form>
        </div>
    );
}