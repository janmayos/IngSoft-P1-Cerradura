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
            text: respServ,
            icon: "success",
            didDestroy: () => {
              location.reload();
            }
          });
        },
        error: (respServ) => {
          // console.log(respServ.status);
          // console.log(respServ.responseText);
          // console.log("error");

          Swal.fire({
            title: "Upps..",
            text: respServ.responseText,
            icon: "error",
            didDestroy: () => {
              location.reload();
            }
          });
        }
      });
    });
});