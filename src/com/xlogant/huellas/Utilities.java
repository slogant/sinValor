/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.huellas;

/**
/**
 *
 * @author oscar
 */
import java.util.EnumMap;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import java.io.Serializable;

import static com.digitalpersona.onetouch.DPFPFingerIndex.*;

public final class Utilities implements Serializable {

    private static final EnumMap<DPFPFingerIndex, String> fingerNames;
    static {
    	fingerNames = new EnumMap<>(DPFPFingerIndex.class);
    	fingerNames.put(LEFT_PINKY,   "Meñique izquierdo");
    	fingerNames.put(LEFT_RING,    "Anular izquiedo");
    	fingerNames.put(LEFT_MIDDLE,  "Dedo medio izquierdo");
    	fingerNames.put(LEFT_INDEX,   "Dedo indice izquierdo");
    	fingerNames.put(LEFT_THUMB,   "Pulgar izquierdo");
    	
    	fingerNames.put(RIGHT_PINKY,  "Meñique derecho");
    	fingerNames.put(RIGHT_RING,   "Anular derecho");
    	fingerNames.put(RIGHT_MIDDLE, "Dedo medio derecho");
    	fingerNames.put(RIGHT_INDEX,  "Dedo indice derecho");
    	fingerNames.put(RIGHT_THUMB,  "Pulgar Derecho");
    }

    public static String fingerName(DPFPFingerIndex finger) {
    	return fingerNames.get(finger); 
    }
    
    public static String fingerprintName(DPFPFingerIndex finger) {
    	return fingerNames.get(finger) + " fingerprint"; 
    }
    
}

