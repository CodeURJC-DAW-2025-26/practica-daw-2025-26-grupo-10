import { Modal, Button } from "react-bootstrap";

export const ConfirmDialog = ({ message, onConfirm, onCancel }:
    { message: string; onConfirm: () => void; onCancel: () => void }) => {
  return (
    <Modal show centered onHide={onCancel}>
      <Modal.Header>
        <Modal.Title>Confirmar acción</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>{message}</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onCancel}>Cancelar</Button>
        <Button variant="danger" onClick={onConfirm}>Eliminar</Button>
      </Modal.Footer>
    </Modal>
  );
};
