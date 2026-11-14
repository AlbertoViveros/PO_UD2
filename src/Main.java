import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

void main() {
    // Datos del administrador
    final String USUARIO_ADMIN = "admin";
    final String CLAVE_ADMIN = "1234";

    // Formatos de fecha y hora
    final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    final DateTimeFormatter FORMATO_NUMERO_FECHA = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    final DateTimeFormatter FORMATO_DIA_MES = DateTimeFormatter.ofPattern("dd-MM");

    // Boolean que controla el apagado del programa
    boolean programaActivo = true;

    // Duraciones de los tickets (segundos)
    final int DURACION_TRAMO_1 = 900;
    final int DURACION_TRAMO_2 = 1790;
    final int DURACION_TRAMO_3 = 3571;
    final int DURACION_TRAMO_4 = 5352;
    final int DURACION_TRAMO_5 = 7200;

    // Tarifas no residentes (céntimos)
    final int PRECIO_NO_RESIDENTE_TRAMO_1 = 25;
    final int PRECIO_NO_RESIDENTE_TRAMO_2 = 45;
    final int PRECIO_NO_RESIDENTE_TRAMO_3 = 85;
    final int PRECIO_NO_RESIDENTE_TRAMO_4 = 125;
    final int PRECIO_NO_RESIDENTE_TRAMO_5 = 170;

    // Tarifas residentes (céntimos)
    final int PRECIO_RESIDENTE_TRAMO_1 = 10;
    final int PRECIO_RESIDENTE_TRAMO_2 = 20;
    final int PRECIO_RESIDENTE_TRAMO_3 = 40;
    final int PRECIO_RESIDENTE_TRAMO_4 = 60;
    final int PRECIO_RESIDENTE_TRAMO_5 = 80;

    // Horarios de la zona azul
    final int APERTURA_MANANA = 9;
    final int CIERRE_MANANA = 14;
    final int APERTURA_TARDE = 17;
    final int CIERRE_TARDE = 21;
    final int CIERRE_SABADO = 14;

    // Contadores y datos para las estadísticas
    long contadorTickets = 0;
    String numerosTicketsGenerados = ";";
    long contadorAnulaciones = 0;
    String numerosTicketsAnulados = ";";
    int multasAnuladas = 0;
    int dineroMultasAnuladas = 0;
    long dineroTicketsIngresado = 0;

    // Menú principal
    do {
        IO.println("""
                ╔══════════════════════════════════════════════════╗
                ║                                                  ║
                ║              ZONA AZUL - MARTOS                  ║
                ║                 AYUNTAMIENTO                     ║
                ║                                                  ║
                ╠══════════════════════════════════════════════════╣
                ║                                                  ║
                ║       [1]  Obtener un ticket                     ║
                ║                                                  ║
                ║       [2]  Anular una multa                      ║
                ║                                                  ║
                ║       [3]  Consultar fecha, tarifas y horarios   ║
                ║                                                  ║
                ║       [4]  Menú de administrador                 ║
                ║                                                  ║
                ╚══════════════════════════════════════════════════╝
                """);

        // Comprobamos que la opción sea un número del 1 al 4
        String textoOpcion = IO.readln("Seleccione una opción: ").trim();
        int opcion = -1;

        if (textoOpcion.length() == 1 && textoOpcion.charAt(0) >= '1'
                && textoOpcion.charAt(0) <= '4') {
            opcion = Integer.parseInt(textoOpcion);
        }

        // Limpiamos la consola
        for (int i = 0; i < 100; i++) {
            IO.println("");
        }

        switch (opcion) {

            case 1 -> { // Obtener un ticket

                // Preguntamos si es residente
                String respuestaResidente = "";

                do {
                    respuestaResidente = IO.readln("¿Eres residente (Si/No)?: ").trim();
                } while (!respuestaResidente.equalsIgnoreCase("si")
                        && !respuestaResidente.equalsIgnoreCase("s")
                        && !respuestaResidente.equalsIgnoreCase("no")
                        && !respuestaResidente.equalsIgnoreCase("n"));

                boolean esResidente = false;
                if (respuestaResidente.equalsIgnoreCase("si") || respuestaResidente.equalsIgnoreCase("s")) {
                    esResidente = true;
                }

                // Pedimos el dinero
                String dineroTexto = "";
                int dineroCentimos = 0;
                boolean dineroCorrecto = false;

                do {
                    dineroTexto = IO.readln("Introduce el dinero (ej. 1,50 €): ").trim();

                    // Permitimos escribir el símbolo del euro al final
                    if (dineroTexto.length() > 0
                            && dineroTexto.charAt(dineroTexto.length() - 1) == '€') {
                        dineroTexto = dineroTexto.substring(0, dineroTexto.length() - 1).trim();
                    }

                    // Comprobamos si tiene uno o dos decimales
                    int posicionSeparador = -1;

                    if (dineroTexto.length() >= 3) {
                        if (dineroTexto.charAt(dineroTexto.length() - 3) == ','
                                || dineroTexto.charAt(dineroTexto.length() - 3) == '.') {
                            posicionSeparador = dineroTexto.length() - 3;
                        } else if (dineroTexto.charAt(dineroTexto.length() - 2) == ','
                                || dineroTexto.charAt(dineroTexto.length() - 2) == '.') {
                            posicionSeparador = dineroTexto.length() - 2;
                        }
                    }

                    boolean formatoCorrecto = posicionSeparador > 0;

                    if (formatoCorrecto) {
                        String eurosTexto = dineroTexto.substring(0, posicionSeparador);
                        String centimosTexto = dineroTexto.substring(posicionSeparador + 1);

                        // Limitamos los euros para que el importe quepa en un int de céntimos
                        if (eurosTexto.length() > 7) {
                            formatoCorrecto = false;
                        }

                        // Comprobamos que todos los caracteres sean números, excepto la coma o el punto
                        for (int i = 0; i < dineroTexto.length(); i++) {
                            if (i != posicionSeparador
                                    && (dineroTexto.charAt(i) < '0' || dineroTexto.charAt(i) > '9')) {
                                formatoCorrecto = false;
                            }
                        }

                        if (formatoCorrecto) {
                            int euros = Integer.parseInt(eurosTexto);
                            int centimos = Integer.parseInt(centimosTexto);

                            // Si solo introduce un decimal, lo convertimos a dos
                            if (centimosTexto.length() == 1) {
                                centimos = centimos * 10;
                            }

                            dineroCentimos = euros * 100 + centimos;
                            dineroCorrecto = true;
                        }
                    }

                    if (!dineroCorrecto) {
                        IO.println("Cantidad incorrecta. Introduce un importe como 1,50 o 1,50 €.");
                    }

                } while (!dineroCorrecto);

                // Variables para guardar el tramo seleccionado
                int duracionComprada = 0;
                int precioTramo = 0;

                // Seleccionamos el tramo correspondiente
                if (esResidente) {

                    // Comprobamos las tarifas de residentes
                    if (dineroCentimos >= PRECIO_RESIDENTE_TRAMO_5) {
                        duracionComprada = DURACION_TRAMO_5;
                        precioTramo = PRECIO_RESIDENTE_TRAMO_5;
                    } else if (dineroCentimos >= PRECIO_RESIDENTE_TRAMO_4) {
                        duracionComprada = DURACION_TRAMO_4;
                        precioTramo = PRECIO_RESIDENTE_TRAMO_4;
                    } else if (dineroCentimos >= PRECIO_RESIDENTE_TRAMO_3) {
                        duracionComprada = DURACION_TRAMO_3;
                        precioTramo = PRECIO_RESIDENTE_TRAMO_3;
                    } else if (dineroCentimos >= PRECIO_RESIDENTE_TRAMO_2) {
                        duracionComprada = DURACION_TRAMO_2;
                        precioTramo = PRECIO_RESIDENTE_TRAMO_2;
                    } else if (dineroCentimos >= PRECIO_RESIDENTE_TRAMO_1) {
                        duracionComprada = DURACION_TRAMO_1;
                        precioTramo = PRECIO_RESIDENTE_TRAMO_1;
                    }

                } else {

                    // Comprobamos las tarifas de no residentes
                    if (dineroCentimos >= PRECIO_NO_RESIDENTE_TRAMO_5) {
                        duracionComprada = DURACION_TRAMO_5;
                        precioTramo = PRECIO_NO_RESIDENTE_TRAMO_5;
                    } else if (dineroCentimos >= PRECIO_NO_RESIDENTE_TRAMO_4) {
                        duracionComprada = DURACION_TRAMO_4;
                        precioTramo = PRECIO_NO_RESIDENTE_TRAMO_4;
                    } else if (dineroCentimos >= PRECIO_NO_RESIDENTE_TRAMO_3) {
                        duracionComprada = DURACION_TRAMO_3;
                        precioTramo = PRECIO_NO_RESIDENTE_TRAMO_3;
                    } else if (dineroCentimos >= PRECIO_NO_RESIDENTE_TRAMO_2) {
                        duracionComprada = DURACION_TRAMO_2;
                        precioTramo = PRECIO_NO_RESIDENTE_TRAMO_2;
                    } else if (dineroCentimos >= PRECIO_NO_RESIDENTE_TRAMO_1) {
                        duracionComprada = DURACION_TRAMO_1;
                        precioTramo = PRECIO_NO_RESIDENTE_TRAMO_1;
                    }
                }

                // Comprobamos si se ha seleccionado algún tramo
                if (duracionComprada == 0) {
                    IO.println("Dinero insuficiente para obtener un ticket.");

                } else {

                    // Preguntamos si quiere simular otra fecha y hora
                    String respuestaSimulacion = "";

                    do {
                        respuestaSimulacion = IO.readln("¿Quieres simular otra fecha y hora (S/N)?: ").trim();
                    } while (!respuestaSimulacion.equalsIgnoreCase("s")
                            && !respuestaSimulacion.equalsIgnoreCase("si")
                            && !respuestaSimulacion.equalsIgnoreCase("n")
                            && !respuestaSimulacion.equalsIgnoreCase("no"));

                    // Guardamos la fecha y hora de inicio
                    LocalDateTime fechaHoraInicio;

                    if (respuestaSimulacion.equalsIgnoreCase("s") || respuestaSimulacion.equalsIgnoreCase("si")) {
                        String fechaHoraTexto = IO.readln("Introduce la fecha y hora (DD/MM/AAAA HH:MM:SS): ");
                        fechaHoraInicio = LocalDateTime.parse(fechaHoraTexto, FORMATO_FECHA_HORA);
                    } else {
                        fechaHoraInicio = LocalDateTime.now();
                    }

                    // Calculamos el vencimiento respetando los horarios
                    long segundosRestantes = duracionComprada;
                    LocalDateTime fechaHoraFin = fechaHoraInicio;

                    // Avanzamos el reloj hasta consumir todos los segundos del ticket
                    while (segundosRestantes > 0) {

                        int diaSemana = fechaHoraFin.getDayOfWeek().getValue();
                        int hora = fechaHoraFin.getHour();
                        boolean zonaAbierta = false;

                        // De lunes a viernes
                        if (diaSemana >= 1 && diaSemana <= 5) {
                            if ((hora >= APERTURA_MANANA && hora < CIERRE_MANANA)
                                    || (hora >= APERTURA_TARDE && hora < CIERRE_TARDE)) {
                                zonaAbierta = true;
                            }
                        }

                        // Los sábados solo por la mañana
                        if (diaSemana == 6) {
                            if (hora >= APERTURA_MANANA && hora < CIERRE_SABADO) {
                                zonaAbierta = true;
                            }
                        }

                        // Los domingos no se consume tiempo
                        if (zonaAbierta) {
                            segundosRestantes--;
                        }

                        // Avanzamos un segundo en el reloj
                        fechaHoraFin = fechaHoraFin.plusSeconds(1);
                    }

                    // Generamos el número del ticket
                    contadorTickets++;
                    dineroTicketsIngresado = dineroTicketsIngresado + dineroCentimos;

                    // Convertimos la fecha de inicio a un número
                    long fechaNumerica = Long.parseLong(fechaHoraInicio.format(FORMATO_NUMERO_FECHA));

                    // Combinamos la fecha con el contador para generar el número
                    long numeroTicket = fechaNumerica * 10000 + contadorTickets;

                    // Guardamos el número del ticket generado
                    numerosTicketsGenerados = numerosTicketsGenerados + numeroTicket + ";";

                    // Convertimos los segundos a horas, minutos y segundos
                    int horas = duracionComprada / 3600;
                    int minutos = (duracionComprada % 3600) / 60;
                    int segundos = duracionComprada % 60;

                    // Convertimos los céntimos a euros
                    String precioTexto = "%d,%02d €".formatted(precioTramo / 100, precioTramo % 100);
                    String dineroIntroducidoTexto = "%d,%02d €".formatted(dineroCentimos / 100, dineroCentimos % 100);
                    String dineroSobranteTexto = "%d,%02d €".formatted(
                            (dineroCentimos - precioTramo) / 100,
                            (dineroCentimos - precioTramo) % 100
                    );

                    // Convertimos las fechas al formato español
                    String fechaInicioTexto = fechaHoraInicio.format(FORMATO_FECHA_HORA);
                    String fechaFinTexto = fechaHoraFin.format(FORMATO_FECHA_HORA);
                    String diaMesVencimiento = fechaHoraFin.format(FORMATO_DIA_MES);

                    // Mostramos el ticket
                    IO.println("""
                            ╔══════════════════════════════════════════════════╗
                            ║                                                  ║
                            ║                 TICKET SELECCIONADO              ║
                            ║                                                  ║
                            ╠══════════════════════════════════════════════════╣
                            ║%-50s║
                            ║%-50s║
                            ║%-50s║
                            ║%-50s║
                            ║%-50s║
                            ║%-50s║
                            ║%-50s║
                            ║%-50s║
                            ║                                                  ║
                            ║              AYUNTAMIENTO DE MARTOS              ║
                            ╚══════════════════════════════════════════════════╝
                            """.formatted(
                            "  Número de ticket: " + numeroTicket,
                            "  Duración: " + "%02d:%02d:%02d".formatted(horas, minutos, segundos),
                            "  Inicio: " + fechaInicioTexto,
                            "  Fin de estacionamiento: " + fechaFinTexto,
                            "  Día-mes del vencimiento: " + diaMesVencimiento,
                            "  Precio: " + precioTexto,
                            "  Dinero introducido: " + dineroIntroducidoTexto,
                            "  Dinero sobrante: " + dineroSobranteTexto
                    ));
                }

            }

            case 2 -> { // Anular una multa

                boolean menuAnulacionActivo = true;

                do {
                    IO.println("""
                            ╔══════════════════════════════════════════════════╗
                            ║                                                  ║
                            ║                 ANULAR UNA MULTA                 ║
                            ║                                                  ║
                            ╠══════════════════════════════════════════════════╣
                            ║                                                  ║
                            ║       [1]  Por exceder el tiempo permitido       ║
                            ║                                                  ║
                            ║       [2]  Por carecer de ticket                 ║
                            ║                                                  ║
                            ║       [3]  Volver al menú principal              ║
                            ║                                                  ║
                            ╚══════════════════════════════════════════════════╝
                            """);

                    // Comprobamos que la opción sea un número del 1 al 3
                    String textoAnulacion = IO.readln("Seleccione una opción: ").trim();
                    int opcionAnulacion = -1;

                    if (textoAnulacion.length() == 1 && textoAnulacion.charAt(0) >= '1'
                            && textoAnulacion.charAt(0) <= '3') {
                        opcionAnulacion = Integer.parseInt(textoAnulacion);
                    }

                    switch (opcionAnulacion) {

                        case 1 -> { // Por exceder el tiempo permitido

                            IO.println("ANULACIÓN POR EXCEDER EL TIEMPO PERMITIDO");

                            // Pedimos el número del ticket
                            String numeroTicketTexto = IO.readln("Introduce el número del ticket: ").trim();

                            // Comprobamos que tenga 18 cifras
                            boolean numeroValido = numeroTicketTexto.length() == 18;

                            // Comprobamos que todos los caracteres sean números
                            for (int i = 0; i < numeroTicketTexto.length() && numeroValido; i++) {
                                if (numeroTicketTexto.charAt(i) < '0' || numeroTicketTexto.charAt(i) > '9') {
                                    numeroValido = false;
                                }
                            }

                            if (!numeroValido) {
                                IO.println("El número de ticket tiene un formato incorrecto.");
                            } else {

                                // Convertimos el número a long
                                long numeroTicketIntroducido = Long.parseLong(numeroTicketTexto);

                                // Recuperamos la fecha y el contador con la fórmula inversa
                                long fechaRecuperada = numeroTicketIntroducido / 10000;
                                long contadorRecuperado = numeroTicketIntroducido % 10000;
                                long numeroComprobado = fechaRecuperada * 10000 + contadorRecuperado;

                                // Comprobamos que el ticket exista y no se haya anulado
                                if (numeroComprobado == numeroTicketIntroducido
                                        && numerosTicketsGenerados.contains(";" + numeroTicketTexto + ";")
                                        && !numerosTicketsAnulados.contains(";" + numeroTicketTexto + ";")) {

                                    // Guardamos el número del ticket anulado
                                    numerosTicketsAnulados = numerosTicketsAnulados + numeroTicketTexto + ";";

                                    // Actualizamos las estadísticas
                                    multasAnuladas++;
                                    dineroMultasAnuladas = dineroMultasAnuladas + 300;

                                    // Generamos el número de referencia de la anulación
                                    contadorAnulaciones++;
                                    LocalDateTime fechaAnulacion = LocalDateTime.now();
                                    long fechaAnulacionNumerica = Long.parseLong(
                                            fechaAnulacion.format(FORMATO_NUMERO_FECHA));

                                    // Usamos una fórmula distinta a la del número de ticket
                                    long numeroReferencia = fechaAnulacionNumerica * 1000 + contadorAnulaciones;
                                    String fechaAnulacionTexto = fechaAnulacion.format(FORMATO_FECHA_HORA);

                                    // Imprimimos el ticket de anulación
                                    IO.println("""
                                            ╔══════════════════════════════════════════════════╗
                                            ║                                                  ║
                                            ║                 MULTA ANULADA                    ║
                                            ║                                                  ║
                                            ╠══════════════════════════════════════════════════╣
                                            ║%-50s║
                                            ║%-50s║
                                            ║%-50s║
                                            ║%-50s║
                                            ║%-50s║
                                            ║                                                  ║
                                            ║              AYUNTAMIENTO DE MARTOS              ║
                                            ╚══════════════════════════════════════════════════╝
                                            """.formatted(
                                            "  Referencia: " + numeroReferencia,
                                            "  Ticket original: " + numeroTicketTexto,
                                            "  Fecha: " + fechaAnulacionTexto,
                                            "  Motivo: Exceso de tiempo permitido",
                                            "  Importe: 3,00 €"
                                    ));

                                } else {
                                    IO.println("El número de ticket no es auténtico o ya ha sido anulado.");
                                    IO.println("No se puede anular la multa.");
                                }
                            }

                            IO.readln("Pulsa enter para continuar");
                        }

                        case 2 -> { // Por carecer de ticket

                            IO.println("ANULACIÓN POR CARECER DE TICKET");

                            // Actualizamos las estadísticas
                            multasAnuladas++;
                            dineroMultasAnuladas = dineroMultasAnuladas + 600;

                            // Generamos el número de referencia de la anulación
                            contadorAnulaciones++;
                            LocalDateTime fechaAnulacion = LocalDateTime.now();
                            long fechaAnulacionNumerica = Long.parseLong(
                                    fechaAnulacion.format(FORMATO_NUMERO_FECHA));
                            long numeroReferencia = fechaAnulacionNumerica * 1000 + contadorAnulaciones;
                            String fechaAnulacionTexto = fechaAnulacion.format(FORMATO_FECHA_HORA);

                            // Imprimimos el ticket de anulación
                            IO.println("""
                                    ╔══════════════════════════════════════════════════╗
                                    ║                                                  ║
                                    ║                 MULTA ANULADA                    ║
                                    ║                                                  ║
                                    ╠══════════════════════════════════════════════════╣
                                    ║%-50s║
                                    ║%-50s║
                                    ║%-50s║
                                    ║%-50s║
                                    ║%-50s║
                                    ║                                                  ║
                                    ║              AYUNTAMIENTO DE MARTOS              ║
                                    ╚══════════════════════════════════════════════════╝
                                    """.formatted(
                                    "  Referencia: " + numeroReferencia,
                                    "  Ticket original: No presentado",
                                    "  Fecha: " + fechaAnulacionTexto,
                                    "  Motivo: Carecer de ticket",
                                    "  Importe: 6,00 €"
                            ));

                            IO.readln("Pulsa enter para continuar");
                        }

                        case 3 -> { // Volver al menú principal
                            menuAnulacionActivo = false;
                        }

                        default -> IO.println("Opción incorrecta. Introduzca un número del 1 al 3.");
                    }

                    if (menuAnulacionActivo) {
                        for (int i = 0; i < 100; i++) {
                            IO.println("");
                        }
                    }

                } while (menuAnulacionActivo);
            }

            case 3 -> { // Consultar fecha, tarifas y horarios

                // Consultamos la fecha y hora actual
                LocalDateTime ahora = LocalDateTime.now();
                String fechaActual = ahora.format(FORMATO_FECHA_HORA);

                // Convertimos los precios a euros
                String precioNoResidente1 = "%d,%02d €".formatted(PRECIO_NO_RESIDENTE_TRAMO_1 / 100, PRECIO_NO_RESIDENTE_TRAMO_1 % 100);
                String precioNoResidente2 = "%d,%02d €".formatted(PRECIO_NO_RESIDENTE_TRAMO_2 / 100, PRECIO_NO_RESIDENTE_TRAMO_2 % 100);
                String precioNoResidente3 = "%d,%02d €".formatted(PRECIO_NO_RESIDENTE_TRAMO_3 / 100, PRECIO_NO_RESIDENTE_TRAMO_3 % 100);
                String precioNoResidente4 = "%d,%02d €".formatted(PRECIO_NO_RESIDENTE_TRAMO_4 / 100, PRECIO_NO_RESIDENTE_TRAMO_4 % 100);
                String precioNoResidente5 = "%d,%02d €".formatted(PRECIO_NO_RESIDENTE_TRAMO_5 / 100, PRECIO_NO_RESIDENTE_TRAMO_5 % 100);

                String precioResidente1 = "%d,%02d €".formatted(PRECIO_RESIDENTE_TRAMO_1 / 100, PRECIO_RESIDENTE_TRAMO_1 % 100);
                String precioResidente2 = "%d,%02d €".formatted(PRECIO_RESIDENTE_TRAMO_2 / 100, PRECIO_RESIDENTE_TRAMO_2 % 100);
                String precioResidente3 = "%d,%02d €".formatted(PRECIO_RESIDENTE_TRAMO_3 / 100, PRECIO_RESIDENTE_TRAMO_3 % 100);
                String precioResidente4 = "%d,%02d €".formatted(PRECIO_RESIDENTE_TRAMO_4 / 100, PRECIO_RESIDENTE_TRAMO_4 % 100);
                String precioResidente5 = "%d,%02d €".formatted(PRECIO_RESIDENTE_TRAMO_5 / 100, PRECIO_RESIDENTE_TRAMO_5 % 100);

                // Mostramos la información de la zona azul
                IO.println("""
                        ╔══════════════════════════════════════════════════╗
                        ║                                                  ║
                        ║              INFORMACIÓN ZONA AZUL              ║
                        ║                                                  ║
                        ╠══════════════════════════════════════════════════╣
                        ║%-50s║
                        ║                                                  ║
                        ║              TARIFAS DE ESTACIONAMIENTO          ║
                        ║                                                  ║
                        ║  DURACIÓN  | NO RESIDENTES | RESIDENTES          ║
                        ║%-50s║
                        ║%-50s║
                        ║%-50s║
                        ║%-50s║
                        ║%-50s║
                        ║                                                  ║
                        ║                  HORARIOS                        ║
                        ║%-50s║
                        ║%-50s║
                        ║%-50s║
                        ║                                                  ║
                        ╚══════════════════════════════════════════════════╝
                        """.formatted(
                        "  Fecha y hora actual: " + fechaActual,
                        "  00:15:00 | " + precioNoResidente1 + " | " + precioResidente1,
                        "  00:29:50 | " + precioNoResidente2 + " | " + precioResidente2,
                        "  00:59:31 | " + precioNoResidente3 + " | " + precioResidente3,
                        "  01:29:12 | " + precioNoResidente4 + " | " + precioResidente4,
                        "  02:00:00 | " + precioNoResidente5 + " | " + precioResidente5,
                        "  Lunes a viernes: 09:00-14:00 / 17:00-21:00",
                        "  Sábados: 09:00-14:00",
                        "  Domingos: Cerrado"
                ));

            }

            case 4 -> { // Menú de administrador

                IO.println("""
                        ╔══════════════════════════════════════════════════╗
                        ║                                                  ║
                        ║               ACCESO DE ADMINISTRADOR            ║
                        ║                                                  ║
                        ║                ÁREA RESTRINGIDA                  ║
                        ║                                                  ║
                        ╚══════════════════════════════════════════════════╝
                        """);

                String usuario = IO.readln("Usuario: ").trim();
                String clave = IO.readln("Contraseña: ").trim();

                if (!usuario.equals(USUARIO_ADMIN) || !clave.equals(CLAVE_ADMIN)) {
                    IO.println("Usuario o contraseña incorrectos.");

                } else {

                    IO.println("Acceso correcto.");

                    // Menú de administrador
                    boolean menuAdminActivo = true;

                    do {
                        IO.println("""
                                ╔══════════════════════════════════════════════════╗
                                ║                                                  ║
                                ║              MENÚ DE ADMINISTRADOR               ║
                                ║                                                  ║
                                ╠══════════════════════════════════════════════════╣
                                ║                                                  ║
                                ║       [1]  Estadísticas de tickets               ║
                                ║                                                  ║
                                ║       [2]  Estadísticas de multas                ║
                                ║                                                  ║
                                ║       [3]  Apagar el software                    ║
                                ║                                                  ║
                                ╚══════════════════════════════════════════════════╝
                                """);

                        // Comprobamos que la opción sea un número del 1 al 3
                        String textoAdmin = IO.readln("Seleccione una opción: ").trim();
                        int opcionAdmin = -1;

                        if (textoAdmin.length() == 1 && textoAdmin.charAt(0) >= '1'
                                && textoAdmin.charAt(0) <= '3') {
                            opcionAdmin = Integer.parseInt(textoAdmin);
                        }

                        switch (opcionAdmin) {

                            case 1 -> { // Estadísticas de tickets

                                // Convertimos el dinero ingresado a euros
                                String dineroTicketsTexto = "%d,%02d €".formatted(
                                        dineroTicketsIngresado / 100,
                                        dineroTicketsIngresado % 100
                                );

                                // Mostramos las estadísticas de tickets
                                IO.println("""
                                        ╔══════════════════════════════════════════════════╗
                                        ║                                                  ║
                                        ║              ESTADÍSTICAS DE TICKETS             ║
                                        ║                                                  ║
                                        ╠══════════════════════════════════════════════════╣
                                        ║%-50s║
                                        ║%-50s║
                                        ║                                                  ║
                                        ╚══════════════════════════════════════════════════╝
                                        """.formatted(
                                        "  Tickets generados: " + contadorTickets,
                                        "  Dinero ingresado: " + dineroTicketsTexto
                                ));

                                IO.readln("Pulsa enter para continuar");
                            }

                            case 2 -> { // Estadísticas de multas

                                // Convertimos el dinero recaudado a euros
                                String dineroMultasTexto = "%d,%02d €".formatted(
                                        dineroMultasAnuladas / 100,
                                        dineroMultasAnuladas % 100
                                );

                                // Mostramos las estadísticas de multas
                                IO.println("""
                                        ╔══════════════════════════════════════════════════╗
                                        ║                                                  ║
                                        ║               ESTADÍSTICAS DE MULTAS             ║
                                        ║                                                  ║
                                        ╠══════════════════════════════════════════════════╣
                                        ║%-50s║
                                        ║%-50s║
                                        ║                                                  ║
                                        ╚══════════════════════════════════════════════════╝
                                        """.formatted(
                                        "  Multas anuladas: " + multasAnuladas,
                                        "  Dinero recaudado: " + dineroMultasTexto
                                ));

                                IO.readln("Pulsa enter para continuar");
                            }

                            case 3 -> { // Apagar el software

                                String confirmarApagado = "";

                                do {
                                    confirmarApagado = IO.readln(
                                            "¿Está seguro de que desea apagar el software? (S/N): ").trim();
                                } while (!confirmarApagado.equalsIgnoreCase("s")
                                        && !confirmarApagado.equalsIgnoreCase("n")
                                        && !confirmarApagado.equalsIgnoreCase("si")
                                        && !confirmarApagado.equalsIgnoreCase("no"));

                                if (confirmarApagado.equalsIgnoreCase("s")
                                        || confirmarApagado.equalsIgnoreCase("si")) {

                                    IO.println("""
                                            ╔══════════════════════════════════════════════════╗
                                            ║                                                  ║
                                            ║                 SOFTWARE APAGADO                 ║
                                            ║                                                  ║
                                            ║            Gracias por utilizarlo                ║
                                            ║                                                  ║
                                            ╚══════════════════════════════════════════════════╝
                                            """);

                                    programaActivo = false;
                                    menuAdminActivo = false;

                                } else {
                                    IO.println("Apagado cancelado. Volviendo al menú principal.");
                                    menuAdminActivo = false;
                                }
                            }

                            default -> IO.println("Opción incorrecta. Introduzca un número del 1 al 3.");
                        }

                        if (menuAdminActivo) {
                            for (int i = 0; i < 100; i++) {
                                IO.println("");
                            }
                        }

                    } while (menuAdminActivo);
                }
            }

            default -> IO.println("Opción incorrecta. Introduzca un número del 1 al 4.");
        }

        if (programaActivo) {
            IO.readln("Pulsa enter para volver al menú principal");
            for (int i = 0; i < 100; i++) {
                IO.println("");
            }
        }

    } while (programaActivo);
}
