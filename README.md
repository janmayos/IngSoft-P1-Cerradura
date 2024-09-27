# IA-P1-Cerradura

Ejercicio 2: Implementación de un API Rest para operaciones de conjuntos
1. Crear un arquetipo de API Rest con las siguientes características:
o Utilizar Spring Initializr
o Spring Boot
o Gradle (deseable) o Maven
o Groovy (deseable) o cualquier lenguaje que se ejecute en la JVM (Java, Kotlin, Jython,
Groovy, Scala, JRuby)
2. Publicar el arquetipo en un repositorio de GitHub público para su consulta y descarga. Ejemplo:
https://github.com/gabrielhuav/Cerraduras/
3. Incluir un README en el repositorio con las instrucciones mínimas para poner en ejecución el
proyecto (opcional).
4. Crear un método que reciba como parámetro un número entero n y calcule la cerradura de Kleene y
la cerradura positiva de un conjunto de cadenas binarias de longitud máxima n(<input>).
5. Implementar endpoints en el API que respondan a las siguientes características:
o Path: /api/cerradura/estrella/<input>
o Path: /api/cerradura/positiva/<input>
o Método: PUT
o Respuesta: JSON
Opcionalmente:
o El parámetro puede enviarse por la querystring.
o El método puede cambiarse por POST o GET.
o Si se usa POST, debe enviarse el parámetro por el cuerpo de la petición.
Sugerencias: Como guía, puede consultar las convenciones de código presentadas en los anexos y una
práctica del curso de Teoría de la Computación disponible en GitHub:
o Dany. (Oscar Daniel Bucio Barrera). GitHub - Dany0343/TeoriaDeLaComputacion:
“All stuff related to TC ESCOM”. Recuperado el 16 de septiembre de 2024, de
https://github.com/Dany0343/TeoriaDeLaComputacion/blob/main/P1/Programa1.py
<input> 1 2 3
/cerradura/estrella/<input> Σ^*={λ,0,1} Σ^*={λ,0,1,00,01,10,11} Σ^*={λ,0,1,00,01,10,11,000,001,010,011,100,101,110,111}
/cerradura/positiva/<input> Σ^+={0,1} Σ^+={0,1,00,01,10,11} Σ^+={0,1,00,01,10,11,000,001,010,011,100,101,110,111}
