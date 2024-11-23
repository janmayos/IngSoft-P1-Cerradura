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
    document.querySelectorAll('.editUserBtn').forEach(button => {
        button.onclick = function() {
            const idActual = this.getAttribute('data-id');
            console.log(idActual);
           // editUser(idActual);
        };
    });

    document.querySelectorAll('.deleteUserBtn').forEach(button => {
        button.onclick = function() {
            const idActual = this.getAttribute('data-id');
            confirmDelete(idActual);
        };
    });

    document.querySelectorAll('.profilePictureBtn').forEach(button => {
        button.onclick = function() {
            const idActual = this.getAttribute('data-id');
            showProfilePictureOptions(idActual);
        };
    });
}

function showProfilePictureOptions(button) {
    const userId = button.getAttribute('data-id');
    const token = localStorage.getItem('token');
    console.log(userId);
    fetch(`/api/profile-picture-blob/admin?userId=${userId}`, {
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
        Swal.fire({
            title: 'Actualizar Foto de Perfil',
            html: `
                <div class="flex justify-center mb-4">
                    <img src="${url}" alt="Profile Picture" class="w-32 h-32 rounded-full object-cover">
                </div>
                <input type="file" id="profilePictureInput" class="swal2-file">
                <button id="deleteProfilePictureBtn" class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out mt-4">Eliminar Foto de Perfil</button>
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
                uploadProfilePicture(result.value, userId);
            }
        });

        document.getElementById('deleteProfilePictureBtn').onclick = function() {
            deleteProfilePicture(userId);
        };
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire({
            title: 'Actualizar Foto de Perfil',
            html: `
                <input type="file" id="profilePictureInput" class="swal2-file">
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
                uploadProfilePicture(result.value, userId);
            }
        });
    });
}

function uploadProfilePicture(file, userId) {
    const token = localStorage.getItem('token');
    const formData = new FormData();
    formData.append('file', file);
    console.log(userId);

    fetch(`/api/profile-picture/admin?userId=${userId}`, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        body: formData
    })
    .then(response => {
        if (response.ok) {
            Swal.fire('Éxito', 'Foto de perfil actualizada', 'success').then(() => {
                location.reload();
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

function deleteProfilePicture(userId) {
    const token = localStorage.getItem('token');

    fetch(`/api/profile-picture/admin?userId=${userId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (response.ok) {
            Swal.fire('Éxito', 'Foto de perfil eliminada', 'success').then(() => {
                location.reload();
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

function editUser(button) {
    const userId = button.getAttribute('data-id');
    localStorage.setItem('idModificar', userId);
    
    window.location.href = "/admin/usuarios/editarTablaPublica";
}

function confirmDelete(button) {

    const idActual = button.getAttribute('data-id');
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
                text: "El usuario ha sido eliminado.",
                icon: "success",
                customClass: {
                    popup: 'dark:bg-gray-800 dark:text-white',
                    confirmButton: 'bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300 ease-in-out'
                },
                didDestroy: () => {
                    location.reload();
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
                    text: "Ocurrió un error al eliminar el usuario.",
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