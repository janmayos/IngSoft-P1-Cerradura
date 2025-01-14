# IA-P1-Cerradura
Ejercicio 1: Configuración del Entorno Simulado
Configure un entorno de desarrollo en una máquina virtual, VPS o PC personal, documentando el proceso desde la instalación del sistema operativo hasta la configuración de herramientas como Java, Spring Boot y bases de datos. Incluya capturas de pantalla o un video explicativo.

Ejercicio 2: Desarrollo de una API para su Software
Implemente una API para consultar datos, manejar autenticación por roles, e incluya documentación de endpoints en README.md con ejemplos de consumo. Opcionalmente, maneje claves API para servicios externos, documentando su generación y configuración.

Ejercicio 3: Diagrama de Despliegue
Diseñe un diagrama de despliegue UML que muestre los nodos físicos (máquina virtual, VPS, o PCs personales), las conexiones entre ellos y las tecnologías utilizadas, acompañado de una explicación breve.

Ejercicio 4: Plan de Pruebas
Diseñe un plan de pruebas funcionales y no funcionales, evaluando rendimiento, seguridad y usabilidad. Documente los resultados con capturas de pantalla y organice las pruebas en branches de GitHub, utilizando pull requests para su integración.

Ejercicio final: Preparación del Proyecto para Despliegue
Prepare el proyecto para ser desplegado en un entorno con 500 MB de espacio en disco y 0.5 GB de RAM por equipo, integrando un servicio de almacenamiento en la nube para optimizar los recursos.

Instrucciones:
Documentación de requisitos técnicos:

Listado detallado de dependencias utilizadas (con versiones específicas).
Puertos necesarios para la ejecución del sistema.
Variables de entorno para configuraciones de la API de almacenamiento en la nube (ej. DROPBOX_API_KEY).
Esquema actualizado de la arquitectura lógica del sistema.
Requisitos de red y servicios externos.
Optimización del proyecto:

Integrar el uso de una API de un servicio en la nube gratuito, como Dropbox, Google Drive o AWS Free Tier, para almacenar archivos grandes.
Implementar caché para reducir el acceso constante a la base de datos.
Comprimir archivos estáticos (CSS, JavaScript, imágenes).
Eliminar archivos temporales y dependencias redundantes.

Ejercicio final: Configuración para Despliegue
Prepare los archivos necesarios para desplegar el proyecto en el servidor dedicado.

Instrucciones:
Creación de archivos de configuración para contenedores:

Dockerfile optimizado:
Usar imágenes base ligeras (Alpine, Debian Slim).
Minimizar las capas de construcción y mantener solo los archivos esenciales.
docker-compose.yml:
Definir los servicios necesarios (ej.: servicio de backend y base de datos).
Establecer límites de recursos:
Memoria máxima: 0.5 GB.
Espacio en disco: máximo 500 MB.
Configurar redes y volúmenes.
Incluir healthchecks para verificar el estado de los servicios.
Configuración de API de almacenamiento en la nube:

Documentar los pasos para generar y configurar las claves API.
Implementar el uso de la API para guardar archivos externos (ej. reportes, logs).
Asegurarse de manejar las credenciales de manera segura.
Guía de despliegue:

Comandos para construir y ejecutar los contenedores.
Procedimiento para configurar la API de almacenamiento.
Instrucciones para detener y reiniciar la aplicación.
Solución a errores comunes.
Capturas de pantalla.
Ejercicio 3: Evaluación de Calidad del Software
Realice pruebas de calidad para evaluar el desempeño y asegurar el correcto funcionamiento del sistema.

Instrucciones:
Métricas de rendimiento:

Tiempo de respuesta bajo carga.
Uso de CPU, memoria y disco.
Throughput máximo (cantidad de solicitudes procesadas por segundo).
Pruebas de seguridad:

Validación de autenticación y autorización.
Verificación de que los archivos cargados en la nube son accesibles solo para usuarios autorizados.
Asegurarse de que las credenciales de la API no estén expuestas.
Evaluación de mantenibilidad:

Revisión de la organización y limpieza del código.
Validación de que el sistema puede ser actualizado sin interrupciones mayores.
Verificar la correcta separación de responsabilidades en los componentes del sistema.

# IA-P1-Cerradura

Ejercicio 2: Implementación de un API Rest para operaciones de conjuntos

## Descripción

Este proyecto implementa un API Rest utilizando Spring Boot para realizar operaciones de conjuntos, específicamente la cerradura de Kleene y la cerradura positiva de un conjunto de cadenas binarias.

## Estructura del Proyecto


## Requisitos

- Docker
- Docker Compose

## Variables de Entorno

Asegúrate de definir las siguientes variables de entorno en un archivo `.env` en la raíz del proyecto:

```env
DATASOURCE_URL=
DATASOURCE_USERNAME=
DATASOURCE_PASSWORD=
JWT_SECRET_KEY=
JWT_EXPIRATION=
JWT_REFRESH_TOKEN_EXPIRATION=
GOOGLE_CLIENT_ID=

#Levantar app 

git clone https://github.com/tu-usuario/IA-P1-Cerradura.git
cd IA-P1-Cerradura
docker-compose up --build

Para la base de datos importar el esquema en practica2.sql
