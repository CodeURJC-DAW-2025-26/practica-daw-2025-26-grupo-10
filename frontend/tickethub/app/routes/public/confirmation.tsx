import { useEffect, useState } from "react";
import { Link, useParams } from "react-router";
import { getPurchase, getDownloadUrl } from "~/services/purchases-service";
import type { PurchaseConfirmation } from "~/services/purchases-service";

export default function Confirmation() {
  const { purchaseId } = useParams<{ purchaseId: string }>();
  const [purchase, setPurchase] = useState<PurchaseConfirmation | null>(null);
  const [isPending, setIsPending] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!purchaseId) return;
    getPurchase(purchaseId)
      .then(setPurchase)
      .catch(() => setError("No se pudo cargar la información de la compra."))
      .finally(() => setIsPending(false));
  }, [purchaseId]);

  if (isPending) {
    return (
      <div className="container my-5 text-center">
        <p>Cargando confirmación...</p>
      </div>
    );
  }

  if (error || !purchase) {
    return (
      <div className="container my-5 text-center">
        <div className="alert alert-danger">
          {error ?? "No se encontró la compra."}
        </div>
        <Link to="/" className="btn btn-primary">
          Volver al inicio
        </Link>
      </div>
    );
  }

  const formattedDate = purchase.session?.date
    ? new Date(purchase.session.date).toLocaleString("es-ES", {
        dateStyle: "long",
        timeStyle: "short",
      })
    : null;

  return (
    <div className="container my-5 text-center">
      <div className="alert alert-success mb-4">
        <h4 className="alert-heading">¡Compra realizada con éxito!</h4>
        <p className="mb-0">Tu pedido ha sido procesado correctamente.</p>
      </div>

      <ul className="list-group list-group-flush col-md-6 mx-auto text-start mb-4">
        <li className="list-group-item">
          <strong>ID de Pedido:</strong> #{purchase.purchaseID}
        </li>
        {purchase.event && (
          <li className="list-group-item">
            <strong>Evento:</strong> {purchase.event.name}
          </li>
        )}
        <li className="list-group-item">
          <strong>Total Pagado:</strong> {purchase.totalPrice} €
        </li>
        {formattedDate && (
          <li className="list-group-item">
            <strong>Fecha:</strong> {formattedDate}
          </li>
        )}
      </ul>

      <div className="d-flex justify-content-center gap-3">
        <Link to="/" className="btn btn-primary">
          Volver al inicio
        </Link>
        <a
          href={getDownloadUrl(purchase.purchaseID)}
          className="btn btn-outline-primary"
          target="_blank"
          rel="noreferrer"
        >
          Descargar entradas (PDF)
        </a>
      </div>
    </div>
  );
}
