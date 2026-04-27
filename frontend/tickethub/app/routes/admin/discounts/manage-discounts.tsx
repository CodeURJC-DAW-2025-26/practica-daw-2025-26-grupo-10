import { useState } from "react";
import { Link, useLoaderData, useNavigate } from "react-router";
import { Container, Table, Button, Alert } from "react-bootstrap";
import { getDiscounts, deleteDiscount } from "~/services/discounts-service";
import type Discount from "~/models/Discount";
import { ConfirmDialog } from "~/components/confirmDialog/ConfirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";

export async function clientLoader() {
  const discounts = await getDiscounts();
  return {discounts}
}

export default function ManageDiscounts() {
  const {discounts: initialDiscounts} = useLoaderData<typeof clientLoader>()
  
  const [discounts, setDiscounts] = useState<Discount[]>(initialDiscounts);
  const navigate = useNavigate();

  const { error: deleteError, setError: setDeleteError, success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();
  const { isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm } = useConfirmDialog();

  function handleDelete(id: number) {
    confirm("¿Estás seguro de que deseas eliminar este descuento?", async () => {
      try {
        await deleteDiscount(id);
        setDiscounts((prev) => prev.filter((discount) => discount.discountID !== id));
        setDeleteSuccess("Descuento eliminado correctamente.");
      } catch (err) {
        console.error(err);
        setDeleteError("No se pudo eliminar el descuento.");
      }
    });
  }

  return (
    <Container className="my-5">
      {isNotConfirmed && (
        <ConfirmDialog message={message} onConfirm={handleConfirm} onCancel={handleCancel} />
      )}

      <h2>Gestión de Descuentos</h2>

      {deleteError && <Alert variant="danger">{deleteError}</Alert>}
      {deleteSuccess && <Alert variant="success">{deleteSuccess}</Alert>}

      
      <Table className="table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Valor</th>
            <th>Tipo</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {discounts.length === 0 && (
            <tr>
              <td colSpan={4} className="text-center text-muted">
                No hay descuentos registrados.
              </td>
            </tr>
          )}
          {discounts.map((d) => (
            <tr key={d.discountID}>
              <td>{d.discountName}</td>
              <td>
                {d.amount} {d.percentage ? "%" : "€"}
              </td>
              <td>{d.percentage ? "Porcentaje" : "Cantidad fija"}</td>
              <td className="d-flex gap-2">
                <Link to={`/admin/discounts/${d.discountID}`} className="btn btn-sm btn-primary">Editar</Link>
                <Button size="sm" variant="danger" onClick={() => handleDelete(d.discountID)}>Eliminar</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <div className="d-flex justify-content-between mt-3">
        <Button variant="outline-secondary" onClick={() => navigate("/admin")}>Volver</Button>
        <Link to="/admin/discounts/new" className="btn btn-success">+ Crear descuento</Link>
      </div>
    </Container>
  );
}
