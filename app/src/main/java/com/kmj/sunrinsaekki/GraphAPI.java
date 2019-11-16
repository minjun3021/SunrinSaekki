package com.kmj.sunrinsaekki;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class GraphAPI {
    public static void getMyInformation(AccessToken accessToken, final Context context){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.e("MyinFor",response.toString());
                        try {
                            Log.e("Myname",object.getString("name"));
                            Log.e("Myid",object.getString("id"));
                            String name=object.getString("name");
                            String facebookId=object.getString("id");
                            Log.e("Myname",name);
                            Log.e("Myid",facebookId);
                            MainActivity.myName=name;
                            MainActivity.myFacebookId=facebookId;
                            MainActivity.myProfileURL="http://graph.facebook.com/"+facebookId+"/picture?type=large";
                            SharedPreferences pref =context.getSharedPreferences("pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("name",name);
                            editor.putString("facebookId",facebookId);
                            editor.putString("profileURL","http://graph.facebook.com/"+facebookId+"/picture?type=large");

                            editor.commit();




                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static void getMyFriends(final AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                new GraphRequest(
                        accessToken,
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                    Log.e("Json Array Length ", "Json Array Length " + rawName.length());
                                    Log.e("Json Array", "Json Array " + rawName.toString());


                                    for (int i = 0; i < rawName.length(); i++) {
                                        JSONObject c = rawName.getJSONObject(i);


                                        String name = c.getString("name");
                                        Log.e("Friends's Name", "JSON NAME :" + name);

                                        String id = c.getString("id");
                                        Log.e("Friends's ID :", name + "'s ID:" + id);

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("trycatch실패", e.toString());
                                }

                            }
                        }
                ).executeAsync();


            }


        });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,profile_pic");
        request.setParameters(parameters);
        request.executeAsync();


    }
}
