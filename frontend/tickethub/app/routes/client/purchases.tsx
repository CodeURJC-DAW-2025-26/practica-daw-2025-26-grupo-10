import { Link } from "react-router";
import React, { useEffect, useState } from "react";
import { type PurchaseBasic } from "../../models/PurchasesPageBasics";
import { getPurchases } from "../../services/user-service";

export default function ClientPurchases() {
    const [purchases, setPurchases] = useState<PurchaseBasic[]>([]);
    const [isPending, setIsPending] = useState(true);
    const [hasNext, setHasNext] = useState(false);
    const [page, setPage] = useState(0);
    const [error, setError] = useState<string | null>(null);
    const [expandedRows, setExpandedRows] = useState<Set<number>>(new Set());

    async function loadPurchases(pageToLoad: number) {
        if (pageToLoad === 0) setIsPending(true);
        setError(null);
        try {
            const data = await getPurchases(pageToLoad);
            setPurchases(prev => pageToLoad === 0 ? data.content : [...prev, ...data.content]);
            setHasNext(data.hasNext);
            setPage(pageToLoad);
        } catch (error) {
            setError(error instanceof Error ? error.message : "Error al cargar las compras");
        } finally {
            setIsPending(false);
        }
    }

    useEffect(() => {
        loadPurchases(0);
    }, []);

    function toggleRow(purchaseID: number) {
        const newExpandedRows = new Set(expandedRows);
        if (newExpandedRows.has(purchaseID)) {
            newExpandedRows.delete(purchaseID);
        } else {
            newExpandedRows.add(purchaseID);
        }
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
        <div>
            <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Mis Entradas</h2>
                <Link to="/clients/profile">
                    <button>Regresar al Perfil</button>
                </Link>
            </header>

            {error && <div style={{ color: "red", padding: "10px", border: "1px solid red" }}>{error}</div>}

            {isPending && page === 0 ? (
                <p>Cargando tu historial...</p>
            ) : purchases.length === 0 && !error ? (
                <p>No hay compras registradas aún.</p>
            ) : (
                <>
                    <table border={1} width="100%" style={{ borderCollapse: "collapse", marginTop: "20px" }}>
                        <thead>
                            <tr style={{ backgroundColor: "#f0f0f0" }}>
                                <th style={{ padding: "10px" }}>Evento</th>
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
                                        <td style={{ padding: "10px" }}><strong>{purchase.session.eventName}</strong></td>
                                        <td>{new Date(purchase.session.date).toLocaleString("es-ES")}</td>
                                        <td>{getPurchaseStatus(purchase.tickets)}</td>
                                        <td>{purchase.totalPrice} €</td>
                                        <td style={{ display: 'flex', gap: '10px', justifyContent: 'center', padding: "5px" }}>
                                            <button onClick={() => toggleRow(purchase.purchaseID)}>
                                                {expandedRows.has(purchase.purchaseID) ? "Ocultar Detalles" : "Ver Detalles"}
                                            </button>
                                            <button onClick={() => alert("Imprimiendo PDF...")}>Imprimir PDF</button>
                                        </td>
                                    </tr>

                                    {expandedRows.has(purchase.purchaseID) && (
                                        <tr style={{ backgroundColor: "#fafafa" }}>
                                            <td colSpan={5} style={{ padding: "15px" }}>
                                                <h4>Desglose de Tickets (Ref: #{purchase.purchaseID})</h4>
                                                <table border={1} width="80%" style={{ borderCollapse: "collapse", margin: "auto" }}>
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
                                                                <td style={{ color: ticket.isActive ? "green" : "red" }}>
                                                                    {ticket.isActive ? "🟢 Válido" : "🔴 Usado/Inactivo"}
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                    )}
                                </React.Fragment>
                            ))}
                        </tbody>
                    </table>

                    {/* BOTÓN CARGAR MÁS (Paginación Slice) */}
                    {hasNext && (
                        <div style={{ textAlign: "center", marginTop: "20px" }}>
                            <button
                                onClick={() => loadPurchases(page + 1)}
                                disabled={isPending}
                            >
                                {isPending ? "Cargando más..." : "Cargar más historial"}
                            </button>
                        </div>
                    )}
                </>
            )}
        </div>
    );
}