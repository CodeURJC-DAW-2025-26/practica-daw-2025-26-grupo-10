import { Link, useLocation, useParams } from "react-router";
import { Container, Alert, ListGroup, Button } from "react-bootstrap";
import { getDownloadUrl } from "~/services/purchases-service";
import type Purchase from "~/models/Purchase";

interface ConfirmationState {
  purchase: Purchase;
  eventName?: string;
}

export default function Confirmation() {
  const { purchaseId } = useParams<{ purchaseId: string }>();
  const location = useLocation();
  const state = location.state as ConfirmationState | null;

  if (!state?.purchase) {
    return (
      <Container className="my-5 text-center">
        <Alert variant="success" className="mb-4">
          <Alert.Heading>¡Compra realizada!</Alert.Heading>
          <p className="mb-0">Tu pedido #{purchaseId} ha sido procesado.</p>
        </Alert>
        <div className="d-flex justify-content-center gap-3">
          <Link to="/" className="btn btn-primary">Volver al inicio</Link>
          {purchaseId && (
            <a href={getDownloadUrl(purchaseId)} className="btn btn-outline-primary" target="_blank" rel="noreferrer">
              Descargar entradas (PDF)
            </a>
          )}
        </div>
      </Container>
    );
  }

  const { purchase, eventName } = state;

  const formattedDate = purchase.session?.date
    ? new Date(purchase.session.date).toLocaleString("es-ES", { dateStyle: "long", timeStyle: "short" })
    : null;

  return (
    <Container className="my-5 text-center">
      <Alert variant="success" className="mb-4">
        <Alert.Heading>¡Compra realizada con éxito!</Alert.Heading>
        <p className="mb-0">Tu pedido ha sido procesado correctamente.</p>
      </Alert>

      <ListGroup className="col-md-6 mx-auto text-start mb-4" variant="flush">
        <ListGroup.Item><strong>ID de Pedido:</strong> #{purchase.purchaseID}</ListGroup.Item>
        {eventName && <ListGroup.Item><strong>Evento:</strong> {eventName}</ListGroup.Item>}
        <ListGroup.Item><strong>Total Pagado:</strong> {purchase.totalPrice} €</ListGroup.Item>
        {formattedDate && <ListGroup.Item><strong>Fecha:</strong> {formattedDate}</ListGroup.Item>}
      </ListGroup>

      <div className="d-flex justify-content-center gap-3">
        <Link to="/" className="btn btn-primary">Volver al inicio</Link>
      </div>
    </Container>
  );
}
