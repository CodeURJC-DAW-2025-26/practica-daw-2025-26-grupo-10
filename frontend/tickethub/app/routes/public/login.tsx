import { useStore } from "~/store/useStore";
import { useNavigate } from "react-router";
import { useActionState } from "react";

export default function Login() {
    const login = useStore((state) => state.login);
    const error = useStore((state) => state.error);
    const navigate = useNavigate();

    async function handleLogin(prevState: any, formData: FormData) {
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;
        try {
            await login(email, password);
            navigate("/clients/profile");
            return null;
        } catch (error) {
            return null;
        }
    }

    const [_, formAction, isPending] = useActionState(
        handleLogin,
        null
    )

    return (
        <>
            <div>
                <h3>Iniciar sesión</h3>
                <form action={formAction}>
                    <input name="email" type="text" placeholder="Correo electrónico" disabled={isPending} required />
                    <br />
                    <input name="password" type="password" placeholder="Contraseña" disabled={isPending} required />
                    <br />
                    <button type="submit" disabled={isPending}>{isPending ? 'Enviando...' : 'Iniciar sesión'}</button>
                </form>
                {error && <p style={{ color: "red" }}>{error}</p>}
            </div>
        </>
    );
}