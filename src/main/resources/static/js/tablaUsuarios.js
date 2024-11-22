// src/main/resources/static/js/tablaUsuarios.js
function showProfilePictureOptions(button) {
    const userId = button.getAttribute('data-id');
    const token = localStorage.getItem('token');

    fetch(`/api/profile-picture-blob?userId=${userId}`, {
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
                <img src="${url}" alt="Profile Picture" class="w-32 h-32 rounded-full mb-4 object-cover">
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
                uploadProfilePicture(result.value, userId);
            }
        });

        document.getElementById('deleteProfilePictureBtn').onclick = function() {
            deleteProfilePicture(userId);
        };
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire('Error', 'No se pudo cargar la foto de perfil', 'error');
    });
}

function uploadProfilePicture(file, userId) {
    const token = localStorage.getItem('token');
    const formData = new FormData();
    formData.append('file', file);

    fetch(`/api/profile-picture?userId=${userId}`, {
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

    fetch(`/api/profile-picture?userId=${userId}`, {
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