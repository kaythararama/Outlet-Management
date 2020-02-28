/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icube.skilltest.outletmanagement.db;

import java.math.BigInteger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author User
 */
public class ObjectUtils {
    
    public static final String ENTITY_PACKAGE_NAME = "com.icube.skilltest.outletmanagement.entity";
    
    //https://stackoverflow.com/questions/25039080/java-how-to-determine-if-type-is-any-of-primitive-wrapper-string-or-something
    public boolean isPrimitive(Class<?> klass) {

        return String.class == klass 
                || Character.class == klass
                || Boolean.class == klass 
                || Double.class == klass
                || Float.class == klass
                || Long.class == klass
                || Integer.class == klass
                || Short.class == klass
                || Byte.class == klass
                || klass.isPrimitive()
                || klass.isAssignableFrom(Number.class)
                || BigInteger.class == klass
                || java.sql.Timestamp.class == klass
                || java.sql.Date.class == klass
                || java.sql.Time.class == klass;
        
    }
    
    public JSONArray getJsonArray( java.util.List objResult )throws Exception{
        
        JSONArray ja = new JSONArray();
        
        for( Object obj : objResult ){
            if( obj == null ) {
                ja.add( obj );
                continue;
            } 
            
            if( obj.getClass().isArray() ){
                //TODO solve arrays
                JSONArray sub_ja = new JSONArray();
                
                for( Object subObj : (Object[])obj ){
                    if( subObj == null ) {
                        sub_ja.add( subObj );
                        continue;
                    } 
                    
                    if( isPrimitive( subObj.getClass() ) ){
                        if( subObj.getClass() == java.sql.Timestamp.class){
                            sub_ja.add(((java.sql.Timestamp)subObj).getTime());
                        }else if( subObj.getClass() == java.sql.Time.class){
                            sub_ja.add(((java.sql.Time)subObj).getTime());
                        }else if( subObj.getClass() == java.sql.Date.class){
                            sub_ja.add(((java.sql.Date)subObj).getTime());
                        }else   
                            sub_ja.add( subObj );
                    }else{
                        sub_ja.add( createJson_Object_With_Class_Name( subObj ) );
                    }
                    
                }

                ja.add( sub_ja );
                    
                
            }else{
                // primitive object can be added directly to arraylist
                if( isPrimitive(obj.getClass()) ){
                    ja.add( obj );
                }else{
                    ja.add( createJson_Object_With_Class_Name(obj) );
                }
            }
        }
        
        return ja;
    }
    
    public JSONObject createJson_Object_With_Class_Name( Object obj )throws Exception{
        
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        
        // convert object to Json 
        String objJson = mapper.writeValueAsString( obj );
        // convert Json string to Json object
        JSONObject jsonObj = (JSONObject) parser.parse( objJson );

        JSONObject jsonObj_With_Class_Name = new JSONObject();
        // adding class name 
        jsonObj_With_Class_Name.put( obj.getClass().getName(), jsonObj );
        
        return jsonObj_With_Class_Name;
    }
    
    public Object read_Json_Value_To_Class( JSONObject objInput )throws Exception{
        
        ObjectMapper mapper = new ObjectMapper();
        
        for( Object keyVal : objInput.keySet()){
            return mapper.readValue( objInput.get(keyVal).toString(), 
                    Class.forName( keyVal.toString().contains(".") ? keyVal.toString() : ObjectUtils.ENTITY_PACKAGE_NAME + "." + keyVal.toString() ) 
            );
        }
        
        return null;
    }
    
    
    public JSONObject create( JSONObject paramObj ){
        
        JSONObject obj = null; 
        JSONObject objInput = (JSONObject)paramObj.get("object");
        
        try {
            Object entityObj = read_Json_Value_To_Class( objInput );
            org.hibernate.Session session = HibernateUtil.getSession();
            HibernateUtil.beginTransaction();
            
            session.save( entityObj );
            
            HibernateUtil.commitTransaction();
            HibernateUtil.closeSession();
            
            obj = createJson_Object_With_Class_Name( entityObj );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return obj;
    }
    
    public JSONObject update( JSONObject paramObj ){
        
        JSONObject obj = null;
        JSONObject objInput = (JSONObject)paramObj.get("object");
        
        try {
            
            Object entityObj = read_Json_Value_To_Class(objInput);
            org.hibernate.Session session = HibernateUtil.getSession();
            HibernateUtil.beginTransaction();
            
            session.update( entityObj );
            
            HibernateUtil.commitTransaction();
            HibernateUtil.closeSession();
            
            obj = createJson_Object_With_Class_Name( entityObj );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return obj;
    }
    
    public JSONObject get( JSONObject paramObj ){
        
        JSONObject obj = null;
        JSONObject objInput = (JSONObject)paramObj.get("object");
        Long id = null;
        for( Object keyVal : objInput.keySet()){
            JSONObject objTmp = (JSONObject)objInput.get(keyVal);
            if( objTmp != null ){
                if( objTmp.get("id") == null ) return obj;
                id = Long.parseLong( objTmp.get("id").toString() ); break;
            }
        }
        
        try{
            
            org.hibernate.Session session = HibernateUtil.getSession();
            HibernateUtil.beginTransaction();
            Object entityObj = read_Json_Value_To_Class(objInput);
            entityObj = session.get( entityObj.getClass(), id );
            HibernateUtil.commitTransaction( );
            HibernateUtil.closeSession( );
            
            obj = createJson_Object_With_Class_Name( entityObj );
            
        }catch(Exception ex){ex.printStackTrace();}
        
        return obj;
    }
        
    public JSONArray query( JSONObject paramObj ){
        
        JSONArray ja = new JSONArray();
        String query = paramObj.get("query").toString();
        int limit = paramObj.get("limit") != null ? Integer.parseInt( paramObj.get("limit").toString() ) : 0;
        int offset = paramObj.get("offset") != null ? Integer.parseInt( paramObj.get("offset").toString() ) : 0;
        
        try{
            org.hibernate.Session session = HibernateUtil.getSession();
            HibernateUtil.beginTransaction();
            org.hibernate.Query qry = session.createQuery( query );
            java.util.List objResult = null;
            
            if( query.toLowerCase().contains("delete") ){
                Integer updated = qry.executeUpdate();
                objResult = new java.util.ArrayList<>();
                objResult.add(updated);
            }else{
                if( limit > 0)
                    qry.setMaxResults( limit );
                if( offset > 0 )
                    qry.setFirstResult( offset );
                objResult = qry.list( );
            }
            
            ja = getJsonArray( objResult );
            
            HibernateUtil.commitTransaction( );
            HibernateUtil.closeSession( );
        }catch(Exception ex){ex.printStackTrace();}
        
        return ja;
    }
 
}
