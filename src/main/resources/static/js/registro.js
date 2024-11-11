$(document).ready(() => {
  console.log("ready!");
  const validaLogin = new JustValidate("#formRegister");

  validaLogin
    .addField("#nombre", [
      { rule: "required", errorMessage: "Falta tu nombre" },
    ])
    .addField("#apellidoPaterno", [
      { rule: "required", errorMessage: "Falta tu apellidoPaterno" },
    ])
    .addField("#correo", [
      { rule: "required", errorMessage: "Falta tu correo" },
      { rule: "email", errorMessage: "Formato invalido"
      }
    ])
    .addField("#edad", [
      { rule: "required", errorMessage: "Falta tu edad" },
    ])
    .addField("#genero", [
      { rule: "required", errorMessage: "Falta tu genero"},
    ])
    .addField("#username", [
      { rule: "required", errorMessage: "Falta tu username"},
    ])
    .addField("#password", [
      { rule: "required", errorMessage: "Falta tu contraseña" },
    ])
    .onSuccess((event) => {
      event.preventDefault();

      let formData = {
          nombre  : $("#nombre ").val(),
          apellidoPaterno : $("#apellidoPaterno").val(),
          apellidoMaterno : $("#apellidoMaterno").val(),
          correo : $("#correo").val(),
          username : $("#username").val(),
          password : $("#password").val(),
          edad : $("#edad").val(),
          genero : $("#genero").val(),
    };
      
      //Realiza la solicitud AJAX al backend para hacer el login
      $.ajax({
        url: window.location.origin + "/auth/register", // Endpoint de login
        method: "POST",
        contentType: 'application/json',
        data: JSON.stringify(formData),  
        cache: false,

        success: (respServ) => {
          console.log(respServ.status);
          console.log(respServ);

          // Verifica si la respuesta contiene un token
          if (respServ) {
            // Almacena el token en localStorage
            localStorage.setItem('token', respServ.token);
            
            // Almacena la información del usuario en localStorage
            localStorage.setItem('nombre', respServ.nombre); // Almacena el objeto usuarioDTO

            // Muestra mensaje de éxito con Swal
            Swal.fire({
              title: "Exito!!",
              text: "Registro exitoso",
              icon: "success",
              didDestroy: () => {
                // Redirige a la página de inicio después de login exitoso
                //window.location.href = window.location.origin + "/PaginaInicio";
              }
            });
          } 
        },

        error: (respServ) => {


          if (respServ.status == 400) {
             // Si la respuesta no contiene el token
              Swal.fire({
              title: "Upps..",
              text: "Error: "+respServ.msg,
              icon: "error",
              didDestroy: () => {
                //location.reload(); // Recarga la página si las credenciales son incorrectas
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
              //location.reload(); // Recarga la página si hay un error
            }
          });

          }
          
        }
      });
    });
});
