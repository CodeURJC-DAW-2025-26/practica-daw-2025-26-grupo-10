import { Link } from "react-router";
import { useStore } from "~/store/useStore";
import { API_URL } from "~/services/homeService";
import { useNavigate } from "react-router";

export default function Profile() {
    const user = useStore((state) => state.user);
    const logout = useStore((state) => state.logout);
    const navigate = useNavigate();
    async function handleLogout() {
        try {
            await logout();
            navigate("/");
        } catch (error) {
            console.error("Error al cerrar sesión", error);
        }
    }
    return (
        <>
            <div>
                <div>
                    <img src={`${API_URL}/users/${user!.userID}/image`} alt="" />
                    <h4>{user!.username}</h4>
                    <p>{user!.email}</p>
                    <div>
                        <Link to="/clients/profile/edit">
                            <button>Editar Perfil</button>
                        </Link>
                        <br />
                        <Link to="/purchases/me">
                            <button>Mis Compras</button>
                        </Link>
                        <br />
                        <Link to="/clients/me/password">
                            <button>Cambiar Contraseña</button>
                        </Link>
                        <br />
                        <button onClick={handleLogout}>Cerrar Sesión</button>

                    </div>
                </div>
            </div>

        </>
    );
}