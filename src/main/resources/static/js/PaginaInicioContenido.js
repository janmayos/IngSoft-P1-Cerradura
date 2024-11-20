// window.onload = function() {
//     console.log("Pagina de inicio contenido");
//     document.getElementById("deleteUserBtn").onclick = function() {
//         confirmDelete();
//     };
//     document.getElementById("logoutBtn").onclick = function() {
//         confirmLogout();
//     };
//     document.querySelectorAll('button[data-action="edit"]').forEach(button => {
//         button.onclick = function() {
//             const id = this.getAttribute('data-id');
//             localStorage.setItem('idModificar', id);
//             window.location.href = "/admin/usuarios/editarTablaPublica";
//         };
//     });
// };
// z
// function viewAllUsers() {
//     const token = localStorage.getItem('token');
//     if (token) {
//         fetch('/admin/usuarios', {
//             method: 'GET',
//             headers: {
//                 'Authorization': 'Bearer ' + token
//             }
//         })
//         .then(response => {
//             if (response.ok) {
//                 window.location.href = '/admin/usuarios';
//             } else {
//                 Swal.fire({
//                     title: "Error",
//                     text: "No tienes permiso para ver esta página.",
//                     icon: "error"
//                 });
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             Swal.fire({
//                 title: "Error",
//                 text: "Hubo un problema al procesar tu solicitud.",
//                 icon: "error"
//             });
//         });
//     } else {
//         window.location.href = '/formlogin';
//     }
// }

// function confirmDelete() {
//     console.log("Confirmar eliminación");
//     Swal.fire({
//         title: '¿Estás seguro?',
//         text: "¡No podrás revertir esto!",
//         icon: 'warning',
//         showCancelButton: true,
//         confirmButtonColor: '#3085d6',
//         cancelButtonColor: '#d33',
//         confirmButtonText: 'Sí, eliminarlo'
//     }).then((result) => {
//         if (result.isConfirmed) {
//             deleteUser();
//         }
//     });
// }

// function deleteUser() {
//     const token = localStorage.getItem('token');
//     const id = localStorage.getItem('id');
//     console.log("Eliminar usduario con id: " + id);
//     $.ajax({
//         url: window.location.origin + "/api/usuarios/eliminar/" + id,
//         method: "DELETE",
//         headers: {
//             "Authorization": "Bearer " + token
//         },
//         success: () => {
//             Swal.fire({
//                 title: "Eliminado!",
//                 text: "Tu cuenta ha sido eliminada.",
//                 icon: "success",
//                 didDestroy: () => {
//                     localStorage.clear();
//                     window.location.href = "/formlogin";
//                 }
//             });
//         },
//         error: (respServ) => {
//             console.log('Error:', respServ.status);
//             if (respServ.status == 403) {
//                 Swal.fire({
//                     title: "Error",
//                     text: "No tienes permiso para realizar esta acción.",
//                     icon: "error"
//                 });
//             } else {
//                 Swal.fire({
//                     title: "Error",
//                     text: "Ocurrió un error al eliminar la cuenta.",
//                     icon: "error"
//                 });
//             }
//         }
//     });
// }

// function confirmLogout() {
//     Swal.fire({
//         title: '¿Cerrar sesión?',
//         text: "¡Se cerrará tu sesión actual!",
//         icon: 'warning',
//         showCancelButton: true,
//         confirmButtonColor: '#3085d6',
//         cancelButtonColor: '#d33',
//         confirmButtonText: 'Sí, cerrar sesión'
//     }).then((result) => {
//         if (result.isConfirmed) {
//             localStorage.clear();
//             window.location.href = "/formlogin";
//         }
//     });
// }