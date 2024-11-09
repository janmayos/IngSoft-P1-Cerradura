$(document).ready(() => {
  console.log("ready!");
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
      // Realiza la solicitud AJAX al backend para hacer el login
      $.ajax({
        url: window.location.origin + "/auth/login", // Endpoint de login
        method: "POST",
        data: {
          userU: $("#userU").val(),
          passwordU: $("#passwordU").val()
        },
        cache: false,

        success: (respServ) => {
          //console.log(respServ.status);
          //console.log(respServ);

          // Verifica si la respuesta contiene un token
          if (respServ) {
            // Almacena el token en localStorage
            localStorage.setItem('jwt_token', respServ);
            
            // Almacena la información del usuario en localStorage
            localStorage.setItem('nombre', respServ.nombre); // Almacena el objeto usuarioDTO

            // Muestra mensaje de éxito con Swal
            Swal.fire({
              title: "Exito!!",
              text: "Bienvenido " + respServ.nombre, // Muestra el nombre del usuario desde la respuesta
              icon: "success",
              didDestroy: () => {
                // Redirige a la página de inicio después de login exitoso
                window.location.href = window.location.origin + "/PaginaInicio";
              }
            });
          } 
        },

        error: (respServ) => {


          if (respServ.status == 401) {
             // Si la respuesta no contiene el token
              Swal.fire({
              title: "Upps..",
              text: "Credenciales inválidas",
              icon: "error",
              didDestroy: () => {
                location.reload(); // Recarga la página si las credenciales son incorrectas
              }
            });
          }else{
            // Manejo de error si ocurre algo en la solicitud
          console.log("Error", respServ.status);
          console.log("Error details:", respServ.responseText);

          Swal.fire({
            title: "Upps..",
            text: "Hubo un error al procesar tu solicitud",
            icon: "error",
            didDestroy: () => {
              location.reload(); // Recarga la página si hay un error
            }
          });

          }
          
        }
      });
    });
});
