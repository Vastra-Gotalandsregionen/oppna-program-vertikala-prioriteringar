package se.vgregion.verticalprio.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface FetchJoinThis {

}
