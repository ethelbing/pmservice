package org.building.pmservice.service.soap;

import org.building.pmservice.service.repository.LoginRepository;
import org.building.pmservice.service.repository.PmRepository;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@WebService(serviceName="LoginInforXMLData",targetNamespace="http://actions")
@Component
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public String LoginInforXMLData(String GetXml) {
        String result = null;
        try {
            Document doc = DocumentHelper.parseText(GetXml);

            Element rootElt = doc.getRootElement();
            Iterator iter = rootElt.elementIterator("LOGINLOG");

            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();

                String USERNAME = recordEle.elementTextTrim("USERNAME");
                String PASSWORD = recordEle.elementTextTrim("PASSWORD");
                String BEGINTIME = recordEle.elementTextTrim("BEGINTIME");

                String ENDTIME = recordEle.elementTextTrim("ENDTIME");

                result = loginRepository.LoginInforXMLData(USERNAME,PASSWORD,BEGINTIME,ENDTIME);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }


        return result;
    }
}