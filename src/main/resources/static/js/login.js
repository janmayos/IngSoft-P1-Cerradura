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
              didDestroy: () => {
                window.location.href = window.location.origin + "/paginaDeInicio";
              }
            });
          } else {
            Swal.fire({
              title: "Upps..",
              text: "No se recibió el token de autenticación",
              icon: "error",
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
            didDestroy: () => {
              location.reload();
            }
          });
        }
      });
    });
});
