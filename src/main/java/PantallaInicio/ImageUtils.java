/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PantallaInicio;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;

public class ImageUtils {
    
    // Método principal para cargar la imagen circular
    public static ImageIcon createCircularIcon(byte[] imageData, int diameter) {
        if (imageData == null || imageData.length == 0) return null;
        
        try {
            ImageIcon originalIcon = new ImageIcon(imageData);
            return createCircularIcon(originalIcon, diameter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Método sobrecargado para ImageIcon
    public static ImageIcon createCircularIcon(ImageIcon icon, int diameter) {
        if (icon == null) return null;
        
        // 1. Escalado manteniendo relación de aspecto
        Image scaled = getScaledImage(icon.getImage(), diameter);
        
        // 2. Crear máscara circular
        BufferedImage circularImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImage.createGraphics();
        
        // Configuración de máxima calidad
        setRenderingHints(g2);
        
        // Crear clip circular
        Shape circle = new Ellipse2D.Double(0, 0, diameter, diameter);
        g2.setClip(circle);
        
        // Centrar la imagen
        int x = (diameter - scaled.getWidth(null)) / 2;
        int y = (diameter - scaled.getHeight(null)) / 2;
        g2.drawImage(scaled, x, y, null);
        
        g2.dispose();
        return new ImageIcon(circularImage);
    }
    
    // Escalado de alta calidad manteniendo relación de aspecto
    private static Image getScaledImage(Image src, int diameter) {
        int srcWidth = src.getWidth(null);
        int srcHeight = src.getHeight(null);
        
        // Calcular dimensiones manteniendo aspect ratio
        int newWidth, newHeight;
        if (srcWidth > srcHeight) {
            newWidth = diameter;
            newHeight = (int) ((double) srcHeight / srcWidth * diameter);
        } else {
            newHeight = diameter;
            newWidth = (int) ((double) srcWidth / srcHeight * diameter);
        }
        
        // Renderizado de alta calidad
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        setRenderingHints(g2);
        g2.drawImage(src, 0, 0, newWidth, newHeight, null);
        g2.dispose();
        
        return resized;
    }
    
    // Configuración de renderizado de alta calidad
    private static void setRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }
}