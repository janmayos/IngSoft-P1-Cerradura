$(document).ready(() => {
  const validaLogin = new JustValidate("#formLogin");

  validaLogin
    .addField("#userU", [
      { rule: "required", errorMessage: "Falta tu usuario" },
    ])
    .addField("#passwordU", [
      { rule: "required", errorMessage: "Falta tu contraseña" },
    ])
    .onSuccess((event) => {
      event.preventDefault();
      $.ajax({
        url: window.location.origin + "/auth/login",
        method: "POST",
        data: {
          userU: $("#userU").val(),
          passwordU: $("#passwordU").val()
        },
        cache: false,
        success: (respServ) => {
          if (respServ.token) {
            localStorage.setItem('token', respServ.token);
            Swal.fire({
              title: "Exito!!",
              text: "Bienvenido " + respServ.nombre,
              icon: "success",
              customClass: {
                popup: 'dark:bg-gray-800 dark:text-white',
                icon: 'dark:bg-gray-800',
                confirmButton: 'bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out'
              },
              didDestroy: () => {
                window.location.href = window.location.origin + "/paginaDeInicio";
              }
            });
          } else {
            Swal.fire({
              title: "Upps..",
              text: "No se recibió el token de autenticación",
              icon: "error",
              customClass: {
                popup: 'dark:bg-gray-800 dark:text-white',
                confirmButton: 'bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out'
              },
              didDestroy: () => {
                location.reload();
              }
            });
          }
        },
        error: (respServ) => {
          Swal.fire({
            title: "Upps..",
            text: "Credenciales inválidas",
            icon: "error",
            customClass: {
              popup: 'dark:bg-gray-800 dark:text-white',
              confirmButton: 'bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 transition duration-300 ease-in-out'
            },
            didDestroy: () => {
              location.reload();
            }
          });
        }
      });
    });
});
