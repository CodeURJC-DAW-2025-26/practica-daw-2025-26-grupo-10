document.addEventListener('DOMContentLoaded', function() {
    const dataStore = document.getElementById('data-store');
    
    const getList = (attr) => dataStore.getAttribute(attr).split(',').filter(x => x !== "");

    const rankingLabels = getList('data-ranking-labels');
    const rankingValues = getList('data-ranking-values').map(Number);
    const evolutionLabels = getList('data-evolution-labels');
    const evolutionValues = getList('data-evolution-values').map(Number);

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

    const rawMonthEvent = dataStore.getAttribute('data-month-event').split(',').filter(x => x !== "");
    const formattedData = rawMonthEvent.map(item => {
        const [month, event, quantity] = item.split('|');
        return { month, event, quantity: Number(quantity) };
    });

    const months = [...new Set(formattedData.map(d => d.month))];
    const events = [...new Set(formattedData.map(d => d.event))];

    const datasets = events.map((evt, i) => ({
        label: evt,
        data: months.map(m => {
            const found = formattedData.find(r => r.month === m && r.event === evt);
            return found ? found.quantity : 0;
        }),
        backgroundColor: `hsl(${i * (360 / events.length)}, 70%, 60%)`
    }));

    new Chart(document.getElementById('chart1'), {
        type: 'bar',
        data: { labels: months, datasets: datasets },
        options: { responsive: true, scales: { y: { beginAtZero: true } } }
    });
});