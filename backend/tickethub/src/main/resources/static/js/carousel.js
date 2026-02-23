document.addEventListener("DOMContentLoaded", function() {
    const carouselEl = document.querySelector('#carouselEvent');
    
    if (carouselEl) {
        const items = carouselEl.querySelectorAll('.carousel-item');
        
        if (items.length > 0) {
            items[0].classList.add('active');
            
            new bootstrap.Carousel(carouselEl, {
                interval: 3000,
                ride: 'carousel'
            });
            console.log("Carrusel inicializado correctamente.");
        }
    }
});