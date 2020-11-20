
package fileshared;

import java.io.*;


public class FileShared implements Serializable{
    public String filename;
    public String filetype;
    public long filesize;
    public long lastmodified;
    public String ip;
    public int port;
    
    public FileShared(String filename, String filetype, long filesize, long lastmodified, int port){
        this.filename = filename;
        this.filetype = filetype;
        this.filesize = filesize;
        this.lastmodified = lastmodified;
        this.port = port;
    }
    
    @Override
    public String toString(){
        return "(" + filename + ", " + filetype + ", " + filesize + ", " + lastmodified + ", " + ip + ", " + port + ")";
    }
}