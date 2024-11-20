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
            // Cargar el script de editarTabla.js después de insertar el contenido dinámico
            const script = document.createElement('script');
            script.src = '/js/Usuarios/editarTabla.js';
            document.body.appendChild(script);
        })
        .catch(error => {
            console.error('Error:', error);
            window.location.href = "/formlogin";
        });
    } else {
        window.location.href = "/formlogin";
    }
});

// function editUser(button) {
//     const userId = button.getAttribute('data-id');
//     localStorage.setItem('idModificar', userId);
//     console.log(userId);
//     //window.location.href = "/admin/usuarios/editarTablaPublica";
// }