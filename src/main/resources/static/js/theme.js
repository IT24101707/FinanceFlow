// Applies the saved dark/light mode preference immediately, before the page paints,
// so there's no flash of the wrong theme. Included as an early, blocking <script> in <head>.
(function () {
    try {
        if (localStorage.getItem('ff-theme') === 'dark') {
            document.documentElement.classList.add('ff-dark');
        }
    } catch (e) {
        // localStorage unavailable (e.g. private browsing) - just fall back to light mode
    }
})();
