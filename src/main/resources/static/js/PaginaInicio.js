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
    $.ajax({
        url: window.location.origin + "/api/usuarios/eliminar/" + userId,
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem('token')
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