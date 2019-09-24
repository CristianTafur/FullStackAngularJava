package orm;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

	//almacena el nombre de la unidad de persistencia PERSISTENCE es el nombre definido en el xml
	private static final String PERSISTENCE_UNIT_NAME="ARPU";
	private static EntityManagerFactory factory;//contiene la conexion a la BD
	
	public static EntityManagerFactory getEntityManagerFactory(){
		if (factory==null) {
			factory=Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);//se instancia la conexion
		}		
		return factory;
	}
	
	/**
	 * Cierra la conexion
	 */
	public static void shutdown(){
		if (factory!=null) {
			factory.close();
		}
	}
	
}
