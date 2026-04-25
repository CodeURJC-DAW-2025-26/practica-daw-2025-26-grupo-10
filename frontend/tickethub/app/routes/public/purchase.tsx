import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { getEvent } from "~/services/event-service";
import { savePurchase } from "~/services/purchases-service";
import type Event from "~/models/Event";
import type TicketSelection from "~/models/TicketSelection";

export default function Purchase() {
  const { eventId } = useParams<{ eventId: string }>();
  const navigate = useNavigate();

  const [event, setEvent] = useState<Event | null>(null);
  const [isPending, setIsPending] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [selectedSession, setSelectedSession] = useState<number | "">("");
  const [ticketCount, setTicketCount] = useState(0);
  const [tickets, setTickets] = useState<number[]>([]);
  const [selectedDiscountIndex, setSelectedDiscountIndex] = useState<number | "">("");
  const [email, setEmail] = useState("");
  const [emailConfirm, setEmailConfirm] = useState("");

  useEffect(() => {
    if (!eventId) return;
    getEvent(eventId)
      .then(setEvent)
      .catch(() => setError("No se pudo cargar el evento."))
      .finally(() => setIsPending(false));
  }, [eventId]);

  useEffect(() => {
    setTickets((prev) => {
      if (ticketCount > prev.length) {
        return [...prev, ...Array(ticketCount - prev.length).fill(0)];
      }
      return prev.slice(0, ticketCount);
    });
  }, [ticketCount]);

  function updateTicketZone(index: number, zoneId: number) {
    setTickets((prev) => prev.map((t, i) => (i === index ? zoneId : t)));
  }

  function buildSelections(): TicketSelection[] {
    const counts = new Map<number, number>();
    for (const zoneId of tickets) {
      counts.set(zoneId, (counts.get(zoneId) || 0) + 1);
    }
    return Array.from(counts.entries()).map(([zoneID, quantity]) => ({ zoneID, quantity }));
  }

  function calculateTotal(): number {
    if (!event) return 0;

    const baseTotal = tickets.reduce((sum, zoneId) => {
      const zone = event.zones.find((z) => z.id === zoneId);
      return sum + (zone?.price ?? 0);
    }, 0);

    if (selectedDiscountIndex === "") return baseTotal;

    const discount = event.discounts[selectedDiscountIndex];
    if (!discount) return baseTotal;

    if (discount.percentage) {
      return baseTotal * (1 - discount.amount / 100);
    }
    return Math.max(0, baseTotal - discount.amount);
  }

  async function handleSubmit() {
    setSubmitError(null);

    if (!selectedSession) {
      setSubmitError("Debes seleccionar una sesión.");
      return;
    }
    if (ticketCount === 0 || tickets.length === 0) {
      setSubmitError("Debes seleccionar al menos una entrada.");
      return;
    }
    if (tickets.some((zoneId) => !zoneId)) {
      setSubmitError("Todas las entradas deben tener una zona asignada.");
      return;
    }
    if (!email) {
      setSubmitError("Introduce tu correo electrónico.");
      return;
    }
    if (email !== emailConfirm) {
      setSubmitError("Los correos electrónicos no coinciden.");
      return;
    }

    setIsSubmitting(true);
    try {
      const purchase = await savePurchase({
        sessionID: Number(selectedSession),
        selections: buildSelections(),
        name: email,
      });
      navigate(`/public/confirmation/${purchase.purchaseID}`, {
        state: {
          purchase,
          eventName: event?.name,
        },
      });
    } catch {
      setSubmitError("No se pudo procesar la compra. Inténtalo de nuevo.");
    } finally {
      setIsSubmitting(false);
    }
  }

  if (isPending) {
    return (
      <div className="container my-5 text-center">
        <p>Cargando evento...</p>
      </div>
    );
  }

  if (error || !event) {
    return (
      <div className="container my-5 text-center">
        <p className="text-danger">{error ?? "Evento no encontrado."}</p>
        <button className="btn btn-outline-secondary" onClick={() => navigate(-1)}>
          Volver
        </button>
      </div>
    );
  }

  const total = calculateTotal();

  return (
    <div className="container my-5">
      <h2 className="mb-4">Compra de Entradas — {event.name}</h2>

      <div className="mb-3">
        <h4 className="mb-0">Selecciona la sesión:</h4>
        <br />
        <select
          className="form-select w-auto"
          value={selectedSession}
          onChange={(e) => setSelectedSession(Number(e.target.value) || "")}
        >
          <option value="">Selecciona una sesión</option>
          {event.sessions.map((s) => (
            <option key={s.sessionID} value={s.sessionID}>
              {new Date(s.date).toLocaleString("es-ES")}
            </option>
          ))}
        </select>
      </div>

      <div className="d-flex align-items-center gap-3 mb-4">
        <h4 className="mb-0">Número de entradas:</h4>
        <select
          className="form-select w-auto"
          value={ticketCount}
          onChange={(e) => setTicketCount(Number(e.target.value))}
        >
          {[0, 1, 2, 3, 4, 5].map((n) => (
            <option key={n} value={n}>
              {n}
            </option>
          ))}
        </select>
      </div>

      <hr />

      {tickets.length > 0 && (
        <div className="mb-4">
          <h5>Selecciona zona para cada entrada:</h5>
          {tickets.map((zoneId, i) => (
            <div key={i} className="d-flex align-items-center gap-2 mb-2">
              <span className="me-2">Entrada {i + 1}:</span>
              <select
                className="form-select w-auto"
                value={zoneId || ""}
                onChange={(e) => updateTicketZone(i, Number(e.target.value))}
              >
                <option value="">Selecciona una zona</option>
                {event.zones.map((z) => (
                  <option key={z.id} value={z.id}>
                    {z.name} ({z.price}€)
                  </option>
                ))}
              </select>
            </div>
          ))}
        </div>
      )}

      <hr />

      <div className="mb-3">
        <strong>Aplicar descuento:</strong>
        <div className="mt-2">
        <select
          className="form-select w-auto"
          value={selectedDiscountIndex}
          onChange={(e) =>
            setSelectedDiscountIndex(e.target.value === "" ? "" : Number(e.target.value))
          }
        >
          <option value="">Sin descuento</option>
          {event.discounts.map((d, i) => (
            <option key={i} value={i}>
              {d.discountName} —{" "}
              {d.percentage ? `${d.amount}%` : `${d.amount}€`}
            </option>
          ))}
        </select>
        </div>
      </div>

      <hr />

      <div className="col-md-4 mb-4">
        <div className="mb-3">
          <label className="form-label fw-bold">Correo electrónico</label>
          <input
            type="email"
            className="form-control"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div className="mb-3">
          <label className="form-label fw-bold">
            Confirmación del correo electrónico
          </label>
          <input
            type="email"
            className="form-control"
            value={emailConfirm}
            onChange={(e) => setEmailConfirm(e.target.value)}
          />
        </div>
      </div>

      <h4 className="mb-4">
        Total: <span>{total.toFixed(2)}</span> €
      </h4>

      <hr />

      <h4 className="mb-3">Datos de pago</h4>
      <div className="row mb-3">
        <div className="col-md-5">
          <label className="form-label">Número de tarjeta</label>
          <input
            className="form-control"
            placeholder="1234 5678 9012 3456"
            maxLength={19}
          />
        </div>
      </div>
      <div className="d-flex gap-4 mb-4">
        <div>
          <label className="form-label">Mes</label>
          <select className="form-select w-auto">
            {["01","02","03","04","05","06","07","08","09","10","11","12"].map(
              (m) => <option key={m}>{m}</option>
            )}
          </select>
        </div>
        <div>
          <label className="form-label">Año</label>
          <select className="form-select w-auto">
            {["2026","2027","2028","2029"].map((y) => (
              <option key={y}>{y}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="form-label">CVV</label>
          <input
            className="form-control"
            style={{ width: 80 }}
            placeholder="123"
            maxLength={3}
          />
        </div>
      </div>

      {submitError && (
        <div className="alert alert-danger">{submitError}</div>
      )}

      <div className="d-flex justify-content-end gap-2">
        <button
          className="btn btn-outline-primary"
          type="button"
          onClick={() => navigate(-1)}
          disabled={isSubmitting}
        >
          Volver
        </button>
        <button
          className="btn btn-outline-primary"
          type="button"
          onClick={handleSubmit}
          disabled={isSubmitting}
        >
          {isSubmitting ? "Procesando..." : "Confirmar compra"}
        </button>
      </div>
    </div>
  );
}
