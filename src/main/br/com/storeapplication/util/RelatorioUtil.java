package br.com.storeapplication.util;

import br.com.storeapplication.factory.ConnectionFactory;
import net.sf.jasperreports.engine.JasperRunManager;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
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

    private static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

}
