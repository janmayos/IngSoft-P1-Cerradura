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
            loadProfilePicture();
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
        const idActual = this.getAttribute('data-id');
        confirmDelete(idActual);
    };

    document.getElementById("logoutBtn").onclick = function() {
        confirmLogout();
    };
}

function loadProfilePicture() {
    const token = localStorage.getItem('token');

    fetch(`/api/profile-picture-blob`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (response.ok) {
            return response.blob();
        } else {
            throw new Error('Failed to fetch profile picture');
        }
    })
    .then(blob => {
        const url = URL.createObjectURL(blob);
        const img = document.getElementById('profile-picture');
        const defaultIcon = document.getElementById('default-profile-icon');
        img.src = url;
        img.classList.remove('hidden');
        defaultIcon.classList.add('hidden');
    })
    .catch(error => {
        console.error('Error:', error);
        // No need to change the src as it already has the default image
    });
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

function confirmDelete(idActual) {
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
            deleteUser(idActual);
        }
    });
}

function deleteUser(idActual) {
    const token = localStorage.getItem('token');
    $.ajax({
        url: window.location.origin + "/api/usuarios/eliminar/" + idActual,
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