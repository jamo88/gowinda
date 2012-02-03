/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.analysis;

import java.util.*;
import gowinda.analysis.Snp;
/**
 *
 * @author robertkofler
 */
public interface IGenomeRepresentation {
    
    public abstract ArrayList<String> getGeneidsForSnp(Snp snp);
    public abstract ArrayList<String> allGeneids();
    
}
