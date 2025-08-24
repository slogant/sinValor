/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.huellas;

/**
import java.util.EnumMap;

import com.digitalpersona.onetouch.DPFPFingerIndex;

import static com.digitalpersona.onetouch.DPFPFingerIndex.*;

/**
 * Clase de utilidad para proporcionar nombres legibles en español
 * para los diferentes dedos representados por {@link DPFPFingerIndex}.
 * <p>
 * Esta clase no puede ser instanciada.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
public final class Utilities {

    private static final EnumMap<DPFPFingerIndex, String> fingerNames;

    static {
    	fingerNames = new EnumMap<>(DPFPFingerIndex.class);
    	// Mano Izquierda
    	fingerNames.put(LEFT_PINKY,   "Meñique Izquierdo");
    	fingerNames.put(LEFT_RING,    "Anular Izquierdo");
    	fingerNames.put(LEFT_MIDDLE,  "Medio Izquierdo");
    	fingerNames.put(LEFT_INDEX,   "Índice Izquierdo");
    	fingerNames.put(LEFT_THUMB,   "Pulgar Izquierdo");
    	
    	// Mano Derecha
    	fingerNames.put(RIGHT_PINKY,  "Meñique Derecho");
    	fingerNames.put(RIGHT_RING,   "Anular Derecho");
    	fingerNames.put(RIGHT_MIDDLE, "Medio Derecho");
    	fingerNames.put(RIGHT_INDEX,  "Índice Derecho");
    	fingerNames.put(RIGHT_THUMB,  "Pulgar Derecho");
    }

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private Utilities() {}

    /**
     * Devuelve el nombre en español para un dedo específico.
     * @param finger el índice del dedo.
     * @return el nombre del dedo o "Dedo desconocido" si no se encuentra.
     */
    public static String fingerName(DPFPFingerIndex finger) {
    	return fingerNames.getOrDefault(finger, "Dedo desconocido");
    }
    
    /**
     * Devuelve una descripción para la huella de un dedo específico.
     * @param finger el índice del dedo.
     * @return una cadena como "Huella del Pulgar Derecho".
     */
    public static String fingerprintName(DPFPFingerIndex finger) {
    	return "Huella del " + fingerName(finger);
    }
    
}
