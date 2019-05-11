package org.building.pmservice.service.soap;

import org.building.pmservice.service.repository.PmRepository;
import org.building.pmservice.service.wclient.JXWCJG.DTJXWCJG;
import org.building.pmservice.service.wclient.JXWCJG.DTJXWCJGRet;
import org.building.pmservice.service.wclient.JXWCJG.SIJXWCJGOutSyn;
import org.building.pmservice.service.wclient.JXWCJG.SIJXWCJGOutSynService;
import org.building.pmservice.service.wclient.WWQX.DTWWQX;
import org.building.pmservice.service.wclient.WWQX.DTWWQXRet;
import org.building.pmservice.service.wclient.WWQX.SIWWQXOutSyn;
import org.building.pmservice.service.wclient.WWQX.SIWWQXOutSynService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebService
@Component
public class PmServiceImpl implements PmService {

    @Autowired
    private PmRepository pmRepository;

    @Override
    public String PM0011(String xml) {
        Map result = new HashMap();
        Document root = DocumentHelper.createDocument();
        Element WriteDataRequest = root.addElement("RetItems");
        Map<String, Object> mapEle = new HashMap<String, Object>();

        try {
            Document doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement();
            List<Element> childElements = rootElt.elements();

            mapEle = getAllElements(childElements, mapEle);

            Map retMap = pmRepository.PM_DEFECT_LOG_FROMPRO_DEFECT_S(mapEle.get("XML").toString());
            String DefectPicUrl = mapEle.get("Url").toString();

            DTWWQX dtwwqx = new DTWWQX();
            List<DTWWQX.Items> dlist = new ArrayList<DTWWQX.Items>();
            DTWWQX.Items items = new DTWWQX.Items();

            List list = (List) retMap.get("list");
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                items.setVBILLADD(map.get("V_BILL_ADD").toString());
                items.setVBILLCODE(map.get("V_BILL_CODE").toString());
                items.setVCPZL(map.get("V_CPZL").toString());
                items.setVDEFECTDATE(map.get("V_DEFECTDATE").toString());
                items.setVDEFECTGUID(map.get("V_DEFECTGUID").toString());
                items.setVDEFECTLIST(map.get("V_DEFECTLIST").toString());
                items.setVDEFECTPERCODE(map.get("V_DEFECTPERCODE").toString());
                items.setVDEFECTSOL(map.get("V_DEFECT_SOL").toString());
                items.setVDEPTCODE(map.get("V_DEPTCODE").toString());
                items.setVEQUMAIN(map.get("V_EQU_MAIN").toString());
                items.setVEQUMAINCODE(map.get("V_EQU_MAIN_CODE").toString());
                items.setVEQUNAME(map.get("V_EQU_NAME").toString());
                items.setVEUQCODE(map.get("V_EUQ_CODE").toString());
                items.setVFILES(DefectPicUrl + map.get("V_DEFECTGUID").toString());
                items.setVJHLB(map.get("V_JHLB").toString());
                items.setVMEMO(map.get("V_MEMO").toString());
                items.setVMONTH(map.get("V_MONTH").toString());
                items.setVORGCODE(map.get("V_ORGCODE").toString());
                items.setVSCLB(map.get("V_SCLB").toString());
                items.setVSPECIALTY(map.get("V_SPECIALTY").toString());
                items.setVSTR01("");
                items.setVSTR02("");
                items.setVSTR03("");
                items.setVSTR04("");
                items.setVSTR05("");
                items.setVSYSTEM(map.get("V_SYSTEM").toString());
                items.setVYEAR(map.get("V_YEAR").toString());
                dlist.add(items);
            }

            dtwwqx.setItems(dlist);
            URL url = new URL("file:" + mapEle.get("WsdlUrl").toString());
            QName name = new QName("http://www.anshanmining.com/EAM_PM/", "SI_WWQX_Out_SynService");
            SIWWQXOutSynService siwwqxOutSynService = new SIWWQXOutSynService(url, name);
            SIWWQXOutSyn soap = siwwqxOutSynService.getSIWWQXOutSynPort();

            BindingProvider bp = (BindingProvider) soap;
            bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, mapEle.get("piusername").toString());//用户名
            bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, mapEle.get("pipassword").toString());
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mapEle.get("Pm0011Url").toString());

            DTWWQXRet ret = soap.siWWQX(dtwwqx);

            if (ret.getVTYPE().equals("S")) {
                result = pmRepository.WebServiceLog("AKSB", mapEle.get("XML").toString(), "成功", "检修完成结果下传WebService成功，信息插入成功！唯一值为维修计划申请guid" + mapEle.get("XML").toString() + "接口返回信息数量为：" + dtwwqx.getItems().size());
                result.put("type", ret.getVTYPE());
                result.put("info", ret.getVINFO());

                WriteDataRequest.addElement("type").setText(ret.getVTYPE());
                WriteDataRequest.addElement("info").setText(ret.getVINFO());

            } else {
                result = pmRepository.WebServiceLog("AKSB", mapEle.get("XML").toString(), "失败", "检修完成结果下传WebService失败，信息插入成功！唯一值为维修计划申请guid" + mapEle.get("XML").toString() + "接口返回信息数量为：" + dtwwqx.getItems().size());

                result.put("type", ret.getVTYPE());
                result.put("info", ret.getVINFO());

                WriteDataRequest.addElement("type").setText(ret.getVTYPE());
                WriteDataRequest.addElement("info").setText(ret.getVINFO());
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = pmRepository.WebServiceLog("AKSB", mapEle.get("XML").toString(), "失败", "检修完成结果下传WebService失败，信息插入成功！唯一值为维修计划申请guid" + mapEle.get("XML").toString() + "错误信息为：" + e.getMessage());
            result.put("type", "E");
            result.put("info", e.getMessage());

            WriteDataRequest.addElement("type").setText("E");
            WriteDataRequest.addElement("info").setText(e.getMessage());
        }
        return root.asXML();
    }

    @Override
    public String PM0014(String clientXml) {
        Map result = new HashMap();
        Document root = DocumentHelper.createDocument();
        Element WriteDataRequest = root.addElement("RetItems");
        Map<String, Object> mapEle = new HashMap<String, Object>();
        try {
            Document doc = DocumentHelper.parseText(clientXml);
            Element rootElt = doc.getRootElement();
            List<Element> childElements = rootElt.elements();

            mapEle = getAllElements(childElements, mapEle);

            DTJXWCJG dtjxwcjg = new DTJXWCJG();
            List<DTJXWCJG.Items> list = new ArrayList<DTJXWCJG.Items>();
            DTJXWCJG.Items items = new DTJXWCJG.Items();


            items.setVDEFECTGUID(mapEle.get("V_DEFECT_GUID").toString());
            items.setVDEFECTTYPE(mapEle.get("V_DEFECT_TYPE").toString());
            items.setVSYSTEM(mapEle.get("V_SYSTEM").toString());
            items.setVGUID(mapEle.get("V_GUID").toString());
            items.setVSTR01(mapEle.get("V_STR01").toString());
            items.setVSTR02(mapEle.get("V_STR02").toString());
            items.setVSTR03(mapEle.get("V_STR03").toString());
            items.setVSTR04(mapEle.get("V_STR04").toString());
            items.setVSTR05(mapEle.get("V_STR05").toString());

            list.add(items);

            dtjxwcjg.setItems(list);

            URL url = new URL("file:" + mapEle.get("WsdlUrl").toString());
            QName name = new QName("http://www.anshanmining.com/EAM_PM/", "SI_JXWCJG_Out_SynService");
            SIJXWCJGOutSynService sijxwcjgOutSynService = new SIJXWCJGOutSynService(url, name);
            SIJXWCJGOutSyn soap = sijxwcjgOutSynService.getSIJXWCJGOutSynPort();


            BindingProvider bp = (BindingProvider) soap;
            bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, mapEle.get("piusername").toString());
            bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, mapEle.get("pipassword").toString());
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mapEle.get("Pm0014Url").toString());

            DTJXWCJGRet ret = soap.siJXWCJG(dtjxwcjg);

            if (ret.getVTYPE().equals("S")) {
                result = pmRepository.WebServiceLog(mapEle.get("V_SYSTEM").toString(), mapEle.get("V_DEFECT_GUID").toString(), "成功", "检修完成结果下传WebService成功，信息插入成功！唯一值为缺陷guid" + mapEle.get("V_DEFECT_GUID").toString() + "接口返回信息为：" + ret.getVINFO());
                result.put("type", ret.getVTYPE());
                result.put("info", ret.getVINFO());
                WriteDataRequest.addElement("type").setText(ret.getVTYPE());
                WriteDataRequest.addElement("info").setText(ret.getVINFO());
            } else {
                result = pmRepository.WebServiceLog(mapEle.get("V_SYSTEM").toString(), mapEle.get("V_DEFECT_GUID").toString(), "失败", "检修完成结果下传WebService失败，信息插入成功！唯一值为缺陷guid" + mapEle.get("V_DEFECT_GUID").toString() + "接口返回信息为：" + ret.getVINFO());

                result.put("type", ret.getVTYPE());
                result.put("info", ret.getVINFO());

                WriteDataRequest.addElement("type").setText(ret.getVTYPE());
                WriteDataRequest.addElement("info").setText(ret.getVINFO());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = pmRepository.WebServiceLog(mapEle.get("V_SYSTEM").toString(), mapEle.get("V_DEFECT_GUID").toString(), "失败", "检修完成结果下传WebService失败，信息插入成功！唯一值为缺陷guid" + mapEle.get("V_DEFECT_GUID").toString() + "错误信息为：" + e.getMessage());
            result.put("type", "E");
            result.put("info", e.getMessage());

            WriteDataRequest.addElement("type").setText("E");
            WriteDataRequest.addElement("info").setText(e.getMessage());
        }
        return root.asXML();
    }

    private Map<String, Object> getAllElements(List<Element> childElements, Map<String, Object> mapEle) {
        for (Element ele : childElements) {
            mapEle.put(ele.getName(), ele.getText());
            if (ele.elements().size() > 0) {
                mapEle = getAllElements(ele.elements(), mapEle);
            }
        }
        return mapEle;
    }

}
