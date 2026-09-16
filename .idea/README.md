# Sistema de Reservas - Proyecto 1

Este proyecto es una aplicación de escritorio desarrollada en Java Swing aplicando estrictamente el patrón arquitectónico Modelo-Vista-Controlador (MVC). El sistema gestiona recursos, funcionarios y reservas, utilizando persistencia de datos mediante archivos XML (JAXP DOM). Incluye características avanzadas como exportación a PDF, gráficos estadísticos de utilización y procesamiento de lenguaje natural mediante inteligencia artificial para facilitar la creación de reservas.

## Requisitos y Configuración

* **Java Development Kit (JDK):** Versión 21.
* **Gestor de dependencias:** Apache Maven 3.9+.
* **IDE recomendado:** IntelliJ IDEA.
* **Conexión a internet:** Activa para consumir la API de Gemini.

Para habilitar el asistente de IA, es obligatorio configurar tu clave de Google AI Studio como una variable de entorno antes de iniciar la aplicación. Si utilizas Windows PowerShell, puedes declararla temporalmente en tu sesión actual de la terminal.

## Compilación y Ejecución

Para compilar el proyecto, ejecutar las pruebas unitarias (`*Test.java`) y de integración (`*IT.java`), y generar el ejecutable final, utiliza el ciclo de vida de Maven desde la raíz del proyecto:
```powershell
mvn clean verify package
```
Credenciales por Defecto (Semilla XML)

    Administrador: ID: admin | Clave: admin

    Funcionario de prueba: ID: 101 | Clave: 101

Integraciones de Terceros

    OpenPDF (com.github.librepdf:openpdf): Generación de reportes tabulares exportables de cualquier vista de datos.

    JFreeChart (org.jfree:jfreechart): Renderizado del panel de estadísticas de uso en tiempo real.

    Gson (com.google.code.gson:gson): Mapeo de solicitudes y respuestas JSON hacia el modelo gemini-3.6-flash.

Desarrollado por: Matías de Jesús Serrano Palma
Cedula: 119430710
