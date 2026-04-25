import { Spinner } from 'react-bootstrap'

export default function GlobalSpinner() {
    return (
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: "200px" }}>
            <Spinner animation="border" role="status">
                <span className="visually-hidden">Cargando...</span>
            </Spinner>
        </div>
    )
}