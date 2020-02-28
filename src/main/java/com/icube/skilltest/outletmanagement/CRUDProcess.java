/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icube.skilltest.outletmanagement;

import com.icube.skilltest.outletmanagement.db.ObjectUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class CRUDProcess {
    
    public JSONObject process( JSONObject paramObj ){
        JSONObject obj = new JSONObject( );
        
        String action = paramObj.get("action").toString( );
        
        if( action.equals( "query" )){
            JSONArray ja = query( paramObj );
            
            obj.put( "message", "Success" );
            obj.put( "results", ja );
            
        }else if( action.equals( "list" )){
            paramObj.put( "query", "from Company p" );
            JSONArray ja = query( paramObj );
            
            obj.put( "message", "Success" );
            obj.put( "results", ja );
            
        }else if( action.equals( "create" )){
            JSONObject resultObj = create( paramObj );
            if( resultObj != null ){
                obj.put( "message", "Success" );
                obj.put( "object", resultObj );
            }else{
                obj.put( "message", "Failed" );
            }
            
        }else if( action.equals( "update" )){
            JSONObject resultObj = update( paramObj );
            if( resultObj != null ){
                obj.put( "message", "Success" );
                obj.put( "object", resultObj );
            }else{
                obj.put( "message", "Failed" );
            }
            
        }else if( action.equals( "get" )){
            JSONObject resultObj = get( paramObj );
            if( resultObj != null ){
                obj.put( "message", "Success" );
                obj.put( "object", resultObj );
            }else{
                obj.put( "message", "Failed" );
            }
            
        }else{
            obj.put("message", "Unsupported");
        }
        
        return obj;
    }
    
    private JSONObject create( JSONObject paramObj ){
        
        return new ObjectUtils( ).create( paramObj );
    }
        
    private JSONObject update( JSONObject paramObj ){
        return new ObjectUtils( ).update(paramObj);
    }
    
    private JSONObject get( JSONObject paramObj ){
        return new ObjectUtils( ).get(paramObj);
    }
    
    private JSONArray query( JSONObject paramObj ){
        return new ObjectUtils( ).query(paramObj);
    }
}
