import { useEffect } from "react";
import { useLoaderData } from "react-router";
import { useAdminArtistsStore } from "~/store/adminArtistsStore";
import ArtistsManagementUI from "~/components/admin/ArtistsManagementUI";
import { getAllArtists } from "~/services/AdminArtistService";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";

export async function clientLoader() {
    const artists = await getAllArtists();
    return { initial: artists };
}

export default function ArtistsManagementRoute() {
    const { initial } = useLoaderData<typeof clientLoader>();
    const { artists, reset, deleteArtist } = useAdminArtistsStore();
    const { isOpen, message, confirm, handleConfirm, handleCancel } = useConfirmDialog();
    const { error, setError, success, setSuccess } = useTemporaryMessage();

    useEffect(() => { reset(initial); }, []);

    const handleDelete = (id: number) => {
        confirm(
            "¿Estás seguro de que deseas eliminar este artista? Esta acción no se puede deshacer.",
            async () => {
                try {
                    await deleteArtist(id);
                    setSuccess("Artista eliminado correctamente.");
                } catch {
                    setError("Hubo un error al eliminar el artista.");
                }
            }
        );
    };

    return (
        <ArtistsManagementUI
            artists={artists}
            onDelete={handleDelete}

            isDialogOpen={isOpen}
            dialogMessage={message}
            onDialogConfirm={handleConfirm}
            onDialogCancel={handleCancel}

            error={error}
            success={success}
        />
    );
}