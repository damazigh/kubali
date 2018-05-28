package fr.jackson.addons.kubali;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.jackson.addons.kubali.core.processors.IProcessor;
import fr.jackson.addons.kubali.dtos.ChimeraDto;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				"fr.jackson.addons.kubali.configuration");
		IProcessor processor = ctx.getBean(IProcessor.class);
		ChimeraDto d = new ChimeraDto();
		d.getHalfGoat().setGoatName("goat");
		d.getHalfLion().setName("roi lion");
		d.getHalfLion().setColor("dorée");
		processor.process(d, "halfGoat");
	}
}
