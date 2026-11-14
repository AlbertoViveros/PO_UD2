# 🅿️ Zona Azul - Ayuntamiento de Martos 🅿️
Práctica realizada para el módulo de Programación (1º DAM).

# Índice

- INTRODUCCIÓN
- REQUISITOS MÍNIMOS
- DESCARGA Y EJECUCIÓN
- MANUAL DE USUARIO
    - MENÚ PRINCIPAL
        - [1] OBTENER UN TICKET
            - VALIDACIÓN DEL DINERO Y TRAMOS
            - SIMULACIÓN DE FECHA Y CÁLCULO DE HORARIOS
        - [2] ANULAR UNA MULTA
            - POR EXCEDER EL TIEMPO PERMITIDO
            - POR CARECER DE TICKET
        - [3] CONSULTAR FECHA, TARIFAS Y HORARIOS
        - [4] MENÚ DE ADMINISTRADOR
            - ESTADÍSTICAS DE TICKETS
            - ESTADÍSTICAS DE MULTAS
            - APAGAR EL SOFTWARE
- EXPLICACIÓN TÉCNICA DEL CÓDIGO
- COLABORADORES

---

## INTRODUCCIÓN 📖
En este proyecto presentamos el software de gestión del parquímetro para la **Zona Azul del Ayuntamiento de Martos**, desarrollado como práctica en Java. La aplicación de consola permite expedir tickets de estacionamiento calculando automáticamente los horarios comerciales vigentes, anular denuncias, consultar las tarifas oficiales y acceder a un panel de administración protegido para el control de recaudación.

> [!NOTE]
> **Horarios Oficiales de la Zona Azul:**
> * **Lunes a viernes:** de 09:00 a 14:00 y de 17:00 a 21:00
> * **Sábados:** de 09:00 a 14:00
> * **Domingos:** Cerrado (estacionamiento gratuito)

Para el desarrollo se ha hecho uso de las clases `LocalDateTime` y `DateTimeFormatter` de Java para toda la gestión de fechas, horas y cálculo de vencimientos.

---

## REQUISITOS MÍNIMOS 💻
Para poder descargar y utilizar el programa, es necesario tener instalado el siguiente entorno:
- Java Development Kit (JDK 23 o superior, recomendado **JDK 25**)

> [!WARNING]
> Si no tiene Java 23/25 instalado o la versión es antigua (como Java 17 o 21), el programa no se ejecutará debido al uso de métodos `main` sin clase envolvente y la clase `IO`. Asegúrese de tener una versión compatible antes de continuar.

Para comprobar la versión de Java que tiene instalada debe realizar los siguientes pasos:
- Abrir la consola del Símbolo del sistema (buscar `cmd` en la barra de búsqueda de Windows)
![Abrir consola](capturas/abrir_consola.png)

- Y a continuación, escribir el comando `java -version`
![Versión de Java](capturas/java_version.png)

---

## DESCARGA Y EJECUCIÓN 💾
Abra la pestaña de **"Code"**, y dentro de las opciones descargue la carpeta comprimida en un archivo `.ZIP` pulsando en **"Download ZIP"**.
![Descargar ZIP](capturas/download_zip.png)

Nos dirigimos a la ubicación donde tengamos la carpeta comprimida y la descomprimimos.
![Descomprimir ZIP](capturas/descomprimir_zip.png)

Abrimos una terminal dentro de la carpeta descomprimida y ejecutamos el archivo fuente `.java` directamente (o ejecutamos el `.bat` si se incluye):
```bash
java ZonaAzulMartos.java
```
![Ejecutar programa](capturas/ejecutar_programa.png)

---

## MANUAL DE USUARIO 👨‍🏫

### MENÚ PRINCIPAL
Al iniciar el programa se mostrará el menú principal con 4 opciones:
1. Obtener un ticket
2. Anular una multa
3. Consultar fecha, tarifas y horarios
4. Menú de administrador

![Menú principal](capturas/menu_principal.png)

---

### [1] OBTENER UN TICKET
Al seleccionar la opción **[1]**, el programa iniciará el proceso de expedición de un ticket de estacionamiento.

![Obtener ticket](capturas/obtener_ticket.png)

- **CONDICIÓN DE RESIDENTE Y PAGO**: En primer lugar, preguntará si el conductor es residente (`Si/No`) y solicitará introducir el importe en euros (admite coma o punto decimal y el símbolo `€`, por ejemplo: `1,50` o `1,50 €`).
  - Si la cantidad introducida tiene un formato inválido o no alcanza el importe mínimo del Tramo 1, el programa avisará al usuario.
  - Si el importe es válido, asignará automáticamente el tramo de mayor duración posible y calculará el dinero sobrante.

> [!TIP]
> **CÁLCULO EN CÉNTIMOS**
> Internamente el programa convierte el dinero introducido a céntimos (`euros * 100 + centimos`) para evitar errores de redondeo con números decimales.

- **SIMULACIÓN DE FECHA Y HORA**: El sistema preguntará si se desea utilizar la hora actual del equipo o simular una fecha manual (en formato `dd/MM/yyyy HH:mm:ss`) para comprobar cómo actúa el parquímetro fuera de horario comercial o en fines de semana.

![Simulación de fecha](capturas/simulacion_fecha.png)

- **TICKET GENERADO**: Finalmente, se imprimirá por pantalla el resguardo con el **número único de ticket (18 cifras)**, la duración, la fecha de inicio, la fecha exacta de vencimiento y el desglose del pago.

![Ticket generado](capturas/ticket_generado.png)

---

### [2] ANULAR UNA MULTA
Al seleccionar la opción **[2]**, se abrirá un submenú con las dos modalidades de anulación de denuncia:
1. Por exceder el tiempo permitido
2. Por carecer de ticket
3. Volver al menú principal

![Submenú anular multa](capturas/menu_anulacion.png)

- **POR EXCEDER EL TIEMPO PERMITIDO (3,00 €)**:
  - Solicita el número del ticket original de **18 dígitos**.
  - El programa verifica que el código tenga exactamente 18 cifras numéricas, que haya sido emitido previamente en el sistema y que **no haya sido utilizado ya** para anular otra multa.
  - Si es válido, genera el justificante de anulación con su número de referencia y suma `3,00 €` a la recaudación.

![Anulación por exceso de tiempo](capturas/anulacion_tiempo.png)

- **POR CARECER DE TICKET (6,00 €)**:
  - No requiere presentar ningún ticket previo. Tramita directamente la anulación por un importe fijo de `6,00 €` y emite el resguardo correspondiente.

![Anulación sin ticket](capturas/anulacion_sin_ticket.png)

---

### [3] CONSULTAR FECHA, TARIFAS Y HORARIOS
Al seleccionar la opción **[3]**, el parquímetro mostrará en pantalla la fecha y hora actual del sistema junto con la tabla oficial de precios para residentes y no residentes y los horarios de funcionamiento:

| Tramo | Duración | No Residentes | Residentes |
| :--- | :---: | :---: | :---: |
| **Tramo 1** | `00:15:00` | 0,25 € | 0,10 € |
| **Tramo 2** | `00:29:50` | 0,45 € | 0,20 € |
| **Tramo 3** | `00:59:31` | 0,85 € | 0,40 € |
| **Tramo 4** | `01:29:12` | 1,25 € | 0,60 € |
| **Tramo 5** | `02:00:00` | 1,70 € | 0,80 € |

![Consulta de tarifas y horarios](capturas/tarifas_horarios.png)

---

### [4] MENÚ DE ADMINISTRADOR
Para acceder a la opción **[4]**, el software solicitará un **usuario y contraseña** de acceso restringido. Si las credenciales son incorrectas, denegará la entrada y volverá al menú principal.

![Acceso administrador](capturas/login_admin.png)

> [!IMPORTANT]
> **Credenciales de Acceso (Según el código):**
> * **USUARIO:** `admin`
> * **CONTRASEÑA:** `1234`

Una vez dentro, el menú de administrador ofrece las siguientes opciones:

- **[1] Estadísticas de tickets:** Muestra el número total de tickets expedidos y el dinero total ingresado por este concepto.
![Estadísticas de tickets](capturas/estadisticas_tickets.png)

- **[2] Estadísticas de multas:** Muestra el total de multas anuladas y el dinero recaudado por las anulaciones.
![Estadísticas de multas](capturas/estadisticas_multas.png)

- **[3] Apagar el software:** Esta es la **única forma** de cerrar la aplicación de manera controlada. Solicitará confirmación (`S/N`) y, tras confirmar, finalizará la ejecución del programa.
![Apagar software](capturas/apagar_software.png)

---

## EXPLICACIÓN TÉCNICA DEL CÓDIGO ⚙️

Para la lógica interna del programa se han implementado las siguientes soluciones sin necesidad de estructuras avanzadas:

1. **Cálculo inteligente del vencimiento (`while` segundo a segundo):**
   En lugar de sumar la duración directamente a la fecha de inicio, un bucle avanza segundo a segundo comprobando si la Zona Azul está abierta en ese instante. Si el reloj entra en horario cerrado (mediodía, noche o domingo), el contador `segundosRestantes` se detiene hasta la siguiente apertura:
   ```java
   while (segundosRestantes > 0) {
       // Comprueba Lunes-Viernes (09:00-14:00 / 17:00-21:00) y Sábados (09:00-14:00)
       if (zonaAbierta) {
           segundosRestantes--;
       }
       fechaHoraFin = fechaHoraFin.plusSeconds(1);
   }
   ```

2. **Generación y validación de identificadores únicos:**
   Cada ticket combina la marca temporal (`yyyyMMddHHmmss`) con un contador incremental multiplicando por `10000`, garantizando un código numérico de 18 cifras (`long numeroTicket = fechaNumerica * 10000 + contadorTickets`).

3. **Almacenamiento en memoria mediante cadenas delimitadas:**
   Para controlar qué tickets son auténticos y evitar que un mismo ticket se use dos veces para anular multas, se emplean cadenas de texto delimitadas por punto y coma (`;`):
   ```java
   if (numeroComprobado == numeroTicketIntroducido
           && numerosTicketsGenerados.contains(";" + numeroTicketTexto + ";")
           && !numerosTicketsAnulados.contains(";" + numeroTicketTexto + ";"))
   ```

---

## COLABORADORES 🤝

- **Autor/a:** [Tu Nombre y Apellidos] / Enlace GitHub: https://github.com/tu-usuario
