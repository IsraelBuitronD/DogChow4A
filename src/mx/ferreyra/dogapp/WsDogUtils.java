package mx.ferreyra.dogapp;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import mx.ferreyra.dogapp.org.ksoap2.SoapEnvelope;
import mx.ferreyra.dogapp.org.ksoap2.serialization.SoapObject;
import mx.ferreyra.dogapp.org.ksoap2.serialization.SoapSerializationEnvelope;
import mx.ferreyra.dogapp.org.ksoap2.transport.HttpTransportSE;
import android.content.Context;
import android.widget.Toast;

/**
 * @author Israel Buitron
 */
public class WsDogUtils {

    public Context context;
    private String url = "http://marketing7veinte.net/dc_app_perroton/appWSDog/wsDog.asmx?WSDL";
    private String namespace = "http://tempuri.org/";

    public WsDogUtils(Context context) {
        this.context = context;
    }

    public String[][] parseEditDuenoMascota(SoapObject result) {
        // TODO implement this method
        return null;
    }

    public String[][] editDuenoMascota(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "editDuenoMascota";
        String action = "http://tempuri.org/editDuenoMascota";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("idDueno",(Integer)parameters.get("owner_id"));
        request.addProperty("idUsuario",(Integer)parameters.get("user_id"));
        request.addProperty("duenoNombre",(String)parameters.get("owner_name"));
        request.addProperty("duenoIdGenero",(Date)parameters.get("owner_gender"));
        request.addProperty("duenoFechaCumpleanos",(Date)parameters.get("owner_bithday"));
        request.addProperty("duenoIdEstado",(Integer)parameters.get("owner_state"));
        request.addProperty("mascotaNombre",(String)parameters.get("dog_name"));
        request.addProperty("mascotaRaza",(String)parameters.get("dog_breed"));
        request.addProperty("mascotaIdGenero",(String)parameters.get("dog_gender"));
        request.addProperty("mascotaIdTipoVida",(Integer)parameters.get("dog_life_style"));
        request.addProperty("mascotaFechaCumpleanos",(Date)parameters.get("dog_birthday"));
        request.addProperty("mascotaIdActividadFisica",(Integer)parameters.get("dog_activity"));
        request.addProperty("mascotaImagen",(String)parameters.get("dog_image"));
        request.addProperty("comentarios1",(String)parameters.get("comment1"));
        request.addProperty("comentarios2",(String)parameters.get("comment2"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseEditDuenoMascota(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1","Moderada"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;IdActividadFisica&gt;1&lt;/IdActividadFisica&gt;
     * &lt;Descripcion&gt;Moderada&lt;/Descripcion&gt;
     * </code></p>
     */
    public String[][] parseGetCatActividadFisica(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        String[][] values = new String[count][2];

        for(int i=0; i<newDataSet.getPropertyCount(); i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);

            values[i][0] = table.getPropertyAsString(0);
            values[i][1] = table.getPropertyAsString(1);
        }

        return values;
    }

    public String[][] getCatActividadFisica()
        throws IOException, XmlPullParserException {
        String method = "getCatActividadFisica";
        String action = "http://tempuri.org/getCatActividadFisica";

        SoapObject request = new SoapObject(namespace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetCatActividadFisica(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1","Aguascalientes"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;IdEstado&gt;1&lt;/IdEstado&gt;
     * &lt;Descripcion&gt;Aguascalientes&lt;/Descripcion&gt;
     * </code></p>
     */
    public String[][] parseGetCatEstados(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        String[][] values = new String[count][2];

        for(int i=0; i<newDataSet.getPropertyCount(); i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);
            values[i][0] = table.getPropertyAsString(0);
            values[i][1] = table.getPropertyAsString(1);
        }

        return values;
    }

    public String[][] getCatEstados()
        throws IOException, XmlPullParserException {
        String method = "getCatEstados";
        String action = "http://tempuri.org/getCatEstados";

        SoapObject request = new SoapObject(namespace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetCatEstados(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1","Masculino","Macho"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;IdGenero&gt;1&lt;/IdGenero&gt;
     * &lt;Descripcion&gt;Masculino&lt;/Descripcion&gt;
     * &lt;Descripcion2&gt;Macho&lt;/Descripcion2&gt;
     * </code></p>
     */
    public String[][] parseGetCatGenero(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        String[][] values = new String[count][3];

        for(int i=0; i<count; i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);

            values[i][0] = table.getPropertyAsString(0);
            values[i][1] = table.getPropertyAsString(1);
            values[i][2] = table.getPropertyAsString(2);
        }

        return values;
    }

    public String[][] getCatGenero()
        throws IOException, XmlPullParserException {
        String method = "getCatGenero";
        String action = "http://tempuri.org/getCatGenero";

        SoapObject request = new SoapObject(namespace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetCatGenero(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1","Exterior"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;IdTipoVida&gt;1&lt;/IdTipoVida&gt;
     * &lt;Descripcion&gt;Exterior&lt;/Descripcion&gt;
     * </code></p>
     */
    public String[][] parseGetCatTipoVida(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        int columns = 17;
        String[][] values = new String[count][columns];

        for(int i=0; i<newDataSet.getPropertyCount(); i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = table.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] getCatTipoVida()
        throws IOException, XmlPullParserException {
        String method = "getCatTipoVida";
        String action = "http://tempuri.org/getCatTipoVida";

        SoapObject request = new SoapObject(namespace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetCatTipoVida(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["14","Israel","1","2012-08-09","6","Balam","Sharpei","2","2",
     *   "2012-09-09","/9j/....",null,null,"2012-09-10","Mientras..."]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;IdDueno&gt;14&lt;/IdDueno&gt;
     * &lt;IdUsuario&gt;10&lt;/IdUsuario&gt;
     * &lt;DuenoNombre&gt;Israel&lt;/DuenoNombre&gt;
     * &lt;DuenoIdGenero&gt;1&lt;/DuenoIdGenero&gt;
     * &lt;DuenoFechaCumpleanos&gt;2012-08-09&lt;/DuenoFechaCumpleanos&gt;
     * &lt;DuenoIdEstado&gt;6&lt;/DuenoIdEstado&gt;
     * &lt;MascotaNombre&gt;Balam&lt;/MascotaNombre&gt;
     * &lt;MascotaRaza&gt;Sharpei&lt;/MascotaRaza&gt;
     * &lt;MascotaIdGenero&gt;2&lt;/MascotaIdGenero&gt;
     * &lt;MascotaIdTipoVida&gt;2&lt;/MascotaIdTipoVida&gt;
     * &lt;MascotaIdActividadFisica&gt;2&lt;/MascotaIdActividadFisica&gt;
     * &lt;MascotaFechaCumpleanos&gt;2012-09-09&lt;/MascotaFechaCumpleanos&gt;
     * &lt;MascotaImagen&gt;/9j/...&lt;/MascotaImagen&gt;
     * &lt;Comentarios1/&gt;
     * &lt;Comentarios2/&gt;
     * &lt;FechaRegistro&gt;2012-09-10&lt;/FechaRegistro&gt;
     * &lt;Tip&gt;Mientras...&lt;/Tip&gt;
     * </code></p>
     */
    public String[][] parseGetDuenosMascotas(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        int columns = 17;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = table.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] getDuenosMascotas()
        throws IOException, XmlPullParserException {
        String method = "getDuenosMascotas";
        String action = "http://tempuri.org/getDuenosMascotas";

        SoapObject request = new SoapObject(namespace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetDuenosMascotas(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["14","Israel","1","2012-08-09","6","Balam","Sharpei","2","2",
     *   "2012-09-09","/9j/....",null,null,"2012-09-10","Mientras..."]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;IdDueno&gt;14&lt;/IdDueno&gt;
     * &lt;IdUsuario&gt;10&lt;/IdUsuario&gt;
     * &lt;DuenoNombre&gt;Israel&lt;/DuenoNombre&gt;
     * &lt;DuenoIdGenero&gt;1&lt;/DuenoIdGenero&gt;
     * &lt;DuenoFechaCumpleanos&gt;2012-08-09&lt;/DuenoFechaCumpleanos&gt;
     * &lt;DuenoIdEstado&gt;6&lt;/DuenoIdEstado&gt;
     * &lt;MascotaNombre&gt;Balam&lt;/MascotaNombre&gt;
     * &lt;MascotaRaza&gt;Sharpei&lt;/MascotaRaza&gt;
     * &lt;MascotaIdGenero&gt;2&lt;/MascotaIdGenero&gt;
     * &lt;MascotaIdTipoVida&gt;2&lt;/MascotaIdTipoVida&gt;
     * &lt;MascotaIdActividadFisica&gt;2&lt;/MascotaIdActividadFisica&gt;
     * &lt;MascotaFechaCumpleanos&gt;2012-09-09&lt;/MascotaFechaCumpleanos&gt;
     * &lt;MascotaImagen&gt;/9j/...&lt;/MascotaImagen&gt;
     * &lt;Comentarios1/&gt;
     * &lt;Comentarios2/&gt;
     * &lt;FechaRegistro&gt;2012-09-10&lt;/FechaRegistro&gt;
     * &lt;Tip&gt;Mientras...&lt;/Tip&gt;
     * </code></p>
     */
    public String[][] parseGetDuenosMascotasByIdUsuario(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        if(count==0)
            return null;

        int columns = 17;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = table.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] getDuenosMascotasByIdUsuario(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "getDuenosMascotasByIdUsuario";
        String action = "http://tempuri.org/getDuenosMascotasByIdUsuario";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("idUsuario",(Integer)parameters.get("user_id"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetDuenosMascotas(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["0","0","NaN"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;Distance&gt;0&lt;/Distance&gt;
     * &lt;SecondTime&gt;0&lt;/SecondTime&gt;
     * &lt;Speed&gt;NaN&lt;/Speed&gt;
     * </code></p>
     */
    public String[][] parseGetStats(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        int columns = 3;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject dtRegister = (SoapObject)documentElement.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = dtRegister.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] getStats(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "getStats";
        String action = "http://tempuri.org/getStats";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("userId",(Integer)parameters.get("user_id"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetStats(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1","Evita..."]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;IdTip&gt;1&lt;/IdTip&gt;
     * &lt;Descripcion&gt;Evita...&lt;/Descripcion&gt;
     * </code></p>
     */
    public String[][] parseGetTipsByIdUsuario(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        if(count==0)
            return null;

        int columns = 2;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = table.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] getTipsByIdUsuario(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "getTipsByIdUsuario";
        String action = "http://tempuri.org/getTipsByIdUsuario";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("idUsuario",(Integer)parameters.get("user_id"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetTipsByIdUsuario(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     */
    public String[][] parseGetTrainingSpot(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject newDataSet = (SoapObject)diffgram.getProperty(0);
        int count = newDataSet.getPropertyCount();
        if(count==0)
            return null;

        int columns = 11;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject table = (SoapObject)newDataSet.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = table.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] getTrainingSpot(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "getTrainingSpot";
        String action = "http://tempuri.org/getTrainingSpot";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("latitude",(String)parameters.get("latitude"));
        request.addProperty("longitude",(String)parameters.get("longitude"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseGetTrainingSpot(result);
    }

    public String[][] parseInsertDuenoMascota(SoapObject result) {
        // TODO implement this method
        return null;
    }

    public String[][] insertDuenoMascota(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "insertDuenoMascota";
        String action = "http://tempuri.org/insertDuenoMascota";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("idUsuario",(String)parameters.get("idUsuario"));
        request.addProperty("duenoNombre",(String)parameters.get("duenoNombre"));
        request.addProperty("duenoIdGenero",(String)parameters.get("duenoIdGenero"));
        request.addProperty("duenoFechaCumpleanos",(String)parameters.get("duenoFechaCumpleanos"));
        request.addProperty("duenoIdEstado",(String)parameters.get("duenoIdEstado"));
        request.addProperty("mascotaNombre",(String)parameters.get("mascotaNombre"));
        request.addProperty("mascotaRaza",(String)parameters.get("mascotaRaza"));
        request.addProperty("mascotaIdGenero",(String)parameters.get("mascotaIdGenero"));
        request.addProperty("mascotaIdTipoVida",(String)parameters.get("mascotaIdTipoVida"));
        request.addProperty("mascotaFechaCumpleanos",(String)parameters.get("mascotaFechaCumpleanos"));
        request.addProperty("mascotaIdActividadFisica",(String)parameters.get("mascotaIdActividadFisica"));
        request.addProperty("mascotaImagen",(String)parameters.get("mascotaImagen"));
        request.addProperty("comentarios1",(String)parameters.get("comentarios1"));
        request.addProperty("comentarios2",(String)parameters.get("comentarios2"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseInsertDuenoMascota(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1|37"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;return&gt;1|37&lt;/return&gt;
     * </code></p>
     */
    public String[][] parseInsertIphoneID(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        int columns = 1;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject dtRegister = (SoapObject)documentElement.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = dtRegister.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] insertIphoneID(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "insertIphoneID";
        String action = "http://tempuri.org/insertIphoneID";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("IphoneID",(Integer)parameters.get("iphone_id"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseInsertIphoneID(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;return&gt;1&lt;/return&gt;
     * </code></p>
     */
    public String[][] parseInsertRating(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        int columns = 1;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject dtRegister = (SoapObject)documentElement.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = dtRegister.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] insertRating(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "insertRating";
        String action = "http://tempuri.org/insertRating";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("routeId",(Integer)parameters.get("route_id"));
        request.addProperty("rating",(String)parameters.get("rating"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseInsertRating(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["27"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;return&gt;27&lt;/return&gt;
     * </code></p>
     */
    public String[][] parseInsertRoute(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        int columns = 1;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject dtRegister = (SoapObject)documentElement.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = dtRegister.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] insertRoute(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "insertRoute";
        String action = "http://tempuri.org/insertRoute";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("routeName",(String)parameters.get("route_name"));
        request.addProperty("sourceLatitude",(String)parameters.get("source_latitude"));
        request.addProperty("sourceLongitude",(String)parameters.get("source_longitude"));
        request.addProperty("routeLatitude",(String)parameters.get("route_latitude"));
        request.addProperty("routeLongitude",(String)parameters.get("route_longitude"));
        request.addProperty("distance",(String)parameters.get("distance"));
        request.addProperty("timeTaken",(String)parameters.get("time_taken"));
        request.addProperty("difficulty",(String)parameters.get("difficulty"));
        request.addProperty("userId",(Integer)parameters.get("user_id"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseInsertRoute(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;return&gt;1&lt;/return&gt;
     * </code></p>
     */
    public String[][] parseInsertUserIphone(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        int columns = 1;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject dtRegister = (SoapObject)documentElement.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = dtRegister.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] insertUserIphone(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "insertUserIphone";
        String action = "http://tempuri.org/insertUserIphone";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("IphoneID",(String)parameters.get("iphone_id"));
        request.addProperty("username",(String)parameters.get("username"));
        request.addProperty("password",(String)parameters.get("password"));
        request.addProperty("isFacebook",(String)parameters.get("is_facebook"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseInsertUserIphone(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["1"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;return&gt;1&lt;/return&gt;
     * </code></p>
     */
    public Integer parseInsertUsers(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        SoapObject dtRegister = (SoapObject)documentElement.getProperty(0);
        return Integer.parseInt(dtRegister.getPropertyAsString(0));
    }

    public Integer insertUsers(String username, String password, boolean isFacebook)
        throws IOException, XmlPullParserException {
        String method = "insertUsers";
        String action = "http://tempuri.org/insertUsers";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("username",username);
        request.addProperty("password",password);
        request.addProperty("isFacebook",isFacebook ? "1" : "0");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseInsertUsers(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["38"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;return&gt;38&lt;/return&gt;
     * </code></p>
     */
    public Integer parseUserLogin(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        SoapObject dtRegister = (SoapObject)documentElement.getProperty(0);
        return Integer.parseInt(dtRegister.getPropertyAsString(0));
    }

    public Integer userLogin(String username, String password)
        throws IOException, XmlPullParserException {
        String method = "userLogin";
        String action = "http://tempuri.org/userLogin";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("username",username);
        request.addProperty("password",password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseUserLogin(result);
    }

    /**
     * Parse soap object response.
     * @param result Soap object response to parse.
     * @return If <code>result</code> parameter it returns <code>null</code>,
     * otherwise return <code>String</code> matrix with elements parsed.
     * This matrix should look like:
     * <p><code>
     * [["SERVER_ERROR"]]
     * </code></p>
     * for this soap response:
     * <p><code>
     * &lt;return&gt;SERVER_ERROR&lt;/return&gt;
     * </code></p>
     */
    public String[][] parseUserRecoveryPWD(SoapObject result) {
        if (result == null)
            return null;

        SoapObject root = (SoapObject)result.getProperty(0);
        SoapObject diffgram = (SoapObject)root.getProperty(1);
        SoapObject documentElement = (SoapObject)diffgram.getProperty(0);
        int count = documentElement.getPropertyCount();
        if(count==0)
            return null;

        int columns = 1;
        String[][] values = new String[count][columns];

        for(int i=0; i<count; i++) {
            SoapObject dtRegister = (SoapObject)documentElement.getProperty(i);
            for(int j=0; j<columns; j++)
                values[i][j] = dtRegister.getPropertyAsString(j);
        }

        return values;
    }

    public String[][] userRecoveryPWD(Map parameters)
        throws IOException, XmlPullParserException {
        String method = "userRecoveryPWD";
        String action = "http://tempuri.org/userRecoveryPWD";

        SoapObject request = new SoapObject(namespace, method);
        request.addProperty("username",(String)parameters.get("username"));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.call(action, envelope);

        SoapObject result = (SoapObject)envelope.bodyIn;
        return parseUserRecoveryPWD(result);
    }
}
