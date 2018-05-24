package fr.jackson.addons.kubali;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.jackson.addons.kubali.configuration.SpringProvider;
import fr.jackson.addons.kubali.core.ITreeBuilder;
import fr.jackson.addons.kubali.dtos.ChimeraDto;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringProvider.class);
		ITreeBuilder tb = ctx.getBean(ITreeBuilder.class);
		ChimeraDto d = new ChimeraDto();
		d.getHalfGoat().setGoatName("goat");
		d.getHalfLion().setName("roi lion");
		d.getHalfLion().setColor("dorée");
		tb.build(d);
	}
}
