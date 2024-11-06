$(document).ready(() => {
  console.log("ready!");
  const validaLogin = new JustValidate("#formLogin");

  validaLogin
    .addField("#userU", [
      { rule: "required", errorMessage: "Falta tu usuario" },
      // {rule:"number", errorMessage:"Solo números"},
      // {rule:"minLength", value:8, errorMessage:"Mínimo 8 digitos"},
      // {rule:"maxLength", value:10, errorMessage:"Máximo 10 digitos"}
    ])
    .addField("#passwordU", [
      { rule: "required", errorMessage: "Falta tu contraseña" },
      // {rule:"minLength", value:18, errorMessage:"Formato incorrecto"},
      // {rule:"maxLength", value:18, errorMessage:"Formato incorrecto"}
    ])
    .onSuccess((event) => {
      event.preventDefault();
      // console.log($("#formLogin").serialize())
      // let formData = jQuery.param({  userU : $("#userU").val(),  passwordU : $("#passwordU").val()})
      // console.log(formData)
      //console.log($("#formLogin").serialize())
      $.ajax({
        url: window.location.origin + "/auth/login", //?userU=" + $("#userU").val() + "&passwordU=" + $("#passwordU").val(),
        method: "POST",
        data: {
          userU: $("#userU").val(),
          passwordU: $("#passwordU").val()
        },
        cache: false,


        success: (respServ) => {
          console.log(respServ.status);
          console.log(respServ);
          Swal.fire({
            title: "Exito!!",
            text: "Bienvenido " + respServ.nombre,
            icon: "success",
            didDestroy: () => {
              window.location.href=window.location.origin+"/PaginaInicio";
              // console.log(respServ)
              // superagent
              //   .get(window.location.origin+"/PaginaInicio")
              //   .send(respServ) // sends a JSON post body
              //   .type('json')
              //   .redirects(1)
              //   .end(function (err, res) {
              //     console.log("error")
              //     console.log(err)
              //     console.log(res)
                  
              //     // Calling the end function will send the request
              //   });

              
              //window.location.href=window.location.origin+"/PaginaInicio";
              // $.get(window.location.origin+"/PaginaInicio", function( data ) {
              //   $( ".result" ).html( respServ );
              //   alert( "Load was performed." );
              // });
            }
          });
        },
        error: (respServ) => {
          // console.log(respServ.status);
          // console.log(respServ.responseText);
          // console.log("error");

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