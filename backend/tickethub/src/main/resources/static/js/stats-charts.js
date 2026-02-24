document.addEventListener('DOMContentLoaded', function() {
    const dataStore = document.getElementById('data-store');
    
    // Extraer y limpiar los datos del HTML
    const getList = (attr) => dataStore.getAttribute(attr).split(',').filter(x => x !== "");

    const rankingLabels = getList('data-ranking-labels');
    const rankingValues = getList('data-ranking-values').map(Number);
    const evolutionLabels = getList('data-evolution-labels');
    const evolutionValues = getList('data-evolution-values').map(Number);

    // --- GRÁFICO 2: Ranking ---
    new Chart(document.getElementById('chart2'), {
        type: 'bar',
        data: {
            labels: rankingLabels,
            datasets: [{
                label: 'Tickets Vendidos',
                data: rankingValues,
                backgroundColor: '#3498db'
            }]
        },
        options: { indexAxis: 'y' }
    });

    // --- GRÁFICO 3: Evolución ---
    new Chart(document.getElementById('chart3'), {
        type: 'line',
        data: {
            labels: evolutionLabels,
            datasets: [{
                label: 'Total Tickets',
                data: evolutionValues,
                borderColor: '#2ecc71',
                fill: true,
                backgroundColor: 'rgba(46, 204, 113, 0.1)',
                tension: 0.3
            }]
        }
    });

    // --- LÓGICA PARA GRÁFICO 1 ---
    const rawMonthEvent = dataStore.getAttribute('data-month-event').split(',').filter(x => x !== "");
    const formattedData = rawMonthEvent.map(item => {
        const [mes, evento, cant] = item.split('|');
        return { mes, evento, cant: Number(cant) };
    });

    const meses = [...new Set(formattedData.map(d => d.mes))];
    const eventos = [...new Set(formattedData.map(d => d.evento))];

    const datasets = eventos.map((evt, i) => ({
        label: evt,
        data: meses.map(m => {
            const found = formattedData.find(r => r.mes === m && r.evento === evt);
            return found ? found.cant : 0;
        }),
        backgroundColor: `hsl(${i * (360 / eventos.length)}, 70%, 60%)`
    }));

    new Chart(document.getElementById('chart1'), {
        type: 'bar',
        data: { labels: meses, datasets: datasets },
        options: { responsive: true, scales: { y: { beginAtZero: true } } }
    });
});