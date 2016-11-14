package org.eclipse.kapua.service.authentication;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringToCharArrayAdapter extends XmlAdapter<String, char[]>{

    @Override
    public String marshal(char[] v) throws Exception {
        return new String(v);
    }

    @Override
    public char[] unmarshal(String v) throws Exception {
        return v.toCharArray();
    }
}
