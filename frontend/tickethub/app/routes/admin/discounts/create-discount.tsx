import { useActionState, useState } from "react";
import { useLoaderData, useNavigate, useParams } from "react-router";
import { Container, Form, Button, Alert } from "react-bootstrap";
import { getDiscount, createDiscount, updateDiscount } from "~/services/discounts-service";
import type Discount from "~/models/Discount";

export async function clientLoader({ params }: { params: { id?: string } }) {
  if (!params.id) return { discount: null };
  const discount = await getDiscount(params.id);
  return { discount };
}

export default function CreateDiscount() {
  const {discount: initialDiscount} = useLoaderData<typeof clientLoader>();

  const { id } = useParams<{ id?: string }>();
  const isEditing = !!id;
  const navigate = useNavigate();

  const [discount] = useState<Discount | null>(initialDiscount);

  async function formAction(_prev: { error: string | null }, formData: FormData) {
    const data = {
      discountName: formData.get("discountName") as string,
      amount: parseFloat(formData.get("amount") as string),
      percentage: formData.get("percentage") === "true",
    };

    if (!data.discountName.trim()) return { error: "El nombre del descuento es obligatorio." };
    if (isNaN(data.amount) || data.amount <= 0) return { error: "El importe debe ser un número positivo." };

    try {
      if (isEditing && id) await updateDiscount(id, data);
      else await createDiscount(data);
      navigate("/admin/discounts");
      return { error: null };
    } catch {
      return { error: isEditing ? "No se pudo actualizar el descuento." : "No se pudo crear el descuento." };
    }
  }

  const [state, dispatchAction, isPending] = useActionState(formAction, { error: null });

  return (
    <Container className="my-5">
      <h2 className="text-center mb-4">
        {isEditing ? "Editar Descuento" : "Crear Descuento"}
      </h2>

      {state.error && <Alert variant="danger">{state.error}</Alert>}

      <Form action={dispatchAction} className="col-md-6 mx-auto">
        <Form.Group className="mb-3">
          <Form.Label htmlFor="discountName">Nombre del descuento</Form.Label>
          <Form.Control
            id="discountName"
            type="text"
            name="discountName"
            defaultValue={discount?.discountName ?? ""}
            required
            disabled={isPending}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="amount">Cantidad</Form.Label>
          <Form.Control
            id="amount"
            type="number"
            step="0.01"
            min="0.01"
            name="amount"
            defaultValue={discount?.amount ?? ""}
            required
            disabled={isPending}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="percentage">Tipo de descuento</Form.Label>
          <Form.Select
            id="percentage"
            name="percentage"
            defaultValue={discount != null ? String(discount.percentage) : "true"}
            disabled={isPending}
          >
            <option value="true">Porcentaje (%)</option>
            <option value="false">Cantidad fija (€)</option>
          </Form.Select>
        </Form.Group>

        <div className="d-flex justify-content-between mt-4">
          <Button type="button" variant="outline-secondary" onClick={() => navigate("/admin/discounts")} disabled={isPending}>
            Cancelar
          </Button>
          <Button type="submit" variant="success" disabled={isPending}>
            {isPending ? (isEditing ? "Actualizando..." : "Creando...") : (isEditing ? "Actualizar" : "Crear")}
          </Button>
        </div>
      </Form>
    </Container>
  );
}
