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
      { rule: "email", errorMessage: "Formato invalido" }
    ])
    .addField("#edad", [
      { rule: "required", errorMessage: "Falta tu edad" },
    ])
    .addField("#genero", [
      { rule: "required", errorMessage: "Falta tu genero" },
    ])
    .addField("#username", [
      { rule: "required", errorMessage: "Falta tu username" },
    ])
    .addField("#password", [
      { rule: "required", errorMessage: "Falta tu contraseña" },
    ])
    .onSuccess((event) => {
      event.preventDefault();

      let formData = {
        nombre: $("#nombre").val(),
        apellidoPaterno: $("#apellidoPaterno").val(),
        apellidoMaterno: $("#apellidoMaterno").val(),
        correo: $("#correo").val(),
        username: $("#username").val(),
        password: $("#password").val(),
        edad: $("#edad").val(),
        genero: $("#genero").val(),
        roles: Array.from(document.querySelectorAll('input[name="roles"]:checked')).map(el => ({ nombre: el.value }))
      };

      // Deshabilitar el botón de envío para evitar múltiples envíos
      $("button[type='submit']").prop("disabled", true);

      // Realiza la solicitud AJAX al backend para hacer el registro
      $.ajax({
        url: window.location.origin + "/api/usuarios/registro/registrar", // Endpoint de registro
        method: "POST",
        contentType: 'application/json',
        data: JSON.stringify(formData),
        cache: false,

        success: (respServ) => {
          console.log(respServ.status);
          console.log(respServ);

          // Verifica si la respuesta contiene un token
         
        // Muestra mensaje de éxito con Swal
        Swal.fire({
            title: "¡Éxito!",
            text: "Registro exitoso",
            icon: "success",
            didDestroy: () => {
            // Redirige a la vista de usuarios después de registro exitoso
            const currentUserId = $("#currentUserId").val();
            window.location.href = window.location.origin + "/vista/usuarios?id=" + currentUserId;
            }
        });
        
        },

        error: (respServ) => {
          if (respServ.status == 400) {
            const errorMsg = JSON.parse(respServ.responseText).msg;

            if (errorMsg.includes('correo')) {
              $("#correo-error").text(errorMsg);
            }
            if (errorMsg.includes('usuario')) {
              $("#username-error").text(errorMsg);
            }

            Swal.fire({
              title: "Upps..",
              text: "Error: " + errorMsg,
              icon: "error",
              didDestroy: () => {
                // Puedes recargar la página si es necesario
              }
            });
          } else {
            // Manejo de error si ocurre algo en la solicitud
            console.log("Error", respServ.status);
            console.log("Error details:", respServ.responseText);

            Swal.fire({
              title: "Upps..",
              text: "Hubo un error al procesar tu solicitud",
              icon: "error",
              didDestroy: () => {
                // Puedes recargar la página si es necesario
              }
            });
          }

          // Habilitar el botón de envío nuevamente
          $("button[type='submit']").prop("disabled", false);
        }
      });
    });
});