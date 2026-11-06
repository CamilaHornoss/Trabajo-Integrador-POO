package biblioteca;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author (Romero Maria Itati) 
 * @version TrabajoIntegrador
 */

/**
 * Representa a un socio de tipo Docente dentro del sistema de gestión de la biblioteca.
 * Los docentes no poseen un límite en la cantidad de libros que pueden solicitar simultáneamente.
 * Inicialmente disponen de 5 días de préstamo, plazo que puede incrementarse individualmente 
 * como incentivo a su responsabilidad.
 */
public class Docente extends Socio {
    private static final long serialVersionUID = 1L;
    
    /** Área de conocimiento o departamento al que pertenece el docente. */
    private String area;
    
    /**
     * Constructor que inicializa un nuevo socio Docente con los datos ingresados
     * y le asigna un plazo inicial de 5 días de préstamo.
     * 
     * @param p_dniSocio       Número de DNI del docente.
     * @param p_nombre         Nombre del docente.
     * @param p_area           Área de desempeño o departamento del docente.
     */
    public Docente(int p_dniSocio, String p_nombre, String p_area){
        super(p_dniSocio, p_nombre, 5);
        this.setArea(p_area);
    }
    
    /**
     * Establece el área a la cual pertenece el docente.
     * 
     * @param p_area Nombre del área o departamento.
     */
    private void setArea(String p_area){
        this.area = p_area;
    }
    
    /**
     * Obtiene el área a la cual pertenece el docente.
     * 
     * @return El String con el área del docente.
     */
    public String getArea(){
        return this.area;
    }
    
    /**
     * Evalúa si el docente es considerado responsable.
     * Un docente es responsable si nunca registró una devolución fuera de término
     * y tampoco posee en la actualidad préstamos cuyo plazo de devolución se encuentre vencido.
     * 
     * @return true si el docente nunca tuvo ni tiene préstamos vencidos, {@code false} en caso contrario.
     */
    public boolean esResponsable(){
        Calendar hoy = new GregorianCalendar();
        for (Prestamo prestamo : getPrestamos()) {
            Calendar devolucion = prestamo.getFechaDevolucion();
            if (prestamo.vencido(devolucion == null ? hoy : devolucion)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Adiciona días de préstamo al plazo actual del docente como premio a su responsabilidad.
     * 
     * @param p_dias Cantidad de días adicionales a incrementar al plazo de préstamo.
     */
    public void cambiarDiasDePrestamo(int p_dias){
        this.setDiasPrestamo(this.getDiasPrestamo() + p_dias);
    }
    
    /**
     * Determina si el docente se encuentra facultado para solicitar un nuevo préstamo.
     * <p>
     * Un docente puede solicitar un libro únicamente si no posee ninguna devolución vencida
     * a la fecha actual. No tiene restricciones sobre el cupo máximo de libros.
     * 
     * @return true si el docente no posee préstamos vencidos pendientes, {@code false} si adeuda un libro fuera de plazo.
     */
    @Override
    public boolean puedePedir(){
        return super.puedePedir();
    }
    
    /**
     * Retorna el tipo de socio correspondiente a esta clase.
     * 
     * @return El String "Docente".
     */
    @Override
    public String soyDeLaClase(){
        return "Docente";
    }
}