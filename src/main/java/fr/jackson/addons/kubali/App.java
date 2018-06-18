package fr.jackson.addons.kubali;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import fr.jackson.addons.kubali.core.IFacade;
import fr.jackson.addons.kubali.core.IIntrospector;
import fr.jackson.addons.kubali.dtos.CatDto;
import fr.jackson.addons.kubali.dtos.ChimeraDto;

/**
 * Hello world!
 *
 */
public class App {

	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws JsonProcessingException, ClassNotFoundException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("fr.jackson.addons.kubali");
		IFacade facade = ctx.getBean(IFacade.class);
		IIntrospector i = ctx.getBean(IIntrospector.class);
		ChimeraDto d = new ChimeraDto();
		d.getHalfGoat().setGoatName("goat");
		d.getHalfLion().setName("roi lion");
		d.getHalfLion().setColor("dorée");
		CatDto ct = new CatDto();
		ct.setAge(188);
		ct.setColor("qsdqsdzq");
		ct.setName("sdqsd");
		d.getHalfLion().setList(new ArrayList<>());
		d.getHalfLion().getList().add(ct);
		Class<?> c = App.class.getClassLoader().loadClass(ChimeraDto.class.getName());
		JsonFilter jf = c.getAnnotation(JsonFilter.class);
		ObjectMapper om = new ObjectMapper();
		SimpleFilterProvider s = facade.process(d, "halfGoat,halfLion.list,halfLion.list.color");
		om.setFilterProvider(s);
		log.error(om.writeValueAsString(d));

	}
}
