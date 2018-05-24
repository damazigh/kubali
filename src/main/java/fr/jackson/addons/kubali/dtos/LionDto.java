package fr.jackson.addons.kubali.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalSerialization
public class LionDto {
	@JsonProperty("nom")
	private String name;
	@JsonProperty("age")
	private Integer age;
	@JsonProperty("couleur")
	private String color;
}
