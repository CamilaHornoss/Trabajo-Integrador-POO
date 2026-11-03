import java.util.ArrayList;

/** 
 * @author Romero Maria Itati
 * @version TrabajoIntegrador
 */

/**
 * /**
 * Representa a un socio de tipo Estudiante en la biblioteca.
 * Los estudiantes disponen de un plazo de 20 días fijados para cada préstamo. 
 * Para solicitar un nuevo ejemplar, no deben superar el límite de 3 libros en su poder
 * ni registrar deudas de préstamos vencidos.
 */
public class Estudiante extends Socio{
    
    /** Nombre de la carrera universitaria que cursa el estudiante. */
    private String carrera;
    
    /**
     * Constructor que asigna los atributos del socio estudiante 
     * y establece un plazo por defecto de 20 días para sus préstamos.
     * 
     * @param p_carrera  Carrera que cursa el estudiante.
     * @param p_dniSocio Número de DNI del estudiante.
     * @param p_nombre   Nombre y apellido del estudiante.
     * @param p_diasPrestamo   Días permitidos para cada préstamo.
     */
    public Estudiante(String p_carrera, int p_dniSocio, String p_nombre, int p_diasPrestamo){
        super(p_dniSocio, p_nombre, p_diasPrestamo);
        this.setCarrera(p_carrera);
    }
    
    /**
     * Establece la carrera en la cual está inscripto el estudiante.
     * 
     * @param p_carrera Nombre de la carrera.
     */
    private void setCarrera(String p_carrera){
        this.carrera = p_carrera;
    }
    
    /**
     * Obtiene la carrera universitaria que se encuentra cursando el estudiante.
     * 
     * @return Nombre de la carrera.
     */
    public String getCarrera(){
        return this.carrera;
    }
    
    /**
     * Evalúa si el estudiante se encuentra apto para retirar un nuevo libro de la biblioteca.
     * Para autorizar el préstamo, se verifica simultáneamente que no posea más de 3 libros 
     * en su poder y que no registre préstamos vencidos al día de la fecha.
     * 
     * @return true si posee menos de 3 libros y ningún préstamo vencido.
     */
    @Override
    public boolean puedePedir(){
        return super.puedePedir() && this.cantLibrosPrestados() < 3;
    }
    
    /**
     * Retorna la denominación del tipo de socio.
     * 
     * @return El String "Estudiante".
     */
    @Override
    public String soyDeLaClase(){
        return "ESTUDIANTE";
    }
}