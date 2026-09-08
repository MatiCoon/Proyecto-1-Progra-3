package data;

import model.Recurso;
import model.Reserva;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XMLManager {
    public static Document cargarXML(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            return dbBuilder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void guardarXML(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void guardarReservas(List<Reserva> reservas, String filePath) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        Document document = dbBuilder.newDocument();

        Element rootElement = document.createElement("reservas");
        document.appendChild(rootElement);

        for (Reserva r : reservas) {
            Element reservaElement = document.createElement("reserva");
            reservaElement.setAttribute("id", r.getId());

            reservaElement.appendChild(crearElemento(document, "funcionarioId", r.getFuncionarioId()));
            reservaElement.appendChild(crearElemento(document, "actividad", r.getActividad()));
            reservaElement.appendChild(crearElemento(document, "fecha", r.getFecha().toString()));
            reservaElement.appendChild(crearElemento(document, "horaInicio", r.getHoraInicio().toString()));
            reservaElement.appendChild(crearElemento(document, "horaFin", r.getHoraFin().toString()));
            reservaElement.appendChild(crearElemento(document, "estado", r.getEstado()));

            Element recursosElement = document.createElement("recursosAsignados");
            for (Recurso recurso : r.getRecursosAsignados()) {
                Element recElement = document.createElement("recursoId");
                recElement.appendChild(document.createTextNode(recurso.getId()));
                recursosElement.appendChild(recElement);
            }
            reservaElement.appendChild(recursosElement);
            rootElement.appendChild(reservaElement);
        }
        guardarXML(document, filePath);
    }

    private static Element crearElemento(Document doc, String nombre, String valor) {
        Element node = doc.createElement(nombre);
        node.appendChild(doc.createTextNode(valor));
        return node;
    }
}
