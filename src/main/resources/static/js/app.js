document.addEventListener('DOMContentLoaded', function () {

    // Hamburger menu - collapses the sidebar to icon-only width
    var menuToggle = document.querySelector('.ff-menu-toggle');
    if (menuToggle) {
        menuToggle.addEventListener('click', function () {
            document.body.classList.toggle('ff-sidebar-collapsed');
        });
    }

    // Notification bell - placeholder feedback until real notifications exist
    var bell = document.querySelector('.ff-icon-btn');
    if (bell) {
        bell.addEventListener('click', function () {
            alert('You have no new notifications yet.');
        });
    }

});
