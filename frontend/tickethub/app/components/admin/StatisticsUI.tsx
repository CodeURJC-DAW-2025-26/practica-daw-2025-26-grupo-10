import { useEffect, useRef } from "react";
import Chart from "chart.js/auto";
import { Link } from "react-router";
import { Container, Row, Col } from "react-bootstrap";
import type { AdminStatistics } from "~/models/AdminStatistics";

interface Props {
    data: AdminStatistics;
}

export default function StatisticsCharts({ data }: Props) {
    const chart1Ref = useRef<HTMLCanvasElement>(null);
    const chart2Ref = useRef<HTMLCanvasElement>(null);
    const chart3Ref = useRef<HTMLCanvasElement>(null);
    const chart1Instance = useRef<Chart | null>(null);
    const chart2Instance = useRef<Chart | null>(null);
    const chart3Instance = useRef<Chart | null>(null);

    useEffect(() => {
        if (chart2Ref.current) {
            chart2Instance.current?.destroy();
            chart2Instance.current = new Chart(chart2Ref.current, {
                type: "bar",
                data: {
                    labels: data.rankingLabels,
                    datasets: [{ label: "Tickets Vendidos", data: data.rankingValues, backgroundColor: "#3498db" }],
                },
                options: { indexAxis: "y" },
            });
        }

        if (chart3Ref.current) {
            chart3Instance.current?.destroy();
            chart3Instance.current = new Chart(chart3Ref.current, {
                type: "line",
                data: {
                    labels: data.evolutionLabels,
                    datasets: [{
                        label: "Total Tickets",
                        data: data.evolutionValues,
                        borderColor: "#2ecc71",
                        fill: true,
                        backgroundColor: "rgba(46, 204, 113, 0.1)",
                        tension: 0.3,
                    }],
                },
            });
        }

        if (chart1Ref.current) {
            const months = [...new Set(data.monthEventData.map(d => d[0]))];
            const events = [...new Set(data.monthEventData.map(d => d[1]))];
            const datasets = events.map((evt, i) => ({
                label: evt,
                data: months.map(m => {
                    const found = data.monthEventData.find(r => r[0] === m && r[1] === evt);
                    return found ? found[2] : 0;
                }),
                backgroundColor: `hsl(${i * (360 / events.length)}, 70%, 60%)`,
            }));
            chart1Instance.current?.destroy();
            chart1Instance.current = new Chart(chart1Ref.current, {
                type: "bar",
                data: { labels: months, datasets },
                options: { responsive: true, scales: { y: { beginAtZero: true } } },
            });
        }

        return () => {
            chart1Instance.current?.destroy();
            chart2Instance.current?.destroy();
            chart3Instance.current?.destroy();
        };
    }, [data]);

    return (
        <Container as="main" className="my-5 flex-grow-1">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2 className="mb-0">Panel de Estadísticas</h2>
                <Link to="/admin" className="btn btn-outline-primary">Volver</Link>
            </div>
            <Row className="g-4">
                <Col xs={12} className="border p-3 bg-light">
                    <h4>Ventas Mensuales por Evento</h4>
                    <canvas ref={chart1Ref} style={{ maxHeight: "400px" }} />
                </Col>
                <Col md={6} className="border p-3 bg-light">
                    <h4>Ranking de Eventos Más Vendidos</h4>
                    <canvas ref={chart2Ref} />
                </Col>
                <Col md={6} className="border p-3 bg-light">
                    <h4>Evolución Total de Ventas</h4>
                    <canvas ref={chart3Ref} />
                </Col>
            </Row>
        </Container>
    );
}
