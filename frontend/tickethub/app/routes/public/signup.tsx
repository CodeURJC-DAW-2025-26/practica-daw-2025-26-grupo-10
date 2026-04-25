import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useStore } from "~/store/useStore";


export default function SingUp() {
    const signup = useStore((state) => state.signup);
    const error = useStore((state) => state.error);
    const navigate = useNavigate();

    async function handleSignup(prevState: any, formData: FormData) {
        const name = formData.get("name") as string;
        const surname = formData.get("surname") as string;
        const username = formData.get("username") as string;
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;
        const passwordConfirmation = formData.get("passwordConfirmation") as string;
        try {
            await signup(name, surname, username, email, password, passwordConfirmation);
            navigate("/");
            return null;
        } catch (error) {
            return null;
        }

    }

    const [_, formAction, isPending] = useActionState(handleSignup, null);
    return (
        <>
            <div>
                <h3>Crear Cuenta</h3>
                <form action={formAction}>
                    <input type="text" name="name" placeholder="Nombre" required />
                    <br />
                    <input type="text" name="surname" placeholder="Apellido" required />
                    <br />
                    <input type="text" name="username" placeholder="Usuario" required />
                    <br />
                    <input type="email" name="email" placeholder="Correo Electrónico" required />
                    <br />
                    <input type="password" name="password" placeholder="Contraseña" required />
                    <br />
                    <input type="password" name="passwordConfirmation" placeholder="Confirmar Contraseña" required />
                    <br />
                    <button type="submit" disabled={isPending}>{isPending ? "Registrando..." : "Registrarse"}</button>
                    {error ? <p style={{ color: "red" }}>{error}</p> : ""}

                </form>
            </div>
        </>
    );
}