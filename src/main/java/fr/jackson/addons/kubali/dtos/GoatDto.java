package fr.jackson.addons.kubali.dtos;

import java.util.ArrayList;
import java.util.List;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalSerialization
public class GoatDto {
	private String goatName;
	List<String> list = new ArrayList<>();
}
