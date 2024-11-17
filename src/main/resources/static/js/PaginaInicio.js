window.onload = function() {
    const token = localStorage.getItem('token');
    const id = localStorage.getItem('id');

    if (token && id) {
        document.getElementById("editUserBtn").href = "/vista/usuarios/editarInicio/" + id;
        document.getElementById("deleteUserBtn").onclick = function() {
            confirmDelete(id);
        };
        document.getElementById("logoutBtn").onclick = function() {
            confirmLogout();
        };

    } else {
        window.location.href = "/formlogin";
    }
};

function viewAllUsers() {
    const token = localStorage.getItem('token');
    if (token) {
        fetch('/vista/usuarios', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.href = '/vista/usuarios';
            } else {
                Swal.fire({
                    title: "Error",
                    text: "No tienes permiso para ver esta página.",
                    icon: "error"
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                title: "Error",
                text: "Hubo un problema al procesar tu solicitud.",
                icon: "error"
            });
        });
    } else {
        window.location.href = '/formlogin';
    }
}

function confirmDelete(userId) {
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
            deleteUser(userId);
        }
    });
}

function deleteUser(userId) {
    const token = localStorage.getItem('token');
    $.ajax({
        url: window.location.origin + "/api/usuarios/eliminar/" + userId,
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
            Swal.fire({
                title: "Error",
                text: "Hubo un problema al eliminar tu cuenta.",
                icon: "error"
            });
        }
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