// 1. Toggle Sidebar
document.addEventListener('click', event => {
    const toggleBtn = event.target.closest('#sidebarToggle');
    if (toggleBtn) { 
        event.preventDefault();
        document.querySelector('#bdSidebar')?.classList.toggle('toggled'); 
    }
});
