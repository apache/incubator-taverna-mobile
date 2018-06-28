package org.apache.taverna.mobile;

import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.InputStreamReader;

public class TestDataFactory {

    private static final String TAG = TestDataFactory.class.getSimpleName();

    public <T> T getObjectTypeBean(Class<T> model, String jsonName) {

        InputStream in = getClass().getResourceAsStream(jsonName);
        Serializer serializer = new Persister();
        T xmlBean = null;
        try {
            xmlBean = serializer.read(model, new InputStreamReader(in));
        } catch (Exception e) {
            Log.e(TAG, "getObjectTypeBean: ", e);
        }

        return xmlBean;
    }

}