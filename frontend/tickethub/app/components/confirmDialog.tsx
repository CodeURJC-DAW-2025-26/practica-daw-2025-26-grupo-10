
export const ConfirmDialog = ({ message, onConfirm, onCancel }:
    { message:string;
      onConfirm: () => void;
      onCancel: () => void;
    }) => {
  return (
    <div className="modal d-block" style={{ backgroundColor: "rgba(0,0,0,0.5)" }}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">Confirmar acción</h5>
          </div>
          <div className="modal-body">
            <p>{message}</p>
          </div>
          <div className="modal-footer">
            <button className="btn btn-secondary" onClick={onCancel}>
              Cancelar
            </button>
            <button className="btn btn-danger" onClick={onConfirm}>
              Eliminar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};