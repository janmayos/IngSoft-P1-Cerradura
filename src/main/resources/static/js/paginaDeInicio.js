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

    document.getElementById("updateProfilePictureBtn").onclick = function() {
        showProfilePictureOptions();
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
        document.getElementById('deleteProfilePictureBtn').disabled = false;
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('deleteProfilePictureBtn').disabled = true;
    });
}

function showProfilePictureOptions() {
    Swal.fire({
        title: 'Actualizar Foto de Perfil',
        html: `
            <input type="file" id="profilePictureInput" class="swal2-file">
            <button id="deleteProfilePictureBtn" class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out">Eliminar Foto de Perfil</button>
        `,
        showCancelButton: true,
        confirmButtonText: 'Subir',
        customClass: {
            popup: 'dark:bg-gray-800 dark:text-white',
            confirmButton: 'bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300 ease-in-out',
            cancelButton: 'bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition duration-300 ease-in-out'
        },
        preConfirm: () => {
            const fileInput = document.getElementById('profilePictureInput');
            if (fileInput.files.length > 0) {
                return fileInput.files[0];
            }
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            uploadProfilePicture(result.value);
        }
    });

    document.getElementById('deleteProfilePictureBtn').onclick = function() {
        deleteProfilePicture();
    };
}

function uploadProfilePicture(file) {
    const token = localStorage.getItem('token');
    const formData = new FormData();
    formData.append('file', file);

    fetch('/api/profile-picture', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        body: formData
    })
    .then(response => {
        if (response.ok) {
            Swal.fire('Éxito', 'Foto de perfil actualizada', 'success').then(() => {
                loadProfilePicture();
            });
        } else {
            throw new Error('Failed to upload profile picture');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire('Error', 'No se pudo actualizar la foto de perfil', 'error');
    });
}

function deleteProfilePicture() {
    const token = localStorage.getItem('token');

    fetch('/api/profile-picture', {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (response.ok) {
            Swal.fire('Éxito', 'Foto de perfil eliminada', 'success').then(() => {
                loadProfilePicture();
            });
        } else {
            throw new Error('Failed to delete profile picture');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire('Error', 'No se pudo eliminar la foto de perfil', 'error');
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
        confirmButtonText: 'Sí, cerrar sesión',
        customClass: {
            popup: 'dark:bg-gray-800 dark:text-white',
            confirmButton: 'bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300 ease-in-out',
            cancelButton: 'bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition duration-300 ease-in-out'
        }
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
        confirmButtonText: 'Sí, eliminarlo',
        customClass: {
            popup: 'dark:bg-gray-800 dark:text-white',
            confirmButton: 'bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out',
            cancelButton: 'bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition duration-300 ease-in-out'
        }
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
                customClass: {
                    popup: 'dark:bg-gray-800 dark:text-white',
                    confirmButton: 'bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300 ease-in-out'
                },
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
                    icon: "error",
                    customClass: {
                        popup: 'dark:bg-gray-800 dark:text-white',
                        confirmButton: 'bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out'
                    }
                });
            } else {
                Swal.fire({
                    title: "Error",
                    text: "Ocurrió un error al eliminar la cuenta.",
                    icon: "error",
                    customClass: {
                        popup: 'dark:bg-gray-800 dark:text-white',
                        confirmButton: 'bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out'
                    }
                });
            }
        }
    });
}