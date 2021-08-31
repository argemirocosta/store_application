package br.com.storeapplication.util;

import br.com.storeapplication.factory.ConnectionFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class RelatorioUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void executeReport(String relatorio, Map<String, Object> map, String filename)
            throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        InputStream reportStream = context.getExternalContext().getResourceAsStream(relatorio);

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);

        ServletOutputStream servletOutputStream = response.getOutputStream();

        try {
            Connection connection = ConnectionFactory.getConnection();

            JasperRunManager.runReportToPdfStream(reportStream, response.getOutputStream(), map, connection);

            if (connection != null) {
                connection.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        getFacesContext().responseComplete();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    public static void executeReportNewTab(String relatorio, Map map, String filename) {

        Connection connection = null;
        ServletOutputStream outputStream = null;
        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        InputStream relatorioStream = context.getExternalContext().getResourceAsStream(relatorio);

        try {
            connection = ConnectionFactory.getConnection();
            JasperPrint print = JasperFillManager.fillReport(relatorioStream, map, connection);

            JRExporter exportador = new JRPdfExporter();
            exportador.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exportador.setParameter(JRExporterParameter.JASPER_PRINT, print);

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=arquivo.pdf");
            ServletOutputStream servletOutputStream = response.getOutputStream();

            exportador.exportReport();
            servletOutputStream.flush();
            servletOutputStream.close();
            context.renderResponse();
            context.responseComplete();
        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

}
