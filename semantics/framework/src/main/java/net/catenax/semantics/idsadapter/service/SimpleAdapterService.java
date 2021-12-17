package net.catenax.semantics.idsadapter.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.exceptions.AdapterException;
import net.catenax.semantics.idsadapter.config.BaseIdsAdapterConfigProperties;
import net.catenax.semantics.idsadapter.restapi.dto.DataSource;
import net.catenax.semantics.idsadapter.restapi.dto.Source;
import net.catenax.semantics.service.SemanticsService;
import net.catenax.semantics.tools.ResultSetsToXmlSource;

@RequiredArgsConstructor
@Slf4j
public class SimpleAdapterService {
    private final SemanticsService semanticsService;
    private final BaseIdsAdapterConfigProperties baseIdsAdapterConfigProperties;
    private final javax.sql.DataSource defaultDataSource;

    /**
     * downloads an xml-based source (file, statement, whatever)
     * @param response the outputstream to put the resource into
     * @param mediaType media type requested
     * @param params request parameters
     * @return the resulting media type of the data written to the response stream
     */
    public String downloadForAgreement(OutputStream response, String mediaType, Map<String,String> params) {
        log.info("Received a download request with params "+params+ "into stream "+response+" with default mediaType "+mediaType);

        if(params.containsKey("file")) {
            Source source=new Source();
            source.setType("file");
            source.setFile(params.get("file"));
            source.setTransformation(params.get("transformation"));

            try {
                mediaType= handleSource(response,mediaType,source,params);
            } catch (Exception e) {
                log.error("File could not be processed. Leaving empty.",e);
            }

            //
            // Not supported approach
            //

        } else {
            log.error("Neither offer nor file given. Leaving empty.");
        }

        return mediaType;
    }

    /**
     * handle a given raw source for the given response stream
     * @param mediaType media type
     * @param so source
     * @param params runtime params
     * @return new mediateType
     */
    protected  Map.Entry<String,javax.xml.transform.Source> handleRawSource(String mediaType, Source so, Map<String,String> params) {
        switch(so.getType()) {
            case "file":
                return handleSourceFile(mediaType, so, params);
            case "jdbc":
                return handleSourceJdbc(mediaType, so, params);
            default:
                throw new UnsupportedOperationException("Source type "+so.getType()+" is not supported.");
        }
    }

    /**
     * handle a given source for the given response stream
     * @param response stream
     * @param so source
     * @param params runtime params
     * @return new mediateType
     */
    protected String handleSource(OutputStream response, String mediaType, Source so, Map<String,String> params) {
        Map.Entry<String,javax.xml.transform.Source> sourceImpl= handleRawSource(mediaType,so,params);

        mediaType=sourceImpl.getKey();

        String transformation=so.getTransformation();
        if(transformation==null) {
            transformation="xml2xml.xsl";
        }

        log.info("Accessing TRANSFORMATION source "+transformation);

        URL sheet = getClass().getClassLoader().getResource(transformation);

        mediaType="application/json";

        log.info("Media Type changed to "+mediaType);

        try {
            StreamSource xslt = new StreamSource(sheet.openStream());
            javax.xml.transform.Result out = new StreamResult(response);
            javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            javax.xml.transform.Transformer transformer = setTransformationParameters(factory.newTransformer(xslt));
            transformer.transform(sourceImpl.getValue(), out);
            if (sourceImpl instanceof StreamSource) {
                ((StreamSource) sourceImpl).getInputStream().close();
            }
            return mediaType;
        } catch (IOException | TransformerException e) {
            throw new AdapterException("error during transforming data",e);
        }
    }

    protected  javax.xml.transform.Transformer setTransformationParameters( javax.xml.transform.Transformer transformer ) {
        transformer.setParameter("SERVICE_URL", baseIdsAdapterConfigProperties.getServiceUrl());
        transformer.setParameter("ADAPTER_URL", baseIdsAdapterConfigProperties.getAdapterUrl());
        transformer.setParameter("PORTAL_URL", baseIdsAdapterConfigProperties.getPortalUrl());
        transformer.setParameter("CONNECTOR_ID","https://w3id.org/idsa/autogen/connectorEndpoint/a73d2202-cb77-41db-a3a6-05ed251c0b");
        return transformer;
    }

    /**
     * Handle a file based adapter/transformation source
     * @param mediaType mediatype requested
     * @param so source representation
     * @param params runtime parameters
     * @return pair of final media type and xml transformation source
     * @throws javax.xml.transform.TransformerFactoryConfigurationError
     */
    protected Map.Entry<String,javax.xml.transform.Source> handleSourceFile(String mediaType, Source so, Map<String,String> params)  {
        log.info("Accessing FILE source "+so.getFile());
        URL resource = getClass().getClassLoader().getResource(so.getFile());
        if (resource!=null) {
            try {
                InputStream resourceStream=resource.openStream();
                javax.xml.transform.Source xml = new StreamSource(resourceStream);
                return new java.util.AbstractMap.SimpleEntry<>("text/xml",xml);
            } catch (IOException e) {
                log.error("download & transform error.", e);
            }
        }

        log.error("File "+so.getFile()+" could not bee found. Leaving empty.");
        try {
            return new java.util.AbstractMap.SimpleEntry<>("text/xml",
                new DOMSource(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()));
        } catch (ParserConfigurationException e) {
            throw new AdapterException("error processing file",e);
        }
    }

    /**
     * access the datasource associated to a source
     * @param so the source rep
     * @return the datasource associated
     */
    protected Connection getConnection(Source so) throws ClassNotFoundException, SQLException {
        if (so.getDatasource() != null
            && so.getDatasource().getDriverClassName() != null
            && so.getDatasource().getDriverClassName().length() > 0) {
            DataSource ds = so.getDatasource();
            Class connClass = Class.forName(ds.getDriverClassName());
            return DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
        } else {
            return defaultDataSource.getConnection();
        }
    }

    /**
     * Handle a relational based adapter/transformation source
     * @param mediaType
     * @param so
     * @param params
     * @return pair of final media type and xml transformation source
     * @throws TransformerFactoryConfigurationError
     */
    protected Map.Entry<String,javax.xml.transform.Source> handleSourceJdbc(String mediaType, Source so, final Map<String,String> params) {
        // load jdbc stuff
        try (final Connection fconn = getConnection(so)) {
            log.info("using configured DataSource Connection: " + fconn.toString());
            Map<String, ResultSet> resultSets = so.getAliases().entrySet()
                .stream().collect(Collectors.toMap(alias -> alias.getKey(), alias -> {
                    try {
                        Statement stmt = fconn.createStatement();
                        String sql = alias.getValue();
                        for (Map.Entry<String, String> param : params.entrySet()) {
                            sql = sql.replace("{" + param.getKey() + "}", param.getValue().replace("+", "%2b"));
                        }
                        log.info(sql);
                        return (ResultSet) stmt.executeQuery(sql);
                    } catch (SQLException e) {
                        log.error("handle jdbc source",e);
                        return null;
                    }
                }));
            ResultSetsToXmlSource converter = new ResultSetsToXmlSource();

            javax.xml.transform.Source source = converter.convert(resultSets);
            return new java.util.AbstractMap.SimpleEntry<>("text/xml", source);
        } catch (SQLException | ClassNotFoundException | TransformerException e) {
            throw new AdapterException("error processing sql",e);
        }
    }

    /**
     * registers new twins
     * @param twinType
     * @param twinSource
     * @return the registration response
     */
    public String registerTwins(String twinType, Source twinSource) {
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            handleSource(outStream, "application/json", twinSource, new java.util.HashMap());
            String result = new String(outStream.toByteArray());
            return semanticsService.registerTwinDefinitions(twinType, result);
        } catch (IOException e) {
            throw new AdapterException("can not process twin registry input",e);
        }
    }
}
