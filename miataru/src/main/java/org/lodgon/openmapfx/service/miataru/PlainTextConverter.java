package org.lodgon.openmapfx.service.miataru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import org.datafx.reader.converter.InputStreamConverter;

/**
 *
 * @author joeri
 */
public class PlainTextConverter extends InputStreamConverter<String> {

    private String plainText;

    @Override
    public void initialize(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is)); StringWriter writer = new StringWriter()) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            plainText = writer.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String get() {
        return plainText;
    }

    @Override
    public boolean next() {
        return false;
    }
    
}
