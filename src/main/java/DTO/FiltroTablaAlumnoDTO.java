
package DTO;

/**
 *
 * @author angel
 */
public class FiltroTablaAlumnoDTO {
    private int limit;
    private int offset;
    private String patron;

    public FiltroTablaAlumnoDTO(int limit, int offset, String patron) {
        this.limit = limit;
        this.offset = offset;
        this.patron = patron;
    }

    public FiltroTablaAlumnoDTO(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public FiltroTablaAlumnoDTO(String patron) {
        this.patron = patron;
    }
    
    
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }
}
