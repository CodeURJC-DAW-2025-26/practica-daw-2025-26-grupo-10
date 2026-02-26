
const carousel = document.querySelector('#carouselEvent');

if (carousel) {
    const items = carousel.querySelectorAll('.carousel-item');
    if (items.length > 0) {
        items[0].classList.add('active');
        
        new bootstrap.Carousel(carousel, {
            interval: 3000,
            ride: 'carousel'
        });
    }
}