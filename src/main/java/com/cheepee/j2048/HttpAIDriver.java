/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cheepee.j2048;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Keys;

/**
 *
 * @author cipi
 */
public class HttpAIDriver implements AIDriver {

    private static final BidiMap keysMap;

    static {
        keysMap = new TreeBidiMap();
        keysMap.put("left", Keys.LEFT);
        keysMap.put("right", Keys.RIGHT);
        keysMap.put("up", Keys.UP);
        keysMap.put("down", Keys.DOWN);
    }

    private HttpClient httpClient;
    private URI baseUrl;

    public HttpAIDriver(URI url) throws MalformedURLException {
        httpClient = HttpClients.createDefault();
        baseUrl = url;
    }

    public void start(GameBoard board) throws AIException {
        try {
            JSONObject json = new JSONObject()
                    .put("board", board.asList());

            HttpPost post = new HttpPost(baseUrl.resolve("/start"));
            StringEntity entity = new StringEntity(json.toString());
            entity.setContentType("application/json");
            post.setEntity(entity);

            httpClient.execute(post, new BasicResponseHandler());
        } catch (IOException ex) {
            throw new AIException(ex);
        } catch (JSONException ex) {
            throw new AIException(ex);
        }
    }

    public void end() throws AIException {
        try {
            HttpPost post = new HttpPost(baseUrl.resolve("/end"));
            httpClient.execute(post, new BasicResponseHandler());
        } catch (IOException ex) {
            throw new AIException(ex);
        }
    }

    public CharSequence nextMove() throws AIException {
        try {
            HttpGet get = new HttpGet(baseUrl.resolve("/move"));
            BasicResponseHandler responseHandler = new BasicResponseHandler();
            String responseString = httpClient.execute(get, responseHandler);
            JSONObject json = new JSONObject(responseString);

            String move = (String) json.get("move");
            CharSequence ret = (CharSequence) keysMap.get(move);
            if (ret == null) {
                throw new AIException("bad reply from " + get.getURI().toString() + ": " + responseString);
            }
            return ret;
        } catch (IOException ex) {
            throw new AIException(ex);
        } catch (IllegalStateException ex) {
            throw new AIException(ex);
        } catch (JSONException ex) {
            throw new AIException(ex);
        }
    }

    public void setBoardState(CharSequence lastMove, GameBoard board) throws AIException {
        try {
            String lastMoveAtom = (String) keysMap.getKey(lastMove);
            JSONObject json = new JSONObject()
                    .put("lastMove", lastMoveAtom)
                    .put("board", board.asList());

            HttpPost post = new HttpPost(baseUrl.resolve("/board"));
            StringEntity entity = new StringEntity(json.toString());
            entity.setContentType("application/json");
            post.setEntity(entity);

            httpClient.execute(post, new BasicResponseHandler());
        } catch (JSONException ex) {
            throw new AIException(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new AIException(ex);
        } catch (IOException ex) {
            throw new AIException(ex);
        }
    }

}
