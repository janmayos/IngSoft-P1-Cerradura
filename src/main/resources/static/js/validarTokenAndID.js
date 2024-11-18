window.onload = function() {
    const token = localStorage.getItem('token');
    const id = localStorage.getItem('id');

    if (!token || !id) {
        window.location.href = "/formlogin";
    }
};