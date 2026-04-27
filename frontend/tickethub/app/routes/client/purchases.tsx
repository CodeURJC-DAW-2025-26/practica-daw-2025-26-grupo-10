import { Link, useLoaderData } from "react-router";
import React, { useState } from "react";
import { Container, Table, Button, Alert, Badge } from "react-bootstrap";
import type { PurchaseBasic } from "../../models/PurchaseBasic";
import { getPurchases } from "../../services/user-service";
import { getDownloadUrl } from "~/services/purchases-service";

export async function clientLoader() {
  const data = await getPurchases(0);
  return { purchases: data.content, hasNext: data.hasNext };
}

export default function ClientPurchases() {
    const { purchases: initialPurchases, hasNext: initialHasNext } = useLoaderData<typeof clientLoader>();

    const [purchases, setPurchases] = useState<PurchaseBasic[]>(initialPurchases);
    const [hasNext, setHasNext] = useState(initialHasNext);
    const [page, setPage] = useState(0);
    const [isPending, setIsPending] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [expandedRows, setExpandedRows] = useState<Set<number>>(new Set());

    async function loadMore(nextPage: number) {
        setIsPending(true);
        setError(null);
        try {
        const data = await getPurchases(nextPage);
        setPurchases(prev => [...prev, ...data.content]);
        setHasNext(data.hasNext);
        setPage(nextPage);
        } catch (error) {
        setError(error instanceof Error ? error.message : "Error al cargar las compras");
        } finally {
        setIsPending(false);
        }
    }

    function toggleRow(purchaseID: number) {
        const newExpandedRows = new Set(expandedRows);
        if (newExpandedRows.has(purchaseID)) newExpandedRows.delete(purchaseID);
        else newExpandedRows.add(purchaseID);
        setExpandedRows(newExpandedRows);
    }

    function getPurchaseStatus(tickets: { isActive: boolean }[]) {
        const allActive = tickets.every(t => t.isActive);
        const noneActive = tickets.every(t => !t.isActive);
        if (noneActive) return "Cancelada";
        if (allActive) return "Válida";
        return "Parcialmente usada";
    }

    return (
        <Container className="my-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Mis Entradas</h2>
                <Link to="/clients/profile" className="btn btn-outline-primary">
                    Regresar al Perfil
                </Link>
            </div>

            {error && <Alert variant="danger">{error}</Alert>}

            {purchases.length === 0 && !error ? (
                <p>No hay compras registradas aún.</p>
            ) : (
                <>
                    <Table hover responsive>
                        <thead>
                            <tr>
                                <th>Evento</th>
                                <th>Fecha Sesión</th>
                                <th>Estado</th>
                                <th>Precio Total</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {purchases.map(purchase => (
                                <React.Fragment key={purchase.purchaseID}>
                                    <tr>
                                        <td><strong>{purchase.session.eventName}</strong></td>
                                        <td>{new Date(purchase.session.date).toLocaleString("es-ES")}</td>
                                        <td>{getPurchaseStatus(purchase.tickets)}</td>
                                        <td>{purchase.totalPrice} €</td>
                                        <td>
                                            <div className="d-flex gap-2 justify-content-center">
                                                <Button
                                                    size="sm"
                                                    variant="outline-primary"
                                                    onClick={() => toggleRow(purchase.purchaseID)}
                                                >
                                                    {expandedRows.has(purchase.purchaseID) ? "Ocultar" : "Ver Detalles"}
                                                </Button>
                                                <Button
                                                    href={getDownloadUrl(purchase.purchaseID)}
                                                    target="_blank"
                                                    rel="noreferrer"
                                                >
                                                    Descargar entradas (PDF)
                                                </Button>
                                            </div>
                                        </td>
                                    </tr>

                                    {expandedRows.has(purchase.purchaseID) && (
                                        <tr>
                                            <td colSpan={5} className="p-3">
                                                <h5>Desglose de Tickets (Ref: #{purchase.purchaseID})</h5>
                                                <Table size="sm" bordered className="mb-0">
                                                    <thead>
                                                        <tr>
                                                            <th>Código / QR</th>
                                                            <th>Precio Unitario</th>
                                                            <th>Estado del Ticket</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        {purchase.tickets.map(ticket => (
                                                            <tr key={ticket.ticketID}>
                                                                <td><code>{ticket.code}</code></td>
                                                                <td>{ticket.ticketPrice} €</td>
                                                                <td>
                                                                    <Badge bg={ticket.isActive ? "success" : "danger"}>
                                                                        {ticket.isActive ? "Válido" : "Usado/Inactivo"}
                                                                    </Badge>
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </Table>
                                            </td>
                                        </tr>
                                    )}
                                </React.Fragment>
                            ))}
                        </tbody>
                    </Table>

                    {hasNext && (
                        <div className="text-center mt-4">
                            <Button
                                onClick={() => loadMore(page + 1)}
                                disabled={isPending}
                            >
                                {isPending ? "Cargando más..." : "Cargar más historial"}
                            </Button>
                        </div>
                    )}
                </>
            )}
        </Container>
    );
}
