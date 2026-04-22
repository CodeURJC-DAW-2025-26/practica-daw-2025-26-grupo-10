import { Link, useLocation, useParams } from "react-router";
import { getDownloadUrl } from "~/services/purchases-service";
import type { PurchaseConfirmation } from "~/models/Purchase";

interface ConfirmationState {
  purchase: PurchaseConfirmation;
  eventName?: string;
}

export default function Confirmation() {
  const { purchaseId } = useParams<{ purchaseId: string }>();
  const location = useLocation();
  const state = location.state as ConfirmationState | null;

  // If user navigates here directly without coming from purchase page
  if (!state?.purchase) {
    return (
      <div className="container my-5 text-center">
        <div className="alert alert-success mb-4">
          <h4 className="alert-heading">¡Compra realizada!</h4>
          <p className="mb-0">Tu pedido #{purchaseId} ha sido procesado.</p>
        </div>
        <div className="d-flex justify-content-center gap-3">
          <Link to="/" className="btn btn-primary">
            Volver al inicio
          </Link>
          {purchaseId && (
            <a
              href={getDownloadUrl(purchaseId)}
              className="btn btn-outline-primary"
              target="_blank"
              rel="noreferrer"
            >
              Descargar entradas (PDF)
            </a>
          )}
        </div>
      </div>
    );
  }

  const { purchase, eventName } = state;

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
        {(eventName || purchase.event?.name) && (
          <li className="list-group-item">
            <strong>Evento:</strong> {eventName ?? purchase.event?.name}
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
