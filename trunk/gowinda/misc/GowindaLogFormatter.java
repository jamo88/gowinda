/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gowinda.misc;

/**
 *
 * @author robertkofler
 */
public class GowindaLogFormatter extends java.util.logging.Formatter
{
    public GowindaLogFormatter()
    {}
    
    @Override
    public String format(java.util.logging.LogRecord record)
    {
        String msg=String.format("%tD %<tT: %s\n",new java.util.Date(record.getMillis()),record.getMessage());
        return msg;
    }
}
