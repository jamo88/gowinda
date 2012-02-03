/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.io;

import java.io.IOException;
import gowinda.analysis.Snp;
/**
 *
 * @author robertkofler
 */
public interface ISnpReader {
    public abstract Snp next() throws IOException;
    public abstract void close() throws IOException;

}
