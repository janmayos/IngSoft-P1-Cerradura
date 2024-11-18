// src/main/resources/static/js/paginaDeInicio.js
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');

    if (token) {
        fetch('/PaginaInicioContenido', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({ token: token })
        })
        .then(response => response.text())
        .then(html => {
            document.body.innerHTML = html;
            initializePage();
        })
        .catch(error => {
            console.error('Error:', error);
            window.location.href = "/formlogin";
        });
    } else {
        window.location.href = "/formlogin";
    }
});

function initializePage() {
    document.getElementById("deleteUserBtn").onclick = function() {
        confirmDelete();
    };
    document.getElementById("logoutBtn").onclick = function() {
        confirmLogout();
    };
}

function confirmLogout() {
    Swal.fire({
        title: '¿Cerrar sesión?',
        text: "¡Se cerrará tu sesión actual!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, cerrar sesión'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.clear();
            window.location.href = "/formlogin";
        }
    });
}

function confirmDelete() {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¡No podrás revertir esto!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminarlo'
    }).then((result) => {
        if (result.isConfirmed) {
            deleteUser();
        }
    });
}

function deleteUser() {
    const token = localStorage.getItem('token');
    const id = localStorage.getItem('id');
    $.ajax({
        url: window.location.origin + "/api/usuarios/eliminar/" + id,
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: () => {
            Swal.fire({
                title: "Eliminado!",
                text: "Tu cuenta ha sido eliminada.",
                icon: "success",
                didDestroy: () => {
                    localStorage.clear();
                    window.location.href = "/formlogin";
                }
            });
        },
        error: (respServ) => {
            console.log('Error:', respServ.status);
            if (respServ.status == 403) {
                Swal.fire({
                    title: "Error",
                    text: "No tienes permiso para realizar esta acción.",
                    icon: "error"
                });
            } else {
                Swal.fire({
                    title: "Error",
                    text: "Ocurrió un error al eliminar la cuenta.",
                    icon: "error"
                });
            }
        }
    });
}