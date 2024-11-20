$(document).ready(() => {
    const validaEdicion = new JustValidate("#edit-user-form");

    validaEdicion
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
        .onSuccess((event) => {
            event.preventDefault();
            submitForm();
        });

    window.togglePassword = function() {
        const passwordField = document.getElementById('password');
        const modifyPassword = document.getElementById('modifyPassword');
        if (modifyPassword.checked) {
            passwordField.disabled = false;
            passwordField.required = true;
            passwordField.value = '';  // Limpia el campo de la contraseña
        } else {
            passwordField.disabled = true;
            passwordField.required = false;
            passwordField.value = '[[${usuario.password}]]';  // Restaura la contraseña
        }
    }

    window.confirmSave = function() {
        console.log("confirmSave");
        if (!isRoleSelected()) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor, selecciona al menos un rol.'
            });
            return;
        }
        Swal.fire({
            title: '¿Guardar cambios?',
            text: "¿Estás seguro de que deseas guardar los cambios?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sí, guardarlo'
        }).then((result) => {
            if (result.isConfirmed) {
                submitForm();
            }
        });
    }

    function isRoleSelected() {
        const roles = document.getElementsByName('roles');
        for (let role of roles) {
            if (role.checked) {
                return true;
            }
        }
        return false;
    }

    function submitForm() {
        const formData = {
            nombre: $("#nombre").val(),
            apellidoPaterno: $("#apellidoPaterno").val(),
            apellidoMaterno: $("#apellidoMaterno").val(),
            correo: $("#correo").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            edad: $("#edad").val(),
            genero: $("#genero").val(),
            roles: Array.from(document.querySelectorAll('input[name="roles"]:checked')).map(el => ({ nombre: el.getAttribute('data-role-name') }))
        };

        const token = localStorage.getItem('token');
        console.log(formData);
        $.ajax({
            url: window.location.origin + "/vista/usuarios/actualizar/" + $("#idUsuario").val(),
            method: "POST",
            contentType: 'application/json',
            data: JSON.stringify(formData),
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: (respServ) => {
                Swal.fire({
                    title: "¡Éxito!",
                    text: "Usuario actualizado exitosamente",
                    icon: "success",
                    didDestroy: () => {
                        window.location.href = window.location.origin + "/admin/usuarios";
                    }
                });
            },
            error: (respServ) => {
                Swal.fire({
                    title: "Error",
                    text: "Hubo un problema al actualizar el usuario: " + respServ.responseText,
                    icon: "error"
                });
            }
        });
    }
});