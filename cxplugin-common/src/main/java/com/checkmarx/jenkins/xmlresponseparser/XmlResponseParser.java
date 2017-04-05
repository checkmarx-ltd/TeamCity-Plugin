package com.checkmarx.jenkins.xmlresponseparser;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

import com.checkmarx.CxJenkinsWebService.*;


/**
 * Created by ehuds on 15/11/2015.
 */
public interface XmlResponseParser {
    public CxWSResponseRunID parse(InputStream inputStream) throws XMLStreamException, JAXBException;
}
