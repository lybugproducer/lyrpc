package com.lyrpc.core.configreader.impl;

import com.lyrpc.core.config.ClientConfig;
import com.lyrpc.core.config.ConsumerRegistryConfig;
import com.lyrpc.core.config.DatagramConfig;
import com.lyrpc.core.config.LoadBalancerConfig;
import com.lyrpc.core.configreader.ConsumerConfigReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.InputStream;

/**
 * 解析服务消费方的 xml 配置文件
 *
 * @author lybugproducer
 * @since 2025/3/4 09:43
 */
public class ConsumerXmlConfigReader extends ConsumerConfigReader {

    private Document document;

    private XPath xPath;

    @Override
    protected void read(String configPath) throws Exception {
        // 加载 XML 文件
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = ConsumerXmlConfigReader.class.getResourceAsStream(configPath);
        document = builder.parse(inputStream);

        // 创建 XPath 对象
        xPath = XPathFactory.newInstance().newXPath();
    }

    @Override
    protected ConsumerRegistryConfig parseRegistryConfig() throws ClassNotFoundException {
        ConsumerRegistryConfig registryConfig = new ConsumerRegistryConfig();

        // 解析 Registry Class
        String registryClass = evaluateXPath(xPath, document, "/lyrpc-consumer/registry/registry-class");
        registryConfig.setRegistryClass(Class.forName(registryClass));

        // 解析 Registry URL
        String registryUrl = evaluateXPath(xPath, document, "/lyrpc-consumer/registry/registry-url");
        registryConfig.setAddress(registryUrl);

        return registryConfig;
    }

    @Override
    protected LoadBalancerConfig parseLoadBalancerConfig() throws ClassNotFoundException {
        LoadBalancerConfig loadBalancerConfig = new LoadBalancerConfig();

        // 解析 Load Balancer Class
        String loadBalancerClass = evaluateXPath(xPath, document, "/lyrpc-consumer/load-balancer/load-balancer-class");
        loadBalancerConfig.setLoadBalancerClass(Class.forName(loadBalancerClass));

        return loadBalancerConfig;
    }

    @Override
    protected DatagramConfig parseDatagramConfig() throws ClassNotFoundException {
        DatagramConfig datagramConfig = new DatagramConfig();

        // 解析 Serializer Class
        String serializerClass = evaluateXPath(xPath, document, "/lyrpc-consumer/datagram/serializer-class");
        datagramConfig.setSerializerClass(Class.forName(serializerClass));

        // 解析 Compressor Class
        String compressorClass = evaluateXPath(xPath, document, "/lyrpc-consumer/datagram/compressor-class");
        datagramConfig.setCompressorClass(Class.forName(compressorClass));

        return datagramConfig;
    }

    @Override
    protected ClientConfig parseClientConfig() {
        ClientConfig clientConfig = new ClientConfig();

        // 解析 Data Center ID
        Long dataCenterId = parseLong(evaluateXPath(xPath, document, "/lyrpc-consumer/client/data-center-id"));
        clientConfig.setDataCenterId(dataCenterId);

        // 解析 Worker ID
        Long workerId = parseLong(evaluateXPath(xPath, document, "/lyrpc-consumer/client/worker-id"));
        clientConfig.setWorkerId(workerId);

        return clientConfig;
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

    private Long parseLong(String value) {
        return Long.parseLong(value.trim());
    }

}
