import { Modal, Button } from "react-bootstrap";

interface ConfirmDialogProps {
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}

export function ConfirmDialog({ message, onConfirm, onCancel }: ConfirmDialogProps) {
  return (
    <Modal show centered onHide={onCancel}>
      <Modal.Body>
        <p className="mb-0">{message}</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onCancel}>Cancelar</Button>
        <Button variant="danger" onClick={onConfirm}>Confirmar</Button>
      </Modal.Footer>
    </Modal>
  );
}
