window.onload = function() {
    // document.getElementById("editUserBtn").onclick = function() {
    //     // Aquí puedes agregar la funcionalidad para editar el usuario
    // };
    document.getElementById("deleteUserBtn").onclick = function() {
        confirmDelete();
    };
    document.getElementById("logoutBtn").onclick = function() {
        confirmLogout();
    };
};

function viewAllUsers() {
    const token = localStorage.getItem('token');
    if (token) {
        fetch('/admin/usuarios', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.href = '/admin/usuarios';
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