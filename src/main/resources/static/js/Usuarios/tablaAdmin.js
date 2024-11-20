document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');

    if (token) {
        fetch('/admin/usuarios/contenido', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({ token: token })
        })
        .then(response => response.text())
        .then(html => {
            document.getElementById('content').innerHTML = html;
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
    document.querySelectorAll('button[data-action="delete"]').forEach(button => {
        button.onclick = function() {
            confirmDelete(this);
        };
    });
}

function editUser(button) {
    const userId = button.getAttribute('data-id');
    localStorage.setItem('idModificar', userId);
    console.log(userId);
    window.location.href = "/admin/usuarios/editarTablaPublica";
}

function confirmDelete(button) {
    const userId = button.getAttribute('data-id');
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
                text: "El usuario ha sido eliminado.",
                icon: "success",
                didDestroy: () => {
                    location.reload();
                }
            });
        },
        error: (respServ) => {
            Swal.fire({
                title: "Error",
                text: "Hubo un problema al eliminar el usuario.",
                icon: "error"
            });
        }
    });
}