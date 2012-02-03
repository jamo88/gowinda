/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.io;
import gowinda.analysis.AnnotationEntry;
import java.util.ArrayList;
/**
 *
 * @author robertkofler
 */
public interface IBulkAnnotationReader {
    public abstract ArrayList<AnnotationEntry> readAnnotation();  
}
