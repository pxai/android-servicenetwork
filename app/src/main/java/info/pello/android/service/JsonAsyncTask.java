package info.pello.android.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by PELLO_ALTADILL on 05/11/2016.
 */
public class JsonAsyncTask extends AsyncTask<String,String,Void> {


    private WebRequest webRequest;
    /**
     * Default constructor
     */
    public JsonAsyncTask() {
        webRequest = new WebRequest();
    }

    /**
     * This is called before doInBackground and is a perfect place
     * to prepare the progress Bar.
     */
    @Override
    protected void onPreExecute () {
        Logger.getInstance().log("Starting Async Task");
    }

    /**
     * This is where the task begins and runs
     * String... url declares variable arguments list,
     * and we can get values using indexes: url[0], url[1],...
     * Whenever we consider that we make som progress we can
     * notify through publishProgress()
     */
    @Override
    protected Void doInBackground(String... url) {
        Logger.getInstance().log("URL passed to AsyncTask: " + url[0]);
        //String jsonData ="{'data': {'technologies':[{name : 'Backbone', description : 'JS MVC Framework' , difficulty: 6}, {name : 'Angular', description: 'JS MVC Framework' ,difficulty: 8}, {name : 'CouchDB', description: 'noSQL Database' ,dificultty: 9} ]}, 'responseDetails': null, 'responseStatus': 200} ";
        String jsonData = "";
        try {
            if (webRequest.get(url[0])) {
                Logger.getInstance().log("Code:" + webRequest.getResponseCode() + "\nReceived data: " + webRequest.getResponseString());
                this.parseJson(webRequest.getResponseString());
            } else {
                Logger.getInstance().log("Error in request: " + webRequest.getExceptionMessage());
            }
        } catch (Exception e) {
            Logger.getInstance().log("Exception processing JSON: " + e.getMessage());
        }
        // With this call we notify to progressUpdate
        Logger.getInstance().log("doInBackbround publishes progress");

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * This method is called when we call this.publishProgress
     * and can be used to update contents,progress bars,... in the Activity
     */
    @Override
    protected void onProgressUpdate(String... item) {
        Logger.getInstance().log("onProgressUpdate> json data. Name " + item[0] + " . Desc: " + item[1]);
    }

    @Override
    protected void onPostExecute(Void unused) {
        Logger.getInstance().log("Finished Async Task");
    }

    /**
     *
     * @param cadenaJSON
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws JSONException
     * @throws NoSuchAlgorithmException
     */
    public void parseJson (String cadenaJSON) throws IllegalStateException,
            IOException, JSONException, NoSuchAlgorithmException {


        String name = "";
        String description = "";




        // and from that object we get the array of data we need
        JSONArray registros = new JSONArray(cadenaJSON);

        Logger.getInstance().log("JSON parser> # records:" + registros.length());

        // and now, for each record we process data.
        for (int i = 0; i < registros.length(); i++) {
            Logger.getInstance().log("JSON parser> json record data: " + i + "] " + registros.get(i).toString());
            // We get name and description
            name = registros.getJSONObject(i).getString("name");
            description = registros.getJSONObject(i).getString("description");

            this.publishProgress(name, description);

        }

        Logger.getInstance().log("JSON parser> finished");

    }

}
