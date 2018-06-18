package fr.jackson.addons.kubali.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalSerialization
@JsonFilter("LionDtoFilter")
public class LionDto {
	@JsonProperty("nom")
	private String name;
	@JsonProperty("age")
	private Integer age;
	@JsonProperty("couleur")
	private String color;
	private List<CatDto> list;
}
