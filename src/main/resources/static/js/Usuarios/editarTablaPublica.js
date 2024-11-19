document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    const idModificar = localStorage.getItem('idModificar');

    if (token && idModificar) {
        fetch('/admin/usuarios/editarTablaContenido', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({ idModificar: idModificar })
        })
        .then(response => response.text())
        .then(html => {
            document.getElementById('content').innerHTML = html;
        })
        .catch(error => {
            console.error('Error:', error);
            window.location.href = "/formlogin";
        });
    } else {
        window.location.href = "/formlogin";
    }
});