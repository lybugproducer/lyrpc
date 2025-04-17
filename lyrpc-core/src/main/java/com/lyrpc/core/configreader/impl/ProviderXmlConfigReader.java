package com.lyrpc.core.configreader.impl;

import com.lyrpc.core.config.*;
import com.lyrpc.core.configreader.ProviderConfigReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析服务消费方的 xml 配置文件
 *
 * @author lybugproducer
 * @since 2025/3/4 09:43
 */
public class ProviderXmlConfigReader extends ProviderConfigReader {

    private Document document;

    private XPath xPath;

    @Override
    protected void read(String configPath) throws Exception {
        // 加载 XML 文件
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = ProviderXmlConfigReader.class.getResourceAsStream(configPath);
        document = builder.parse(inputStream);

        // 创建 XPath 对象
        xPath = XPathFactory.newInstance().newXPath();
    }

    @Override
    protected DatagramConfig parseDatagramConfig() throws ClassNotFoundException {
        DatagramConfig datagramConfig = new DatagramConfig();

        // 解析 Serializer Class
        String serializerClass = evaluateXPath(xPath, document, "/lyrpc-provider/datagram/serializer-class");
        datagramConfig.setSerializerClass(Class.forName(serializerClass));

        // 解析 Compressor Class
        String compressorClass = evaluateXPath(xPath, document, "/lyrpc-provider/datagram/compressor-class");
        datagramConfig.setCompressorClass(Class.forName(compressorClass));

        return datagramConfig;
    }

    @Override
    protected ServerConfig parseServerConfig() {
        ServerConfig serverConfig = new ServerConfig();

        // 解析 address
        String address = evaluateXPath(xPath, document, "/lyrpc-provider/server/address");
        serverConfig.setAddress(address);

        // 解析 worker thread
        String workerThreads = evaluateXPath(xPath, document, "/lyrpc-provider/server/worker-threads");
        serverConfig.setWorkerThreads(Integer.parseInt(workerThreads));
        return serverConfig;

    }

    @Override
    protected ProviderRegistryConfig parseRegistryConfig() throws ClassNotFoundException {
        ProviderRegistryConfig registryConfig = new ProviderRegistryConfig();

        // 解析 Registry Class
        String registryClass = evaluateXPath(xPath, document, "/lyrpc-provider/registry/registry-class");
        registryConfig.setRegistryClass(Class.forName(registryClass));

        // 解析 Registry URL
        List<String> registryUrlList = evaluateXPathNodeList(xPath, document, "/lyrpc-provider/registry/registry-url");
        registryUrlList.forEach(registryConfig::setAddress);

        return registryConfig;
    }

    private String evaluateXPath(XPath xPath, Document document, String expression) {
        XPathExpression expr;
        try {
            expr = xPath.compile(expression);
            return (String) expr.evaluate(document, XPathConstants.STRING);
        } catch (Exception e) {
            throw new RuntimeException("Failed to evaluate XPath expression: " + expression, e);
        }
    }

    private List<String> evaluateXPathNodeList(XPath xPath, Document document, String expression) {
        List<String> list = new ArrayList<>();
        try {
            NodeList nodeList = (NodeList) xPath.evaluate(expression, document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(nodeList.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Failed to evaluate XPath expression: " + expression, e);
        }
        return list;
    }
}
