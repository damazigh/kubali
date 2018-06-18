package fr.jackson.addons.kubali.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalSerialization
@JsonFilter("GoatDtoFilter")
public class GoatDto {
	private String goatName;
	private CatDto c = new CatDto();
	List<String> list = new ArrayList<>();
}
