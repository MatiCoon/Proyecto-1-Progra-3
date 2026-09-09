package data;

import org.w3c.dom.*;
import model.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class XMLManager {
   public static Document cargarDocumento(String filePath) throws Exception {
       File file = new File(filePath);
       if (!file.exists()) {
           return null;
       }
       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
       Document doc = dBuilder.parse(file);
       doc.getDocumentElement().normalize();
       return doc;
   }

    public static void guardarDocumento(Document doc, String filePath) throws Exception {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    private static Element crearElementoConTexto(Document doc, String tag, String texto) {
        Element el = doc.createElement(tag);
        el.appendChild(doc.createTextNode(texto != null ? texto : ""));
        return el;
    }

    private static String obtenerTextoElemento(Element padre, String tag) {
        NodeList list = padre.getElementsByTagName(tag);
        if (list.getLength() > 0 && list.item(0).getTextContent() != null) {
            return list.item(0).getTextContent().trim();
        }
        return "";
    }

    public static void guardarUsuarios(List<Usuario> usuarios, String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("usuarios");
        doc.appendChild(root);

        for (Usuario u : usuarios) {
            if (u instanceof Funcionario f) {
                Element elFuncionario = doc.createElement("funcionario");
                elFuncionario.setAttribute("id", f.getId());
                elFuncionario.appendChild(crearElementoConTexto(doc, "clave", f.getClave()));
                elFuncionario.appendChild(crearElementoConTexto(doc, "rol", f.getRol()));
                elFuncionario.appendChild(crearElementoConTexto(doc, "nombre", f.getNombre()));
                elFuncionario.appendChild(crearElementoConTexto(doc, "telefono", f.getTelefono()));
                root.appendChild(elFuncionario);
            } else {
                Element elUsuario = doc.createElement("usuario");
                elUsuario.setAttribute("id", u.getId());
                elUsuario.appendChild(crearElementoConTexto(doc, "clave", u.getClave()));
                elUsuario.appendChild(crearElementoConTexto(doc, "rol", u.getRol()));
                root.appendChild(elUsuario);
            }
        }

        guardarDocumento(doc, filePath);
    }

    public static List<Usuario> cargarUsuarios(String filePath) throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        Document doc = cargarDocumento(filePath);
        if (doc == null) {
            return usuarios;
        }

        NodeList listaFuncionarios = doc.getElementsByTagName("funcionario");
        for (int i = 0; i < listaFuncionarios.getLength(); i++) {
            Element el = (Element) listaFuncionarios.item(i);
            String id = el.getAttribute("id");
            String clave = obtenerTextoElemento(el, "clave");
            String nombre = obtenerTextoElemento(el, "nombre");
            String telefono = obtenerTextoElemento(el, "telefono");
            usuarios.add(new Funcionario(id, clave, nombre, telefono));
        }

        NodeList listaUsuarios = doc.getElementsByTagName("usuario");
        for (int i = 0; i < listaUsuarios.getLength(); i++) {
            Element el = (Element) listaUsuarios.item(i);
            String id = el.getAttribute("id");
            String clave = obtenerTextoElemento(el, "clave");
            String rol = obtenerTextoElemento(el, "rol");
            usuarios.add(new Usuario(id, clave, rol));
        }

        return usuarios;
    }

    public static void guardarCategorias(List<Categoria> categorias, String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("categorias");
        doc.appendChild(root);

        for (Categoria c : categorias) {
            Element elCat = doc.createElement("categoria");
            elCat.setAttribute("id", c.getId());
            elCat.appendChild(crearElementoConTexto(doc, "descripcion", c.getDescripcion()));
            root.appendChild(elCat);
        }

        guardarDocumento(doc, filePath);
    }

    public static List<Categoria> cargarCategorias(String filePath) throws Exception {
        List<Categoria> categorias = new ArrayList<>();
        Document doc = cargarDocumento(filePath);
        if (doc == null) {
            return categorias;
        }

        NodeList lista = doc.getElementsByTagName("categoria");
        for (int i = 0; i < lista.getLength(); i++) {
            Element el = (Element) lista.item(i);
            String id = el.getAttribute("id");
            String descripcion = obtenerTextoElemento(el, "descripcion");
            categorias.add(new Categoria(id, descripcion));
        }

        return categorias;
    }

    public static void guardarRecursos(List<Recurso> recursos, String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("recursos");
        doc.appendChild(root);

        for (Recurso r : recursos) {
            Element elRec = doc.createElement("recurso");
            elRec.setAttribute("id", r.getId());
            elRec.appendChild(crearElementoConTexto(doc, "categoriaId", r.getCategoria().getId()));
            elRec.appendChild(crearElementoConTexto(doc, "descripcion", r.getDescripcion()));
            root.appendChild(elRec);
        }

        guardarDocumento(doc, filePath);
    }

    public static List<Recurso> cargarRecursos(String filePath, List<Categoria> categoriasDisponibles) throws Exception {
        List<Recurso> recursos = new ArrayList<>();
        Document doc = cargarDocumento(filePath);
        if (doc == null) {
            return recursos;
        }

        NodeList lista = doc.getElementsByTagName("recurso");
        for (int i = 0; i < lista.getLength(); i++) {
            Element el = (Element) lista.item(i);
            String id = el.getAttribute("id");
            String categoriaId = obtenerTextoElemento(el, "categoriaId");
            String descripcion = obtenerTextoElemento(el, "descripcion");

            Categoria cat = categoriasDisponibles.stream()
                    .filter(c -> c.getId().equals(categoriaId))
                    .findFirst()
                    .orElse(new Categoria(categoriaId, "Sin Categoría"));

            recursos.add(new Recurso(id, cat, descripcion));
        }

        return recursos;
    }

    public static void guardarReservas(List<Reserva> reservas, String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("reservas");
        doc.appendChild(root);

        for (Reserva r : reservas) {
            Element elRes = doc.createElement("reserva");
            elRes.setAttribute("id", r.getId());
            elRes.appendChild(crearElementoConTexto(doc, "funcionarioId", r.getFuncionarioId()));
            elRes.appendChild(crearElementoConTexto(doc, "actividad", r.getActividad()));
            elRes.appendChild(crearElementoConTexto(doc, "fecha", r.getFecha().toString()));
            elRes.appendChild(crearElementoConTexto(doc, "horaInicio", r.getHoraInicio().toString()));
            elRes.appendChild(crearElementoConTexto(doc, "horaFin", r.getHoraFin().toString()));
            elRes.appendChild(crearElementoConTexto(doc, "estado", r.getEstado()));

            Element elRecursos = doc.createElement("recursosAsignados");
            for (Recurso rec : r.getRecursosAsignados()) {
                Element elItem = doc.createElement("recursoId");
                elItem.appendChild(doc.createTextNode(rec.getId()));
                elRecursos.appendChild(elItem);
            }
            elRes.appendChild(elRecursos);

            root.appendChild(elRes);
        }

        guardarDocumento(doc, filePath);
    }

    public static List<Reserva> cargarReservas(String filePath, List<Recurso> recursosDisponibles) throws Exception {
        List<Reserva> reservas = new ArrayList<>();
        Document doc = cargarDocumento(filePath);
        if (doc == null) {
            return reservas;
        }

        NodeList lista = doc.getElementsByTagName("reserva");
        for (int i = 0; i < lista.getLength(); i++) {
            Element el = (Element) lista.item(i);
            String id = el.getAttribute("id");
            String funcionarioId = obtenerTextoElemento(el, "funcionarioId");
            String actividad = obtenerTextoElemento(el, "actividad");
            LocalDate fecha = LocalDate.parse(obtenerTextoElemento(el, "fecha"));
            LocalTime horaInicio = LocalTime.parse(obtenerTextoElemento(el, "horaInicio"));
            LocalTime horaFin = LocalTime.parse(obtenerTextoElemento(el, "horaFin"));
            String estado = obtenerTextoElemento(el, "estado");

            List<Recurso> asignados = new ArrayList<>();
            NodeList recList = el.getElementsByTagName("recursoId");
            for (int j = 0; j < recList.getLength(); j++) {
                String recId = recList.item(j).getTextContent().trim();
                recursosDisponibles.stream()
                        .filter(r -> r.getId().equals(recId))
                        .findFirst()
                        .ifPresent(asignados::add);
            }

            Reserva reserva = new Reserva(id, funcionarioId, actividad, fecha, horaInicio, horaFin, asignados);
            reserva.setEstado(estado);
            reservas.add(reserva);
        }

        return reservas;
    }
}

