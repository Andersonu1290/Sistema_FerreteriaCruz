const techPalette = [
    '#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', 
    '#ec4899', '#06b6d4', '#f97316', '#14b8a6', '#6366f1'
];

document.addEventListener("DOMContentLoaded", async function() {
    if (!sessionStorage.getItem('usuarioActivo')) {
        window.location.href = 'login';
        return;
    }

    await cargarDatosDashboard();
});

async function cargarDatosDashboard() {
    try {
        const dashboardInfo = await API.get('/reportes/dashboard');

        // --- A. ACTUALIZAR KPIs NUMÉRICOS ---
        document.getElementById('kpiIngresos').textContent = formatMoneda(dashboardInfo.ingresosTotales);
        document.getElementById('kpiTotalStock').textContent = dashboardInfo.kpis.totalStock;
        document.getElementById('kpiTotalVentas').textContent = dashboardInfo.kpis.totalVentas;
        document.getElementById('kpiTotalMermas').textContent = dashboardInfo.kpis.totalMermas;
        document.getElementById('kpiStockCritico').textContent = dashboardInfo.kpis.stockCritico;

        // --- B. RENDERIZAR TABLA DE AUDITORÍA EN VIVO ---
        renderizarTablaVentas(dashboardInfo.ultimasVentas);

        // --- C. PROCESAR DATOS Y RENDERIZAR GRÁFICOS ---
        const topProd = dashboardInfo.topProd || []; 
        const catStock = dashboardInfo.catStock || []; 
            
        const diccionarioColores = {};
        
        // 1. Procesar Categorías (Gráfico de Dona) - ESTO FALTABA EN TU ERROR
        const catLabels = [];
        const catData = [];
        const catColors = [];

        catStock.forEach((item, index) => {
            const [categoria, stock] = item.split('||');
            catLabels.push(categoria);
            catData.push(parseInt(stock || 0));

            const colorAsignado = techPalette[index % techPalette.length];
            diccionarioColores[categoria] = colorAsignado; 
            catColors.push(colorAsignado);
        });

        // 2. Procesar Top Productos (Gráfico de Barras con nombres acortados)
        const topLabels = [];
        const topData = [];
        const topColors = [];

        topProd.forEach(item => {
            const [productoFull, ventas, categoria] = item.split('||');
            
            // ✂️ Cortamos el nombre para que el gráfico se vea elegante
            let productoCorto = productoFull.split(' ')[0];
            
            if (productoCorto.length <= 4 && productoFull.split(' ').length > 1) {
                productoCorto += " " + productoFull.split(' ')[1];
            }
            if (productoCorto.length > 12) {
                productoCorto = productoCorto.substring(0, 12) + '...';
            }

            topLabels.push(productoCorto);
            topData.push(parseInt(ventas || 0));

            const colorHeredado = diccionarioColores[categoria] || '#9ca3af';
            topColors.push(colorHeredado);
        });
            
        // Inicializamos ambos gráficos con las variables ya definidas
        inicializarGraficos(topLabels, topData, topColors, catLabels, catData, catColors);

    } catch (error) {
        console.error("Error al cargar las métricas del dashboard:", error);
        mostrarNotificacion("Error al obtener los datos del Dashboard.", "error");
    }
}

function renderizarTablaVentas(ventas) {
    const tbody = document.getElementById('tbodyAuditoriaVentas');
    if (!tbody) return;

    if (!ventas || ventas.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center p-30 text-muted">
                    No hay transacciones recientes para mostrar.
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = "";
    
    // Mostramos las ventas consolidadas (Web y Física)
    ventas.forEach(v => {
        const anulada = v.estado === 'ANULADA' || v.estado === 'CANCELADO';
        const rowClass = anulada ? "row-disabled" : "";
        
        let badge = '';
        if (anulada) {
            badge = '<span class="badge badge-alert">ANULADA</span>';
        } else if (v.estado === 'COMPLETADA' || v.estado === 'ENTREGADO') {
            badge = '<span class="badge badge-optimal">COMPLETADA</span>';
        } else {
            // Para pedidos WEB que están en proceso
            badge = `<span class="badge" style="background:rgba(59,130,246,0.2); color:#60a5fa; border:1px solid #3b82f6;">${v.estado}</span>`;
        }

        tbody.innerHTML += `
            <tr class="${rowClass}">
                <td class="text-muted text-xs">${formatFecha(v.fecha)}</td>
                <td class="font-mono text-sm">${v.nroComprobante}</td>
                <td class="td-nombre">${v.nombreCliente}</td>
                <td class="text-sm">${v.nombreProducto}</td>
                <td class="font-mono text-success font-bold">${formatMoneda(v.total)}</td>
                <td class="text-center">${badge}</td>
            </tr>
        `;
    });
}

function inicializarGraficos(topLabels, topData, topColors, catLabels, catData, catColors) {
    // 🌙 DETECTAMOS SI EL MODO NOCHE ESTÁ ACTIVO
    const isDark = document.documentElement.classList.contains('dark');
    
    // Asignamos colores dinámicos dependiendo del tema
    const textColor = isDark ? '#9ca3af' : '#475569'; // Gris claro en noche, gris oscuro en día
    const gridColor = isDark ? '#374151' : '#e2e8f0'; // Líneas oscuras en noche, líneas suaves en día
    const donutBorder = isDark ? '#1e293b' : '#ffffff'; // Borde oscuro en noche, borde blanco en día
    
    Chart.defaults.color = textColor;
    Chart.defaults.font.family = "'Inter', sans-serif";

    if (window.miBarChart) window.miBarChart.destroy();
    if (window.miDoughnutChart) window.miDoughnutChart.destroy();

    const ctxBar = document.getElementById('barChart');
    if (ctxBar) {
        window.miBarChart = new Chart(ctxBar.getContext('2d'), {
            type: 'bar',
            data: {
                labels: topLabels,
                datasets: [{
                    label: 'Unidades Vendidas',
                    data: topData,
                    backgroundColor: topColors,
                    borderWidth: 1,
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { 
                        beginAtZero: true, 
                        grid: { color: gridColor }, // 🎨 Color dinámico para la cuadrícula
                        ticks: { stepSize: 1, color: textColor } // 🎨 Color dinámico para los números
                    },
                    x: { 
                        grid: { display: false },
                        ticks: { color: textColor } // 🎨 Color dinámico para los nombres abajo
                    }
                }
            }
        });
    }

    const ctxDoughnut = document.getElementById('doughnutChart');
    if (ctxDoughnut) {
        window.miDoughnutChart = new Chart(ctxDoughnut.getContext('2d'), {
            type: 'doughnut',
            data: {
                labels: catLabels,
                datasets: [{
                    data: catData,
                    backgroundColor: catColors,
                    borderColor: donutBorder, // 🎨 Borde dinámico
                    borderWidth: 2,
                    hoverOffset: 12
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { 
                        position: 'bottom', 
                        labels: { color: textColor, padding: 15, boxWidth: 12 } // 🎨 Nombres de categorías abajo
                    }
                },
                cutout: '65%'
            }
        });
    }
}

// 💡 TRUCO PRO: Escuchamos cuando el usuario hace clic en el botón de tema 
// para repintar los gráficos instantáneamente sin recargar la página.
document.addEventListener('click', (e) => {
    if (e.target.closest('.btn-flotante-tema')) {
        setTimeout(cargarDatosDashboard, 50); // Recargamos el chart después de que Tailwind cambie las clases
    }
});