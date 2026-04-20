import { useActionState, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import {
  getDiscount,
  createDiscount,
  updateDiscount,
} from "~/services/discounts-service";
import type { Discount } from "~/services/discounts-service";

export default function CreateDiscount() {
  const { id } = useParams<{ id?: string }>();
  const isEditing = !!id;
  const navigate = useNavigate();

  const [discount, setDiscount] = useState<Discount | null>(null);
  const [isLoadingDiscount, setIsLoadingDiscount] = useState(isEditing);

  // Load existing discount data when editing
  useEffect(() => {
    if (!id) return;
    setIsLoadingDiscount(true);
    getDiscount(id)
      .then(setDiscount)
      .catch(() => navigate("/admin/discounts"))
      .finally(() => setIsLoadingDiscount(false));
  }, [id]);

  async function formAction(
    _prev: { error: string | null },
    formData: FormData
  ) {
    const discountName = formData.get("discountName") as string;
    const amount = parseFloat(formData.get("amount") as string);
    const percentage = formData.get("percentage") === "true";

    if (!discountName.trim()) {
      return { error: "El nombre del descuento es obligatorio." };
    }
    if (isNaN(amount) || amount <= 0) {
      return { error: "El importe debe ser un número positivo." };
    }

    try {
      if (isEditing && id) {
        await updateDiscount(id, { discountName, amount, percentage });
      } else {
        await createDiscount({ discountName, amount, percentage });
      }
      navigate("/admin/discounts");
      return { error: null };
    } catch (err) {
      return {
        error: isEditing
          ? "No se pudo actualizar el descuento."
          : "No se pudo crear el descuento.",
      };
    }
  }

  const [state, dispatchAction, isPending] = useActionState(formAction, {
    error: null,
  });

  if (isLoadingDiscount) {
    return (
      <div className="container my-5 text-center">
        <p>Cargando datos del descuento...</p>
      </div>
    );
  }

  return (
    <div className="container my-5">
      <h2 className="text-center mb-4">
        {isEditing ? "Editar Descuento" : "Crear Descuento"}
      </h2>

      {state.error && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          {state.error}
        </div>
      )}

      <form action={dispatchAction} className="col-md-6 mx-auto">
        <div className="mb-3">
          <label htmlFor="discountName" className="form-label">
            Nombre del descuento
          </label>
          <input
            id="discountName"
            type="text"
            className="form-control"
            name="discountName"
            defaultValue={discount?.discountName ?? ""}
            required
            disabled={isPending}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="amount" className="form-label">
            Cantidad
          </label>
          <input
            id="amount"
            type="number"
            step="0.01"
            min="0.01"
            className="form-control"
            name="amount"
            defaultValue={discount?.amount ?? ""}
            required
            disabled={isPending}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="percentage" className="form-label">
            Tipo de descuento
          </label>
          <select
            id="percentage"
            className="form-select"
            name="percentage"
            defaultValue={
              discount != null ? String(discount.percentage) : "true"
            }
            disabled={isPending}
          >
            <option value="true">Porcentaje (%)</option>
            <option value="false">Cantidad fija (€)</option>
          </select>
        </div>

        <div className="d-flex justify-content-between mt-4">
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={() => navigate("/admin/discounts")}
            disabled={isPending}
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="btn btn-success"
            disabled={isPending}
          >
            {isPending
              ? isEditing
                ? "Actualizando..."
                : "Creando..."
              : isEditing
              ? "Actualizar"
              : "Crear"}
          </button>
        </div>
      </form>
    </div>
  );
}
